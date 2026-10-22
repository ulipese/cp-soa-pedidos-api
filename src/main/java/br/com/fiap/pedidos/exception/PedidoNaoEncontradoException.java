package br.com.fiap.pedidos.exception;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException(String id) {
        super("Pedido " + id + " não encontrado");
    }
}
