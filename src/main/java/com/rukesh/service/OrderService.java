package com.rukesh.service;

import java.util.List;

import com.rukesh.model.Order;
import com.rukesh.model.User;
import com.rukesh.request.OrderRequest;

public interface OrderService {
	
	public Order createOrder(OrderRequest order,User user) throws Exception;
	
	public Order updateOrder(Long orderId,String orderStatus)throws Exception;
	
	public void cancelOrder(Long orderId)throws Exception;
	
	public List<Order> getUsersOrder(Long userId)throws Exception;
	
	
	public List<Order> getRestaurantsOrder(Long restaurantId,String orderStatus)throws Exception;
	
	public Order findOrderById(Long orderId)throws Exception;

}
