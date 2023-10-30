package br.com.fiap.cp2_tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fiap.cp2_tasks.repository.UserRepository;

/**
 * Um serviço que fornece autenticação de usuários.
 * 
 * @author Kamilla
 * @version 1.0
 */
@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    /**
     * Carrega os detalhes do usuário com base no nome de usuário.
     *
     * @param username O nome de usuário a ser pesquisado.
     * @return Os detalhes do usuário correspondentes.
     * @throws UsernameNotFoundException Se o usuário não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
