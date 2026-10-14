package com.javanauta.usuariogradlegroovy.business;

import com.javanauta.usuariogradlegroovy.business.DTO.EnderecoDTO;
import com.javanauta.usuariogradlegroovy.business.DTO.TelefoneDTO;
import com.javanauta.usuariogradlegroovy.business.DTO.UsuarioDTO;
import com.javanauta.usuariogradlegroovy.business.converter.UsuarioConverter;
import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Endereco;
import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Telefone;
import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Usuario;
import com.javanauta.usuariogradlegroovy.infrastructure.exceptions.ConflictException;
import com.javanauta.usuariogradlegroovy.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.usuariogradlegroovy.infrastructure.repository.EnderecoRepository;
import com.javanauta.usuariogradlegroovy.infrastructure.repository.TelefoneRepository;
import com.javanauta.usuariogradlegroovy.infrastructure.repository.UsuarioRepository;
import com.javanauta.usuariogradlegroovy.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// @Service: avisa o Spring que esta classe é um bean da camada de negócio
// @RequiredArgsConstructor: o Lombok gera o construtor com todos os campos final,
// e é por esse construtor que o Spring injeta cada dependência abaixo
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    // Repositories de Endereco e Telefone: precisam estender JpaRepository,
    // senão o Spring não cria o bean e a aplicação não sobe
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;


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

    // Atualiza um endereço já existente, localizado pelo id
    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {

        // 1) Busca o endereço no banco pelo id.
        // Sem cast: com JpaRepository<Endereco, Long>, o findById já devolve Optional<Endereco>.
        // orElseThrow: se não achar, lança a exceção e o método para aqui.
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("id não encontrado! " + idEndereco));

        // 2) Mescla: o que veio no DTO sobrescreve, o que veio nulo mantém o valor do banco
        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);

        // 3) Salva a entidade atualizada e devolve convertida para DTO
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    // Atualiza um telefone já existente, localizado pelo id (mesma ideia do endereço)
    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO) {

        // 1) Busca o telefone no banco pelo id (Optional<Telefone>, sem cast)
        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResourceNotFoundException("id não encontrado! " + idTelefone));

        // 2) Mescla os dados do DTO com os dados que já estão no banco
        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, entity);

        // 3) Salva e devolve convertido para DTO
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

}