package com.atguigu.scw.project.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.atguigu.scw.project.component.OssCompont;

@SpringBootConfiguration
public class AppProjectConfig {
	
	@ConfigurationProperties(prefix="oss")
	@Bean
	public OssCompont ossCompont() {
		return new OssCompont();
	}

}
