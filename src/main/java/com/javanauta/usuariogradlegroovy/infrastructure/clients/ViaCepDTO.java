package com.javanauta.usuariogradlegroovy.infrastructure.clients;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ViaCepDTO {

        public String cep;
        public String logradouro;
        public String complemento;
        public String unidade;
        public String bairro;
        public String localidade;
        public String uf;
        public String estado;
        public String regiao;
        public String ibge;
        public String gia;
        public String ddd;
        public String siafi;
    }
