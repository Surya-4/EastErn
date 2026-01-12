package com.ecommerce.products.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.products.bean.ProductBean;
import com.ecommerce.products.dao.ProductDAOInterface;

@Service
public class ProductService implements ProductServiceInterface {
	@Autowired
	private ProductDAOInterface productDaoInterface;
	
	@Override
	public ProductBean addProduct(ProductBean productBean) {
		return productDaoInterface.addProduct(productBean);
	}
	
	@Override
	public boolean deleteProduct(String productId) {
		return productDaoInterface.deleteProduct(productId);
	}
	
	@Override
	public boolean modifyProduct(String productId,Map<String,Object> updates) {
		return productDaoInterface.modifyProduct(productId, updates);
	}

	@Override
	public List<ProductBean> getAllProducts() {
		return productDaoInterface.getAllProducts();
	}

	@Override
	public List<ProductBean> getProductsByCategory(String category) {
		return productDaoInterface.getProductsByCategory(category);
	}

	@Override
	public List<ProductBean> getProductsByBrand(String brand) {
		return productDaoInterface.getProductsByBrand(brand);
	}

	@Override
	public List<ProductBean> getProductsByPriceRange(int minPrice,int maxPrice) {
		return productDaoInterface.getProductsByPriceRange(minPrice, maxPrice);
	}
	
	@Override
	public ProductBean getProductById(String productId) {
		return productDaoInterface.getProductById(productId);
	}
}
