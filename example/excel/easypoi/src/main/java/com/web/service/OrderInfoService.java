package com.web.service;

import com.web.entity.OrderInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wuheping
 * @since 2022-07-08
 */
public interface OrderInfoService extends IService<OrderInfoEntity> {

    public void load(MultipartFile file) throws Exception ;
}
