package com.savlero.ShoeStore.controller;

import com.savlero.ShoeStore.domain.ErrorResponse;
import com.savlero.ShoeStore.domain.Model;
import com.savlero.ShoeStore.dto.ModelPatchDto;
import com.savlero.ShoeStore.exceptions.ModelNotFoundException;
import com.savlero.ShoeStore.service.ModelService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ModelController {
    @Autowired
    private ModelService modelService;
    @Autowired
    private BrandService brandService;
    private Logger logger = LoggerFactory.getLogger(ModelController.class);

    @GetMapping("/models")
    public ResponseEntity<List<Model>> getAll(@Valid @RequestParam(defaultValue = "") String name,
                                              @RequestParam(defaultValue = "0") int minimumSize,
                                              @RequestParam(defaultValue = "0") int maximumSize) {
        logger.info("ini GET /models by parameters: name={}, minimumSize={}, maximumSize={}", name, minimumSize, maximumSize);
        List<Model> modelList = modelService.findAll();

        if (!name.isEmpty()) {
            modelList = modelList.stream()
                    .filter(model -> model.getName().contains(name))
                    .collect(Collectors.toList());
        }
        if (minimumSize > 0) {
            modelList = modelList.stream()
                    .filter(model -> model.getMinimumSize() == minimumSize)
                    .collect(Collectors.toList());
        }
        if (maximumSize > 0) {
            modelList = modelList.stream()
                    .filter(model -> model.getMinimumSize() == maximumSize)
                    .collect(Collectors.toList());
        }
        logger.info("end GET /models . List size: {}", modelList.size());
        return new ResponseEntity<>(modelList, HttpStatus.OK);
    }

    @GetMapping("/model/{modelId}")
    public ResponseEntity<Model> getModel(@PathVariable long modelId) throws ModelNotFoundException {
        logger.info("ini GET/model/" + modelId);
        Optional<Model> optionalModel = modelService.findById(modelId);
        Model model = optionalModel.orElseThrow(() -> new ModelNotFoundException(modelId));
        logger.info("end GET/model/" + modelId);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("/brand/{brandId}/models")
    public ResponseEntity<List<Model>> getModelByAirlineId(@PathVariable long modelId) {
        logger.info("ini GET/brand/ " + modelId + "/models");
        try {
            List<Model> model = modelService.findModelByBrnadlId(brandId);
            return new ResponseEntity<>(model, HttpStatus.OK);
        } catch (BrandNotFoundException e) {
            logger.warn("BrandNotFoundException ID: " + brandId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Brand not found with ID: " + modelId, e);
        } finally {
            logger.info("end GET/brand/ " + modelId + "/models");
        }
    }

    @PostMapping("/model")
    public ResponseEntity<Model> saveModel(@Valid @RequestBody Model model) {
        logger.info("ini Post /model" + model);
        Model newModel = modelService.saveModel(model);
        logger.info("end Post /model CREATED: {}", newModel);
        return new ResponseEntity<>(newModel, HttpStatus.CREATED);
    }


    @DeleteMapping("/model/{modelId}")
    public ResponseEntity<Void> deleteModel(@PathVariable long modelId) throws ModelNotFoundException {
        logger.info("ini DELETE /model/" + modelId);
        modelService.removeModel(modelId);
        logger.info("end DELETE /model/" + modelId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/model/{modelId}")
    public ResponseEntity<Void> modifyModel(@Valid @RequestBody Model model, @PathVariable long modelId) throws ModelNotFoundException {
        logger.info("ini PUT /model/" + modelId);
        modelService.modifyModel(model, modelId);
        logger.info("end PUT /model/" + modelId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = "/model/{modelId}")
    public ResponseEntity<Void> patchModel(@PathVariable long modelId, @RequestBody ModelPatchDto modelPatchDto) throws ModelNotFoundException {
        logger.info("ini PATCH /model/" + modelId);
        modelService.patchModel(modelId, modelPatchDto);
        logger.info("end PATCH /model/" + modelId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<ErrorResponse> ModelNotFoundException(ModelNotFoundException pnfe) {
        logger.error("Model not found. Details: {}", pnfe.getMessage());
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        logger.error("Model validation Exception. Details: {}", manve.getMessage());
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }
}

