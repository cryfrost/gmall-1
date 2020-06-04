package com.oyyo.gmall.cart.service;

import com.alibaba.fastjson.JSON;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.cart.feign.GmallPmsClient;
import com.oyyo.gmall.cart.feign.GmallSmsClient;
import com.oyyo.gmall.cart.feign.GmallWmsClient;
import com.oyyo.gmall.cart.interceptors.LoginInterceptor;
import com.oyyo.gmall.cart.vo.CartVO;
import com.oyyo.gmall.cart.vo.UserInfo;
import com.oyyo.gmall.pms.entity.SkuInfoEntity;
import com.oyyo.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.oyyo.gmall.sms.vo.SalseVO;
import com.oyyo.gmall.wms.entity.WareSkuEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: CartService
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-6-4 9:51
 * @Version: 1.0
 */
@Service
@Slf4j
public class CartService {

    @Autowired
    private GmallPmsClient pmsClient;
    @Autowired
    private GmallSmsClient smsClient;
    @Autowired
    private GmallWmsClient wmsClient;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "gmall:cart:";

    /**
     * 添加购物车
     * @param cartVO
     * @return
     */
    public Boolean addCart(CartVO cartVO) {

        String key = KEY_PREFIX;

        //获取用户登录信息
        UserInfo userInfo = LoginInterceptor.getThreadLoclUserInfo();
        if (userInfo.getUserId() != null) {
            //已登录
            log.info("用户已登录");
            key += userInfo.getUserId();
        } else {
            //未登录
            log.info("用户未登录");
            key += userInfo.getUserKey();
        }
        String skuId = cartVO.getSkuId().toString();
        Integer count = cartVO.getCount();
        //购物车业务逻辑
        //1.获取该用户购物车
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        //2. 判断购物车中是否有该记录
        Boolean hasKey = hashOps.hasKey(skuId);
        //3.有则更新，无则添加
        if (hasKey != null && hasKey) {
            //获取 sku 商品记录
            String cartJson = hashOps.get(skuId).toString();
            //反序列化
            cartVO = JSON.parseObject(cartJson, CartVO.class);

            log.info("有skuId为 [{}] 的商品信息,数量为：[{}]",skuId,cartVO.getCount());
            //更新数量
            cartVO.setCount(cartVO.getCount() + count);

            log.info("更新后的数量为：[{}]",cartVO.getCount() + count);

        } else {
            cartVO.setCheck(true);
            //sku 属性

            Resp<SkuInfoEntity> skuInfoEntityResp = pmsClient.querySkuBySkuId(cartVO.getSkuId());
            SkuInfoEntity skuInfoEntity = skuInfoEntityResp.getData();
            if (skuInfoEntity == null) {
                log.info("商品不存在,skuId 为：[{}]",cartVO.getSkuId());
                return false;
            }

            cartVO.setDefaultImage(skuInfoEntity.getSkuDefaultImg());
            cartVO.setPrice(skuInfoEntity.getPrice());
            cartVO.setTitle(skuInfoEntity.getSkuTitle());
            //sku销售属性
            Resp<List<SkuSaleAttrValueEntity>> salesAttrValuesBySkuId = pmsClient.querySkuSalesAttrValuesBySkuId(cartVO.getSkuId());
            List<SkuSaleAttrValueEntity> saleAttrValue = salesAttrValuesBySkuId.getData();
            cartVO.setSaleAttrValues(saleAttrValue);

            //营销信息
            Resp<List<SalseVO>> salseResp = smsClient.querySkuSalseBySkuId(cartVO.getSkuId());
            List<SalseVO> salseVOList = salseResp.getData();
            cartVO.setSales(salseVOList);

            //库存信息
            Resp<List<WareSkuEntity>> wareResp = wmsClient.queryWareSkuBySkuId(cartVO.getSkuId());
            List<WareSkuEntity> wareSkuEntities = wareResp.getData();
            if (!CollectionUtils.isEmpty(wareSkuEntities)) {
                cartVO.setStore(wareSkuEntities.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() > 0));
            }
        }
        //重新写入
        log.info("写入购物车");
        hashOps.put(skuId,JSON.toJSONString(cartVO));
        return true;
    }

    /**
     * 查询购物车
     * @return
     */
    public List<CartVO> queryCarts() {
        //获取登录状态
        UserInfo userInfo = LoginInterceptor.getThreadLoclUserInfo();

        //查询未登录状态 购物车
        String unLoginKey = KEY_PREFIX + userInfo.getUserKey();
        BoundHashOperations<String, Object, Object> unLoginHashOps = redisTemplate.boundHashOps(unLoginKey);
        List<Object> cartJsonList = unLoginHashOps.values();
        List<CartVO> unLoginCarts = null;
        if (!CollectionUtils.isEmpty(cartJsonList)) {
            unLoginCarts = cartJsonList.stream().map(cartJson -> JSON.parseObject(cartJson.toString(), CartVO.class)).collect(Collectors.toList());
        }

        //判断是否登录
        if (userInfo.getUserId() == null) {
            //未登录 返回
            log.info("未登录，返回未登录状态购物车信息");
            return unLoginCarts;
        }
        //已登录，购物车同步
        log.info("已登录，同步未登录购物车信息至已登录购物车");
        String loginKey = KEY_PREFIX + userInfo.getUserId();
        BoundHashOperations<String, Object, Object> loginHashOps = redisTemplate.boundHashOps(loginKey);

        //判断未登录购物车是否为空
        if (!CollectionUtils.isEmpty(unLoginCarts)) {
            unLoginCarts.forEach(cartVO -> {
                Integer count = cartVO.getCount();
                if (loginHashOps.hasKey(cartVO.getSkuId().toString())) {
                    String cartJson = loginHashOps.get(cartVO.getSkuId().toString()).toString();
                    cartVO = JSON.parseObject(cartJson, CartVO.class);
                    cartVO.setCount(cartVO.getCount() + count);
                }
                loginHashOps.put(cartVO.getSkuId().toString(),JSON.toJSONString(cartVO));
                unLoginHashOps.put(unLoginKey,JSON.toJSONString(null));
                log.info("同步完毕，删除未登录购物车信息");
            });
        }


        return null;
    }
}