package br.com.fiap.cp2_tasks.exceptions;

/**
 * Um registro (record) que representa um erro de resposta REST.
 * 
 * @author Kamilla
 * @version 1.0
 */
public record RestError(int cod, String message) {
}
