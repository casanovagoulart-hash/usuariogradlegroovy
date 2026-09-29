package com.javanauta.usuariogradlegroovy.infrastructure.repository;

import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Verifica se já existe um usuário com esse e-mail (útil no cadastro)
    boolean existsByEmail(String email);

    // Busca o usuário pelo e-mail; Optional evita NullPointerException
    Optional<Usuario> findByEmail(String email);

    // Métodos "deleteBy" precisam de transação ativa, por isso o @Transactional
    @Transactional
    void deleteByEmail(String email);
}