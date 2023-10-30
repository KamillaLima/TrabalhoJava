package br.com.fiap.cp2_tasks.models;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * Um registro (record) que representa as credenciais de autenticação.
 * 
 * @author Kamilla
 * @version 1.0
 */
public record Credencial(String username, String password) {

    /**
     * Converte as credenciais em um objeto de autenticação.
     *
     * @return Um objeto de autenticação baseado nas credenciais.
     */
    public Authentication toAuthentication() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
