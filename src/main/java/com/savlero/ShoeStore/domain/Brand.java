package com.savlero.ShoeStore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "El nombre es obligatorio")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 25, message = "El nombre no puede tener más de 25 caracteres")
    @Column
    private String name;
    @NotNull(message = "El teléfono es obligatorio")
    @Column
    @Positive
    private int telephone;
    @NotNull(message = "El el color es obligatorio")
    @Column
    private String address;

    @ToString.Exclude
    @OneToMany(mappedBy = "model")
    @JsonIgnore
    private List<Model> model;

}
