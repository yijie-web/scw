package com.atguigu.scw.webui.vo.resp;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ReturnPayConfirmVo implements Serializable {

	// 项目信息
	private Integer projectId;
	private String projectName;
	private String projectRemark;
	// 发起人信息
	private Integer memberId;
	private String memberName;
	// 回报的信息
	private Integer returnId;
	private String returnContent;
	private Integer num; // 支持数量，默认数量1，不能大于signalpurchase单笔限购数量
	private Integer price;// 支持单价
	private Integer freight; //运费
	private Integer signalpurchase;// 单笔限购数量
	private Integer purchase;//总限购数量
	// 所有的double和float的运算在任何情况下都会导致丢失精度，使用BigDecimal进行计算
	private BigDecimal totalPrice;// 总价 totalPrice =price* num+ freight
}
