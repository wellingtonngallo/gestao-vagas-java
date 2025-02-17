package br.com.gallo.gestao_de_vagas.exceptions;

public class UserFoundException extends RuntimeException {
  public UserFoundException() {
    super("Usuario ja existe");
  }
  
}
