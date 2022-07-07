package com.web.vo.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ Author : wuheping
 * @ Date   : 2022/4/1
 * @ Desc   : 描述
 */
@Data
public class ChainStoreBaseResVO {
    @ApiModelProperty(value = "门店id")
    private long chain_store_id;

    @ApiModelProperty(value = "门店主店名")
    private String name;

    @ApiModelProperty(value = "门店分店名")
    private String branch_name;

    @ApiModelProperty(value = "门店认证状态: 10-上架审核中, 20-正常(已上架), 30-上架审核失败, 40-已冻结, 50-已下架")
    private int status;

    @ApiModelProperty(value = "门店认证状态描述")
    private String status_desc;

    @ApiModelProperty(value = "门店修改状态: 0-无修改, 10-资料修改审核中, 20-审核通过, 30-a审核驳回")
    private int modify_status;

    @ApiModelProperty(value = "门店修改状态描述")
    private String modify_status_desc;

    @ApiModelProperty(value = "所属商户id")
    private String merchant_id;

    @ApiModelProperty(value = "门店地址")
    private String address;

    @ApiModelProperty(value = "门店经度")
    private double longitude;

    @ApiModelProperty(value = "门店纬度")
    private double latitude;

    @ApiModelProperty(value = "经纬度来源: 1:腾讯地图, 2:百度地图, 3:高德地图")
    private int position_source;

    @ApiModelProperty(value = "外部门店编码")
    private String out_shop_code;

}
