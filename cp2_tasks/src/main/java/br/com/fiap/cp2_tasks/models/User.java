package br.com.fiap.cp2_tasks.models;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.fiap.cp2_tasks.controllers.UserController;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Uma entidade que representa um usuário.
 * 
 * @author Kamilla
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "T_CT_USUARIO")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 50)
    @Column(name = "nm_user")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8)
    @Column(name = "ds_password")
    private String password;
    
    @NotBlank(message = "Roles is mandatory")
    private String roles;

    /**
     * Converte o usuário em um objeto EntityModel, adicionando links HATEOAS.
     *
     * @return Um objeto EntityModel representando o usuário com links HATEOAS.
     */
    public EntityModel<User> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(UserController.class).show(id)).withSelfRel(),
            linkTo(methodOn(UserController.class).delete(id)).withRel("delete"),
            linkTo(methodOn(UserController.class).index(null, Pageable.unpaged())).withRel("all")
        );
    }

    /**
     * Retorna a coleção de autoridades (papéis) associadas ao usuário.
     *
     * @return A coleção de autoridades (papéis) do usuário.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USUARIO"));
    }

    /**
     * Retorna a senha do usuário.
     *
     * @return A senha do usuário.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Retorna o nome de usuário do usuário.
     *
     * @return O nome de usuário do usuário.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Verifica se a conta do usuário não está expirada.
     *
     * @return `true` se a conta não estiver expirada, caso contrário, `false`.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Verifica se a conta do usuário não está bloqueada.
     *
     * @return `true` se a conta não estiver bloqueada, caso contrário, `false`.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Verifica se as credenciais do usuário não estão expiradas.
     *
     * @return `true` se as credenciais não estiverem expiradas, caso contrário, `false`.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Verifica se o usuário está habilitado e ativo.
     *
     * @return `true` se o usuário estiver habilitado, caso contrário, `false`.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
