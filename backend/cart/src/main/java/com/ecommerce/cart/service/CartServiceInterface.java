package com.ecommerce.cart.service;

import java.util.List;

import com.ecommerce.cart.bean.CartBean;

public interface CartServiceInterface {

	List<CartBean> getAllCartsByUser(String userId);
		
	CartBean getCartByUser(String userId);
	
	CartBean addOrUpdateItem(String userId, String productId,String color, int delta);

    CartBean removeItem(String userId, String productId, String color);
    
	Boolean clearCart(String userId);

	CartBean checkout(String userId, String transactionId,String paymentMethod);

}