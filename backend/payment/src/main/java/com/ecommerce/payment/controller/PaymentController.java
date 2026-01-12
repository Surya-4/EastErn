package com.ecommerce.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.payment.bean.CartBean;
import com.ecommerce.payment.bean.PaymentBean;
import com.ecommerce.payment.service.PaymentServiceInterface;

import jwtcommon.security.CustomPrincipal;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	@Autowired
	private PaymentServiceInterface paymentServiceInterface;
	
	@PostMapping("/pay")
    @PreAuthorize("hasRole('USER')")
	public ResponseEntity<CartBean> processPayment(@AuthenticationPrincipal CustomPrincipal principal, @RequestBody PaymentBean paymentBean){
		CartBean response=paymentServiceInterface.processPayment(principal.getUserId(),paymentBean);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
