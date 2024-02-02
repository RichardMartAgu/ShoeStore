package com.savlero.ShoeStore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelPatchDto {


    private String field;
    private int maximumSize;

}
