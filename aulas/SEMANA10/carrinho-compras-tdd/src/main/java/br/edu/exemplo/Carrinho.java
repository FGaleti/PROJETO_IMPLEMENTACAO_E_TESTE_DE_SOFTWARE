package br.edu.exemplo;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {

    // Implemente esta classe seguindo a sequência de requisitos
    // apresentada em REQUISITOS.md e utilizando TDD.

    private final List<Produto> produtos = new ArrayList<>();

    public void adicionar(Produto produto) {
        validar(produto);
        produtos.add(produto);
    }

    public boolean remover(Produto produto) {
        return produtos.remove(produto);
    }

    public double getSubtotal() {
        double subtotal = 0.0;
        for (Produto produto : produtos) {
            subtotal += produto.getPreco();
        }
        return subtotal;
    }

    public double getDesconto() {
        double subtotal = getSubtotal();
        if (subtotal >= 500.00) {
            return subtotal * 0.10;
        }
        return 0.0;
    }

    public double getTotal() {
        return getSubtotal() - getDesconto();
    }

    public int getQuantidadeItens() {
        return produtos.size();
    }

    private static void validar(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        if (produto.getPreco() < 0) {
            throw new IllegalArgumentException("Preço do produto não pode ser negativo");
        }
    }
}
