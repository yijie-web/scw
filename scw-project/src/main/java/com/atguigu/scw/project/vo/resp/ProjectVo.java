package com.atguigu.scw.project.vo.resp;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;


@Data
public class ProjectVo {

	private Integer memberid;// 会员id
	
	private Integer id;
//	
//	//第二步：收集项目基本信息以及发起人信息（暂时不做）
	private List<Integer> typeids; // 项目的分类id
	private List<Integer> tagids; // 项目的标签id
//	
	private String name;// 项目名称
	private String remark;// 项目简介
	
	
	private String headerImage;// 项目头部图片
	private List<String> detailsImage =  new ArrayList<String>();;// 项目详情图片
	// 发起人信息：自我介绍，详细自我介绍，联系电话，客服电话
	
	
}
