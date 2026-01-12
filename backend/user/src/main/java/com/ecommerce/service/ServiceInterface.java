package com.ecommerce.service;

import com.ecommerce.bean.UserBean;
import com.ecommerce.bean.UserData;

public interface ServiceInterface {

	String addUser(UserBean userBean);

	UserData findUser(String userName);
	
	boolean deleteUser(String userName);

	String getId(String userName);

	UserData editUser(UserData userData);

}