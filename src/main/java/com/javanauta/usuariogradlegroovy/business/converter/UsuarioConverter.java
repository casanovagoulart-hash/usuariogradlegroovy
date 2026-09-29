package com.javanauta.usuariogradlegroovy.business.converter;

import com.javanauta.usuariogradlegroovy.business.DTO.EnderecoDTO;
import com.javanauta.usuariogradlegroovy.business.DTO.TelefoneDTO;
import com.javanauta.usuariogradlegroovy.business.DTO.UsuarioDTO;
import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Endereco;
import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Telefone;
import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Usuario;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.List;

@Builder
@Component
public class UsuarioConverter {

    public Usuario paraUsuario(UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEndereco(usuarioDTO.getEnderecos()))
                .telefones(paraListaTelefone(usuarioDTO.getTelefones()))
                .build();
    }
    public List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecoDTOS) {
        return enderecoDTOS.stream().map(this::paraEndereco).toList();
    }

    public Endereco paraEndereco(EnderecoDTO enderecoDTO) {
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .complemeto(enderecoDTO.getComplemento())
                .bairro(enderecoDTO.getBairro())
                .cidade(enderecoDTO.getCidade())
                .estado(enderecoDTO.getEstado())
                .cep(enderecoDTO.getCep())
                .build();
    }
    public List<Telefone>  paraListaTelefone(List<TelefoneDTO> telefoneDTOS) {
        return telefoneDTOS.stream().map(this::paraListaTelefone).toList();
    }

    public Telefone paraListaTelefone(TelefoneDTO telefoneDTO) {
        return Telefone.builder()
                .ddd(telefoneDTO.getDdd())
                .numero(telefoneDTO.getNumero())
                .build()
    }

    public UsuarioDTO paraUsuarioDTO(Usuario usuarioDTO) {
        return UsuarioDTO.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEnderecoDTO(usuarioDTO.getEnderecos()))
                .telefones(paraListaTelefoneDTO(usuarioDTO.getTelefones()))
                .build();
    }
    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> enderecoDTOS) {
        return enderecoDTOS.stream().map(this::paraListaEnderecoDTO).toList();
    }

    public EnderecoDTO paraEnderecoDTO(Endereco enderecoDTO) {
        return EnderecoDTO.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .complemeto(enderecoDTO.getComplemento())
                .bairro(enderecoDTO.getBairro())
                .cidade(enderecoDTO.getCidade())
                .estado(enderecoDTO.getEstado())
                .cep(enderecoDTO.getCep())
                .build();
    }
    public List<TelefoneDTO>  paraListaTelefoneDTO(List<Telefone> telefoneDTOS) {
        return telefoneDTOS.stream().map(this::paraListaTelefoneDTO).toList();
    }

    public TelefoneDTO paraListaTelefoneDTO(Telefone telefoneDTO) {
        return TelefoneDTO.builder()
                .ddd(telefoneDTO.getDdd())
                .numero(telefoneDTO.getNumero())
                .build()
    }

}