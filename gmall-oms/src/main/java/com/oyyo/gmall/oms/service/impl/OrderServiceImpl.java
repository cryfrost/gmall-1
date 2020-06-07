package com.oyyo.gmall.oms.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oyyo.core.bean.PageVo;
import com.oyyo.core.bean.Query;
import com.oyyo.core.bean.QueryCondition;
import com.oyyo.core.bean.Resp;
import com.oyyo.gmall.oms.dao.OrderDao;
import com.oyyo.gmall.oms.dao.OrderItemDao;
import com.oyyo.gmall.oms.entity.OrderEntity;
import com.oyyo.gmall.oms.entity.OrderItemEntity;
import com.oyyo.gmall.oms.feign.GmallPmsClient;
import com.oyyo.gmall.oms.feign.GmallUmsClient;
import com.oyyo.gmall.oms.service.OrderService;
import com.oyyo.gmall.oms.vo.OrderItemVo;
import com.oyyo.gmall.oms.vo.OrderSubmitVO;
import com.oyyo.gmall.pms.entity.SkuInfoEntity;
import com.oyyo.gmall.pms.entity.SpuInfoEntity;
import com.oyyo.gmall.ums.entity.MemberEntity;
import com.oyyo.gmall.ums.entity.MemberReceiveAddressEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    private GmallUmsClient umsClient;

    @Autowired
    private GmallPmsClient pmsClient;
    @Autowired
    private OrderItemDao orderItemDao;


    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageVo(page);
    }

    /**
     * 保存订单
     * @param submitVO
     * @return
     */
    @Override
    public OrderEntity saveOrder(OrderSubmitVO submitVO) {
        //保存订单
        OrderEntity orderEntity = new OrderEntity();
        //设置收货地址
        MemberReceiveAddressEntity address = submitVO.getAddress();
        orderEntity.setReceiverRegion(address.getRegion());
        orderEntity.setReceiverProvince(address.getProvince());
        orderEntity.setReceiverPostCode(address.getPostCode());
        orderEntity.setReceiverPhone(address.getPhone());
        orderEntity.setReceiverName(address.getName());
        orderEntity.setReceiverDetailAddress(address.getDetailAddress());
        orderEntity.setReceiverCity(address.getCity());

        //查询用户信息
        Resp<MemberEntity> memberEntityResp = umsClient.queryMemberById(submitVO.getUserId());
        MemberEntity memberEntity = memberEntityResp.getData();
        orderEntity.setMemberUsername(memberEntity.getUsername());
        orderEntity.setMemberId(submitVO.getUserId());
        //清算每个商品赠送的积分之和
        orderEntity.setIntegration(0);
        orderEntity.setGrowth(0);
        orderEntity.setDeleteStatus(0);
        orderEntity.setStatus(0);
        orderEntity.setConfirmStatus(0);
        orderEntity.setCreateTime(new Date());
        orderEntity.setModifyTime(orderEntity.getCreateTime());
        orderEntity.setAutoConfirmDay(0);
        orderEntity.setDeliveryCompany(submitVO.getDeliveryCompany());
        orderEntity.setSourceType(1);
        orderEntity.setPayType(submitVO.getPayType());
        orderEntity.setTotalAmount(submitVO.getTotalPrice());
        orderEntity.setOrderSn(submitVO.getOrderToken());
        orderEntity.setPayAmount(submitVO.getTotalPrice());
        orderEntity.setDeliverySn(IdWorker.getIdStr());
        orderEntity.setAutoConfirmDay(15);

        //保存订单
        save(orderEntity);

        //保存订单详情
        Long orderId = orderEntity.getId();
        List<OrderItemVo> items = submitVO.getItems();
        items.forEach(item -> {
            OrderItemEntity itemEntity = new OrderItemEntity();
            itemEntity.setSkuId(item.getSkuId());
            Resp<SkuInfoEntity> skuInfoEntityResp = pmsClient.querySkuBySkuId(item.getSkuId());
            SkuInfoEntity skuInfoEntity = skuInfoEntityResp.getData();
            Resp<SpuInfoEntity> spuInfoEntityResp = pmsClient.querySpuById(skuInfoEntity.getSpuId());
            SpuInfoEntity spuInfoEntity = spuInfoEntityResp.getData();
            itemEntity.setSkuPrice(skuInfoEntity.getPrice());
            itemEntity.setSkuAttrsVals(JSON.toJSONString(item.getSaleAttrValues()));
            itemEntity.setCategoryId(skuInfoEntity.getCatalogId());
            itemEntity.setOrderId(orderId);
            itemEntity.setOrderSn(submitVO.getOrderToken());
            itemEntity.setSpuId(spuInfoEntity.getId());
            itemEntity.setSkuName(skuInfoEntity.getSkuName());
            itemEntity.setSkuPic(skuInfoEntity.getSkuDefaultImg());
            itemEntity.setSkuQuantity(item.getCount());
            itemEntity.setSpuBrand(spuInfoEntity.getBrandId().toString());
            itemEntity.setSpuName(spuInfoEntity.getSpuName());

            orderItemDao.insert(itemEntity);
        });

        return orderEntity;
    }

}