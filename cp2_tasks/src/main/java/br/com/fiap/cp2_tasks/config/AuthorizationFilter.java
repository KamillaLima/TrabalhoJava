package br.com.fiap.cp2_tasks.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fiap.cp2_tasks.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de autorização que verifica e valida tokens de autenticação.
 * 
 * @author Kamilla
 * @version 1.0
 */
@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Pegar o token do header
        String token = getToken(request);

        // Se houver um token
        if (token != null) {
            // Validar o token
            var usuario = tokenService.validate(token);

            // Autenticar o usuário
            Authentication auth = new UsernamePasswordAuthenticationToken(usuario.getUsername(), null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Obtém o token de autenticação do cabeçalho da solicitação.
     *
     * @param request A solicitação HTTP.
     * @return O token de autenticação, ou nulo se não estiver presente.
     */
    private String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization"); // Bearer eyhjshdkjhfks.gusjdfkgjsdfkjg.ndkfsdkfgbksd

        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        return header.replace("Bearer ", "");
    }
}
