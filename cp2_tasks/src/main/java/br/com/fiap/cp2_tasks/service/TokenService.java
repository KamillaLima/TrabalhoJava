package br.com.fiap.cp2_tasks.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.fiap.cp2_tasks.models.Credencial;
import br.com.fiap.cp2_tasks.models.Token;
import br.com.fiap.cp2_tasks.models.User;
import br.com.fiap.cp2_tasks.repository.UserRepository;

/**
 * Um serviço para geração e validação de tokens JWT.
 * 
 * @author Kamilla
 * @version 1.0
 */
@Service
public class TokenService {

    @Autowired
    UserRepository usuarioRepository;

    /**
     * Gera um token JWT com base nas credenciais fornecidas.
     *
     * @param credencial As credenciais do usuário.
     * @return Um objeto Token contendo o token JWT gerado.
     */
    public Token generateToken(Credencial credencial) {
        Algorithm alg = Algorithm.HMAC256("meusecret");
        var jwt = JWT.create()
                    .withSubject(credencial.username())
                    .withIssuer("Cp2_tasks")
                    .withExpiresAt(Instant.now().plus(20, ChronoUnit.MINUTES))
                    .sign(alg);
        return new Token(jwt, "JWT", "Bearer");
    }

    /**
     * Valida um token JWT e retorna o usuário associado a ele.
     *
     * @param token O token JWT a ser validado.
     * @return O usuário associado ao token, se válido.
     * @throws JWTVerificationException Se o token não for válido ou o usuário não for encontrado.
     */
    public User validate(String token) {
        Algorithm alg = Algorithm.HMAC256("meusecret");
        var username = JWT.require(alg)
                .withIssuer("Cp2_tasks")
                .build()
                .verify(token)
                .getSubject();

        return usuarioRepository.findByUsername(username).orElseThrow(
                () -> new JWTVerificationException("Usuário não encontrado"));
    }
}
