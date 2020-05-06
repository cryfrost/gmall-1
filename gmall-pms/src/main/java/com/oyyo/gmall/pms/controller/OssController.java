package com.oyyo.gmall.pms.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.oyyo.core.bean.Resp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName: OssController
 * @Description: 直接上传图片到 阿里云 oss
 * @Author: LiKui
 * @Date: 2020-4-25 13:10
 * @Version: 1.0
 */
@RestController
@RequestMapping("pms/oss")
@PropertySource("classpath:myConfigProperties/myconfig.properties")
public class OssController {
    @Value("${oos.accessId}")
    private String accessId;
    @Value("${oos.accessKey}")
    private String accessKey;
    @Value("${oos.endpoint}")
    private String endpoint;
    @Value("${oos.bucket}")
    private String bucket;

    @GetMapping("policy")
    public Resp<Object> policy(){
//        String accessId = "LTAI4GELXCTX49Lzj2QyGeNv"; // 请填写您的AccessKeyId。
//        String accessKey = "xviW2tUWJRrEcHZnCLgEmFnnDHr672"; // 请填写您的AccessKeySecret。
//        String endpoint = "oss-cn-beijing.aliyuncs.com"; // 请填写您的 endpoint。
//        String bucket = "bucket"; // 请填写您的 bucketname 。
        String host = "https://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint
        // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
//        String callbackUrl = "http://88.88.88.88:8888";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-YY-dd");
        simpleDateFormat.format(new Date());
        String dir = simpleDateFormat.format(new Date()); // 用户上传文件时指定的前缀。

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));
            return Resp.ok(respMap);

        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }

        return Resp.ok(null);
    }

}