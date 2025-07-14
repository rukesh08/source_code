package com.rukesh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rukesh.model.Cart;

public interface CartRepository  extends JpaRepository<Cart, Long>{
	public Cart findByCustomerId(Long userId);
}
