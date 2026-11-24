package com.javanauta.usuariogradlegroovy.business;

import com.javanauta.usuariogradlegroovy.infrastructure.clients.ViaCepClient;
import com.javanauta.usuariogradlegroovy.infrastructure.clients.ViaCepDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ViaCepService {

    private final ViaCepClient client;

    public ViaCepDTO buscaDadosEndereco(String cep) {
        return client.buscaDadosEndereco(processarCep(cep));
    }

    private String processarCep(String cep) {
        String cepFormatado = cep.replace(" ", "").replace("-", "");

        // Também pode-se usar, ("[0-9]+") especifica a quantidade de caracteres.
        if (!cepFormatado.matches("\\d+") || !Objects.equals(cepFormatado.length(), 8)) {
            throw new IllegalArgumentException("O cep contém caractéres inválidos, verifique!");
        }

        return cepFormatado;
    }
}
