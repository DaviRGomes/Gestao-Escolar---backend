package com.davi.gestaoescolar.gestao_escolar.exception;

public class MatriculaException extends RuntimeException {

    public MatriculaException(String message) {
        super(message);
    }

    public MatriculaException(String message, Throwable cause) {
        super(message, cause);
    }

    // Exceção para matrícula não encontrada
    public static class MatriculaNaoEncontradaException extends MatriculaException {
        public MatriculaNaoEncontradaException(String message) {
            super(message);
        }
    }

    // Exceção para aluno inválido
    public static class AlunoInvalidoException extends MatriculaException {
        public AlunoInvalidoException(String message) {
            super(message);
        }
    }

    // Exceção para turma inválida
    public static class TurmaInvalidaException extends MatriculaException {
        public TurmaInvalidaException(String message) {
            super(message);
        }
    }

    // Exceção para dados inválidos
    public static class DadosInvalidosException extends MatriculaException {
        public DadosInvalidosException(String message) {
            super(message);
        }
    }

    // Exceção para matrícula duplicada
    public static class MatriculaDuplicadaException extends MatriculaException {
        public MatriculaDuplicadaException(String message) {
            super(message);
        }
    }
}