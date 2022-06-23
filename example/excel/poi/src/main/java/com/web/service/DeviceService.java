package com.web.service;

import com.google.common.collect.Lists;
import com.web.vo.device.DeviceVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DeviceService {

    public List<DeviceVO> load(MultipartFile file) throws Exception{
        List<DeviceVO> deviceVOs = Lists.newArrayList();
        Workbook sheets = WorkbookFactory.create(file.getInputStream());
        Sheet firstSheet = sheets.getSheetAt(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Row rows : firstSheet) {
            if (rows.getRowNum() == 0) {
                continue;
            }
            Cell idCell = rows.getCell(0);
            Cell nameCell = rows.getCell(1);
            Cell createDateCell = rows.getCell(2);
            DeviceVO deviceVO = new DeviceVO();
            deviceVO.setId(idCell.getStringCellValue());
            deviceVO.setName(nameCell.getStringCellValue());
            deviceVO.setCreateDate(sdf.parse(createDateCell.getStringCellValue()));
            deviceVOs.add(deviceVO);
        }
        return deviceVOs;
    }
}
