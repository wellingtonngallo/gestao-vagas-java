package br.com.gallo.gestao_de_vagas.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// ControllerAdvice faz os tratamentos de exceções globais
@ControllerAdvice
public class ExceptionHandlerController {

  private MessageSource messageSource;
  
  public ExceptionHandlerController(MessageSource message) {
    this.messageSource = message;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ErrorMessageDTO>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    List<ErrorMessageDTO> dto = new ArrayList<>();

    exception.getBindingResult()
      .getFieldErrors().forEach(error -> {
        String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());

        ErrorMessageDTO getErrors = new ErrorMessageDTO(message, error.getField());

        dto.add(getErrors);
      });

      return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
  }
}
