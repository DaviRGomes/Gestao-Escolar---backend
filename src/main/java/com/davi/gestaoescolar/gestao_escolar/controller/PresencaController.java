package com.davi.gestaoescolar.gestao_escolar.controller;

import com.davi.gestaoescolar.gestao_escolar.model.Presenca;
import com.davi.gestaoescolar.gestao_escolar.service.PresencaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/presencas")
@CrossOrigin(origins = "*")
public class PresencaController {

    @Autowired
    private PresencaService presencaService;

    /**
     * Salvar nova presença
     */
    @PostMapping
    public ResponseEntity<Presenca> salvar(@RequestBody Presenca presenca) {
        try {
            Presenca novaPresenca = presencaService.salvar(presenca);
            return new ResponseEntity<>(novaPresenca, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Atualizar presença existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Presenca> atualizar(@PathVariable Long id, @RequestBody Presenca presenca) {
        try {
            Presenca presencaAtualizada = presencaService.atualizar(id, presenca);
            return new ResponseEntity<>(presencaAtualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar presença por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Presenca> buscarPorId(@PathVariable Long id) {
        Optional<Presenca> presenca = presencaService.buscarPorId(id);
        if (presenca.isPresent()) {
            return new ResponseEntity<>(presenca.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Listar todas as presenças
     */
    @GetMapping
    public ResponseEntity<List<Presenca>> listarTodas() {
        List<Presenca> presencas = presencaService.listarTodas();
        return new ResponseEntity<>(presencas, HttpStatus.OK);
    }

    /**
     * Buscar presenças por aluno
     */
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<Presenca>> buscarPorAluno(@PathVariable Long alunoId) {
        try {
            List<Presenca> presencas = presencaService.buscarPorAluno(alunoId);
            return new ResponseEntity<>(presencas, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Buscar presenças por registro de aula
     */
    @GetMapping("/aula/{registroAulaId}")
    public ResponseEntity<List<Presenca>> buscarPorRegistroAula(@PathVariable Long registroAulaId) {
        try {
            List<Presenca> presencas = presencaService.buscarPorRegistroAula(registroAulaId);
            return new ResponseEntity<>(presencas, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Buscar presenças por status (presente/ausente)
     */
    @GetMapping("/status/{presente}")
    public ResponseEntity<List<Presenca>> buscarPorStatus(@PathVariable Boolean presente) {
        try {
            List<Presenca> presencas = presencaService.buscarPorStatus(presente);
            return new ResponseEntity<>(presencas, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Buscar presenças de um aluno por status
     */
    @GetMapping("/aluno/{alunoId}/status/{presente}")
    public ResponseEntity<List<Presenca>> buscarPorAlunoEStatus(@PathVariable Long alunoId, @PathVariable Boolean presente) {
        try {
            List<Presenca> presencas = presencaService.buscarPorAlunoEStatus(alunoId, presente);
            return new ResponseEntity<>(presencas, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Buscar presenças de uma aula por status
     */
    @GetMapping("/aula/{registroAulaId}/status/{presente}")
    public ResponseEntity<List<Presenca>> buscarPorRegistroAulaEStatus(@PathVariable Long registroAulaId, @PathVariable Boolean presente) {
        try {
            List<Presenca> presencas = presencaService.buscarPorRegistroAulaEStatus(registroAulaId, presente);
            return new ResponseEntity<>(presencas, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Buscar presença específica de um aluno em uma aula
     */
    @GetMapping("/aluno/{alunoId}/aula/{registroAulaId}")
    public ResponseEntity<List<Presenca>> buscarPorAlunoERegistroAula(@PathVariable Long alunoId, @PathVariable Long registroAulaId) {
        try {
            List<Presenca> presencas = presencaService.buscarPorAlunoERegistroAula(alunoId, registroAulaId);
            return new ResponseEntity<>(presencas, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Calcular frequência de um aluno
     */
    @GetMapping("/frequencia/aluno/{alunoId}")
    public ResponseEntity<BigDecimal> calcularFrequenciaAluno(@PathVariable Long alunoId) {
        try {
            BigDecimal frequencia = presencaService.calcularFrequenciaAluno(alunoId);
            return new ResponseEntity<>(frequencia, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Contar presenças de um aluno
     */
    @GetMapping("/contador/presencas/aluno/{alunoId}")
    public ResponseEntity<Long> contarPresencasAluno(@PathVariable Long alunoId) {
        try {
            long presencas = presencaService.contarPresencasAluno(alunoId);
            return new ResponseEntity<>(presencas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Contar faltas de um aluno
     */
    @GetMapping("/contador/faltas/aluno/{alunoId}")
    public ResponseEntity<Long> contarFaltasAluno(@PathVariable Long alunoId) {
        try {
            long faltas = presencaService.contarFaltasAluno(alunoId);
            return new ResponseEntity<>(faltas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Contar total de aulas de um aluno
     */
    @GetMapping("/contador/aulas/aluno/{alunoId}")
    public ResponseEntity<Long> contarTotalAulasAluno(@PathVariable Long alunoId) {
        try {
            long totalAulas = presencaService.contarTotalAulasAluno(alunoId);
            return new ResponseEntity<>(totalAulas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Verificar se aluno tem frequência mínima
     */
    @GetMapping("/frequencia-minima/aluno/{alunoId}")
    public ResponseEntity<Boolean> verificarFrequenciaMinima(@PathVariable Long alunoId) {
        try {
            boolean temFrequenciaMinima = presencaService.verificarFrequenciaMinima(alunoId);
            return new ResponseEntity<>(temFrequenciaMinima, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Registrar presença de um aluno (presente ou ausente)
     */
    @PostMapping("/aluno/{alunoId}/aula/{registroAulaId}")
    public ResponseEntity<Presenca> registrarPresencaAluno(@PathVariable Long alunoId, @PathVariable Long registroAulaId, @RequestParam Boolean presente, @RequestParam(required = false) String justificativa) {
        try {
            Presenca presenca = presencaService.criarPresencaBasica(alunoId, registroAulaId, presente, justificativa);
            return new ResponseEntity<>(presenca, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Alterar status de presença
     */
    @PatchMapping("/alterar-status/{presencaId}")
    public ResponseEntity<Presenca> alterarStatusPresenca(@PathVariable Long presencaId, @RequestParam Boolean novoStatus, @RequestParam(required = false) String justificativa) {
        try {
            Presenca presenca = presencaService.alterarStatusPresenca(presencaId, novoStatus, justificativa);
            return new ResponseEntity<>(presenca, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Buscar alunos com baixa frequência
     */
    @GetMapping("/baixa-frequencia")
    public ResponseEntity<List<Long>> buscarAlunosComBaixaFrequencia() {
        try {
            List<Long> alunosIds = presencaService.buscarAlunosComBaixaFrequencia();
            return new ResponseEntity<>(alunosIds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gerar relatório de frequência de um aluno
     */
    @GetMapping("/relatorio/aluno/{alunoId}")
    public ResponseEntity<String> gerarRelatorioFrequencia(@PathVariable Long alunoId) {
        try {
            String relatorio = presencaService.gerarRelatorioFrequencia(alunoId);
            return new ResponseEntity<>(relatorio, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletar presença
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            presencaService.deletar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}