package com.davi.gestaoescolar.gestao_escolar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlanejamentoException.class)
    public ResponseEntity<ResponseError> handlePlanejamentoException(PlanejamentoException ex){

        ResponseError response = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ComportamentoException.class)
    public ResponseEntity<ResponseError> handleComportamentoException(ComportamentoException ex){

        ResponseError response = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConteudoPlanejadoException.class)
    public ResponseEntity<ResponseError> handleConteudoPlanejadoException(ConteudoPlanejadoException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DisciplinaException.class)
    public ResponseEntity<ResponseError> handleDisciplinaException(DisciplinaException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MatriculaException.class)
    public ResponseEntity<ResponseError> handleMatriculaException(MatriculaException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NotaException.class)
    public ResponseEntity<ResponseError> handleNotaException(NotaException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NotaException.NotaNaoEncontradaException.class)
    public ResponseEntity<ResponseError> handleNotaNaoEncontradaException(NotaException.NotaNaoEncontradaException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(NotaException.AlunoInvalidoException.class)
    public ResponseEntity<ResponseError> handleAlunoInvalidoException(NotaException.AlunoInvalidoException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NotaException.RegistroAulaInvalidoException.class)
    public ResponseEntity<ResponseError> handleRegistroAulaInvalidoException(NotaException.RegistroAulaInvalidoException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NotaException.DadosInvalidosException.class)
    public ResponseEntity<ResponseError> handleNotaDadosInvalidosException(NotaException.DadosInvalidosException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NotaException.NotaDuplicadaException.class)
    public ResponseEntity<ResponseError> handleNotaDuplicadaException(NotaException.NotaDuplicadaException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleTrataException(Exception ex){

        ResponseError response = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ResponseError> handleGlobalException(ComportamentoException ex){

        ResponseError response = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


}
