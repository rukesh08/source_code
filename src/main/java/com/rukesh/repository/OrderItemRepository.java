package com.rukesh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rukesh.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
