package com.ecommerce.products.dao;

import java.util.List;
import java.util.Map;

import com.ecommerce.products.bean.ProductBean;

public interface ProductDAOInterface {

	ProductBean addProduct(ProductBean productBean);

	boolean deleteProduct(String productId);

	boolean modifyProduct(String productId, Map<String, Object> updates);

	List<ProductBean> getProductsByCategory(String category);
	
	List<ProductBean> getProductsByBrand(String brand);
	
	List<ProductBean> getProductsByPriceRange(int minPrice,int maxPrice);

	List<ProductBean> getAllProducts();
	
	ProductBean getProductById(String productId);
}