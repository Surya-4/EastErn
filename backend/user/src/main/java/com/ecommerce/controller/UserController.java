package com.ecommerce.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.bean.UserBean;
import com.ecommerce.bean.UserData;
import com.ecommerce.service.ServiceInterface;
import java.time.Duration;
import jakarta.servlet.http.HttpServletResponse;
import jwtcommon.security.CustomPrincipal;
import jwtcommon.security.JwtUtil;


@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private ServiceInterface serviceInterface;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private AuthenticationManager authenticationManager;
	@PostMapping("/register")
	public ResponseEntity<UserData> addUser(@RequestBody UserBean userBean,HttpServletResponse response){
		userBean.setRoles(Arrays.asList("ROLE_USER"));
		String password=userBean.getPassword();
		String userId=serviceInterface.addUser(userBean);
		Authentication auth= authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(userBean.getUserName(),password)
		);
		UserDetails userDetails=(UserDetails) auth.getPrincipal();
		var roles=userDetails.getAuthorities().stream().map(grantedAuthority->grantedAuthority.getAuthority()).collect(Collectors.toList());
		UserData userData=serviceInterface.findUser(userBean.getUserName());
		String token = jwtUtil.generateJwtToken(userId,userDetails.getUsername(),roles);
		ResponseCookie cookie=ResponseCookie.from("jwt", token)
		.httpOnly(true)
		.secure(false)
		.sameSite("Lax")
		.path("/")
		.maxAge(Duration.ofHours(1))
		.build();
		response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(userData);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletResponse response){
		ResponseCookie cookie = ResponseCookie.from("jwt", "")
			    .httpOnly(true)
			    .secure(false)
			    .sameSite("Lax")
			    .path("/")
			    .maxAge(0)
			    .build();

			response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return ResponseEntity.status(HttpStatus.OK).build();
	}
	@PostMapping("/login")
	public ResponseEntity<UserData> login(@RequestBody UserBean userBean,HttpServletResponse response){
		UserData userData=serviceInterface.findUser(userBean.getUserName());
		Authentication auth = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(userBean.getUserName(),userBean.getPassword()));
		UserDetails userDetails=(UserDetails) auth.getPrincipal();
		var roles=userDetails.getAuthorities().stream().map(grantedAuthority->grantedAuthority.getAuthority()).collect(Collectors.toList());
		String userId=serviceInterface.getId(userData.getUserName());
		String token=jwtUtil.generateJwtToken(userId,userDetails.getUsername(),roles);
		ResponseCookie cookie = ResponseCookie.from("jwt", token)
			    .httpOnly(true)
			    .secure(false)
			    .sameSite("Lax")
			    .path("/")
			    .maxAge(Duration.ofHours(1))
			    .build();

		response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(userData);
	}
	
	@GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserData> getCurrentUser(Authentication auth) {
	    CustomPrincipal user = (CustomPrincipal) auth.getPrincipal();
	    UserData userData=serviceInterface.findUser(user.getUsername());
	    return ResponseEntity.ok(userData);
	}
	
	@PatchMapping("/editUser")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserData> editUser(@RequestBody UserData userData){
		UserData modified=serviceInterface.editUser(userData);
		return ResponseEntity.status(HttpStatus.OK).body(modified);
	}
 	
	@GetMapping("/findUser/{userName}")
	public ResponseEntity<String> findUser(@PathVariable String userName){
		UserData existing=serviceInterface.findUser(userName);
		if(existing==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}
		return ResponseEntity.status(HttpStatus.OK).body(userName);
	}
	
	@DeleteMapping("/deleteUser/{userName}")
	public ResponseEntity<String> deleteUser(@PathVariable String userName){
		Boolean b = serviceInterface.deleteUser(userName);
		if(!b){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Don't exist");
		}
		return ResponseEntity.status(HttpStatus.OK).body("User Deleted Successfully");
	}
}
