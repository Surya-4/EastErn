package com.ecommerce.products.controllertests;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.ecommerce.products.bean.ProductBean;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductsControllerTest {

	    @Autowired
	    private MockMvc mockMvc;

	    @Autowired
	    private ObjectMapper objectMapper;

	    @Test
	    public void testAddProduct() throws Exception {
	        ProductBean product = new ProductBean();
	        product.setProductId("P1001");
	        product.setProductName("Test Laptop");
	        product.setPrice(55000.0);
	        product.setProductCategory("Electronics");
	        product.setProductBrand("HP");
	        product.setImageURL("https://example.com/image.jpg");
	        product.setProductDescription("A very nice test laptop");
	        product.setProductColors(Arrays.asList("Black", "Silver"));
	        product.setMaterialType("Metal");

	        mockMvc.perform(post("/products/add")
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(objectMapper.writeValueAsString(product)))
	                .andExpect(status().isOk()); // or isCreated() if you set 201
	    }
}
