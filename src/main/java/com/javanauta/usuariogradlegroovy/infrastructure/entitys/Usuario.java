package com.javanauta.usuariogradlegroovy.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuario")
public class Usuario implements UserDetails{

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O banco gera o id sozinho
    private Long id;
    @Column(name = "nome", length = 100)
    private String nome;
    // unique = true: não permite e-mails repetidos no banco
    @Column(name = "email", length = 100, unique = true)
    private String email;
    @Column(name = "senha")
    private String senha; // Sempre salvar já criptografada (BCrypt)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private List<Telefone> telefones;
    // Um usuário tem vários endereços: relação um-para-muitos
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private List<Endereco> enderecos;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return senha; // a senha (já criptografada) que o Spring Security compara no login
    }

    @Override
    public String getUsername() {
        return email; // o e-mail é o "login" do usuário
    }
}