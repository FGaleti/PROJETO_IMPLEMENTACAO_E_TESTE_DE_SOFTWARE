package br.edu.exemplo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Casos adicionais ao ProdutoTest do professor (que não é alterado).
 * Contrato: operação inválida é IGNORADA (estoque inalterado, sem exceção),
 * como definem os testes de R5 e R6.
 */
@DisplayName("Produto (estoque) — casos adicionais")
class ProdutoCasosAdicionaisTest {

    @Test
    @DisplayName("getNome() deve devolver o nome informado na criação")
    void deveGuardarONome() {
        // Arrange + Act
        Produto produto = new Produto("Teclado");

        // Assert
        assertEquals("Teclado", produto.getNome());
    }

    @Test
    @DisplayName("R6 — Adicionar quantidade zero deve ser ignorado")
    void adicionarZeroDeveSerIgnorado() {
        // Arrange
        Produto produto = new Produto("Teclado");
        produto.adicionarEstoque(5);

        // Act
        produto.adicionarEstoque(0);

        // Assert
        assertEquals(5, produto.getQuantidade());
    }

    @ParameterizedTest(name = "adicionar {0} → estoque {1}")
    @CsvSource({
            "-1, 5",  // logo abaixo de zero: ignorado
            "0,  5",  // zero: ignorado (R6: 'negativa ou igual a zero')
            "1,  6"   // menor quantidade válida
    })
    @DisplayName("R6 — Fronteira da entrada em zero")
    void fronteiraDaEntrada(int entrada, int esperado) {
        // Arrange
        Produto produto = new Produto("Teclado");
        produto.adicionarEstoque(5);

        // Act
        produto.adicionarEstoque(entrada);

        // Assert
        assertEquals(esperado, produto.getQuantidade());
    }

    @ParameterizedTest(name = "retirar {0} é ignorado")
    @ValueSource(ints = {0, -1, -10})
    @DisplayName("Retirar quantidade zero ou negativa deve ser ignorado")
    void retirarNaoPositivoDeveSerIgnorado(int quantidade) {
        // Arrange
        Produto produto = new Produto("Teclado");
        produto.adicionarEstoque(10);

        // Act
        produto.retirarEstoque(quantidade);

        // Assert
        assertEquals(10, produto.getQuantidade());
    }

    @ParameterizedTest(name = "estoque 10, retirar {0} → estoque {1}")
    @CsvSource({
            "9,  1",   // abaixo do disponível
            "10, 0",   // exatamente todo o estoque: permitido
            "11, 10"   // 1 acima do disponível: recusado
    })
    @DisplayName("R4/R5 — Fronteira da retirada em relação ao disponível")
    void fronteiraDaRetirada(int retirada, int esperado) {
        // Arrange
        Produto produto = new Produto("Teclado");
        produto.adicionarEstoque(10);

        // Act
        produto.retirarEstoque(retirada);

        // Assert
        assertEquals(esperado, produto.getQuantidade());
    }

    @Test
    @DisplayName("Retirar de estoque zerado deve ser recusado")
    void retirarDeEstoqueZerado() {
        // Arrange
        Produto produto = new Produto("Teclado");

        // Act
        produto.retirarEstoque(1);

        // Assert
        assertEquals(0, produto.getQuantidade());
    }

    @Test
    @DisplayName("Entradas e saídas intercaladas mantêm o saldo coerente")
    void entradasESaidasIntercaladas() {
        // Arrange
        Produto produto = new Produto("Teclado");

        // Act
        produto.adicionarEstoque(10); // 10
        produto.retirarEstoque(4);    // 6
        produto.adicionarEstoque(3);  // 9
        produto.retirarEstoque(20);   // recusado → 9
        produto.adicionarEstoque(-5); // ignorado → 9
        produto.retirarEstoque(-2);   // ignorado → 9
        produto.retirarEstoque(9);    // 0

        // Assert
        assertEquals(0, produto.getQuantidade());
    }
}
