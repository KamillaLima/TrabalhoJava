package br.com.fiap.cp2_tasks.config;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.cp2_tasks.exceptions.RestError;
import lombok.extern.slf4j.Slf4j;

/**
 * Classe que trata exceções em respostas de serviços REST.
 * 
 * @author Kamilla
 * @version 1.0
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    /**
     * Trata exceções relacionadas a argumentos inválidos em requisições.
     *
     * @return Uma resposta com um objeto RestError indicando o erro.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestError> argumentExceptionHandler() {
        log.error("Erro de argumento inválido");
        return ResponseEntity.badRequest().body(
            new RestError(400, "Campos inválidos")
        );
    }

    /**
     * Trata exceções de ResponseStatusException e retorna a resposta apropriada.
     *
     * @param e A exceção ResponseStatusException a ser tratada.
     * @return Uma resposta com um objeto RestError indicando o erro.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<RestError> responseStatusExceptionHandler(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(
            new RestError(e.getStatusCode().value(), e.getBody().getDetail())
        );
    }

    /**
     * Trata exceções relacionadas a mensagens HTTP não legíveis em requisições.
     *
     * @param e A exceção HttpMessageNotReadableException a ser tratada.
     * @return Uma resposta com um objeto RestError indicando o erro.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RestError> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(
            new RestError(400, "Campos inválidos")
        );
    }
}
