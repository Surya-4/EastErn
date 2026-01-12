package com.ecommerce.cart.dao;

import java.util.List;

import com.ecommerce.cart.bean.CartBean;
import com.ecommerce.cart.bean.ProductBean;

public interface CartDAOInterface {

	CartBean checkout(String userId,String transactionString,String paymentMethod);

	List<CartBean> getAllCartsByUser(String userId);
	
	CartBean addOrUpdateItem(String userId, String productId, String color, int delta, ProductBean productBean);

	CartBean removeItem(String userId, String productId,String color);

	CartBean getCartByUser(String userId);

	Boolean clearCart(String userId);

}