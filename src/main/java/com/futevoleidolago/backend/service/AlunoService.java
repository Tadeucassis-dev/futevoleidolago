package com.futevoleidolago.backend.service;

import com.futevoleidolago.backend.RequestDTO.AlunoResponseDTO;
import com.futevoleidolago.backend.RequestDTO.CadastroAlunoRequestDTO;
import com.futevoleidolago.backend.enums.StatusSolicitacao;
import com.futevoleidolago.backend.models.Aluno;
import com.futevoleidolago.backend.repositories.AlunoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public AlunoResponseDTO cadastrarSolicitacao(CadastroAlunoRequestDTO request) {
        // Verificar se já existe um aluno com o mesmo email
        Optional<Aluno> alunoExistente = alunoRepository.findByEmail(request.getEmail());
        if (alunoExistente.isPresent()) {
            throw new RuntimeException("Já existe uma solicitação com este email");
        }

        Aluno aluno = new Aluno();
        aluno.setNome(request.getNome());
        aluno.setEmail(request.getEmail());
        aluno.setTelefone(request.getTelefone());
        aluno.setDataNascimento(request.getDataNascimento());
        
        // Calcular idade automaticamente a partir da data de nascimento
        Integer idadeCalculada = calcularIdade(request.getDataNascimento());
        aluno.setIdade(idadeCalculada);
        
        aluno.setInstituicaoEnsino(request.getInstituicaoEnsino());
        aluno.setStatusSolicitacao(StatusSolicitacao.PENDENTE);
        aluno.setDataSolicitacao(LocalDateTime.now());
        aluno.setAtivo(false);

        Aluno alunoSalvo = alunoRepository.save(aluno);
        return convertToResponseDTO(alunoSalvo);
    }

    public List<AlunoResponseDTO> listarSolicitacoesPendentes() {
        return alunoRepository.findByStatusSolicitacao(StatusSolicitacao.PENDENTE)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AlunoResponseDTO> listarTodosAlunos() {
        return alunoRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AlunoResponseDTO> listarAlunosAtivos() {
        return alunoRepository.findByAtivoTrue()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public AlunoResponseDTO aprovarSolicitacao(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (aluno.getStatusSolicitacao() != StatusSolicitacao.PENDENTE) {
            throw new RuntimeException("Esta solicitação já foi processada");
        }

        aluno.setStatusSolicitacao(StatusSolicitacao.APROVADO);
        aluno.setDataAprovacao(LocalDateTime.now());
        aluno.setAtivo(true);
        aluno.setMotivoRejeicao(null);

        Aluno alunoSalvo = alunoRepository.save(aluno);
        return convertToResponseDTO(alunoSalvo);
    }

    public AlunoResponseDTO rejeitarSolicitacao(Long id, String motivo) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (aluno.getStatusSolicitacao() != StatusSolicitacao.PENDENTE) {
            throw new RuntimeException("Esta solicitação já foi processada");
        }

        aluno.setStatusSolicitacao(StatusSolicitacao.REJEITADO);
        aluno.setMotivoRejeicao(motivo);
        aluno.setAtivo(false);

        Aluno alunoSalvo = alunoRepository.save(aluno);
        return convertToResponseDTO(alunoSalvo);
    }

    public AlunoResponseDTO buscarPorId(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        return convertToResponseDTO(aluno);
    }

    public AlunoResponseDTO buscarPorEmail(String email) {
        Aluno aluno = alunoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        return convertToResponseDTO(aluno);
    }

    public void deletarAlunoRejeitado(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (aluno.getStatusSolicitacao() != StatusSolicitacao.REJEITADO) {
            throw new RuntimeException("Apenas alunos rejeitados podem ser deletados");
        }

        alunoRepository.delete(aluno);
    }

    private AlunoResponseDTO convertToResponseDTO(Aluno aluno) {
        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getDataNascimento(),
                aluno.getIdade(),
                aluno.getInstituicaoEnsino(),
                aluno.getStatusSolicitacao(),
                aluno.getDataSolicitacao(),
                aluno.getDataAprovacao(),
                aluno.getMotivoRejeicao(),
                aluno.getAtivo()
        );
    }
    
    /**
     * Calcula a idade a partir da data de nascimento
     * @param dataNascimento Data de nascimento do aluno
     * @return Idade em anos
     */
    private Integer calcularIdade(java.time.LocalDate dataNascimento) {
        if (dataNascimento == null) {
            return null;
        }
        
        LocalDate hoje = LocalDate.now();
        Period periodo = Period.between(dataNascimento, hoje);
        int idade = periodo.getYears();
        
        // Validação de idade mínima e máxima
        if (idade < 5) {
            throw new RuntimeException("Idade mínima é 5 anos");
        }
        if (idade > 80) {
            throw new RuntimeException("Idade máxima é 80 anos");
        }
        
        return idade;
    }
}