package com.savlero.ShoeStore.service;

import com.savlero.ShoeStore.controller.ShopController;
import com.savlero.ShoeStore.domain.Shop;
import com.savlero.ShoeStore.dto.ShopPatchDto;
import com.savlero.ShoeStore.exceptions.ShopNotFoundException;
import com.savlero.ShoeStore.repository.ShopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;
    private Logger logger = LoggerFactory.getLogger(ShopController.class);


    public List<Shop> findAll() {
        logger.info("Do Shop findAll");
        return shopRepository.findAll();
    }


    public Optional<Shop> findById(long id) {
        logger.info("Do Shop findById " + id);
        return shopRepository.findById(id);
    }


    public Shop saveShop(Shop shop) {
        logger.info("Ini saveShop " + shop);
        shopRepository.save(shop);
        logger.info("End saveShop " + shop);
        return shop;
    }

    public void removeShop(long shopId) throws ShopNotFoundException {
        logger.info("Ini removeShop ID: " + shopId);
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new ShopNotFoundException(shopId));
        logger.info("End removeShop Shop: " + shop);
        shopRepository.delete(shop);
    }

    public void modifyShop(Shop shop, long shopId) throws ShopNotFoundException {
        logger.info("Ini modifyShop ID: " + shopId);
        Optional<Shop> shopOptional = shopRepository.findById(shopId);
        if (shopOptional.isPresent()) {
            Shop existingShop = shopOptional.get();
            existingShop.setName(shop.getName());
            existingShop.setLatitude(shop.getLatitude());
            existingShop.setLongitude(shop.getLongitude());


            shopRepository.save(existingShop);
        } else {
            throw new ShopNotFoundException(shopId);
        }
        logger.info("End modifyShop Shop: " + shopOptional);
    }

    public void patchShop(long shopId, ShopPatchDto shopPatchDto) throws ShopNotFoundException {
        logger.info("Ini patchShop ID: " + shopId);
        Shop oldShop = shopRepository.findById(shopId).orElseThrow(ShopNotFoundException::new);
        if (shopPatchDto.getField().equals("name")) {
            oldShop.setName(shopPatchDto.getName());
        }
        shopRepository.save((oldShop));
        logger.info("End patchShop");
    }
}