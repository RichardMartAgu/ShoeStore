package com.savlero.ShoeStore.service;

import com.savlero.ShoeStore.controller.BrandController;
import com.savlero.ShoeStore.controller.ModelController;
import com.savlero.ShoeStore.domain.Brand;
import com.savlero.ShoeStore.domain.Model;
import com.savlero.ShoeStore.dto.ModelPatchDto;
import com.savlero.ShoeStore.exceptions.BrandNotFoundException;
import com.savlero.ShoeStore.exceptions.ModelNotFoundException;
import com.savlero.ShoeStore.repository.BrandRepository;
import com.savlero.ShoeStore.repository.ModelRepository;
import org.modelmapper.ModelMapper;
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
    @Autowired
    private com.savlero.ShoeStore.service.BrandService mService;
    @Autowired
    private BrandMapper brandMapper;
    private Logger logger = LoggerFactory.getLogger(BrandController.class);

    public List<Brand> findAll() {
        logger.info("Do Brand findAll");
        return brandRepository.findAll();
    }

    public Optional<Brand> findById(long id) {
        logger.info("Do ModBrandel findById " + id);
        return brandRepository.findById(id);
    }

    public List<Brand> findBrandByBrandId(long brandId) throws BrandNotFoundException {
        logger.info("Ini findBrandByBrandId " + brandId);
        Optional<Brand> brandOptional = brandService.findById(brandId);
        if (brandOptional.isPresent()) {
            logger.info("End findBrandByBrandId " + brandId);
            return brandRepository.findAByBrand(brandOptional);
        } else {
            throw new BrandNotFoundException();
        }
    }

    public Brand saveModel(Brand model) {
        logger.info("Ini saveModel " + model);
        brandRepository.save(model);
        logger.info("End saveBrand " + model);
        return model;
    }

    public void removeModel(long modelId) throws BrandNotFoundException {
        logger.info("Ini removeModel ID: " + modelId);
        Brand model = brandRepository.findById(modelId).orElseThrow(() -> new BrandNotFoundException(modelId));
        logger.info("End removeModel Model: " + model);
        brandRepository.delete(model);
    }

    public void modifyModel(Brand newBrand, long brandId) throws BrandNotFoundException {
        logger.info("Ini modifyModel ID: " + brandId);
        Optional<Brand> model = brandRepository.findById(brandId);
        if (model.isPresent()) {
            Brand existingBrand = model.get();
            existingBrand.setName(newBrand.getName());
            existingBrand.setTelephone(newBrand.setTelephone());
            existingBrand.setMinimumSize(newBrand.getMinimumSize());
            existingBrand.setMaximumSize(newBrand.getMaximumSize());

            brandRepository.save(existingBrand);
        } else {
            throw new ModelNotFoundException(brandId);
        }
        logger.info("End modifyModel Model: " + model);
    }

    public void patchModel(long modelId, ModelPatchDto modelPatchDto) throws BrandNotFoundException {
        logger.info("Ini patchModel ID: " + modelId);
        Model oldModel = brandRepository.findById(modelId).orElseThrow(BrandNotFoundException::new);
        if (modelPatchDto.getField().equals("maximumSize")) {
            oldModel.setMaximumSize(modelPatchDto.getMaximumSize());
        }
        brandRepository.save((oldModel));
        logger.info("End patchModel");
    }
}

