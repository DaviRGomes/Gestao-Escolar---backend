package com.davi.gestaoescolar.gestao_escolar.exception;

/**
 * Exception base para operações relacionadas a ConteudoPlanejado
 * 
 * @author Sistema de Gestão Escolar
 * @version 1.0
 */
public class ConteudoPlanejadoException extends RuntimeException {
    
    public ConteudoPlanejadoException(String message) {
        super(message);
    }
    
    public ConteudoPlanejadoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception para quando um conteúdo planejado não é encontrado
     */
    public static class ConteudoPlanejadoNaoEncontradoException extends ConteudoPlanejadoException {
        public ConteudoPlanejadoNaoEncontradoException(String message) {
            super(message);
        }
        
        public ConteudoPlanejadoNaoEncontradoException(Long id) {
            super("Conteúdo planejado não encontrado com ID: " + id);
        }
    }

    /**
     * Exception para dados inválidos do conteúdo planejado
     */
    public static class DadosInvalidosException extends ConteudoPlanejadoException {
        public DadosInvalidosException(String message) {
            super(message);
        }
    }

    /**
     * Exception para planejamento inválido ou não encontrado
     */
    public static class PlanejamentoInvalidoException extends ConteudoPlanejadoException {
        public PlanejamentoInvalidoException(String message) {
            super(message);
        }
        
        public PlanejamentoInvalidoException(Long planejamentoId) {
            super("Planejamento não encontrado com ID: " + planejamentoId);
        }
    }

    /**
     * Exception para conflitos de data ou ordem
     */
    public static class ConflitoException extends ConteudoPlanejadoException {
        public ConflitoException(String message) {
            super(message);
        }
    }

    /**
     * Exception para operações não permitidas
     */
    public static class OperacaoNaoPermitidaException extends ConteudoPlanejadoException {
        public OperacaoNaoPermitidaException(String message) {
            super(message);
        }
    }
}