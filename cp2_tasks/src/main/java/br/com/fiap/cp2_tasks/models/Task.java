package br.com.fiap.cp2_tasks.models;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import br.com.fiap.cp2_tasks.controllers.TaskController;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Uma entidade que representa uma tarefa.
 * 
 * @author Kamilla
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "T_CT_TASK")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_task")
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 50)
    @Column(name = "title_task")
    private String title;

    @Size(max = 255)
    @Column(name = "ds_task")
    private String description;

    @NotNull
    @Column(name = "st_task")
    private String status;

    @NotNull
    @Column(name = "dt_due")
    private LocalDate dueDate;

    /**
     * Converte a tarefa em um objeto EntityModel, adicionando links HATEOAS.
     *
     * @return Um objeto EntityModel representando a tarefa com links HATEOAS.
     */
    public EntityModel<Task> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(TaskController.class).show(id)).withSelfRel(),
            linkTo(methodOn(TaskController.class).delete(id)).withRel("delete"),
            linkTo(methodOn(TaskController.class).index(null, Pageable.unpaged())).withRel("all")
        );
    }
}
