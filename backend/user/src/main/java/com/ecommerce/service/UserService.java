package com.ecommerce.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.bean.UserBean;
import com.ecommerce.bean.UserData;
import com.ecommerce.dao.DAOInterface;
import com.ecommerce.exceptions.UserAlreadyExistsException;
import com.ecommerce.exceptions.UserNotFoundException;

@Service
public class UserService implements ServiceInterface  {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private DAOInterface daoInterface;
	@Override
	public String addUser(UserBean userBean){
		Map<String, Object> map=daoInterface.findUser(userBean.getUserName());
		if(map!=null) {
			throw new UserAlreadyExistsException("User already exists with username: "+userBean.getUserName());
		}
		userBean.setPassword(passwordEncoder.encode(userBean.getPassword()));
		return daoInterface.addUser(userBean);
	}
	
	@Override
	public String getId(String userName) {
		Map<String,Object> map=daoInterface.findUser(userName);
		UserBean userBean=(UserBean) map.get("userBean");
		return userBean.getUserId();
	}
	
	@Override
	public UserData findUser(String userName) {
		Map<String,Object> map=daoInterface.findUser(userName);
		if(map==null) {
			throw new UserNotFoundException("User doesn't exist");
		}
		UserData userData=(UserData) map.get("userData");
		return userData;
	}
	
	@Override
	public UserData editUser(UserData userData) {
		return daoInterface.editUser(userData);
	}
	@Override
	public boolean deleteUser(String userName) {
		boolean deleted=daoInterface.deleteUser(userName);
		if(!deleted) {
			throw new UserNotFoundException("User doesn't exist");
		}
		return true;
	}
}
