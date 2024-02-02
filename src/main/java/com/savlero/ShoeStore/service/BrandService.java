package com.savlero.ShoeStore.service;

import com.savlero.ShoeStore.controller.BrandController;
import com.savlero.ShoeStore.domain.Brand;
import com.savlero.ShoeStore.dto.BrandPatchDto;
import com.savlero.ShoeStore.exceptions.BrandNotFoundException;
import com.savlero.ShoeStore.repository.BrandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {


    @Autowired
    private BrandRepository brandRepository;

    private Logger logger = LoggerFactory.getLogger(BrandController.class);

    public List<Brand> findAll() {
        logger.info("Do Brand findAll");
        return brandRepository.findAll();
    }

    public Optional<Brand> findById(long id) {
        logger.info("Do Brand findById " + id);
        return brandRepository.findById(id);
    }


    public Brand saveBrand(Brand brand) {
        logger.info("Ini saveBrand " + brand);
        brandRepository.save(brand);
        logger.info("End saveBrand " + brand);
        return brand;
    }

    public void removeBrand(long brandId) throws BrandNotFoundException {
        logger.info("Ini removeBrand ID: " + brandId);
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new BrandNotFoundException(brandId));
        logger.info("End removeBrand Brand: " + brand);
        brandRepository.delete(brand);
    }

    public void modifyBrand(Brand newBrand, long brandId) throws BrandNotFoundException {
        logger.info("Ini modifyBrand ID: " + brandId);
        Optional<Brand> brand = brandRepository.findById(brandId);
        if (brand.isPresent()) {
            Brand existingBrand = brand.get();
            existingBrand.setName(newBrand.getName());
            existingBrand.setTelephone(newBrand.getTelephone());
            existingBrand.setAddress(newBrand.getAddress());


            brandRepository.save(existingBrand);
        } else {
            throw new BrandNotFoundException(brandId);
        }
        logger.info("End modifyBrand Brand: " + brand);
    }

    public void patchBrand(long brandId, BrandPatchDto brandPatchDto) throws BrandNotFoundException {
        logger.info("Ini patchBrand ID: " + brandId);
        Brand oldBrand = brandRepository.findById(brandId).orElseThrow(BrandNotFoundException::new);
        if (brandPatchDto.getField().equals("address")) {
            oldBrand.setAddress(brandPatchDto.getAddress());
        }
        brandRepository.save((oldBrand));
        logger.info("End patchBrand");
    }
}

