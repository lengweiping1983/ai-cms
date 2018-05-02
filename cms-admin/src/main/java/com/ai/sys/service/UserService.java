package com.ai.sys.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.common.bean.OperationObject;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.sys.entity.User;
import com.ai.sys.repository.UserRepository;

@Service
@Transactional(value = "sysTransactionManager", readOnly = true)
public class UserService extends AbstractService<User, Long> {

	@Autowired
	private UserRepository userRepository;

	@Override
	public AbstractRepository<User, Long> getRepository() {
		return userRepository;
	}

	@Transactional(readOnly = false)
	public void updateUserLoginInfo(Long id, String ip) {
		User user = userRepository.findOne(id);
		user.setLastLoginIp(user.getLoginIp());
		user.setLastLoginTime(user.getLoginTime());

		user.setLoginIp(ip);
		user.setLoginTime(new Date());
		if (user.getLoginNum() != null && user.getLoginNum() >= 0) {
			user.setLoginNum(user.getLoginNum() + 1);
		} else {
			user.setLoginNum(1);
		}
		userRepository.save(user);
	}

	public OperationObject transformOperationObject(User user) {
		if (user == null) {
			return null;
		}
		OperationObject operationObject = new OperationObject();
		operationObject.setId(user.getId());
		operationObject.setName(user.getName());
		return operationObject;
	}
}
