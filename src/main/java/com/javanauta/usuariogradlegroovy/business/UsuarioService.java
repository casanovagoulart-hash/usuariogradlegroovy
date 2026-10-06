package com.javanauta.usuariogradlegroovy.business;

import com.javanauta.usuariogradlegroovy.business.DTO.UsuarioDTO;
import com.javanauta.usuariogradlegroovy.business.converter.UsuarioConverter;
import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Usuario;
import com.javanauta.usuariogradlegroovy.infrastructure.exceptions.ConflictException;
import com.javanauta.usuariogradlegroovy.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.usuariogradlegroovy.infrastructure.repository.UsuarioRepository;
import com.javanauta.usuariogradlegroovy.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        // setSenha (e não getSenha): substitui a senha em texto puro pela versão criptografada
        usuarioDTO.setSenha(bCryptPasswordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email) {
        if (verificaEmailExistente(email)) {
            throw new ConflictException("Email já cadastrado! " + email);
        }
    }

    private boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {

        /*Obuscador do email do usuario é, através do token(tirar a obrigatoriedade do email). */
        String email = jwtUtil.extrairEmaildoToken(token.substring(7));

       /*Criptografia */
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        /*Busca os dados do usuário no banco de dados. */
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não encontrado!"));

        /*Mescla os dados que recebemos na requisição DTO com os dados do banco de dados.*/
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        /*Criptografia de senha.*/
        /* usuario.setSenha(passwordEncoder.encode(usuario.getPassword())); */

        /*Salva os dados do usuário convertido e depois e o retorna convertendo para UsuárioDTO.*/
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    // Busca o usuário pelo e-mail e devolve como DTO (nunca devolva a entidade direto)
    // @Transactional(readOnly = true): mantém a sessão do banco aberta durante todo o método,
    // assim o Hibernate consegue carregar a lista de endereços (lazy) na hora da conversão para DTO.
    // readOnly = true avisa que o método só lê dados e não altera nada.
    @Transactional(readOnly = true)
    public UsuarioDTO buscaUsuarioporEmail(String email) {
        // orElseThrow é do Optional: se não achar o usuário, lança a exceção
        // e o método para aqui. Não precisa criar nada na classe Usuario.
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.findByEmail(email).orElseThrow(
                        () -> new ResourceNotFoundException("Email não encontrado! " + email)));
    }

    // @Transactional: obrigatório para métodos de delete derivados do Spring Data
    @Transactional
    public void deleteUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }
}