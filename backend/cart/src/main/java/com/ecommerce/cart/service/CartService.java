package com.ecommerce.cart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.cart.bean.CartBean;
import com.ecommerce.cart.bean.ProductBean;
import com.ecommerce.cart.dao.CartDAOInterface;

@Service
public class CartService implements CartServiceInterface {
	
	private RestTemplate restTemplate;
	
	public CartService(RestTemplate restTemplate) {
		this.restTemplate=restTemplate;
	}
	
	@Autowired
	private CartDAOInterface cartDAOInterface;
	
	@Override
	public List<CartBean> getAllCartsByUser(String userId){
		return cartDAOInterface.getAllCartsByUser(userId);
	}
	
	@Override
	public CartBean checkout(String userId,String transactionId,String paymentMethod) {
		return cartDAOInterface.checkout(userId, transactionId,paymentMethod);
	}
	
	@Override
	public CartBean addOrUpdateItem(String userId, String productId,String color, int delta) {
		String productUrl="http://localhost:8083/products/getProduct/"+productId;
		ProductBean productBean=restTemplate.getForObject(productUrl,ProductBean.class);
	    return cartDAOInterface.addOrUpdateItem(userId, productId,color, delta,productBean);
	}

	@Override
	public CartBean removeItem(String userId, String productId,String color) {
		return cartDAOInterface.removeItem(userId, productId,color);
	}
	
	@Override
	public CartBean getCartByUser(String userId) {
		return cartDAOInterface.getCartByUser(userId);
	}

	@Override
	public Boolean clearCart(String userId) {
		return cartDAOInterface.clearCart(userId);
	}
}
