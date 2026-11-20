package br.edu.exemplo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Os testes NÃO estão implementados.
 *
 * Para cada requisito apresentado em REQUISITOS.md:
 *
 * 1. escreva primeiro o teste;
 * 2. execute e observe a falha (RED);
 * 3. implemente somente o necessário em Carrinho;
 * 4. execute novamente até o teste passar (GREEN);
 * 5. refatore, se necessário;
 * 6. avance para o próximo requisito.
 */
class CarrinhoTest {

    private static final double DELTA = 0.001;

    // ---------------------------------------------------------------- R1

    @Test
    @DisplayName("R1 — Carrinho recém-criado deve ter total R$ 0,00")
    void r1CarrinhoNovoDeveTerTotalZero() {
        // Arrange
        Carrinho carrinho = new Carrinho();

        // Act
        double total = carrinho.getTotal();

        // Assert
        assertEquals(0.0, total, DELTA);
    }
}
