package com.javanauta.usuariogradlegroovy.business.DTO;

import jakarta.persistence.Column;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnderecoDTO {

    @Column(name = "id", nullable = false, length = 200)
    private String id;

    private String rua;
    private Long numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
}
