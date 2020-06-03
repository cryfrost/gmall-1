package com.oyyo.gmall.auth.feign;

import com.oyyo.gmall.ums.api.GmallUmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName: GmallUmsClient
 * @Description: TODO
 * @Author: LiKui
 * @Date: 2020-6-3 18:04
 * @Version: 1.0
 */
@FeignClient("ums-service")
public interface GmallUmsClient extends GmallUmsApi {
}