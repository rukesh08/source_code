package com.rukesh.service;

import java.util.List;

import com.rukesh.dto.RestaurantDto;
import com.rukesh.model.Restaurant;
import com.rukesh.model.User;
import com.rukesh.request.CreateRestaurantRequest;

public interface RestaurantService {
	
	public Restaurant createRestaurant(CreateRestaurantRequest req,User user);
	
	public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception;
	
	public void deleteRestaurant(Long restaurantId)throws Exception;
	
	public List<Restaurant> getAllRestaurant();
	
	public List<Restaurant> searchRestaurant(String keyword);
	
	public Restaurant findRestaurantById(Long id) throws Exception;
	
	public Restaurant getRestaurantByUserId(Long userId) throws Exception;
	
	public RestaurantDto addToFavorites(Long restaurantId,User user)throws Exception;
	
	public Restaurant updaterestaurantStatus (Long id) throws Exception;
	
}
