package br.com.fiap.pedidos.repository;

import br.com.fiap.pedidos.model.Pedido;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PedidoRepository {

    // armazenamento em memoria, os dados somem quando a aplicacao reinicia
    private final Map<String, Pedido> pedidos = new ConcurrentHashMap<>();

    public Pedido salvar(Pedido pedido) {
        pedidos.put(pedido.id(), pedido);
        return pedido;
    }

    public Optional<Pedido> buscarPorId(String id) {
        return Optional.ofNullable(pedidos.get(id));
    }
}
