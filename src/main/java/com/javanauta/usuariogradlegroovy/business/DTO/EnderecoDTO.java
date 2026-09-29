package com.javanauta.usuariogradlegroovy.business.DTO;

import jakarta.persistence.Entity;
import lombok.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class EnderecoDTO {

    private String rua;
    private Long numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
}
