package com.ecommerce.cart.bean;

public class AddRequest {
	    private String productId;
	    private String color;
	    private int delta;

	    public String getProductId() {
	        return productId;
	    }

	    public void setProductId(String productId) {
	        this.productId = productId;
	    }

	    public int getDelta() {
	        return delta;
	    }

	    public void setDelta(int delta) {
	        this.delta = delta;
	    }

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}
}
