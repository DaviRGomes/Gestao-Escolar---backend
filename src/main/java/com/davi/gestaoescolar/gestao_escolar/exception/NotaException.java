package com.davi.gestaoescolar.gestao_escolar.exception;

public class NotaException extends Exception {
    
    public NotaException(String message) {
        super(message);
    }

    
    // Exceções específicas para diferentes cenários
    public static class NotaNaoEncontradaException extends RuntimeException {
        public NotaNaoEncontradaException(Long id) {
            super("Nota não encontrada com ID: " + id);
        }
    }
    
    public static class AlunoInvalidoException extends RuntimeException {
        public AlunoInvalidoException(String message) {
            super("Aluno inválido: " + message);
        }
    }
    
    public static class RegistroAulaInvalidoException extends RuntimeException {
        public RegistroAulaInvalidoException(String message) {
            super("Registro de aula inválido: " + message);
        }
    }
    
    public static class DadosInvalidosException extends RuntimeException {
        public DadosInvalidosException(String message) {
            super("Dados inválidos: " + message);
        }
    }
    
    public static class NotaDuplicadaException extends RuntimeException {
        public NotaDuplicadaException(String message) {
            super("Nota duplicada: " + message);
        }
    }
}