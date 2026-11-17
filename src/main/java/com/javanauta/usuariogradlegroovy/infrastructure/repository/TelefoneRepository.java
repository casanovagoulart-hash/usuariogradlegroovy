package com.javanauta.usuariogradlegroovy.infrastructure.repository;

import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

// Mesma ideia do EnderecoRepository, agora para a entidade Telefone
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {




}
