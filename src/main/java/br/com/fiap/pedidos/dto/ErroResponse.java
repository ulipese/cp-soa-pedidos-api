package br.com.fiap.pedidos.dto;

import java.time.LocalDateTime;

public record ErroResponse(
        int status,
        String erro,
        String mensagem,
        String requestId,
        LocalDateTime timestamp
) {
}
