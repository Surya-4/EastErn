package com.ecommerce.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.bean.UserBean;
import com.ecommerce.dao.DAOInterface;

@Service
public class CustomeUserDetailsService implements UserDetailsService {

	@Autowired
	private DAOInterface daoInterface;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Map<String, Object> map = daoInterface.findUser(username);
		UserBean user=(UserBean) map.get("userBean");
		if(user==null) {
			throw new UsernameNotFoundException("User not found");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(),user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
	}

}
