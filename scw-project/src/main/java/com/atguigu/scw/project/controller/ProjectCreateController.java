package com.atguigu.scw.project.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.atguigu.scw.project.bean.TReturn;
import com.atguigu.scw.project.component.OssCompont;
import com.atguigu.scw.project.contants.ProjectConstant;
import com.atguigu.scw.project.service.TPeojectService;
import com.atguigu.scw.project.vo.rep.BaseVo;
import com.atguigu.scw.project.vo.rep.ProjectBaseInfoVo;
import com.atguigu.scw.project.vo.rep.ProjectRedisStorageVo;
import com.atguigu.scw.project.vo.rep.ProjectReturnVo;
import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.user.enums.ProjectStatusEnume;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.spring.web.json.Json;

@Slf4j
@Api(tags = "项目发起模块")
@RequestMapping("/project/create")
@RestController
public class ProjectCreateController {

	@Autowired
	OssCompont ossCompont;
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	TPeojectService projectService;
	
	@ApiOperation(value = "1-项目初始创建")
	@PostMapping("/init")
	public AppResponse<Object> init(BaseVo vo) {
		
		try {
			String accessToken = vo.getAccessToken();
			//先判断accessToken
			if (StringUtils.isEmpty(accessToken)) {
				AppResponse response = AppResponse.fail(null);
				response.setMsg("必须提供accesstoken值");
				return response;
			}
			//判断是否登录
			String memerId = stringRedisTemplate.opsForValue().get(accessToken);
			if (StringUtils.isEmpty(memerId)) {
				AppResponse response = AppResponse.fail(null);
				response.setMsg("请先登录在发布项目");
				return response;
			}
			//小vo对拷到大vo，并且把token值存到redis数据库里面
			ProjectRedisStorageVo bigVO = new ProjectRedisStorageVo();
			BeanUtils.copyProperties(vo, bigVO);
			String projectToken = UUID.randomUUID().toString().replaceAll("-", "");
			bigVO.setProjectToken(projectToken);
			String bigStr = JSON.toJSONString(bigVO);//fastjson
			
			stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken, bigStr);
			log.debug("大VO数据 {}",bigVO);
			return AppResponse.ok(bigVO);//jakson
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AppResponse.fail(null);
		}
	}
	
	@ApiOperation(value = "2-项目基本信息")
	@PostMapping("/baseinfo")
	public AppResponse<Object> baseinfo(ProjectBaseInfoVo vo) {
		
		try {
			String accessToken = vo.getAccessToken();
			//先判断accessToken
			if (StringUtils.isEmpty(accessToken)) {
				AppResponse response = AppResponse.fail(null);
				response.setMsg("必须提供accesstoken值");
				return response;
			}
			//判断是否登录
			String memerId = stringRedisTemplate.opsForValue().get(accessToken);
			if (StringUtils.isEmpty(memerId)) {
				AppResponse response = AppResponse.fail(null);
				response.setMsg("请先登录在发布项目");
				return response;
			}
			//小vo对拷到大vo，并且把token值存到redis数据库里面
			String bigStr = stringRedisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX+vo.getProjectToken());
			ProjectRedisStorageVo bigVo = JSON.parseObject(bigStr, ProjectRedisStorageVo.class);
			BeanUtils.copyProperties(vo, bigVo);
			bigStr = JSON.toJSONString(bigVo);
			log.debug("大VO数据 {}",bigVo);
			stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+vo.getProjectToken(),  bigStr);
			return AppResponse.ok(bigVo);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("表单数据处理失败{}",e.getMessage());
			return AppResponse.fail(null);
			
		}
		
	}
	
	@ApiOperation(value = "添加项目回报档位")
	@PostMapping("/return")
	public AppResponse<Object> returnDetail(@RequestBody List<ProjectReturnVo> pro) {
		
		try {
			String accessToken = pro.get(0).getAccessToken();
			//先判断accessToken
			if (StringUtils.isEmpty(accessToken)) {
				AppResponse response = AppResponse.fail(null);
				response.setMsg("必须提供accesstoken值");
				return response;
			}
			//判断是否登录
			String memerId = stringRedisTemplate.opsForValue().get(accessToken);
			if (StringUtils.isEmpty(memerId)) {
				AppResponse response = AppResponse.fail(null);
				response.setMsg("请先登录在发布项目");
				return response;
			}
			//小vo对拷到大vo，并且把token值存到redis数据库里面
			String bigStr = stringRedisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX+pro.get(0).getProjectToken());
			ProjectRedisStorageVo bigVo = JSON.parseObject(bigStr, ProjectRedisStorageVo.class);
			List<TReturn> projectReturns = bigVo.getProjectReturns();
			for (ProjectReturnVo projectReturnVo : pro) {
				TReturn returnObj = new TReturn();
				BeanUtils.copyProperties(projectReturnVo, returnObj);
				projectReturns.add(returnObj);
			}
			
			bigStr = JSON.toJSONString(bigVo);
			log.debug("大VO数据 {}",bigVo);
			stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+pro.get(0).getProjectToken(),  bigStr);
			return AppResponse.ok(bigVo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("表单数据处理失败{}",e.getMessage());
			return AppResponse.fail(null);
		}
		
	}
	
//	@ApiOperation(value = "删除项目回报档位")
//	@DeleteMapping("/return")
//	public AppResponse<Object> deleteReturnDetail() {
//		return AppResponse.ok("ok");
//	}
	/*
	 * 文件上传表单要求
	 * 1.method="post"
	 * 2.enctype="multipart/from-data"
	 * 3.type="file" name="uploadfile"
	 * 
	 * SpringMvc框架集成commons-fileupload和commons-io组件，完成文件上传功能
	 * SpringMvc提供文件上传解析器
	 * Controller处理文件上传时，通过MultipartFile接受文件
	 * 文件上传必须加注解
	 * */
	@ApiOperation(value = "上传图片")
	@PostMapping("/upload")
	public AppResponse<Object> upload(@RequestParam("uploadfile") MultipartFile[] files) {
		List<String> filepathList = new ArrayList<String>();
		try {
			for(MultipartFile multipartFile : files) {
				String filename = multipartFile.getOriginalFilename();
				filename = UUID.randomUUID().toString().replaceAll("-", "")+"_"+filename;
				String filepath = ossCompont.load(filename, multipartFile.getInputStream());
				filepathList.add(filepath);
			}
			log.debug("上传文件成功-{}",filepathList);
			return AppResponse.ok(filepathList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("上传文件异常");
			e.printStackTrace();
			return AppResponse.fail(null);
		}
	}
	
//	@ApiOperation(value = "确认项目法人信息")
//	@PostMapping("/confirm/legal")
//	public AppResponse<Object> legal() {
//		return AppResponse.ok("ok");
//	}
	
//	@ApiOperation(value = "项目草稿保存")
//	@PostMapping("/tempsave")
//	public AppResponse<Object> tempsave() {
//		return AppResponse.ok("ok");
//	}
	
	@ApiOperation(value = "项目提交审核申请")
	@PostMapping("/submit")
	public AppResponse<Object> submit(String accessToken,String projectToken,String ops) {
		
				//先判断accessToken
		try {
			if (StringUtils.isEmpty(accessToken)) {
				AppResponse response = AppResponse.fail(null);
				response.setMsg("必须提供accesstoken值");
				return response;
			}
			//判断是否登录
			String memerId = stringRedisTemplate.opsForValue().get(accessToken);
			if (StringUtils.isEmpty(memerId)) {
				AppResponse response = AppResponse.fail(null);
				response.setMsg("请先登录在发布项目");
				return response;
			}
			if ("0".equals(ops)) {
				projectService.saveProject(accessToken,projectToken,ProjectStatusEnume.DRAFT.getCode());
				return AppResponse.ok("ok");
			}else if("1".equals(ops)) {
				projectService.saveProject(accessToken,projectToken,ProjectStatusEnume.SUBMIT_AUTH.getCode());
				return AppResponse.ok("ok");
			}else {
				log.error("请求方式不支持");
				return AppResponse.fail("请求方式不支持");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("项目操作失败-{}",e.getMessage());
			e.printStackTrace();
			return AppResponse.fail(null);
		}
		
	}
	
}
