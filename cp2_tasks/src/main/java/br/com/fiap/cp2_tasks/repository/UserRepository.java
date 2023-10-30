package br.com.fiap.cp2_tasks.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.cp2_tasks.models.User;

/**
 * Uma interface que define um repositório para entidades de usuários.
 * 
 * @author Kamilla
 * @version 1.0
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Encontra usuários com base em uma parte do nome de usuário.
     *
     * @param busca A parte do nome de usuário a ser pesquisada.
     * @param pageable As opções de paginação.
     * @return Uma página de usuários que correspondem à busca.
     */
    Page<User> findByUsernameContaining(String busca, Pageable pageable);

    /**
     * Encontra um usuário com base no nome de usuário.
     *
     * @param username O nome de usuário a ser pesquisado.
     * @return Um objeto Optional que pode conter o usuário encontrado.
     */
    Optional<User> findByUsername(String username);
}
