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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.cp2_tasks.models.Credencial;
import br.com.fiap.cp2_tasks.models.Token;
import br.com.fiap.cp2_tasks.models.User;
import br.com.fiap.cp2_tasks.repository.UserRepository;
import br.com.fiap.cp2_tasks.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador para manipulação de dados de usuários cadastrados.
 * 
 * @author Kamilla
 * @version 1.0
 */
@RestController
@RequestMapping("api/usuarios")
@Tag(name = "Usuário", description = "Manipulação de dados dos usuários cadastrados")
@Slf4j
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PagedResourcesAssembler<Object> assembler;

	@Autowired
	AuthenticationManager manager;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	TokenService tokenService;

	/**
	 * Obtém uma lista paginada de usuários.
	 *
	 * @param busca   Termo de busca opcional para filtrar usuários.
	 * @param pageable Configuração da paginação.
	 * @return Uma lista paginada de usuários.
	 */
	@GetMapping
	@SecurityRequirement(name = "bearer-key")
	@Operation(
		summary = "Listar usuários",
		description = "Retorna todos os usuários cadastrados"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Listagem feita com sucesso"),
		@ApiResponse(responseCode = "404", description = "Lista não encontrada"),
	})
	public PagedModel<EntityModel<Object>> index(
		@RequestParam(required = false) String busca,
		@ParameterObject @PageableDefault(size = 5) Pageable pageable) {
		log.info("Buscar Usuários");

		Page<User> usuarios = (busca == null) ?
			userRepository.findAll(pageable) :
			userRepository.findByUsernameContaining(busca, pageable);

		return assembler.toModel(usuarios.map(User::toEntityModel));
	}

	/**
	 * Obtém detalhes de um usuário por ID.
	 *
	 * @param id O ID do usuário a ser obtido.
	 * @return Detalhes do usuário.
	 */
	@GetMapping("{id}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(
		summary = "Detalhes usuário",
		description = "Retorna o usuário cadastrado com o ID informado"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Os dados foram retornados com sucesso"),
		@ApiResponse(responseCode = "404", description = "Não foi encontrado um usuário com esse ID"),
	})
	public EntityModel<User> show(@PathVariable Long id) {
		log.info("Buscar Usuário " + id);
		var user = findByUser(id);
		return user.toEntityModel();
	}

	/**
	 * Cadastra um novo usuário.
	 *
	 * @param user O usuário a ser cadastrado.
	 * @return O usuário cadastrado com sucesso.
	 */
	@PostMapping("/cadastro")
	@Operation(
		summary = "Cadastrar usuário",
		description = "Cadastrando o usuário com os campos requisitados"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
		@ApiResponse(responseCode = "400", description = "Usuário inválido"),
		@ApiResponse(responseCode = "409", description = "Já existe um usuário com o e-mail fornecido"),
	})
	public ResponseEntity<Object> create(@RequestBody @Valid User user) {
		log.info("Cadastrando Usuário" + user);
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
		return ResponseEntity
			.created(user.toEntityModel().getRequiredLink("self").toUri())
			.body(user.toEntityModel());
	}

	/**
	 * Exclui um usuário por ID.
	 *
	 * @param id O ID do usuário a ser excluído.
	 * @return Resposta de sucesso.
	 */
	@DeleteMapping("{id}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(
		summary = "Excluindo usuário",
		description = "Exclui o usuário cadastrado com o ID informado"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Requisição bem-sucedida"),
		@ApiResponse(responseCode = "404", description = "Conteúdo não encontrado"),
	})
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		log.info("Deletando Usuário");
		userRepository.delete(findByUser(id));
		return ResponseEntity.noContent().build();
	}

	/**
	 * Atualiza os dados de um usuário por ID.
	 *
	 * @param id   O ID do usuário a ser atualizado.
	 * @param user Os dados atualizados do usuário.
	 * @return Os dados atualizados do usuário.
	 */
	@PutMapping("{id}")
	@SecurityRequirement(name = "bearer-key")
	@Operation(
		summary = "Alterar dados do usuário",
		description = "Alteração de dados do usuário cadastrado com o ID informado"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Alteração realizada com sucesso"),
		@ApiResponse(responseCode = "400", description = "Alteração inválida"),
		@ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
	})
	public EntityModel<User> update(@PathVariable @Valid Long id, @RequestBody User user) {
		log.info("Alterar Usuário " + id);
		findByUser(id);
		user.setId(id);
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
		return user.toEntityModel();
	}

	/**
	 * Realiza o login de um usuário.
	 *
	 * @param credencial As credenciais de login do usuário.
	 * @return O token de autenticação.
	 */
	@PostMapping("/login")
	@Operation(
		summary = "Login do usuário",
		description = "Loga o usuário retornando o token do mesmo"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
		@ApiResponse(responseCode = "400", description = "Campos inválidos")
	})
	public ResponseEntity<Token> login(@RequestBody Credencial credencial) {
		manager.authenticate(credencial.toAuthentication());
		var token = tokenService.generateToken(credencial);
		return ResponseEntity.ok(token);
	}

	/**
	 * Busca um usuário por ID no repositório de usuários.
	 *
	 * @param id O ID do usuário a ser encontrado.
	 * @return O usuário encontrado.
	 * @throws ResponseStatusException Se o usuário não for encontrado, lança uma exceção com status HTTP 404 (NOT FOUND).
	*/
	private User findByUser(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
	}
}
