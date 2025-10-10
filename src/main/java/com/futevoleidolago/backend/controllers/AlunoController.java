package com.futevoleidolago.backend.controllers;

import com.futevoleidolago.backend.RequestDTO.AlunoResponseDTO;
import com.futevoleidolago.backend.RequestDTO.AprovacaoRequestDTO;
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
@CrossOrigin(origins = "http://localhost:3000")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Map<String, Object>> cadastrarSolicitacao(@Valid @RequestBody CadastroAlunoRequestDTO request) {
        try {
            AlunoResponseDTO response = alunoService.cadastrarSolicitacao(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Solicitação de cadastro enviada com sucesso! Aguarde a aprovação.",
                "aluno", response
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/solicitacoes/pendentes")
    public ResponseEntity<List<AlunoResponseDTO>> listarSolicitacoesPendentes() {
        List<AlunoResponseDTO> solicitacoes = alunoService.listarSolicitacoesPendentes();
        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunosAtivos() {
        List<AlunoResponseDTO> alunos = alunoService.listarAlunosAtivos();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarTodosAlunos() {
        List<AlunoResponseDTO> alunos = alunoService.listarTodosAlunos();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            AlunoResponseDTO aluno = alunoService.buscarPorId(id);
            return ResponseEntity.ok(aluno);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<AlunoResponseDTO> buscarPorEmail(@PathVariable String email) {
        try {
            AlunoResponseDTO aluno = alunoService.buscarPorEmail(email);
            return ResponseEntity.ok(aluno);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<AlunoResponseDTO> aprovarSolicitacao(@PathVariable Long id) {
        try {
            AlunoResponseDTO aluno = alunoService.aprovarSolicitacao(id);
            return ResponseEntity.ok(aluno);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/rejeitar")
    public ResponseEntity<AlunoResponseDTO> rejeitarSolicitacao(
            @PathVariable Long id, 
            @RequestBody AprovacaoRequestDTO request) {
        try {
            String motivo = request.getMotivoRejeicao() != null ? 
                request.getMotivoRejeicao() : "Não especificado";
            AlunoResponseDTO aluno = alunoService.rejeitarSolicitacao(id, motivo);
            return ResponseEntity.ok(aluno);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/processar")
    public ResponseEntity<AlunoResponseDTO> processarSolicitacao(
            @PathVariable Long id, 
            @RequestBody AprovacaoRequestDTO request) {
        try {
            if (request.getAprovado()) {
                return ResponseEntity.ok(alunoService.aprovarSolicitacao(id));
            } else {
                String motivo = request.getMotivoRejeicao() != null ? 
                    request.getMotivoRejeicao() : "Não especificado";
                return ResponseEntity.ok(alunoService.rejeitarSolicitacao(id, motivo));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(null);
        }
    }
}