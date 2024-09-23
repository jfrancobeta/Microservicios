package com.andres.springcloud.msvc.items.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andres.libs.msvc.commons.entities.Product;
import com.andres.springcloud.msvc.items.clients.ProductFeignClient;
import com.andres.springcloud.msvc.items.models.Item;

import feign.FeignException;

@Service
public class ItemServiceFeign implements ItemService {

    @Autowired
    private ProductFeignClient client;

    @Override
    public List<Item> findAll() {
        return client.findAll()
                .stream()
                .map(product -> new Item(product, new Random().nextInt(10) + 1))
        .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> findById(Long id) {
        try {
            Product product = client.details(id);
            return Optional.of(new Item(product, new Random().nextInt(10) + 1));
        } catch (FeignException e) {
            return Optional.empty();
        }
    }

    @Override
    public Product save(Product product) {
        return client.create(product);
    }

    @Override
    public Product update(Product product, Long id) {
        return client.update(product, id);
    }

    @Override
    public void delete(Long id) {
        client.delete(id);
    }

}
