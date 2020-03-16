package com.atguigu.scw.webui.config;

import java.io.FileWriter;
import java.io.IOException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class AlipayConfig implements InitializingBean {
	

	public static String app_id;
    public static String merchant_private_key;
    public static String alipay_public_key;
	public static String notify_url;
	public static String return_url;
	public static String sign_type;
	public static String charset;
	public static String gatewayUrl;
	public static String log_path = "D:\\log";



    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Value("${alipay.app_id}")
	public String appid;
	@Value("${alipay.merchant_private_key}")
	public String merchantprivatekey;
	@Value("${alipay.alipay_public_key}")
	public String alipaypublickey;
	@Value("${alipay.notify_url}")
	public String notifyurl;
	@Value("${alipay.return_url}")
	public String returnurl;
	@Value("${alipay.sign_type}")
	public String signtype;
	@Value("${alipay.charset}")
	public String charset2;
	@Value("${alipay.gatewayUrl}")
	public String gatewayUrl2;

@Override
public void afterPropertiesSet() throws Exception {
	// TODO Auto-generated method stub
	
	app_id = appid;
	merchant_private_key = merchantprivatekey;
	alipay_public_key = alipaypublickey;
	notify_url = notifyurl;
	return_url = returnurl;
	sign_type = signtype;
	charset = charset2;
	gatewayUrl = gatewayUrl2;
}
}

