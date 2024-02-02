package com.savlero.ShoeStore.controller;

import com.savlero.ShoeStore.domain.Brand;
import com.savlero.ShoeStore.domain.ErrorResponse;
import com.savlero.ShoeStore.dto.BrandPatchDto;
import com.savlero.ShoeStore.exceptions.BrandNotFoundException;
import com.savlero.ShoeStore.service.BrandService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
public class BrandController {



        @Autowired
        private BrandService brandService;
        private Logger logger = LoggerFactory.getLogger(BrandController.class);

        @GetMapping("/brand")
        public ResponseEntity<List<Brand>> getAll(@Valid @RequestParam(defaultValue = "") String name,
                                                  @RequestParam(defaultValue = "0") int telephone,
                                                  @RequestParam(defaultValue = "") String address) {
            logger.info("ini GET /airlines by parameters: name={}, telephone={}, color={}", name, telephone, address);
            List<Brand> brandsList = brandService.findAll();

            if (!name.isEmpty()) {
                brandsList = brandsList.stream()
                        .filter(brand -> brand.getName().contains(name))
                        .collect(Collectors.toList());
            }
            if (telephone > 0) {
                brandsList = brandsList.stream()
                        .filter(brand -> brand.getTelephone() == telephone)
                        .collect(Collectors.toList());
            }
            if (!address.isEmpty()) {
                brandsList = brandsList.stream()
                        .filter(brand -> brand.getAddress() == address)
                        .collect(Collectors.toList());
            }
            logger.info("end GET /brands . List size: {}", brandsList.size());
            return new ResponseEntity<>(brandsList, HttpStatus.OK);
        }

        @GetMapping("/brand/{brandId}")
        public ResponseEntity<Brand> getBrand(@PathVariable long brandId) throws BrandNotFoundException {
            logger.info("ini GET/airline/" + brandId );
            Optional<Brand> optionalBrand = brandService.findById(brandId);
            Brand airline = optionalBrand.orElseThrow(() -> new BrandNotFoundException(brandId));
            logger.info("end GET/brand/" + brandId );
            return new ResponseEntity<>(airline, HttpStatus.OK);
        }

        @PostMapping("/brand")
        public ResponseEntity<Brand> saveBrand(@Valid @RequestBody Brand brand) {
            logger.info("ini Post /brand" + brand );
            Brand newBrand = brandService.saveBrand(brand);
            logger.info("end Post /brand CREATED: {}", newBrand);
            return new ResponseEntity<>(newBrand, HttpStatus.CREATED);
        }


        @DeleteMapping("/brand/{brandId}")
        public ResponseEntity<Void> deleteBrand(@PathVariable long brandId) throws BrandNotFoundException {
            logger.info("ini DELETE /airline/" + brandId );
            brandService.removeBrand(brandId);
            logger.info("end DELETE /airline/" + brandId );
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        @PutMapping("/brand/{brandId}")
        public ResponseEntity<Void> modifyBrand(@Valid @RequestBody Brand brand, @PathVariable long brandId) throws BrandNotFoundException {
            logger.info("ini PUT /brand/" + brandId );
            brandService.modifyBrand(brand, brandId);
            logger.info("end PUT /brand/" + brandId );
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        @PatchMapping(value = "/brand/{airlineId}")
        public ResponseEntity<Void> patchBrand(@PathVariable long brandId, @RequestBody BrandPatchDto brandPatchDto) throws BrandNotFoundException {
            logger.info("ini PATCH /brand/" + brandId );
            brandService.patchBrand(brandId, brandPatchDto);
            logger.info("end PATCH /brand/" + brandId );
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        @ExceptionHandler(BrandNotFoundException.class)
        public ResponseEntity<ErrorResponse> BrandNotFoundException(BrandNotFoundException pnfe) {
            logger.error("Brand not found. Details: {}", pnfe.getMessage());
            ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
            logger.error("Brand validation Exception. Details: {}", manve.getMessage());
            Map<String, String> errors = new HashMap<>();
            manve.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String message = error.getDefaultMessage();
                errors.put(fieldName, message);
            });

            return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
        }
    }


}
