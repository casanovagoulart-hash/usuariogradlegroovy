package com.javanauta.usuariogradlegroovy.controller;

import com.javanauta.usuariogradlegroovy.business.DTO.UsuarioDTO;
import com.javanauta.usuariogradlegroovy.business.UsuarioService;
import com.javanauta.usuariogradlegroovy.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

// @RestController: marca a classe como controller REST (já é um bean do Spring,
// por isso NÃO precisa de @Service aqui)
// @RequiredArgsConstructor: gera o construtor com os campos final (injeção de dependência)
@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // AuthenticationManager: confere e-mail e senha usando o UserDetailsServiceImpl e o BCrypt
    private final AuthenticationManager authenticationManager;
    // JwtUtil: gera o token depois que o login der certo
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    // @PostMapping("/login"): atende POST /usuario/login (rota já liberada na SecurityConfig)
    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {
        // authenticate: se e-mail ou senha estiverem errados, lança exceção e não gera token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha()));
        // authentication.getName() devolve o e-mail do usuário autenticado
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    // O retorno agora é UsuarioDTO (e não UsuarioService)
    @GetMapping
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscaUsuarioporEmail(email));
    }

    // @PathVariable: pega o e-mail que vem na própria URL (/usuario/{email})
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email) {
        usuarioService.deleteUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }
}