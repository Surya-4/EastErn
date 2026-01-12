package com.ecommerce.products.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.ecommerce.products.bean.ProductBean;
import com.ecommerce.products.doc.ProductDocument;
import com.ecommerce.products.exceptions.ProductNotFoundException;
import com.mongodb.DuplicateKeyException;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

@Repository
public class ProductDAO implements ProductDAOInterface {
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public ProductBean addProduct(ProductBean productBean) {
		try {
			ProductDocument pd=new ProductDocument();
			BeanUtils.copyProperties(productBean, pd);
			ProductDocument resultDoc=mongoTemplate.save(pd);
			ProductBean resultBean=new ProductBean();
			BeanUtils.copyProperties(resultDoc, resultBean);
			return resultBean;
		} catch(DuplicateKeyException e){
			throw new RuntimeException("The product with brand and name already exists");
		}
	}
	
	@Override
	public boolean deleteProduct(String  productId) {
		Query query=new Query();
		query.addCriteria(Criteria.where("productId").is(productId));
		DeleteResult result=mongoTemplate.remove(query, ProductDocument.class);
		if(result.getDeletedCount()==0) {
			throw new ProductNotFoundException("Failed to delete product ");
		}
		return true;
	}
	
	@Override
	public boolean modifyProduct(String productId,Map<String,Object> updates) {
		Query query=new Query();
		query.addCriteria(Criteria.where("productId").is(productId));
		Update update =new Update();
		updates.forEach(update::set);
		UpdateResult updated= mongoTemplate.updateFirst(query, update, ProductDocument.class);
		if(updated.getModifiedCount()==0) {
			throw new ProductNotFoundException("Failed to modify product");
		}
		return true;
	}

	@Override
	public List<ProductBean> getProductsByCategory(String category) {
		List<ProductBean> list=new ArrayList<ProductBean>();
		Query query=new Query();
		query.addCriteria(Criteria.where("productCategory").is(category));
		List<ProductDocument> docList=mongoTemplate.find(query, ProductDocument.class);
		for(ProductDocument productDocument:docList) {
			ProductBean productBean=new ProductBean();
			BeanUtils.copyProperties(productDocument, productBean);
			list.add(productBean);
		}
		return list;
	}

	@Override
	public List<ProductBean> getProductsByBrand(String brand) {
		List<ProductBean> list=new ArrayList<ProductBean>();
		Query query=new Query();
		query.addCriteria(Criteria.where("productBrand").is(brand));
		List<ProductDocument> docList=mongoTemplate.find(query, ProductDocument.class);
		for(ProductDocument productDocument:docList) {
			ProductBean productBean=new ProductBean();
			BeanUtils.copyProperties(productDocument, productBean);
			list.add(productBean);
		}
		return list;
	}

	@Override
	public List<ProductBean> getProductsByPriceRange(int minPrice, int maxPrice) {
		List<ProductBean> list=new ArrayList<ProductBean>();
		Query query=new Query();
		query.addCriteria(Criteria.where("price").gte(minPrice).lte(maxPrice));
		List<ProductDocument> docList=mongoTemplate.find(query, ProductDocument.class);
		for(ProductDocument productDocument:docList) {
			ProductBean productBean=new ProductBean();
			BeanUtils.copyProperties(productDocument, productBean);
			list.add(productBean);
		}
		return list;
	}
	
	@Override
	public List<ProductBean> getAllProducts(){
		List<ProductBean> list=new ArrayList<ProductBean>();
		List<ProductDocument> docList=mongoTemplate.findAll(ProductDocument.class);
		for(ProductDocument productDocument:docList) {
			ProductBean productBean=new ProductBean();
			BeanUtils.copyProperties(productDocument, productBean);
			list.add(productBean);
		}
		return list;
	}

	@Override
	public ProductBean getProductById(String productId) {
		Query query=new Query();
		query.addCriteria(Criteria.where("productId").is(productId));
		ProductDocument productDocument = mongoTemplate.findOne(query, ProductDocument.class);
		if(productDocument==null) {
			throw new ProductNotFoundException("The product Doesn't exist");
		}
		ProductBean productBean=new ProductBean();
		BeanUtils.copyProperties(productDocument, productBean);
		return productBean;
	}
}
