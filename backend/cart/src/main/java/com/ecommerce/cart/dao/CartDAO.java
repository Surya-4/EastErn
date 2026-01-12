package com.ecommerce.cart.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.cart.bean.CartBean;
import com.ecommerce.cart.bean.CartItem;
import com.ecommerce.cart.bean.ProductBean;
import com.ecommerce.cart.doc.CartDocument;
import com.ecommerce.cart.doc.OrderDocument;
import com.ecommerce.cart.exceptions.CartNotExistsException;
import com.mongodb.client.result.DeleteResult;

@Repository
public class CartDAO implements CartDAOInterface {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public CartBean checkout(String userId,String transactionId,String paymentMethod){
		CartBean cartBean = getCartByUser(userId);
	    if (cartBean == null || cartBean.getProducts() == null || cartBean.getProducts().isEmpty()) {
	        return null;
	    }
	    Query orderQuery = new Query();
	    orderQuery.addCriteria(Criteria.where("userId").is(userId));
	    OrderDocument orders = mongoTemplate.findOne(orderQuery, OrderDocument.class);
	    if (orders == null) {
	        orders = new OrderDocument();
	        orders.setUserId(userId);
	        orders.setOrders(new ArrayList<>());
	    }
	    cartBean.setTransactionId(transactionId);
	    cartBean.setPaymentMethod(paymentMethod);
	    orders.getOrders().add(cartBean);
	    mongoTemplate.save(orders);
	    clearCart(userId);
	    return cartBean;
	}

	
	@Override
	public List<CartBean> getAllCartsByUser(String userId){
		Query query=new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		OrderDocument orders = mongoTemplate.findOne(query,OrderDocument.class);
		if(orders==null || orders.getOrders()==null) {
			return new ArrayList<CartBean>();
		}
		List<CartBean> list=orders.getOrders();
		return list;
	}

	@Override
	public CartBean getCartByUser(String userId) {
		CartBean cartBean=new CartBean();
		Query query=new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		CartDocument cartDocument=mongoTemplate.findOne(query, CartDocument.class);
		if(cartDocument==null) {
			return new CartBean();
		}
		BeanUtils.copyProperties(cartDocument,cartBean);
		return cartBean;
	}

	@Override
	public Boolean clearCart(String userId) {
		Query query=new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		CartBean cartBean= getCartByUser(userId);
		if(cartBean==null || cartBean.getProducts().isEmpty()) {
			throw new CartNotExistsException("Cart is empty");
		}
		DeleteResult result= mongoTemplate.remove(query,CartDocument.class);
		return result.getDeletedCount()>0;
	}


	@Override
	public CartBean addOrUpdateItem(
	        String userId,
	        String productId,
	        String color,
	        int delta,
	        ProductBean productBean) {

	    Query query = new Query(Criteria.where("userId").is(userId));
	    CartDocument cartDoc = mongoTemplate.findOne(query, CartDocument.class);

	    if (cartDoc == null) {
	        cartDoc = new CartDocument();
	        cartDoc.setUserId(userId);
	        cartDoc.setProducts(new ArrayList<>());
	        cartDoc.setAmount(0.0);
	    }

	    boolean isExisting = false;

	    Iterator<CartItem> it = cartDoc.getProducts().iterator();
	    while (it.hasNext()) {
	        CartItem item = it.next();

	        if (Objects.equals(item.getProductId(), productId)
	                && Objects.equals(item.getColor(), color)) {

	            int newQty = item.getQuantity() + delta;

	            if (newQty <= 0) {
	                it.remove();
	            } else {
	                item.setQuantity(newQty);
	                item.setSubTotal(newQty * productBean.getPrice());
	            }

	            isExisting = true;
	            break;
	        }
	    }

	    if (!isExisting && delta > 0) {
	        CartItem item = new CartItem();
	        item.setProductId(productId);
	        item.setColor(color);
	        item.setQuantity(delta);
	        item.setSubTotal(delta * productBean.getPrice());
	        cartDoc.getProducts().add(item);
	    }

	    double total = 0.0;
	    for (CartItem item : cartDoc.getProducts()) {
	    	total += item.getSubTotal() != null ? item.getSubTotal() : 0.0;
	    }
	    cartDoc.setAmount(total);

	    CartDocument saved = mongoTemplate.save(cartDoc);

	    CartBean result = new CartBean();
	    BeanUtils.copyProperties(saved, result);
	    return result;
	}


	@Override
	public CartBean removeItem(String userId, String productId, String color) {

	    Query query = new Query(Criteria.where("userId").is(userId));
	    CartDocument cartDoc = mongoTemplate.findOne(query, CartDocument.class);

	    if (cartDoc == null || cartDoc.getProducts() == null) {
	        return new CartBean();
	    }

	    cartDoc.getProducts().removeIf(item ->
	            Objects.equals(item.getProductId(), productId)
	            && Objects.equals(item.getColor(), color)
	    );

	    if (cartDoc.getProducts().isEmpty()) {
	        mongoTemplate.remove(query, CartDocument.class);
	        return new CartBean();
	    }

	    double total = 0.0;
	    for (CartItem item : cartDoc.getProducts()) {
	    	total += item.getSubTotal() != null ? item.getSubTotal() : 0.0;
	    }
	    cartDoc.setAmount(total);

	    CartDocument saved = mongoTemplate.save(cartDoc);

	    CartBean result = new CartBean();
	    BeanUtils.copyProperties(saved, result);
	    return result;
	}

}
