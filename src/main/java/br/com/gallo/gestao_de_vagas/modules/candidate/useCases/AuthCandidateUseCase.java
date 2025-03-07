package br.com.gallo.gestao_de_vagas.modules.candidate.useCases;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.gallo.gestao_de_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.gallo.gestao_de_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import br.com.gallo.gestao_de_vagas.modules.candidate.repositories.CandidateRepository;

@Service
public class AuthCandidateUseCase {

  
  @Value("{security.token.secret.candidate}")
  private String secretKey;

  @Autowired
  private CandidateRepository candidateRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;
  
  public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO) throws AuthenticationException {
    var candidate = this.candidateRepository.findByUsername(authCandidateRequestDTO.username())
      .orElseThrow(() -> {
        throw new UsernameNotFoundException("Usuario e senha incorretos");
      });

    var passwordMatches = this.passwordEncoder.matches(authCandidateRequestDTO.password(), candidate.getPassword());

    if (!passwordMatches) {
      throw new AuthenticationException();
    }

    Algorithm algorithm = Algorithm.HMAC256(secretKey);

    var expriesIn = Instant.now().plus(Duration.ofMinutes(10));
    var token = JWT.create()
      .withIssuer("javagas")
      .withSubject(candidate.getId().toString())
      .withClaim("roles", Arrays.asList("CANDIDATE"))
      .withExpiresAt(expriesIn)
      .sign(algorithm);

    var authCandidateResponse = AuthCandidateResponseDTO.builder()
      .access_token(token)
      .expries_in(expriesIn.toEpochMilli())
      .build();

    return authCandidateResponse;
  }
}
