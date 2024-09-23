package com.andres.springcloud.msvc.products.repositories;

import org.springframework.data.repository.CrudRepository;

import com.andres.libs.msvc.commons.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{

}
