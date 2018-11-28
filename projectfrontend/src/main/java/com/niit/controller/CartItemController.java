package com.niit.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.niit.model.CartItem;
import com.niit.model.Customer;
import com.niit.model.CustomerOrder;
import com.niit.model.Product;
import com.niit.model.ShippingAddress;
import com.niit.model.User;
import com.niit.service.CartItemService;
import com.niit.service.CustomerService;
import com.niit.service.ProductService;

@Controller
public class CartItemController {
	@Autowired
private CartItemService cartItemService;
@Autowired
private ProductService productService;
@Autowired
private CustomerService customerService;
@RequestMapping(value="/cart/addtocart/{id}")
public String addToCart(
		@PathVariable int id,
		@RequestParam int requestedQuantity,
		@AuthenticationPrincipal Principal principal
		){
	String email=  principal.getName();
	//CartItem has Product, User 
	//using id get Product object
	//from email get User Object
	Product product=productService.selectProducts(id);//session.get(Product.class,id) in dao
	User user=customerService.getUser(email);//session.get(User.class,email) in dao
	List<CartItem> cartItems=cartItemService.getCart(email);
	for(CartItem cartItem:cartItems){//if user updates the quantity of the product already in the cart
		if(cartItem.getProduct().getId()==id){
			cartItem.setQuantity(requestedQuantity);
			cartItem.setTotalPrice(requestedQuantity * product.getPrice());
		    cartItemService.addToCart(cartItem);//session.saveOrUpdate(cartItem) - UPDATE
		    return "redirect:/cart/getcart";
		}
	}
	
	
	//New Instance
	CartItem cartItem=new CartItem();
	cartItem.setProduct(product);
	cartItem.setUser(user);
	cartItem.setQuantity(requestedQuantity);
	cartItem.setTotalPrice(requestedQuantity * product.getPrice());
	
	cartItemService.addToCart(cartItem);//session.saveOrUpdate(cartItem) in dao - INSERT
	
	return "redirect:/cart/getcart";
}
@RequestMapping(value="/cart/getcart")
public String getCart(@AuthenticationPrincipal Principal principal,Model model,HttpSession session){ 
	//list of cartitems for a particular user
	String email=principal.getName();
	List<CartItem> cartItems=cartItemService.getCart(email);
	//pass the list of cartitems to cart.jsp
	session.setAttribute("cartSize", cartItems.size());
	model.addAttribute("cartItems",cartItems);
	return "cart";
}
@RequestMapping(value="/cart/removecartitem/{cartItemId}")
public String removeCartItem(@PathVariable int cartItemId){
	cartItemService.removeCartItem(cartItemId);
	return "redirect:/cart/getcart";
}

@RequestMapping(value="/cart/clearcart")
public String clearCart(@AuthenticationPrincipal Principal principal){
	List<CartItem> cartItems=cartItemService.getCart(principal.getName());
	for(CartItem cartItem:cartItems)
		cartItemService.removeCartItem(cartItem.getCartItemId());
	return "redirect:/cart/getcart";
}

@RequestMapping(value="/cart/shippingaddressform")
public String getShippingAddressForm(@AuthenticationPrincipal Principal principal,Model model){
	String email=principal.getName();
	//using email -> shippingaddress?
     User user=customerService.getUser(email);
     Customer customer= user.getCustomer();
     ShippingAddress shippingAddress=customer.getShippingaddress();
     model.addAttribute("shippingaddress",shippingAddress);
	return "shippingaddress";
}

@RequestMapping(value="/cart/createorder")
public String createCustomerOrder(@ModelAttribute ShippingAddress shippingaddress,
		Model model,
		@AuthenticationPrincipal Principal principal,HttpSession session){
	String email=principal.getName();
	
	//Get User object and List<CartItem>, for updating shippingaddress set the updated values
	User user=customerService.getUser(email);
	List<CartItem> cartItems=cartItemService.getCart(email);
	Customer customer=user.getCustomer();
	customer.setShippingaddress(shippingaddress);
	
	user.setCustomer(customer);
	customer.setUser(user);
	
	
	//Calculate Grandtotal
	double grandTotal=0.0;
	for(CartItem cartItem:cartItems){
		grandTotal=grandTotal+cartItem.getTotalPrice();
	}
	
	
	//insert customerorder details in customerorder table
	CustomerOrder customerOrder=new CustomerOrder();
	customerOrder.setPurchaseDate(new Date());
	customerOrder.setGrandTotal(grandTotal);
	customerOrder.setUser(user);
	
	if(cartItems.size()>0)
	      cartItemService.createCustomerOrder(customerOrder);//customerOrder -> user -> customer -> shippingaddres
	//Insert into customerorder table,update shippingaddress
	
	
	//Update the quantity of the product, clear the cart(remove cartitems from the cart)
	for(CartItem cartItem:cartItems){
		Product product=cartItem.getProduct();
		product.setQuantity(product.getQuantity() - cartItem.getQuantity());
		productService.updateProducts(product);//update the quantity of the product
		cartItemService.removeCartItem(cartItem.getCartItemId());//delete from cartitem where cartitemid=?
	}
	session.setAttribute("cartSize", 0);
	
	model.addAttribute("cartItems",cartItems);
	model.addAttribute("customerorder",customerOrder);
	return "orderdetails";
}

}