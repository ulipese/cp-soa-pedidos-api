package br.com.fiap.pedidos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Pedido(
        String id,
        String cliente,
        String descricao,
        BigDecimal valor,
        StatusPedido status,
        LocalDateTime dataCriacao
) {
}
