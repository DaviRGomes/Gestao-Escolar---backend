package com.davi.gestaoescolar.gestao_escolar.exception;

public class ComportamentoException extends RuntimeException {
    
    public ComportamentoException(String message) {
        super(message);
    }

    public ComportamentoException(String message, Throwable cause) {
        super(message, cause);
    }

    
    // Exceções específicas para diferentes cenários
    public static class ComportamentoNaoEncontradoException extends ComportamentoException {
        public ComportamentoNaoEncontradoException(String message) {
            super(message);
        }

        public ComportamentoNaoEncontradoException(Long id) {
            super("Comportamento não encontrado com ID: " + id);
        }
    }
    
    public static class ProfessorInvalidoException extends ComportamentoException {
        public ProfessorInvalidoException(String message) {
            super("Professor inválido: " + message);
        }
    }
    
    public static class AlunoInvalidoException extends ComportamentoException {
        public AlunoInvalidoException(String message) {
            super("Aluno inválido: " + message);
        }
    }
}