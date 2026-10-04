package com.javanauta.usuariogradlegroovy.infrastructure.security;

import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Usuario;
import com.javanauta.usuariogradlegroovy.infrastructure.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // Apenas @Service: esta classe é uma regra de negócio, não um repositório
public class UserDetailsServiceImpl implements UserDetailsService {

    // Repositório para acessar dados de usuário no banco de dados
    // "final" + construtor = injeção segura (a dependência nunca fica nula)
    private final UsuarioRepository usuarioRepository;

    // O Spring injeta o repositório automaticamente pelo construtor (sem @Autowired)
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Implementação do método para carregar detalhes do usuário pelo e-mail
    // readOnly = true porque aqui só lemos dados, não alteramos nada
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca o usuário no banco de dados pelo e-mail
        // (sem cast: o Optional já vem tipado como Usuario)
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Cria e retorna um objeto UserDetails com base no usuário encontrado
        return User
                .withUsername(usuario.getEmail()) // Define o nome de usuário como o e-mail
                .password(usuario.getSenha())     // Define a senha (precisa estar criptografada com BCrypt no banco)
                .roles("USER")                    // Define o perfil de acesso; sem isso o usuário fica sem permissões
                .build();                         // Constrói o objeto UserDetails
    }
}