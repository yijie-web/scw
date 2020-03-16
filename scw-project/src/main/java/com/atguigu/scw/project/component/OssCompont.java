package com.atguigu.scw.project.component;

import java.io.InputStream;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Data
@Slf4j
public class OssCompont {
	
	String endpoint ;
	// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
	String accessKeyId ;
	String accessKeySecret ;
	String buket ;

	public String load(String filename,InputStream inputStream) {
		

		try {
			// 创建OSSClient实例。
			OSS ossClient = new OSSClientBuilder().build("https://"+endpoint, accessKeyId, accessKeySecret);
			log.debug("数据为->{}",ossClient.toString());
			// 上传文件流。
			//InputStream inputStream = new FileInputStream("D:/学习/product-5.jpg");
			ossClient.putObject(buket, "picture/"+filename, inputStream);

			String filepath = "https://"+buket+"."+endpoint+"/picture/"+filename;
			// 关闭OSSClient。
			ossClient.shutdown();
			log.debug("文件上传成功-{}",filepath);
			return filepath;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("文件上传失败-{}",filename);
			return null;
		} 
	}
}
