package com.ai.sys.kaptcha.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Component
public class KaptchaConfig {

	@Bean
	public DefaultKaptcha getDefaultKaptcha() {
		com.google.code.kaptcha.impl.DefaultKaptcha defaultKaptcha = new com.google.code.kaptcha.impl.DefaultKaptcha();
		Properties properties = new Properties();
		properties.setProperty("kaptcha.border", "yes");// 图片边框
		properties.setProperty("kaptcha.border.color", "105,179,90");// 图片边框颜色
		properties.setProperty("kaptcha.textproducer.font.color", "blue");// 颜色
		properties.setProperty("kaptcha.image.width", "94");// 图片宽度
		properties.setProperty("kaptcha.image.height", "34");// 图片高度
		properties.setProperty("kaptcha.textproducer.font.size", "30");// 字体大小
		properties.setProperty("kaptcha.session.key", "code");
		properties.setProperty("kaptcha.textproducer.char.length", "4");
		properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
		properties
				.setProperty("kaptcha.textproducer.char.string", "0123456789");
		properties.setProperty("kaptcha.noise.impl",
				"com.google.code.kaptcha.impl.NoNoise");

		Config config = new Config(properties);
		defaultKaptcha.setConfig(config);

		return defaultKaptcha;
	}
}
