package com.ai.env.handler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用来获取操作日志
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface OperationLogAnnotation {

	/**
	 * 操作的模块
	 * 
	 * @return
	 */
	String module() default "";

	/**
	 * 操作的子模块
	 * 
	 * @return
	 */
	String subModule() default "";

	/**
	 * 操作的动作(增删改上下线，批量增删改上下线，导出导入)
	 * 
	 * @return
	 */
	String action() default "";

	/**
	 * 操作的描述
	 * 
	 * @return
	 */
	String message() default "";

}
