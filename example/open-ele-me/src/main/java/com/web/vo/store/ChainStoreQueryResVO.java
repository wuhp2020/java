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
public class ChainStoreQueryResVO extends ChainStoreBaseResVO {

    @ApiModelProperty(value = "门店联系方式")
    private String contact_phone;

    @ApiModelProperty(value = "门店类目")
    private int category_id;

    @ApiModelProperty(value = "门店拥有人姓名")
    private String owner_name;

    @ApiModelProperty(value = "门店拥有人身份证号")
    private String owner_id_num;

    @ApiModelProperty(value = "门店拥有人手持身份证、营业执照图片")
    private String handheld_licence_pic_hash;

    @ApiModelProperty(value = "身份证正面")
    private String owner_id_pic_front_hash;

    @ApiModelProperty(value = "身份证反面")
    private String owner_id_pic_back_hash;

    @ApiModelProperty(value = "统一社会信用代码")
    private String credit_code;

    @ApiModelProperty(value = "营业执照图片")
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

    @ApiModelProperty(value = "门店结算方式: 1:实时结算, 2:账期结算")
    private int settlement_model;

    @ApiModelProperty(value = "门店结算账号id")
    private int settlement_account_id;
}
