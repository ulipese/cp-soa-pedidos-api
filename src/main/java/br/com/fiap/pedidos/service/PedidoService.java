package br.com.fiap.pedidos.service;

import br.com.fiap.pedidos.dto.PedidoRequest;
import br.com.fiap.pedidos.exception.PedidoNaoEncontradoException;
import br.com.fiap.pedidos.model.Pedido;
import br.com.fiap.pedidos.model.StatusPedido;
import br.com.fiap.pedidos.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public Pedido criar(PedidoRequest request) {
        Pedido pedido = new Pedido(
                UUID.randomUUID().toString(),
                request.cliente(),
                request.descricao(),
                request.valor(),
                StatusPedido.CRIADO,
                LocalDateTime.now()
        );
        repository.salvar(pedido);

        MDC.put("operacao", "CRIAR_PEDIDO");
        MDC.put("pedidoId", pedido.id());
        log.info("Pedido criado - cliente: {}, valor: {}", pedido.cliente(), pedido.valor());

        return pedido;
    }

    public Pedido buscarPorId(String id) {
        MDC.put("operacao", "CONSULTAR_PEDIDO");
        MDC.put("pedidoId", id);

        Pedido pedido = repository.buscarPorId(id).orElseThrow(() -> {
            log.warn("Pedido não encontrado");
            return new PedidoNaoEncontradoException(id);
        });

        log.info("Pedido consultado");
        return pedido;
    }
}
