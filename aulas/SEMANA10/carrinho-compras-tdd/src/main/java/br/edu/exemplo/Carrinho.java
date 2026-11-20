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

    public double getTotal() {
        double total = 0.0;
        for (Produto produto : produtos) {
            total += produto.getPreco();
        }
        return total;
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
