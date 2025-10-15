package com.davi.gestaoescolar.gestao_escolar.exception;

public class DisciplinaException extends RuntimeException {
    
    public DisciplinaException(String message) {
        super(message);
    }

    public DisciplinaException(String message, Throwable cause) {
        super(message, cause);
    }

    
    // Exceções específicas para diferentes cenários
    public static class DisciplinaNaoEncontradaException extends DisciplinaException {
        public DisciplinaNaoEncontradaException(Long id) {
            super("Disciplina não encontrada com ID: " + id);
        }
    }
    
    public static class ProfessorInvalidoException extends DisciplinaException {
        public ProfessorInvalidoException(String message) {
            super("Professor inválido: " + message);
        }
    }
    
    public static class DadosInvalidosException extends DisciplinaException {
        public DadosInvalidosException(String message) {
            super("Dados inválidos: " + message);
        }
    }
}