package com.ecommerce.cart.bean;

import java.util.List;

public class CartBean {
	private String cartId;
	private String userId;
	private List<CartItem> products;
	private Double amount;
	private String paymentMethod;
	private String transactionId;
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getCartId() {
		return cartId;
	}
	public void setCartId(String cartId) {
		this.cartId = cartId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<CartItem> getProducts() {
		return products;
	}
	public void setProducts(List<CartItem> products) {
		this.products = products;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}