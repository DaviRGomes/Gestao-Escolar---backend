package com.davi.gestaoescolar.gestao_escolar.exception;

public class AlunoException extends RuntimeException {
    public AlunoException(String mensagem) {
        super(mensagem);
    }

    public AlunoException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }

    public static class AlunoNaoEncontradoException extends AlunoException {
        public AlunoNaoEncontradoException(String message) {
            super(message);
        }

        public AlunoNaoEncontradoException(Long id) {
            super("Aluno não encontrado com ID: " + id);
        }
    }

    public static class DadosInvalidosException extends AlunoException {
        public DadosInvalidosException(String message) {
            super(message);
        }
    }

    public static class ConflitoException extends AlunoException {
        public ConflitoException(String message) {
            super(message);
        }
    }

    public static class OperacaoNaoPermitidaException extends AlunoException {
        public OperacaoNaoPermitidaException(String message) {
            super(message);
        }
    }
}