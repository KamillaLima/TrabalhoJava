package br.com.fiap.cp2_tasks.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.cp2_tasks.models.Task;
import br.com.fiap.cp2_tasks.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador para manipulação de tarefas cadastradas.
 * 
 * @author Kamilla
 * @version 1.0
 */
@RestController
@RequestMapping("api/tasks")
@Tag(name = "Tarefa", description = "Manipulação de tarefas cadastradas")
@Slf4j
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    /**
     * Lista todas as tarefas cadastradas com suporte à paginação.
     *
     * @param busca Parâmetro de busca opcional para filtrar tarefas por título.
     * @param pageable Configuração de paginação.
     * @return Uma lista paginada de tarefas em formato HATEOAS.
     */
    @GetMapping
    @SecurityRequirement(name = "bearer-key")
    @Operation(
            summary = "Listar tarefas",
            description = "Retorna todas as tarefas cadastradas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem feita com sucesso"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada"),
    })
    public PagedModel<EntityModel<Object>> index(@RequestParam(required = false) String busca, @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        log.info("Buscar Tarefas");

        Page<Task> tasks = (busca == null) ?
                taskRepository.findAll(pageable) :
                taskRepository.findByTitleContaining(busca, pageable);

        return assembler.toModel(tasks.map(Task::toEntityModel));
    }

    /**
     * Retorna os detalhes de uma tarefa específica com base em seu ID.
     *
     * @param id O ID da tarefa a ser consultada.
     * @return Os detalhes da tarefa em formato HATEOAS.
     */
    @GetMapping("{id}")
    @SecurityRequirement(name = "bearer-key")
    @Operation(
            summary = "Detalhes da tarefa",
            description = "Retorna a tarefa cadastrada com o ID informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Os dados foram retornados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrada uma tarefa com esse ID"),
    })
    public EntityModel<Task> show(@PathVariable Long id) {
        log.info("Buscar Tarefa " + id);
        var task = findByTask(id);
        return task.toEntityModel();
    }

    /**
     * Cadastra uma nova tarefa com base nos dados fornecidos.
     *
     * @param task A tarefa a ser cadastrada.
     * @return A tarefa cadastrada em formato HATEOAS.
     */
    @PostMapping
    @SecurityRequirement(name = "bearer-key")
    @Operation(
            summary = "Cadastrar tarefa",
            description = "Cadastra a tarefa com os campos requisitados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Campos inválidos"),
    })
    public ResponseEntity<Object> create(@RequestBody @Valid Task task) {
        log.info("Cadastrando Tarefa" + task);
        taskRepository.save(task);
        return ResponseEntity
                .created(task.toEntityModel().getRequiredLink("self").toUri())
                .body(task.toEntityModel());
    }

    /**
     * Exclui uma tarefa com base no seu ID.
     *
     * @param id O ID da tarefa a ser excluída.
     * @return Resposta vazia com status 204 indicando que a exclusão foi bem-sucedida.
     */
    @DeleteMapping("{id}")
    @SecurityRequirement(name = "bearer-key")
    @Operation(
            summary = "Excluindo tarefa",
            description = "Exclui a tarefa cadastrada com o ID informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Requisição bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Conteúdo não encontrado"),
    })
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        log.info("Deletando Tarefa");

        taskRepository.delete(findByTask(id));
        return ResponseEntity.noContent().build();
    }

    /**
     * Atualiza os dados de uma tarefa com base no seu ID.
     *
     * @param id O ID da tarefa a ser atualizada.
     * @param task A tarefa com os dados atualizados.
     * @return Os detalhes da tarefa atualizada em formato HATEOAS.
     */
    @PutMapping("{id}")
    @SecurityRequirement(name = "bearer-key")
    @Operation(
            summary = "Alterar dados da tarefa",
            description = "Altera os dados da tarefa cadastrada com o ID informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alteração realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Alteração inválida"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public EntityModel<Task> update(@PathVariable @Valid Long id, @RequestBody Task task) {
        log.info("Alterar Tarefa " + id);
        findByTask(id);
        task.setId(id);
        taskRepository.save(task);
        return task.toEntityModel();
    }

    /**
     * Localiza uma tarefa com base em seu ID.
     *
     * @param id O ID da tarefa a ser localizada.
     * @return A tarefa encontrada, se existir.
     * @throws ResponseStatusException Se a tarefa não for encontrada, uma exceção com status HTTP 404 (Not Found) é lançada.
    */
    private Task findByTask(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
    }
}
