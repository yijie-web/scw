package com.atguigu.scw.project.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.scw.project.bean.TProject;
import com.atguigu.scw.project.bean.TProjectImages;
import com.atguigu.scw.project.bean.TProjectImagesExample;
import com.atguigu.scw.project.bean.TProjectTag;
import com.atguigu.scw.project.bean.TProjectType;
import com.atguigu.scw.project.bean.TReturn;
import com.atguigu.scw.project.bean.TReturnExample;
import com.atguigu.scw.project.mapper.TProjectImagesMapper;
import com.atguigu.scw.project.mapper.TProjectMapper;
import com.atguigu.scw.project.mapper.TProjectTagMapper;
import com.atguigu.scw.project.mapper.TProjectTypeMapper;
import com.atguigu.scw.project.mapper.TReturnMapper;
import com.atguigu.scw.project.service.ProjectInfoService;

@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {

	@Autowired
	TProjectMapper  projectMapper;
	
	@Autowired
	TProjectImagesMapper  projectImagesMapper;
	
	@Autowired
	TProjectTypeMapper  projectTypeMapper;
	
	@Autowired
	TProjectTagMapper  projectTagMapper;
	
	@Autowired
	TReturnMapper  returnMapper;
	
	@Override
	public List<TProject> getAllProjects() {
		// TODO Auto-generated method stub
		return projectMapper.selectByExample(null);
	}

	@Override
	public List<TProjectImages> getProjectImages(Integer id) {
		
		TProjectImagesExample example = new TProjectImagesExample();
		example.createCriteria().andProjectidEqualTo(id);
		return projectImagesMapper.selectByExample(example);
	}

	@Override
	public List<TProjectType> getAllProjectTypes() {
		// TODO Auto-generated method stub
		return projectTypeMapper.selectByExample(null);
	}

	@Override
	public List<TProjectTag> getAllProjectTags() {
		// TODO Auto-generated method stub
		return projectTagMapper.selectByExample(null);
	}

	@Override
	public TProject getProjectInfo(Integer projectId) {
		
		TProject project = projectMapper.selectByPrimaryKey(projectId);
		return project;
	}

	@Override
	public List<TReturn> getProjectReturns(Integer projectId) {
		TReturnExample example = new TReturnExample();
		example.createCriteria().andProjectidEqualTo(projectId);
		return returnMapper.selectByExample(example);
	}

	@Override
	public TReturn getProjectReturnById(Integer returnId) {
		TReturn return1 = returnMapper.selectByPrimaryKey(returnId);
		return return1;
	}

}
