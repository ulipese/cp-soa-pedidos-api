package br.com.fiap.pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PedidoRequest(
        @NotBlank(message = "cliente é obrigatório")
        String cliente,

        @NotBlank(message = "descricao é obrigatória")
        String descricao,

        @NotNull(message = "valor é obrigatório")
        @Positive(message = "valor deve ser maior que zero")
        BigDecimal valor
) {
}
