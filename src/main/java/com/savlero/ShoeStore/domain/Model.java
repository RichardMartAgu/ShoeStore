package com.savlero.ShoeStore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "model")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 25, message = "El nombre no puede tener más de 25 caracteres")
    @NotNull(message = "El nombre es obligatorio")
    @Column
    private String name;
    @NotNull(message = "El precio es obligatorio")
    @Column
    private int price;
    @Min(value = 0, message = "El tamaño mínimo debe ser mayor que cero")
    @Column
    @NotNull(message = "El tamaño mínimo es obligatorio")
    @Positive
    private int minimumSize;
    @Min(value = 0, message = "El tamaño máximo debe ser mayor que cero")
    @Column
    @NotNull(message = "El tamaño máximo es obligatorio")
    @Positive
    private int maximumSize;

    @ToString.Exclude
    @ManyToOne
    @NotNull(message = "El Id de la marca asociada es obligatoria")
    @JoinColumn(name = "brand_id")
    private Brand brand;

}

