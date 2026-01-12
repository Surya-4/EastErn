package com.ecommerce.products.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.products.bean.ProductBean;
import com.ecommerce.products.service.ProductServiceInterface;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/products")
public class ProductController {
	
	@Autowired
	private ProductServiceInterface productServiceInterface;
	
	@PostMapping("/add")
	public ResponseEntity<String> addProduct(@RequestBody ProductBean productBean){
		ProductBean result=productServiceInterface.addProduct(productBean);
		return ResponseEntity.status(HttpStatus.CREATED).body("product "+result.getProductName()+" has been created");
	}

	@DeleteMapping("/deleteProduct/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable String productId){
		productServiceInterface.deleteProduct(productId);
		return ResponseEntity.status(HttpStatus.OK).body("Product has been deleted");
	}
	
	@PatchMapping("/modifyProduct/{productId}")
	public ResponseEntity<String> modifyProduct(@PathVariable String productId, @RequestBody Map<String,Object> updates){
		productServiceInterface.modifyProduct(productId, updates);
		return ResponseEntity.status(HttpStatus.OK).body("The product has been modified");
	}
	
	@GetMapping("/")
	public ResponseEntity<List<ProductBean>> getAllProducts(){
		List<ProductBean> list=productServiceInterface.getAllProducts();
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/getProductByPrice")
	public ResponseEntity<List<ProductBean>> getProductsByPriceRange(@RequestParam int minPrice,@RequestParam int maxPrice){
		List<ProductBean> list=productServiceInterface.getProductsByPriceRange(minPrice, maxPrice);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/getProductByBrand")
	public ResponseEntity<List<ProductBean>> getProductsByBrand(@RequestParam String brand){
		List<ProductBean> list=productServiceInterface.getProductsByBrand(brand);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/getProductByCat")
	public ResponseEntity<List<ProductBean>> getProductsByCategory(@RequestParam String category){
		List<ProductBean> list = productServiceInterface.getProductsByCategory(category);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/getProduct/{productId}")
	public ResponseEntity<ProductBean> getProductById(@PathVariable String productId){
		ProductBean productBean = productServiceInterface.getProductById(productId);
		return ResponseEntity.status(HttpStatus.OK).body(productBean);
	}
}
