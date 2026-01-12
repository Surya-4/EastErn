package com.ecommerce.payment.service;

import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.payment.bean.CartBean;
import com.ecommerce.payment.bean.PaymentBean;

@Service
public class PaymentService implements PaymentServiceInterface {
	
	private RestTemplate restTemplate;
	
	public PaymentService(RestTemplate restTemplate) {
		this.restTemplate=restTemplate;
	}
	
	@Override
	public CartBean processPayment(String userId,PaymentBean paymentBean) {
		HttpHeaders headers=new HttpHeaders();
		headers.set("X-INTERNAL-TOKEN","INTERNAL_PAYMENT_TOKEN");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);
        String cartServiceUrl = "http://localhost:8080/cart/checkout?userId="+userId+"&transactionId="+transactionId+"&paymentMethod="+paymentBean.getPaymentMethod();
            ResponseEntity<CartBean> response =
                    restTemplate.exchange(cartServiceUrl,org.springframework.http.HttpMethod.POST ,entity, CartBean.class);

            return response.getBody();
	}
}
	

