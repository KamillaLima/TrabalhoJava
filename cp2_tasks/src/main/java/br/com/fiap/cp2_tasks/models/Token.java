package br.com.fiap.cp2_tasks.models;

/**
 * Um registro (record) que representa um token de autenticação.
 * 
 * @author Kamilla
 * @version 1.0
 */
public record Token(
    String token,
    String type,
    String prefix
) {}
