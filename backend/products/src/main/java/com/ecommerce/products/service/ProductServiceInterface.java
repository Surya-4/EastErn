package com.ecommerce.products.service;

import java.util.List;
import java.util.Map;

import com.ecommerce.products.bean.ProductBean;

public interface ProductServiceInterface {

	ProductBean addProduct(ProductBean productBean);

	boolean deleteProduct(String productId);

	boolean modifyProduct(String productId, Map<String, Object> updates);

	List<ProductBean> getAllProducts();
		
	List<ProductBean> getProductsByBrand(String brand);
	
	List<ProductBean> getProductsByPriceRange(int minPrice,int maxPrice);

	List<ProductBean> getProductsByCategory(String category);

	ProductBean getProductById(String productId);
}