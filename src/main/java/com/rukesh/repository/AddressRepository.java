package com.rukesh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rukesh.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
