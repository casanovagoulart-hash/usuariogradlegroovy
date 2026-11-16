package com.javanauta.usuariogradlegroovy.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// JwtUtil: é a "fábrica e o leitor" de tokens. Aqui o token NASCE (generateToken)
// e aqui ele é LIDO e conferido (extractClaims, extrairEmaildoToken, validateToken).
// Quem chama: o UsuarioController (no login) e o JwtRequestFilter (a cada requisição).
@Service
public class JwtUtil {

    // Chave secreta usada para assinar e verificar tokens JWT
    // Agora ela vem de fora do código (application.properties / variável de ambiente),
    // assim não fica exposta no GitHub
    // É ela que garante a segurança: sem essa chave, ninguém consegue fabricar um token válido
    // nem alterar o conteúdo de um token existente sem a assinatura deixar de bater.
    private final SecretKey secretKey;

    // O Spring lê "jwt.secret" do application.properties e entrega aqui pelo construtor
    // A chave é convertida em bytes uma única vez, em vez de a cada chamada
    // Atenção: para o algoritmo HS256, a chave precisa ter pelo menos 32 caracteres (256 bits),
    // senão o Keys.hmacShaKeyFor lança erro e a aplicação nem sobe.
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Gera um token JWT com o nome de usuário e validade de 1 hora
    // Aqui "username" é o E-MAIL do usuário (o login usa o e-mail como identificador).
    // O token tem 3 partes separadas por ponto: cabeçalho . conteúdo (payload) . assinatura
    // O conteúdo é apenas assinado, NÃO criptografado: qualquer um consegue ler (jwt.io),
    // por isso nunca se coloca senha ou dado sensível dentro dele.
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username) // Define o nome de usuário como o assunto do token (o e-mail fica guardado aqui)
                .issuedAt(new Date()) // Define a data e hora de emissão do token
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Define a data e hora de expiração (1 hora a partir da emissão)
                .signWith(secretKey) // Assina o token com a chave secreta (HS256 é escolhido pelo tamanho da chave)
                .compact(); // Constrói o token JWT
    }

    // Extrai as claims do token JWT (informações adicionais do token)
    // "Claims" são os dados que ficam dentro do token (e-mail, data de emissão, data de expiração).
    // parseSignedClaims é quem CONFERE o token: recalcula a assinatura com a chave secreta e compara.
    // Se o conteúdo foi alterado, a assinatura não bate e lança exceção (JwtException).
    // Se o token já expirou, também lança exceção (ExpiredJwtException).
    // O servidor não guarda tokens em lugar nenhum: ele só recalcula a assinatura a cada requisição.
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // Define a chave secreta para validar a assinatura do token
                .build()
                .parseSignedClaims(token) // Analisa o token JWT e valida a assinatura
                .getPayload(); // Retorna o corpo das claims
    }

    // Extrai o nome de usuário do token JWT
    // É este método que o UsuarioService usa no PUT /usuario: o e-mail sai de dentro do token,
    // então o cliente não escolhe quem vai ser atualizado (evita a falha IDOR).
    public String extrairEmaildoToken(String token) {
        // Obtém o assunto (nome de usuário) das claims do token
        return extractClaims(token).getSubject();
    }

    // Verifica se o token JWT está expirado
    // Compara a data de expiração guardada no token com a data de agora.
    public boolean isTokenExpired(String token) {
        // Compara a data de expiração do token com a data atual
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Valida o token JWT verificando o nome de usuário e se o token não está expirado
    // Usado pelo JwtRequestFilter antes de marcar o usuário como autenticado na requisição.
    public boolean validateToken(String token, String username) {
        // Extrai o nome de usuário do token
        final String extractedUsername = extrairEmaildoToken(token);
        // Verifica se o nome de usuário do token corresponde ao fornecido e se o token não está expirado
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}