package com.atguigu.scw.project.service;

import java.util.List;

import com.atguigu.scw.project.bean.TProject;
import com.atguigu.scw.project.bean.TProjectImages;
import com.atguigu.scw.project.bean.TProjectTag;
import com.atguigu.scw.project.bean.TProjectType;
import com.atguigu.scw.project.bean.TReturn;

public interface ProjectInfoService {

	List<TProject> getAllProjects();

	List<TProjectImages> getProjectImages(Integer id);

	List<TProjectType> getAllProjectTypes();

	List<TProjectTag> getAllProjectTags();

	TProject getProjectInfo(Integer projectId);

	List<TReturn> getProjectReturns(Integer projectId);

	TReturn getProjectReturnById(Integer returnId);

}
