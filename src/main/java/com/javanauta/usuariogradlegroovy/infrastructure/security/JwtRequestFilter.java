package com.javanauta.usuariogradlegroovy.infrastructure.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Define a classe JwtRequestFilter, que estende OncePerRequestFilter
public class JwtRequestFilter extends OncePerRequestFilter {

    // Define propriedades para armazenar instâncias de JwtUtil e UserDetailsService
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Construtor que inicializa as propriedades com instâncias fornecidas
    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Método chamado uma vez por requisição para processar o filtro
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Obtém o valor do header "Authorization" da requisição
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            final String token = authorizationHeader.substring(7);
            try {
                // extrairEmaildoToken já valida assinatura e expiração; se falhar, cai no catch.
                // O servidor NÃO guarda tokens em lugar nenhum: ele recalcula a assinatura com a
                // chave secreta e compara. Se o conteúdo foi adulterado, a assinatura não bate.
                final String username = jwtUtil.extrairEmaildoToken(token);

                // getAuthentication() == null: ainda ninguém foi autenticado nesta requisição
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Busca o usuário no banco pelo e-mail que estava dentro do token
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(token, username)) {
                        // Avisa o Spring Security: "este usuário está autenticado nesta requisição".
                        // Daqui em diante, as rotas protegidas passam a aceitar o pedido.
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }

            } catch (JwtException | UsernameNotFoundException e) {
                // Token inválido/expirado ou usuário inexistente: segue sem autenticar (resultado: acesso negado)
                logger.debug("Token JWT rejeitado: " + e.getMessage());
            }
        }

        // Continua a cadeia de filtros, permitindo que a requisição prossiga
        chain.doFilter(request, response);
    }
}
