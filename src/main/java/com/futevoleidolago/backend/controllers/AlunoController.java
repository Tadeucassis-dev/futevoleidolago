package com.futevoleidolago.backend.controllers;

import com.futevoleidolago.backend.RequestDTO.AlunoResponseDTO;
import com.futevoleidolago.backend.RequestDTO.CadastroAlunoRequestDTO;
import com.futevoleidolago.backend.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Map<String, Object>> cadastrarSolicitacao(@Valid @RequestBody CadastroAlunoRequestDTO request) {
        try {
            AlunoResponseDTO aluno = alunoService.cadastrarSolicitacao(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Solicitação de cadastro enviada com sucesso! Aguarde a aprovação.",
                "aluno", aluno
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    @org.springframework.web.bind.annotation.GetMapping
    public List<AlunoResponseDTO> listarTodosAlunos() {
        return alunoService.listarTodosAlunos();
    }

    @org.springframework.web.bind.annotation.GetMapping("/ativos")
    public List<AlunoResponseDTO> listarAlunosAtivos() {
        return alunoService.listarAlunosAtivos();
    }

    @org.springframework.web.bind.annotation.GetMapping("/solicitacoes-pendentes")
    public List<AlunoResponseDTO> listarSolicitacoesPendentes() {
        return alunoService.listarSolicitacoesPendentes();
    }

    @PostMapping("/{id}/aprovar")
    public ResponseEntity<Map<String, Object>> aprovarSolicitacao(@PathVariable Long id) {
        try {
            AlunoResponseDTO aluno = alunoService.aprovarSolicitacao(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Solicitação aprovada com sucesso!",
                "aluno", aluno
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    @PostMapping("/{id}/rejeitar")
    public ResponseEntity<Map<String, Object>> rejeitarSolicitacao(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String motivo = body.get("motivo");
            if (motivo == null || motivo.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "error", "Motivo da rejeição é obrigatório"
                ));
            }
            
            AlunoResponseDTO aluno = alunoService.rejeitarSolicitacao(id, motivo);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Solicitação rejeitada com sucesso!",
                "aluno", aluno
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    @org.springframework.web.bind.annotation.GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            AlunoResponseDTO aluno = alunoService.buscarPorId(id);
            return ResponseEntity.ok(aluno);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @org.springframework.web.bind.annotation.GetMapping("/email/{email}")
    public ResponseEntity<AlunoResponseDTO> buscarPorEmail(@PathVariable String email) {
        try {
            AlunoResponseDTO aluno = alunoService.buscarPorEmail(email);
            return ResponseEntity.ok(aluno);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletarAlunoRejeitado(@PathVariable Long id) {
        try {
            alunoService.deletarAlunoRejeitado(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Aluno rejeitado deletado com sucesso!"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }
}