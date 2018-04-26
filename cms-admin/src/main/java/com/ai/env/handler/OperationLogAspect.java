package com.ai.env.handler;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ai.common.bean.BaseResult;
import com.ai.common.bean.OperationObject;
import com.ai.common.utils.IpUtils;
import com.ai.sys.entity.OperationLog;
import com.ai.sys.entity.User;
import com.ai.sys.repository.OperationLogRepository;
import com.ai.sys.security.SecurityUtils;

@Aspect
@Component
public class OperationLogAspect {
	public final Logger logger = LoggerFactory
			.getLogger(OperationLogAspect.class);

	@Autowired
	private OperationLogRepository operationLogRepository;

	@Pointcut("@annotation(com.ai.env.handler.OperationLogAnnotation)")
	public void controllerAspect() {
	}

	@AfterReturning(returning = "returnResult", pointcut = "controllerAspect()")
	public void log(JoinPoint pjp, Object returnResult) throws Throwable {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		User user = (User) SecurityUtils.getUser();
		if (user == null) {
			return;
		}
		String ip = IpUtils.getIpAddr(request);
		OperationLog log = new OperationLog();
		log.setUserId(user.getId());
		log.setUserName(user.getName());
		log.setCpId(SecurityUtils.getCpId());
		log.setIp(ip);
		log.setUri(request.getRequestURI());
		OperationLogAnnotation an = getOperationLogAnnotation(pjp);
		if (an != null && StringUtils.isNotEmpty(an.module())) {
			log.setModule(an.module());
			log.setSubModule(an.subModule());
			log.setAction(an.action());
			log.setActionResult(0);
			log.setMessage(an.message());
			if (returnResult instanceof BaseResult) {
				BaseResult baseResult = (BaseResult) returnResult;
				log.setActionResult(baseResult.getCode());
				List<OperationObject> operationObjectList = baseResult
						.getOperationObjectList();
				String message = baseResult.getMessage();
				if (operationObjectList != null
						&& operationObjectList.size() > 0) {
					log.setObjectId(StringUtils.join(
							operationObjectList.stream().map(s -> s.getId())
									.collect(Collectors.toList()).toArray(),
							"\n"));
					log.setObjectName(StringUtils.join(
							operationObjectList.stream().map(s -> s.getName())
									.collect(Collectors.toList()).toArray(),
							"\n"));
				}
				if (StringUtils.isNotEmpty(message)) {
					log.setMessage(an.message() + "\n" + message + "");
					baseResult.setMessage(log.getMessage());
				}
			}
			operationLogRepository.save(log);
		}
	}

	/**
	 * 获得注解
	 * 
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 */
	public static OperationLogAnnotation getOperationLogAnnotation(
			JoinPoint joinPoint) throws Exception {
		Signature signature = joinPoint.getSignature();
		if (signature instanceof MethodSignature) {
			MethodSignature methodSignature = (MethodSignature) signature;
			Method method = methodSignature.getMethod();
			if (method != null) {
				return method.getAnnotation(OperationLogAnnotation.class);
			}
		}
		return null;
	}

}
