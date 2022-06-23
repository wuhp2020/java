package com.web.service;

import com.google.common.collect.Lists;
import com.web.util.ExcelUtil;
import com.web.vo.device.DeviceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@Slf4j
public class DeviceService {

    public List<DeviceVO> load(MultipartFile file) throws Exception{
        List<DeviceVO> deviceVOS = ExcelUtil.readExcel(file, DeviceVO.class, 1, 1);
        return deviceVOS;
    }

    public List<DeviceVO> export() throws Exception{
        List<DeviceVO> deviceVOs = Lists.newArrayList();

        return deviceVOs;
    }
}
