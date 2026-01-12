package com.ecommerce.cart.doc;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ecommerce.cart.bean.CartBean;

@Document(collection = "orders")
public class OrderDocument {
	@Id
	private String userId;
	private List<CartBean> orders;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<CartBean> getOrders() {
		return orders;
	}
	public void setOrders(List<CartBean> orders) {
		this.orders = orders;
	}
}
