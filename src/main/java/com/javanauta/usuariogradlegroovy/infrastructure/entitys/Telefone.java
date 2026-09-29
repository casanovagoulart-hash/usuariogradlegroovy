package com.javanauta.usuariogradlegroovy.infrastructure.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ddd", length = 3)
    private String ddd;
    @Column(name = "numero", length = 10)
    private String numero;
}
