package br.edu.exemplo;

public class Carrinho {

    // Implemente esta classe seguindo a sequência de requisitos
    // apresentada em REQUISITOS.md e utilizando TDD.

    private double total;

    public void adicionar(Produto produto) {
        total += produto.getPreco();
    }

    public double getTotal() {
        return total;
    }
}
