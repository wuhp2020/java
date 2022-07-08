package com.web.entity;

import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wuheping
 * @since 2022-07-08
 */
@Data
@TableName("order_info")
@ApiModel(value="OrderInfoEntity对象", description="")
public class OrderInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @Excel(name = "订单号")
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @Excel(name = "取货码")
    @ApiModelProperty(value = "取货码")
    private String takeGoodsCode;

    @Excel(name = "渠道来源编码")
    @ApiModelProperty(value = "渠道来源编码")
    private String channelSourceCode;

    @Excel(name = "渠道来源名称")
    @ApiModelProperty(value = "渠道来源名称")
    private String channelSourceName;

    @Excel(name = "商家编码")
    @ApiModelProperty(value = "商家编码")
    private String businessCode;

    @Excel(name = "商家名称")
    @ApiModelProperty(value = "商家名称")
    private String businessName;

    @Excel(name = "商家手机号")
    @ApiModelProperty(value = "商家手机号")
    private String businessPhone;

    @Excel(name = "商品金额")
    @ApiModelProperty(value = "商品金额")
    private BigDecimal money;

    @Excel(name = "用户名")
    @ApiModelProperty(value = "用户名")
    private String userName;

    @Excel(name = "配送门店编码")
    @ApiModelProperty(value = "配送门店编码")
    private String storeCode;

    @Excel(name = "配送门店名称")
    @ApiModelProperty(value = "配送门店名称")
    private String storeName;

    @Excel(name = "是否支付")
    @ApiModelProperty(value = "是否支付: 1-是, 0-否")
    private String isPay;

    @Excel(name = "配送方式")
    @ApiModelProperty(value = "配送方式")
    private String deliveryType;

    @Excel(name = "配送公司编码")
    @ApiModelProperty(value = "配送公司编码")
    private String deliveryCompanyCode;

    @Excel(name = "配送公司名称")
    @ApiModelProperty(value = "配送公司名称")
    private String deliveryCompanyName;

    @Excel(name = "订单状态")
    @ApiModelProperty(value = "订单状态")
    private String status;

    @Excel(name = "订单收货地址")
    @ApiModelProperty(value = "订单收货地址")
    private String address;

    @Excel(name = "订单创建时间")
    @ApiModelProperty(value = "订单创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Excel(name = "订单修改时间")
    @ApiModelProperty(value = "订单修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
