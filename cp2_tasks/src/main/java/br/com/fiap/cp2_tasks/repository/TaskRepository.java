package br.com.fiap.cp2_tasks.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.cp2_tasks.models.Task;

/**
 * Uma interface que define um repositório para entidades de tarefas.
 * 
 * @author Kamilla
 * @version 1.0
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Encontra tarefas com base em uma parte do título.
     *
     * @param busca A parte do título a ser pesquisada.
     * @param pageable As opções de paginação.
     * @return Uma página de tarefas que correspondem à busca.
     */
    Page<Task> findByTitleContaining(String busca, Pageable pageable);
}
