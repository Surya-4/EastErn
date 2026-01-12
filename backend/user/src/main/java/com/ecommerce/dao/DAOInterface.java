package com.ecommerce.dao;

import java.util.Map;

import com.ecommerce.bean.UserBean;
import com.ecommerce.bean.UserData;

public interface DAOInterface {

	String addUser(UserBean userBean);
	
	Map<String, Object> findUser(String userName);

	boolean deleteUser(String userName);

	UserData editUser(UserData userData);

}