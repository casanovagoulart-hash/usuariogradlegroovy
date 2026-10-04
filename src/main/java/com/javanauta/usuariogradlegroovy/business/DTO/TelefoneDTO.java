package com.javanauta.usuariogradlegroovy.business.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TelefoneDTO {

// IDENTITY: o próprio banco gera o valor (auto incremento)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ddd;
    private String numero;
}
