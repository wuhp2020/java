package com.web.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.web.entity.OrderInfoEntity;
import com.web.mapper.OrderInfoMapper;
import com.web.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wuheping
 * @since 2022-07-08
 */
@Service
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfoEntity> implements OrderInfoService {

    @Override
    public void load(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        //设置表头占行
        params.setHeadRows(1);
        params.setTitleRows(1);
        List<OrderInfoEntity> orderInfoEntityList = ExcelImportUtil.importExcel(file.getInputStream(), OrderInfoEntity.class, params);
        log.info("======== 加载完成 ========");
    }
}
