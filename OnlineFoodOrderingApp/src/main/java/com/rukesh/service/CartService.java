package com.rukesh.service;

import com.rukesh.model.Cart;
import com.rukesh.model.CartItem;
import com.rukesh.request.AddCartItemRequest;

public interface CartService {
	
	public CartItem addItemToCart(AddCartItemRequest req,String jwt)throws Exception;
	
	public CartItem updateCardItemQuantity(Long cartItemId,int quantity)throws Exception;
	
	public Cart removeItemFromCart(Long cartItemId,String jwt)throws Exception;
	
	public Long calculateCartTotals(Cart cart)throws Exception;
	
	public Cart findCartById(Long id)throws Exception;
	
	public Cart findCartByUserId(Long userId)throws Exception;
	
	public Cart clearCart(Long userId)throws Exception;
	
	
}
