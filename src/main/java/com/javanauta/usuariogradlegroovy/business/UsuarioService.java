package com.javanauta.usuariogradlegroovy.business;

import com.javanauta.usuariogradlegroovy.business.DTO.EnderecoDTO;
import com.javanauta.usuariogradlegroovy.business.DTO.TelefoneDTO;
import com.javanauta.usuariogradlegroovy.business.DTO.UsuarioDTO;
import com.javanauta.usuariogradlegroovy.business.converter.UsuarioConverter;
import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Endereco;
import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Telefone;
import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Usuario;
import com.javanauta.usuariogradlegroovy.infrastructure.exceptions.ConflictExceptions;
import com.javanauta.usuariogradlegroovy.infrastructure.exceptions.ResourceNotFoundExceptions;
import com.javanauta.usuariogradlegroovy.infrastructure.exceptions.UnauthorizedExceptions;
import com.javanauta.usuariogradlegroovy.infrastructure.repository.EnderecoRepository;
import com.javanauta.usuariogradlegroovy.infrastructure.repository.TelefoneRepository;
import com.javanauta.usuariogradlegroovy.infrastructure.repository.UsuarioRepository;
import com.javanauta.usuariogradlegroovy.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final String BEARER_PREFIX = "Bearer ";

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    // REFATORAÇÃO: Unificado apenas em PasswordEncoder (elimina duplicidade com BCryptPasswordEncoder)
    private final PasswordEncoder passwordEncoder;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public String autenticarUsuario(UsuarioDTO usuarioDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha())
            );
            return BEARER_PREFIX + jwtUtil.generateToken(authentication.getName());
        } catch (BadCredentialsException | UsernameNotFoundException | AuthorizationDeniedException e) {
            throw new UnauthorizedExceptions("Usuário ou senha inválidos!", e);
        }
    }

    public void emailExiste(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new ConflictExceptions("Email já cadastrado! " + email);
        }
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        String email = extrairEmailDoHeader(token);
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundExceptions("Email não encontrado!"));

        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscaUsuarioporEmail(String email) {
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.findByEmail(email).orElseThrow(
                        () -> new ResourceNotFoundExceptions("Email não encontrado! " + email)));
    }

    @Transactional
    public void deleteUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundExceptions("id não encontrado! " + idEndereco));

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO) {
        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResourceNotFoundExceptions("id não encontrado! " + idTelefone));

        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO dto) {
        String email = extrairEmailDoHeader(token);

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundExceptions("Email não localizado! " + email));

        Endereco endereco = usuarioConverter.paraEnderecoEntity(dto, usuario.getId());
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO dto) {
        String email = extrairEmailDoHeader(token);

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundExceptions("Email não localizado! " + email));

        Telefone telefone = usuarioConverter.paraTelefoneEntity(dto, usuario.getId());
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

    // REFATORAÇÃO (DRY): Centraliza a remoção do prefixo "Bearer " e a leitura do Subject JWT
    private String extrairEmailDoHeader(String token) {
        String tokenPuro = token.startsWith(BEARER_PREFIX)
                ? token.substring(BEARER_PREFIX.length())
                : token;
        return jwtUtil.extrairEmaildoToken(tokenPuro);
    }
}
