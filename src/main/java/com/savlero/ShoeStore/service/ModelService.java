package com.savlero.ShoeStore.service;

import com.savlero.ShoeStore.controller.ModelController;
import com.savlero.ShoeStore.domain.Brand;
import com.savlero.ShoeStore.domain.Model;
import com.savlero.ShoeStore.dto.ModelPatchDto;
import com.savlero.ShoeStore.exceptions.ModelNotFoundException;
import com.savlero.ShoeStore.repository.ModelRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ModelService {

    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelMapper modelMapper;
    private Logger logger = LoggerFactory.getLogger(ModelController.class);

    public List<Model> findAll() {
        logger.info("Do Model findAll");
        return modelRepository.findAll();
    }

    public Optional<Model> findById(long id) {
        logger.info("Do Model findById " + id);
        return modelRepository.findById(id);
    }

    public List<Model> findModelByBrandId(long brandId) throws ModelNotFoundException {
        logger.info("Ini findModelByBrandId " + brandId);
        Optional<Brand> brandOptional = brandService.findById(brandId);
        if (brandOptional.isPresent()) {
            logger.info("End findModelByBrandId " + brandId);
            return modelRepository.findAByBrand(brandOptional);
        } else {
            throw new BrandNotFoundException();
        }
    }

    public Model saveModel(Model model) {
        logger.info("Ini saveModel " + model);
        modelRepository.save(model);
        logger.info("End saveModel " + model);
        return model;
    }

    public void removeModel(long modelId) throws ModelNotFoundException {
        logger.info("Ini removeModel ID: " + modelId);
        Model model = modelRepository.findById(modelId).orElseThrow(() -> new ModelNotFoundException(modelId));
        logger.info("End removeModel Model: " + model);
        modelRepository.delete(model);
    }

    public void modifyModel(Model newModel, long modelId) throws ModelNotFoundException {
        logger.info("Ini modifyModel ID: " + modelId);
        Optional<Model> model = modelRepository.findById(modelId);
        if (model.isPresent()) {
            Model existingModel = model.get();
            existingModel.setName(newModel.getName());
            existingModel.setPrice(newModel.getPrice());
            existingModel.setMinimumSize(newModel.getMinimumSize());
            existingModel.setMaximumSize(newModel.getMaximumSize());

            modelRepository.save(existingModel);
        } else {
            throw new ModelNotFoundException(modelId);
        }
        logger.info("End modifyModel Model: " + model);
    }

    public void patchModel(long modelId, ModelPatchDto modelPatchDto) throws ModelNotFoundException {
        logger.info("Ini patchModel ID: " + modelId);
        Model oldModel = modelRepository.findById(modelId).orElseThrow(ModelNotFoundException::new);
        if (modelPatchDto.getField().equals("maximumSize")) {
            oldModel.setMaximumSize(modelPatchDto.getMaximumSize());
        }
        modelRepository.save((oldModel));
        logger.info("End patchModel");
    }
}

