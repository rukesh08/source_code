package com.rukesh.contoller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rukesh.model.CartItem;
import com.rukesh.model.Order;
import com.rukesh.model.User;
import com.rukesh.request.AddCartItemRequest;
import com.rukesh.request.OrderRequest;
import com.rukesh.service.OrderService;
import com.rukesh.service.UserService;

@RestController
@RequestMapping("/api")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/order")
	public ResponseEntity<Order> createOrder(@RequestBody OrderRequest req,
			@RequestHeader("Authorization") String jwt)throws Exception{
		User user=userService.findUserByJwtToken(jwt);
		Order order=orderService.createOrder(req, user);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}
	
	@GetMapping("/order/user")
	public ResponseEntity<List<Order>> getOrderHistory(
			@RequestHeader("Authorization") String jwt)throws Exception{
		User user=userService.findUserByJwtToken(jwt);
		List<Order> order=orderService.getUsersOrder(user.getId());
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

}
