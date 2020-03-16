package com.atguigu.scw;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScwUserApplicationTests {

	@Autowired
	DataSource dataSource;
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	@Test
	public void test01() throws SQLException{
		
		Connection conn = dataSource.getConnection();
		System.out.println(conn);
		conn.close();
	}
	
	@Test
	public void test02() {
		stringRedisTemplate.opsForValue().set("key11", "value111");
	}
	@Test
	public void test03() {
		String key = stringRedisTemplate.opsForValue().get("key11");
		System.out.println(key);
	}

}
