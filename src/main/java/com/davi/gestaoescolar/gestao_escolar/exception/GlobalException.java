package com.davi.gestaoescolar.gestao_escolar.exception;

public class GlobalException extends Exception{

    public static class DadosInvalidosException extends IllegalArgumentException {
        public DadosInvalidosException(String message) {
            super("Dados inválidos: " + message);
        }
    }
}
