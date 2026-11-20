package br.edu.exemplo;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {

    // Implemente esta classe seguindo a sequência de requisitos
    // apresentada em REQUISITOS.md e utilizando TDD.

    private final List<Produto> produtos = new ArrayList<>();

    public void adicionar(Produto produto) {
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
}
