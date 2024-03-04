package com.ums.repository;

import com.ums.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProduct extends JpaRepository<Product, String> {
}