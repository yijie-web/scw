package com.atguigu.scw.project.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.scw.project.bean.TProject;
import com.atguigu.scw.project.bean.TProjectImages;
import com.atguigu.scw.project.bean.TProjectTag;
import com.atguigu.scw.project.bean.TProjectType;
import com.atguigu.scw.project.bean.TReturn;
import com.atguigu.scw.project.service.MeberServiceFeign;
import com.atguigu.scw.project.service.ProjectInfoService;
import com.atguigu.scw.project.vo.resp.ProjectDetailVo;
import com.atguigu.scw.project.vo.resp.ProjectVo;
import com.atguigu.scw.project.vo.resp.ReturnPayConfirmVo;
import com.atguigu.scw.project.vo.resp.TMember;
import com.atguigu.scw.vo.resp.AppResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "项目信息模块")
@RequestMapping("/project")
@RestController
public class ProjectInfoController {
	
	@Autowired
	ProjectInfoService projectInfoService;
	
	@Autowired
	MeberServiceFeign meberServiceFeign;

//	@ApiOperation(value="获取首页广告项目") 
//	@GetMapping("/adv")
//	public AppResponse<Object> adv(){
//		return AppResponse.ok("ok");
//	} 
	
//	@ApiOperation(value="获取首页热门推荐项目") 
//	@GetMapping("/recommend/index")
//	public AppResponse<Object> index(){
//		return AppResponse.ok("ok");
//	} 
	
//	@ApiOperation(value="获取首页分类推荐项目") 
//	@GetMapping("/recommend/type")
//	public AppResponse<Object> type(){
//		return AppResponse.ok("ok");
//	} 
	
	@ApiOperation(value="获取项目总览列表") 
	@GetMapping("/index")
	public AppResponse<List<ProjectVo>> all(){
		
		
		// 1、分步查询，先查出所有项目
				// 2、再查询这些项目图片
				List<ProjectVo> prosVo = new ArrayList<>();

				// 1、连接查询，所有的项目left join 图片表，查出所有的图片
				// left join：笛卡尔积 A*B 1000万*6 = 6000万
				// 大表禁止连接查询；
				List<TProject> pros = projectInfoService.getAllProjects();
				log.debug("项目是-》{}",pros);

				for (TProject tProject : pros) {
					Integer id = tProject.getId();
					List<TProjectImages> images = projectInfoService.getProjectImages(id);
					ProjectVo projectVo = new ProjectVo();
					BeanUtils.copyProperties(tProject, projectVo);

					for (TProjectImages tProjectImages : images) {
						if (tProjectImages.getImgtype() == 0) {
							projectVo.setHeaderImage(tProjectImages.getImgurl());
						} else {
							List<String> detailsImage = projectVo.getDetailsImage();
							detailsImage.add(tProjectImages.getImgurl());
						}
					}
					log.debug("具体某个项目是-》{}",projectVo);
					prosVo.add(projectVo);

				}

				return AppResponse.ok(prosVo);
	} 
	
	@ApiOperation(value="获取项目系统分类信息") 
	@GetMapping("/type")
	public AppResponse<List<TProjectType>> systype(){
		List<TProjectType> types = projectInfoService.getAllProjectTypes();
		return AppResponse.ok(types);
	} 
	
	@ApiOperation(value="获取项目系统标签信息") 
	@GetMapping("/tags")
	public AppResponse<List<TProjectTag>> systags(){
		List<TProjectTag> tags = projectInfoService.getAllProjectTags();
		return AppResponse.ok(tags);
	} 
	
	@ApiOperation(value="获取项目详情信息") 
	@GetMapping("/details/info/{projectId}")
	public AppResponse<ProjectDetailVo> detail(@PathVariable("projectId") Integer projectId){
		
		TProject p = projectInfoService.getProjectInfo(projectId);
		ProjectDetailVo projectVo = new ProjectDetailVo();
		BeanUtils.copyProperties(p, projectVo);

		// 1、查出这个项目的所有图片
		List<TProjectImages> projectImages = projectInfoService.getProjectImages(p.getId());
		for (TProjectImages tProjectImages : projectImages) {
			if (tProjectImages.getImgtype() == 0) {
				projectVo.setHeaderImage(tProjectImages.getImgurl());
			} else {
				List<String> detailsImage = projectVo.getDetailsImage();
				detailsImage.add(tProjectImages.getImgurl());
			}
			List<TReturn> returns = projectInfoService.getProjectReturns(p.getId());
			projectVo.setProjectReturns(returns);
		}

		return AppResponse.ok(projectVo);
	} 
	
	@ApiOperation(value="获取项目回报档位信息") 
	@GetMapping("/details/returns/{projectId}")
	public AppResponse<List<TReturn>> support(@PathVariable("projectId") Integer projectId){
		List<TReturn> returns = projectInfoService.getProjectReturns(projectId);
		return AppResponse.ok(returns);
		
	} 	
	
	@ApiOperation("[+]获取项目某个回报档位信息")
	@GetMapping("/details/returns/info/{returnId}")
	public AppResponse<TReturn> returnInfo(@PathVariable("returnId") Integer returnId) {
		TReturn tReturn = projectInfoService.getProjectReturnById(returnId);
		return AppResponse.ok(tReturn);
	}
	
	@ApiOperation("[+]确认回报信息")
	@GetMapping("/confirm/returns/{projectId}/{returnId}")
	public AppResponse<ReturnPayConfirmVo> returnInfo(@PathVariable("projectId")Integer projectId,@PathVariable("returnId") Integer returnId) {
		ReturnPayConfirmVo vo = new ReturnPayConfirmVo();
		TReturn treturn = projectInfoService.getProjectReturnById(returnId);
		vo.setReturnId(treturn.getId());
		vo.setReturnContent(treturn.getContent());
		vo.setNum(1);
		vo.setPrice(treturn.getSupportmoney());
		vo.setFreight(treturn.getFreight());
		vo.setSignalpurchase(treturn.getSignalpurchase());
		vo.setPurchase(treturn.getPurchase());
		
		TProject project = projectInfoService.getProjectInfo(projectId);
		Integer memberid = project.getMemberid();
		vo.setProjectId(project.getId());
		vo.setProjectName(project.getName());
		vo.setProjectRemark(project.getRemark());
		
		AppResponse<TMember> response = meberServiceFeign.getMemberInfo(memberid);
		TMember meber = response.getData();
		vo.setMemberId(meber.getId());
		vo.setMemberName(meber.getLoginacct());
		vo.setTotalPrice(new BigDecimal(vo.getNum() * vo.getPrice() + vo.getFreight()));
		
		return AppResponse.ok(vo);
	}

	
}
