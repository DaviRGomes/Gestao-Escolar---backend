package com.davi.gestaoescolar.gestao_escolar.exception;

public class DisciplinaException extends Exception {
    
    public DisciplinaException(String message) {
        super(message);
    }

    
    // Exceções específicas para diferentes cenários
    public static class DisciplinaNaoEncontradaException extends RuntimeException {
        public DisciplinaNaoEncontradaException(Long id) {
            super("Disciplina não encontrada com ID: " + id);
        }
    }
    
    public static class ProfessorInvalidoException extends RuntimeException {
        public ProfessorInvalidoException(String message) {
            super("Professor inválido: " + message);
        }
    }
    
    public static class DadosInvalidosException extends RuntimeException {
        public DadosInvalidosException(String message) {
            super("Dados inválidos: " + message);
        }
    }
}