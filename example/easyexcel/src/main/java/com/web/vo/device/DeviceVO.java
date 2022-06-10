package com.web.vo.device;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.metadata.BaseRowModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceVO extends BaseRowModel {

    @ColumnWidth(10)
    @ApiModelProperty(value = "设备id")
    @ExcelProperty(value = "设备id", index = 0)
    private String id;

    @ColumnWidth(10)
    @ApiModelProperty(value = "设备名称")
    @ExcelProperty(value = "设备名称", index = 1)
    private String name;

    @ColumnWidth(10)
    @ApiModelProperty(value = "生产日期")
    @ExcelProperty(value = "生产日期", index = 2)
    private Date createDate;

}
