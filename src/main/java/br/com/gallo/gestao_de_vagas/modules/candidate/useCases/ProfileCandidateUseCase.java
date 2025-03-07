package br.com.gallo.gestao_de_vagas.modules.candidate.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.gallo.gestao_de_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.gallo.gestao_de_vagas.modules.candidate.repositories.CandidateRepository;

@Service
public class ProfileCandidateUseCase {

  @Autowired
  private CandidateRepository candidateRepository;
  
  public ProfileCandidateResponseDTO execute(UUID idCandidate) {
    var candidate = this.candidateRepository.findById(idCandidate)
      .orElseThrow(() -> {
        throw new UsernameNotFoundException("Usuario nao encontrado");
      });

    var candidateDTO = ProfileCandidateResponseDTO.builder()
      .description(candidate.getDescription())
      .username(candidate.getUsername())
      .email(candidate.getEmail())
      .name(candidate.getName())
      .id(candidate.getId())
      .build();

    return candidateDTO;
  }
}
