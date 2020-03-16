package com.atguigu.scw.project.vo.rep;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ProjectReturnVo extends BaseVo {

	private String projectToken;// 项目的临时token
	// type supportmoney content count signalpurchase purchase freight invoice
	// rtndate
	@ApiModelProperty(value = "回报类型：  0-虚拟回报   1-实物回报", required = true)
	private String type;
	
	@ApiModelProperty(value = "支持金额", required = true)
	private Integer supportmoney;
	
	@ApiModelProperty(value = "回报内容", required = true)
	private String content;
	
	@ApiModelProperty(value = "回报数量", required = true)
	private Integer count;
	
	@ApiModelProperty(value = "单笔限购", required = true)
	private Integer signalpurchase;
	
	@ApiModelProperty(value = "限购数量", required = true)
	private Integer purchase;
	
	@ApiModelProperty(value = "运费", required = true)
	private Integer freight;
	
	@ApiModelProperty(value = "发票  0-不开发票  1-开发票", required = true)
	private String invoice;
	
	@ApiModelProperty(value = "回报时间，天数", required = true)
	private Integer rtndate;
}
