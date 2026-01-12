package com.ecommerce.payment.service;

import com.ecommerce.payment.bean.CartBean;
import com.ecommerce.payment.bean.PaymentBean;

public interface PaymentServiceInterface {

	CartBean processPayment(String userId, PaymentBean paymentBean);

}