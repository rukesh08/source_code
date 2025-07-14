package com.rukesh.service;

import java.util.Optional;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rukesh.model.Cart;
import com.rukesh.model.CartItem;
import com.rukesh.model.Food;
import com.rukesh.model.User;
import com.rukesh.repository.CartItemRepository;
import com.rukesh.repository.CartRepository;
import com.rukesh.request.AddCartItemRequest;

@Service
public class CartServiceImp implements CartService{
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private FoodService foodService;
	
	@Override
	public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception {
	    User user = userService.findUserByJwtToken(jwt);

	    Food food = foodService.findFoodById(req.getFoodId());

	    // Try to find existing cart
	    Cart cart = cartRepository.findByCustomerId(user.getId());

	    // If cart does not exist, create and save a new one
	    if (cart == null) {
	        cart = new Cart();
	        cart.setCustomer(user);
	        cart.setItems(new ArrayList<>());
	        cart = cartRepository.save(cart);
	    }

	    // Check if item already exists in cart
	    for (CartItem cartItem : cart.getItems()) {
	        if (cartItem.getFood().equals(food)) {
	            int newQuantity = cartItem.getQuantity() + req.getQuantity();
	            return updateCardItemQuantity(cartItem.getId(), newQuantity);
	        }
	    }

	    // Create new cart item
	    CartItem newCartItem = new CartItem();
	    newCartItem.setFood(food);
	    newCartItem.setCart(cart);
	    newCartItem.setQuantity(req.getQuantity());
	    newCartItem.setIngredients(req.getIngredients());
	    newCartItem.setTotalPrice(food.getPrice() * req.getQuantity());

	    CartItem savedCartItem = cartItemRepository.save(newCartItem);

	    // Add new item to cart's items list and save cart
	    cart.getItems().add(savedCartItem);
	    cartRepository.save(cart);

	    return savedCartItem;
	}

	@Override
	public CartItem updateCardItemQuantity(Long cartItemId, int quantity) throws Exception {
		Optional<CartItem> cartItemOptional=cartItemRepository.findById(cartItemId);
		if(cartItemOptional.isEmpty()) {
			throw new Exception("cart item not found");
		}
		CartItem item=cartItemOptional.get();
		item.setQuantity(quantity);
		
		item.setTotalPrice(item.getFood().getPrice()*quantity);
		return cartItemRepository.save(item);
	}

	@Override
	public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {
		User user=userService.findUserByJwtToken(jwt);
		
		
		Cart cart=cartRepository.findByCustomerId(user.getId());
		Optional<CartItem> cartItemOptional=cartItemRepository.findById(cartItemId);
		if(cartItemOptional.isEmpty()) {
			throw new Exception("cart item not found");
		}
		CartItem item=cartItemOptional.get();
		
		cart.getItems().remove(item);
		return cartRepository.save(cart);
	}

	@Override
	public Long calculateCartTotals(Cart cart) throws Exception {
		Long total=0L;
		for(CartItem cartItem :cart.getItems()) {
			total+=cartItem.getFood().getPrice()*cartItem.getQuantity();
		}
		return total;
	}

	@Override
	public Cart findCartById(Long id) throws Exception {
		Optional<Cart> optionalCart=cartRepository.findById(id);
		if(optionalCart.isEmpty()) {
			throw new Exception("cart not found with id "+id);
		}
		return optionalCart.get();
	}

	@Override
	public Cart findCartByUserId(Long userId) throws Exception {
		//User user =userService.findUserByJwtToken(jwt);
		Cart cart= cartRepository.findByCustomerId(userId);
		cart.setTotal(calculateCartTotals(cart));
		
		return cart;
	}

	@Override
	public Cart clearCart(Long userId) throws Exception {
		//User user=userService.findUserByJwtToken(userId);
		
		Cart cart=findCartById(userId);
		
		cart.getItems().clear();
		return cartRepository.save(cart);
	}

}
