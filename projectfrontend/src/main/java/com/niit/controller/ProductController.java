package com.niit.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletRequest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.niit.model.Category;
import com.niit.model.Product;
import com.niit.service.ProductService;


@Controller
public class ProductController {
	@Autowired
private ProductService 	productService;
	
public ProductController(){
	System.out.println("productController bean is created");
}
//url to request this method is http://localhost:8080/project1frontend/all/getallproducts
@RequestMapping(value="/all/getallproducts")
public String getallproducts(Model model){
	List<Product> products=productService.getAllProducts();//get the data call service
	model.addAttribute("products",products);//to Send the data to JSP (UI) page use Model
	return "listofproducts"; // jsp page name, logical view name
}
@RequestMapping(value="/all/getproduct")
public String getproduct(@RequestParam int id,Model model){
	Product product=productService.selectProducts(id);//get product object from the service
	  model.addAttribute("productAttr",product);//send the data to the jsp page
	  return "viewproduct";
}

@RequestMapping(value="/admin/deleteproduct")
public String deleteproduct(@RequestParam int id){
	productService.deleteProducts(id);
	return "redirect:/all/getallproducts";
	
} 

@RequestMapping(value="/admin/getproductform")//to get productform.jsp 
public String getproductform(Model model){
	Product p=new Product();
	model.addAttribute("product",p);//p is newly created product object which has default values
	List<Category> categories=productService.getAllCategories();
	model.addAttribute("categories",categories);
	return "productform";//send a new product object to productform.jsp
}
@RequestMapping(value="/admin/addproduct")//form in productform.jsp is submitted
public String addproduct(@Valid @ModelAttribute Product product,BindingResult result,Model model, ServletRequest request){	
	if(result.hasErrors()){//after validation if any errors
       List<Category> categories=productService.getAllCategories();
		model.addAttribute("categories",categories);
		
		return "productform";
	}
	
	productService.addProducts(product);//in dao session.save(product)
	MultipartFile img=product.getImage();
	

	System.out.println(request.getServletContext().getRealPath("/"));
	
	//Defining a path
	Path path=Paths.get(request.getServletContext().getRealPath("/")+"/WEB-INF/resources/images/"+product.getId()+".png");
	
	//Create a file in the path
	
	try {
		if(img!=null && !img.isEmpty()){
		File file=new File(path.toString());
		img.transferTo(file);
		}
	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return "redirect:/all/getallproducts";
}
@RequestMapping(value="/admin/getupdateproductform")
public String getupdateproductform(@RequestParam int id,Model model){
	Product product=productService.selectProducts(id);
	model.addAttribute("product",product);
	List<Category> categories=productService.getAllCategories();
	model.addAttribute("categories",categories);
	
	return "updateproductform";
}
@RequestMapping(value="/admin/updateproduct")
public String updateProduct(@Valid @ModelAttribute Product product,BindingResult result, ServletRequest request,Model model ){
	if(result.hasErrors()){
		List<Category> categories=productService.getAllCategories();
		model.addAttribute("categories",categories);
		return "updateproductform";
	}
	productService.updateProducts(product);
	MultipartFile img=product.getImage();
	System.out.println(request.getServletContext().getRealPath("/"));
	
	//Defining a path
	Path path=Paths.get(request.getServletContext().getRealPath("/")+"/WEB-INF/resources/images/"+product.getId()+".png");
	
	//Create a file in the path
	
	try {
		if(img!=null && !img.isEmpty()){
		File file=new File(path.toString());
		img.transferTo(file);
		}
	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return "redirect:/all/getallproducts";
}

}
