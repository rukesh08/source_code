package com.rukesh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rukesh.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	
}
