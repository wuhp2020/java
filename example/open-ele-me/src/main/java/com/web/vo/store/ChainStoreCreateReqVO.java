package com.web.vo.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/27
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class ChainStoreCreateReqVO {


    @ApiModelProperty(value = "门店主店名", example = "上海陆家嘴餐饮")
    private String head_shop_name;

    @ApiModelProperty(value = "门店分店名", example = "上海陆家嘴餐饮")
    private String branch_shop_name;

    @ApiModelProperty(value = "门店类型: 1:正式门店, 2:测试门店, 默认为1", example = "1")
    private int chainstore_type;

    @ApiModelProperty(value = "门店联系方式", example = "18811892365")
    private String contact_phone;

    @ApiModelProperty(value = "门店地址", example = "上海陆家嘴100号")
    private String address;

    @ApiModelProperty(value = "门店经度", example = "116.473531")
    private double longitude;

    @ApiModelProperty(value = "门店纬度", example = "39.964274")
    private double latitude;

    @ApiModelProperty(value = "经纬度来源: 3:高德地图", example = "3")
    private int position_source;

    @ApiModelProperty(value = "外部门店编码", example = "18811892365")
    private String out_shop_code;

    @ApiModelProperty(value = "门店类目", example = "11")
    private int category_id;

    @ApiModelProperty(value = "门店拥有人姓名", example = "wuhp")
    private String owner_name;

    @ApiModelProperty(value = "门店拥有人身份证号", example = "110101199003070310")
    private String owner_id_num;

    @ApiModelProperty(value = "门店拥有人手持身份证、营业执照图片", example = "cff90625-e5c6-445c-9e02-88d5b6e1978166226.png")
    private String handheld_licence_pic_hash;

    @ApiModelProperty(value = "身份证正面", example = "cff90625-e5c6-445c-9e02-88d5b6e1978166226.png")
    private String owner_id_pic_front_hash;

    @ApiModelProperty(value = "身份证反面", example = "cff90625-e5c6-445c-9e02-88d5b6e1978166226.png")
    private String owner_id_pic_back_hash;

    @ApiModelProperty(value = "统一社会信用代码", example = "ETE684189001015688")
    private String credit_code;

    @ApiModelProperty(value = "营业执照图片", example = "cff90625-e5c6-445c-9e02-88d5b6e1978166226.png")
    private String business_licence_pic_hash;

    @ApiModelProperty(value = "食品安全执照图片")
    private String food_license_pic_hash;

    @ApiModelProperty(value = "第二类医疗器械经营备案证明图片")
    private String second_medical_equipment_license_pic_hash;

    @ApiModelProperty(value = "医疗机构执业许可证图片")
    private String medical_institution_license_pic_hash;

    @ApiModelProperty(value = "医疗器械经营许可证图片")
    private String medical_equipment_license_pic_hash;

    @ApiModelProperty(value = "药品经营许可证")
    private String medicine_license_pic_hash;

    @ApiModelProperty(value = "烟草经营许可证图片")
    private String tabacoo_license_pic_hash;

    @ApiModelProperty(value = "门店结算方式: 1:实时结算, 2:账期结算", example = "1")
    private int settlement_model;

    @ApiModelProperty(value = "门店结算账号id")
    private int settlement_account_id;

}
