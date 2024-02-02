package com.savlero.ShoeStore.repository;

import com.savlero.ShoeStore.domain.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends CrudRepository<Shop, Long> {
    List<Shop> findAll();
}