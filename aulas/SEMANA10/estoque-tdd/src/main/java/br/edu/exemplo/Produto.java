package br.edu.exemplo;

/**
 * Produto com controle de estoque. Operações inválidas são ignoradas (estoque
 * inalterado, sem exceção): entrada menor ou igual a zero (R6), retirada menor
 * ou igual a zero e retirada maior que o disponível (R5).
 */
public class Produto {

    private String nome;
    private int quantidade;

    public Produto(String nome) {
        this.nome = nome;
        this.quantidade = 0;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void adicionarEstoque(int quantidade) {
        if (quantidade > 0) {
            this.quantidade += quantidade;
        }
    }

    public void retirarEstoque(int quantidade) {
        if (quantidade > 0 && quantidade <= this.quantidade) {
            this.quantidade -= quantidade;
        }
    }
}
