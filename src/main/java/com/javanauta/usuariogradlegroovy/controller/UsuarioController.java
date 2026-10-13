package com.javanauta.usuariogradlegroovy.controller;

import com.javanauta.usuariogradlegroovy.business.DTO.EnderecoDTO;
import com.javanauta.usuariogradlegroovy.business.DTO.TelefoneDTO;
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
    // LOGIN: é aqui que o token NASCE.
    // 1) authenticate confere e-mail e senha (usa o UserDetailsServiceImpl e o BCrypt)
    // 2) se estiver certo, generateToken coloca o e-mail do usuário DENTRO do token
    //    (campo "subject") e assina com a chave secreta que só o servidor conhece
    // 3) Cada login gera um token novo, e todos funcionam até expirar
    // Atenção: "Bearer " (com espaço) NÃO faz parte do token, é só o tipo de autenticação.
    // O token de verdade é o texto longo que vem depois (eyJ...).
    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha()));
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    // O retorno agora é UsuarioDTO (e não UsuarioService)
    @GetMapping
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscaUsuarioporEmail(email));
    }

    // @PutMapping: atende PUT /usuario (PUT é o verbo usado para ATUALIZAR dados que já existem)
    // Não recebe id nem e-mail na URL de propósito: quem decide QUEM é o usuário é o token.
    // Se o id viesse nos Params, qualquer usuário logado poderia trocar o número e alterar
    // os dados de outra pessoa (falha de segurança chamada IDOR).
    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizaDadosUsuario(
            // @RequestBody: transforma o JSON enviado no Apidog em um UsuarioDTO
            @RequestBody UsuarioDTO dto,
            // @RequestHeader("Authorization"): pega o "Bearer eyJ..." do cabeçalho da requisição
            @RequestHeader("Authorization") String token) {

        // O controller só repassa: a regra de negócio fica toda no UsuarioService
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, dto));
    }

    // @PutMapping("/endereco"): atende PUT /usuario/endereco?id=1
    // Aqui o id nos Params FAZ sentido: um usuário pode ter vários endereços,
    // então precisamos dizer QUAL deles será atualizado.
    @PutMapping("/endereco")
    public ResponseEntity<EnderecoDTO> atualizaEndereco(
            // @RequestBody: transforma o JSON do Body em um EnderecoDTO
            // (só os campos enviados mudam, os nulos mantêm o valor do banco)
            @RequestBody EnderecoDTO dto,
            // @RequestParam("id"): pega o id que vem na URL, depois do "?" (aba Params do Apidog)
            @RequestParam("id") Long id) {

        // O controller só repassa: a regra de negócio fica no UsuarioService
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, dto));
    }

    // Mesma ideia do endereço, agora para o telefone: PUT /usuario/telefone?id=1
    @PutMapping("/telefone")
    public ResponseEntity<TelefoneDTO> atualizaTelefone(
            @RequestBody TelefoneDTO dto,
            @RequestParam("id") Long id) {

        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, dto));
    }

    // @PathVariable: pega o e-mail que vem na própria URL (/usuario/{email})
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email) {
        usuarioService.deleteUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }



}