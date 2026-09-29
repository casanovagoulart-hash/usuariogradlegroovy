package com.javanauta.usuariogradlegroovy.business.DTO;

import jakarta.persistence.Entity;
import lombok.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TelefoneDTO {

    private String ddd;
    private String numero;
}
