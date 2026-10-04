package com.javanauta.usuariogradlegroovy.infrastructure.repository;

import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository: marca a interface como componente de acesso ao banco
// extends JpaRepository<Usuario, Long>: ganha save, findById, delete etc. prontos
// (Usuario = entidade, Long = tipo do id)
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Verifica se já existe um usuário com esse e-mail (útil no cadastro)
    boolean existsByEmail(String email);

    // Busca o usuário pelo e-mail; Optional evita NullPointerException
    // e é ele que permite usar o .orElseThrow no service
    Optional<Usuario> findByEmail(String email);

    // Métodos "deleteBy" precisam de transação ativa.
    // Como já coloquei @Transactional no service, não precisa repetir aqui.
    void deleteByEmail(String email);
}