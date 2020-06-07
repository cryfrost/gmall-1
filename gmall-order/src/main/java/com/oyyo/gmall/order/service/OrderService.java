package com.oyyo.gmall.order.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.oyyo.core.bean.Resp;
import com.oyyo.core.bean.UserInfo;
import com.oyyo.core.exception.OrderException;
import com.oyyo.gmall.cart.vo.CartVO;
import com.oyyo.gmall.oms.entity.OrderEntity;
import com.oyyo.gmall.order.feign.*;
import com.oyyo.gmall.order.interceptors.LoginInterceptor;
import com.oyyo.gmall.order.vo.OrderConfirmVO;
import com.oyyo.gmall.oms.vo.OrderItemVo;
import com.oyyo.gmall.oms.vo.OrderSubmitVO;
import com.oyyo.gmall.pms.entity.SkuInfoEntity;
import com.oyyo.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.oyyo.gmall.sms.vo.SalseVO;
import com.oyyo.gmall.ums.entity.MemberEntity;
import com.oyyo.gmall.ums.entity.MemberReceiveAddressEntity;
import com.oyyo.gmall.wms.entity.WareSkuEntity;
import com.oyyo.gmall.wms.vo.SkuLockVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private GmallPmsClient pmsClient;
    @Autowired
    private GmallOmsClient omsClient;
    @Autowired
    private GmallCartClient cartClient;
    @Autowired
    private GmallSmsClient smsClient;
    @Autowired
    private GmallWmsClient wmsClient;
    @Autowired
    private GmallUmsClient umsClient;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    private static final String ORDER_TOKEN_PREFIX = "order:token";


    /**
     * 订单确认页
     * @return
     */
    public OrderConfirmVO orderConfirm() {

        OrderConfirmVO confirmVO = new OrderConfirmVO();

        //获取登录状态
        UserInfo userInfo = LoginInterceptor.getThreadLocalUserInfo();
        Long userId = userInfo.getUserId();
        if (userId == null) {
            log.info("用户未登录");
            return null;
        }
        log.info("用户以登录，id为：[{}]",userId);


        CompletableFuture<Void> addressCompletableFuture = CompletableFuture.runAsync(() -> {
            //获取用户的收货地址列表,根据用户id查询收货地址列表

            Resp<List<MemberReceiveAddressEntity>> addressesResp = umsClient.queryAddressesByUserId(userId);
            List<MemberReceiveAddressEntity> addressEntities = addressesResp.getData();
            log.info("查询用户的收货地址列表完成");
            confirmVO.setAddress(addressEntities);
        }, threadPoolExecutor);

        CompletableFuture<Void> itemCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //获取购物车中选中的商品信息
            Resp<List<CartVO>> cartsResp = cartClient.queryCheckCartsByUserId(userId);
            List<CartVO> cartList = cartsResp.getData();
            if (CollectionUtils.isEmpty(cartList)) {
                log.info("选中商品为空");
                throw new OrderException("选中商品为空，请选择之后再提交！");
            }
            return cartList;
        }, threadPoolExecutor).thenAcceptAsync(cartList -> {

            List<OrderItemVo> itemVos = cartList.stream().map(cart -> {
                OrderItemVo orderItemVo = new OrderItemVo();
                Long skuId = cart.getSkuId();
                CompletableFuture<Void> skuCompletableFuture = CompletableFuture.runAsync(() -> {

                    Resp<SkuInfoEntity> skuInfoEntityResp = pmsClient.querySkuBySkuId(skuId);
                    SkuInfoEntity skuInfoEntity = skuInfoEntityResp.getData();
                    if (skuInfoEntity != null) {
                        orderItemVo.setSkuId(skuId);
                        orderItemVo.setCount(cart.getCount());
                        orderItemVo.setWeight(skuInfoEntity.getWeight());
                        orderItemVo.setDefaultImage(skuInfoEntity.getSkuDefaultImg());
                        orderItemVo.setPrice(skuInfoEntity.getPrice());
                        orderItemVo.setTitle(skuInfoEntity.getSkuTitle());
                    }
                }, threadPoolExecutor);

                CompletableFuture<Void> salesAttrCompletableFuture = CompletableFuture.runAsync(() -> {
                    //销售属性
                    Resp<List<SkuSaleAttrValueEntity>> salesAttrValuesResp = pmsClient.querySkuSalesAttrValuesBySkuId(skuId);
                    List<SkuSaleAttrValueEntity> attrValueEntities = salesAttrValuesResp.getData();
                    if (!CollectionUtils.isEmpty(attrValueEntities)) {

                        orderItemVo.setSaleAttrValues(attrValueEntities);
                    }
                }, threadPoolExecutor);

                CompletableFuture<Void> wareSkuCompletableFuture = CompletableFuture.runAsync(() -> {
                    //库存信息
                    Resp<List<WareSkuEntity>> wareSkuResp = wmsClient.queryWareSkuBySkuId(skuId);
                    List<WareSkuEntity> wareSkuEntities = wareSkuResp.getData();
                    if (!CollectionUtils.isEmpty(wareSkuEntities)) {

                        orderItemVo.setStore(wareSkuEntities.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() > 0));
                    }
                }, threadPoolExecutor);

                CompletableFuture<Void> salseCompletableFuture = CompletableFuture.runAsync(() -> {
                    //营销信息
                    Resp<List<SalseVO>> salseResp = smsClient.querySkuSalseBySkuId(skuId);
                    List<SalseVO> salses = salseResp.getData();
                    orderItemVo.setSales(salses);
                }, threadPoolExecutor);

                CompletableFuture.allOf(skuCompletableFuture, salesAttrCompletableFuture, wareSkuCompletableFuture, salseCompletableFuture).join();

                return orderItemVo;
            }).collect(Collectors.toList());
            log.info("查询用户:【{}】购物车选中商品完成", userId);
            confirmVO.setOrderItems(itemVos);
        }, threadPoolExecutor);
        CompletableFuture<Void> memberCompletableFuture = CompletableFuture.runAsync(() -> {
            //查询用户信息 获取积分
            Resp<MemberEntity> memberEntityResp = umsClient.queryMemberById(userId);
            MemberEntity memberEntity = memberEntityResp.getData();
            log.info("查询用户积分信息:【{}】购物车选中商品完成", userId);
            confirmVO.setBounds(memberEntity.getIntegration());
        }, threadPoolExecutor);
        CompletableFuture<Void> orderTokenCompletableFuture = CompletableFuture.runAsync(() -> {
            //生成订单唯一标志，防止重复提交 （一份响应到页面，一份保存在redis中）
            String orderToken = IdWorker.getIdStr();
            log.info("生成订单号完成：[{}]", orderToken);
            confirmVO.setOrderToken(orderToken);
            redisTemplate.opsForValue().set(ORDER_TOKEN_PREFIX + orderToken,orderToken);
        }, threadPoolExecutor);

        CompletableFuture.allOf(addressCompletableFuture, itemCompletableFuture,memberCompletableFuture,orderTokenCompletableFuture).join();

        return confirmVO;
    }

    /**
     * 订单提交
     * @param orderSubmitVO
     */
    public void submit(OrderSubmitVO orderSubmitVO) {
        UserInfo userInfo = LoginInterceptor.getThreadLocalUserInfo();
        //防重复标志
        String orderToken = orderSubmitVO.getOrderToken();
        log.info("提交订单的 orderToken = [{}]",orderToken);
        //防止重复提价 根据 orderToken 如果有 则是第一次提交，无 则是重复提交
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        Long flag = redisTemplate.execute(new DefaultRedisScript<>(script,Long.class), Arrays.asList(ORDER_TOKEN_PREFIX + orderToken), orderToken);
        if (flag == 0) {
            log.info("重复提交订单！");
            throw new OrderException("请勿重复提交订单！");
        }

        //校验总价
        List<OrderItemVo> items = orderSubmitVO.getItems();
        BigDecimal totalPrice = orderSubmitVO.getTotalPrice();
        if (CollectionUtils.isEmpty(items)) {
            log.info("没有勾选商品");
            throw new OrderException("没有选中购物车商品");
        }
        //获取实时总价
        BigDecimal currentTotalPrice = items.stream().map(item -> {
            Resp<SkuInfoEntity> skuInfoEntityResp = pmsClient.querySkuBySkuId(item.getSkuId());
            SkuInfoEntity skuInfoEntity = skuInfoEntityResp.getData();
            if (skuInfoEntity != null) {
                return skuInfoEntity.getPrice().multiply(new BigDecimal(item.getCount()));
            }
            return new BigDecimal(0);
        }).reduce((bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2)).get();
        log.info("获取到的实时总价为：[{}]",currentTotalPrice);
        if (currentTotalPrice.compareTo(totalPrice) != 0) {
            log.info("提交价格与实时价格不相等");
            throw new OrderException("页面已过期，请刷新页面后在提交");
        }
        log.info("校验库存，并锁定库存");
        //校验库存，并锁定库存，并提示库存不够的所有商品
        List<SkuLockVO> lockVOS = items.stream().map(orderItemVo -> {
            SkuLockVO skuLockVO = new SkuLockVO();
            skuLockVO.setSkuId(orderItemVo.getSkuId());
            skuLockVO.setCount(orderItemVo.getCount());
            return skuLockVO;
        }).collect(Collectors.toList());

        Resp<Object> wareStoreResp = wmsClient.checkAndLockStore(lockVOS);
        if (wareStoreResp.getCode() == 0) {
            throw new OrderException(wareStoreResp.getMsg());
        }
        log.info("创建订单（订单详情）");
        //下单 创建订单（订单详情）
        orderSubmitVO.setUserId(userInfo.getUserId());
        try {
            Resp<OrderEntity> orderEntityResp = omsClient.saveOrder(orderSubmitVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OrderException("服务器错误，创建订单失败！");
        }


        //删除购物车 发送消息删除购物车

    }
}
