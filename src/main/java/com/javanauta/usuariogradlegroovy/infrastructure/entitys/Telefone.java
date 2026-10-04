package com.javanauta.usuariogradlegroovy.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

// @Entity: diz ao Hibernate que esta classe vira uma tabela no banco
// (precisa ser do pacote jakarta.persistence, não de outro pacote)
@Entity
// @Table: define o nome da tabela no banco
@Table(name = "telefone")
// Lombok: gera getters, setters, construtores e builder automaticamente
@Getter
@Setter
@NoArgsConstructor // o JPA exige um construtor sem argumentos
@AllArgsConstructor
@Builder
public class Telefone {

    // @Id: chave primária da tabela
    // @GeneratedValue: o banco gera o valor sozinho (auto incremento)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", length = 10)
    private String numero;

    @Column(name = "ddd", length = 3)
    private String ddd;
}