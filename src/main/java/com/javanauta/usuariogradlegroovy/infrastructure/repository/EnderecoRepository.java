package com.javanauta.usuariogradlegroovy.infrastructure.repository;

import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<Entidade, TipoDoId>: o Spring Data cria a implementação sozinho
// e já entrega save, findById, findAll, delete etc. Por isso não precisa declarar nada dentro.
// A lógica de atualizar o endereço fica no UsuarioService, nunca aqui no repository.
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}