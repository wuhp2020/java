package com.web.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/27
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class GetOrderDetailResVO {

    @ApiModelProperty(value = "订单id")
    private long order_id;

    @ApiModelProperty(value = "运单id")
    private long tracking_id;

    @ApiModelProperty(value = "外部订单号")
    private String partner_order_code;

    @ApiModelProperty(value = "订单状态: 0-订单生成, 1-运单生成成功, 20-骑手接单, 80-骑手到店, 2-配送中, 3-已完成, 4-已取消, 5-配送异常")
    private int order_status;

    @ApiModelProperty(value = "配送员ID")
    private String carrier_driver_id;

    @ApiModelProperty(value = "配送员姓名")
    private String carrier_driver_name;

    @ApiModelProperty(value = "配送员电话")
    private String carrier_driver_phone;

    @ApiModelProperty(value = "预计送达时间 毫秒")
    private long estimate_arrive_time;

    @ApiModelProperty(value = "时效赔付")
    private long overtime_compensation_cost_cent;

    @ApiModelProperty(value = "是否支持添加调度费: 1可以, 0不可以")
    private int if_can_add_tip;

    @ApiModelProperty(value = "订单当前小费总金额 分")
    private long order_tip_amount_cent;

    @ApiModelProperty(value = "骑手取货照片地址")
    private List<String> delivery_fetch_photos;

    @ApiModelProperty(value = "订单实际配送支付总金额 分")
    private long order_actual_amount_cent;

    @ApiModelProperty(value = "配送费价格明细")
    private PriceDetailResVO price_detail;

    @ApiModelProperty(value = "运单异常原因code")
    private String abnormal_code;

    @ApiModelProperty(value = "运单异常原因描述")
    private String abnormal_desc;

    @ApiModelProperty(value = "运单事件节点信息")
    private List<GetOrderEventLogDetailResVO> event_log_details;

    @ApiModelProperty(value = "投诉编号")
    private long complaint_id;

    @ApiModelProperty(value = "投诉原因描述")
    private String complaint_reason_desc;

    @ApiModelProperty(value = "投诉状态: 1待处理, 2成功, 3失败")
    private int complaint_status;

    @ApiModelProperty(value = "索赔id")
    private long claim_id;

    @ApiModelProperty(value = "索赔原因描述")
    private String claim_reason_desc;

    @ApiModelProperty(value = "索赔状态: 1待处理, 2成功, 3失败")
    private int claim_status;

    @ApiModelProperty(value = "骑手体温")
    private String temperature;

    @ApiModelProperty(value = "配送距离 米")
    private double order_distance;

}
