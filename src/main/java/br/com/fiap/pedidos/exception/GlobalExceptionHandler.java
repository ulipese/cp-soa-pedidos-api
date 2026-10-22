package br.com.fiap.pedidos.exception;

import br.com.fiap.pedidos.dto.ErroResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> pedidoNaoEncontrado(PedidoNaoEncontradoException ex) {
        return erro(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining("; "));

        MDC.put("operacao", "VALIDACAO");
        log.warn("Dados inválidos: {}", mensagem);
        return erro(HttpStatus.BAD_REQUEST, mensagem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> jsonInvalido(HttpMessageNotReadableException ex) {
        MDC.put("operacao", "VALIDACAO");
        log.warn("JSON inválido no corpo da requisição");
        return erro(HttpStatus.BAD_REQUEST, "JSON inválido");
    }

    private ResponseEntity<ErroResponse> erro(HttpStatus status, String mensagem) {
        ErroResponse body = new ErroResponse(
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                MDC.get("requestId"),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(body);
    }
}
