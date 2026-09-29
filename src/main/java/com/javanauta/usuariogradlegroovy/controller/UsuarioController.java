package com.javanauta.usuariogradlegroovy.controller;

import com.javanauta.usuariogradlegroovy.business.DTO.UsuarioDTO;
import com.javanauta.usuariogradlegroovy.business.UsuarioService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Builder
@Data
@Service
@Getter
@Setter
@RequiredArgsConstructor
@RequestMapping ("/usuario")
@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }
}
