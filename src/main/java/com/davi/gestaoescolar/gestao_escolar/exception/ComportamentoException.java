package com.davi.gestaoescolar.gestao_escolar.exception;

public class ComportamentoException extends Exception {
    
    public ComportamentoException(String message) {
        super(message);
    }

    
    // Exceções específicas para diferentes cenários
    public static class ComportamentoNaoEncontradoException extends RuntimeException {
        public ComportamentoNaoEncontradoException(Long id) {
            super("Comportamento não encontrado com ID: " + id);
        }
    }
    
    public static class ProfessorInvalidoException extends RuntimeException {
        public ProfessorInvalidoException(String message) {
            super("Professor inválido: " + message);
        }
    }
    
    public static class AlunoInvalidoException extends RuntimeException {
        public AlunoInvalidoException(String message) {
            super("Aluno inválido: " + message);
        }
    }
}