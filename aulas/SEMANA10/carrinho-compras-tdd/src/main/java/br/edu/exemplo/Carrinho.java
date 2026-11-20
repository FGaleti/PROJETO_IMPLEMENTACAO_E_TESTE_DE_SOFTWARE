package br.edu.exemplo;

import java.util.ArrayList;
import java.util.List;

/**
 * Carrinho de compras desenvolvido por TDD (ver REQUISITOS.md).
 *
 * <ul>
 *   <li>Produto null ou com preço negativo é rejeitado com IllegalArgumentException (R5);
 *       preço zero é válido.</li>
 *   <li>Subtotal igual ou superior a {@value #VALOR_MINIMO_PARA_DESCONTO} recebe
 *       desconto de 10% (R6).</li>
 *   <li>remover() compara por identidade (Produto não define equals) e retira
 *       apenas a primeira ocorrência (R4).</li>
 * </ul>
 */
public class Carrinho {

    static final double VALOR_MINIMO_PARA_DESCONTO = 500.00;
    static final double PERCENTUAL_DESCONTO = 0.10;

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
        if (subtotal >= VALOR_MINIMO_PARA_DESCONTO) {
            return subtotal * PERCENTUAL_DESCONTO;
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
