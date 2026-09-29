package com.javanauta.usuariogradlegroovy.business;

import com.javanauta.usuariogradlegroovy.business.DTO.UsuarioDTO;
import com.javanauta.usuariogradlegroovy.business.converter.UsuarioConverter;
import com.javanauta.usuariogradlegroovy.infrastructure.entitys.Usuario;
import com.javanauta.usuariogradlegroovy.infrastructure.repository.UsuarioRepository;
import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Builder
@Getter
@RequiredArgsConstructor
@Setter
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }

}
