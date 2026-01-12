package com.ecommerce.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.cart.bean.AddRequest;
import com.ecommerce.cart.bean.CartBean;
import com.ecommerce.cart.service.CartServiceInterface;
import jwtcommon.security.CustomPrincipal;


@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartServiceInterface cartServiceInterface;
	
    @PutMapping("/modifyCart")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<CartBean> addCart(@AuthenticationPrincipal CustomPrincipal principal,@RequestBody AddRequest request) {
    	CartBean cartBean=cartServiceInterface.addOrUpdateItem(principal.getUserId(), request.getProductId(),request.getColor() ,request.getDelta());
    	return ResponseEntity.status(HttpStatus.ACCEPTED).body(cartBean);
    }
    
    @DeleteMapping("/remove/{productId}")
    @PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<CartBean> removeItem(@AuthenticationPrincipal CustomPrincipal principal, @PathVariable String productId,@RequestParam String color){
    	CartBean cart=cartServiceInterface.removeItem(principal.getUserId(), productId, color);
    	return ResponseEntity.status(HttpStatus.OK).body(cart);
    }
	@GetMapping("/myCart")
    @PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<CartBean> getCartByUser(@AuthenticationPrincipal CustomPrincipal principal){
		String userId=principal.getUserId();
		CartBean cartBean=cartServiceInterface.getCartByUser(userId);
		return ResponseEntity.status(HttpStatus.OK).body(cartBean);
	}

    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<CartBean> clearCart(@AuthenticationPrincipal CustomPrincipal principal) {
    	String userId=principal.getUserId();
        boolean cleared = cartServiceInterface.clearCart(userId);
        if (cleared) {
            return ResponseEntity.ok(new CartBean());
        } 
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<CartBean>> getAllOrders(@AuthenticationPrincipal CustomPrincipal principal) {
    	String userId=principal.getUserId();
        List<CartBean> orders = cartServiceInterface.getAllCartsByUser(userId);
        return ResponseEntity.ok(orders);
    }
    
    @PostMapping("/checkout")
    public ResponseEntity<CartBean> checkout(@RequestParam String userId,@RequestParam String transactionId,@RequestParam String paymentMethod){
    	CartBean cartBean=cartServiceInterface.checkout(userId, transactionId,paymentMethod);
    	return ResponseEntity.ok(cartBean);
    }
}
