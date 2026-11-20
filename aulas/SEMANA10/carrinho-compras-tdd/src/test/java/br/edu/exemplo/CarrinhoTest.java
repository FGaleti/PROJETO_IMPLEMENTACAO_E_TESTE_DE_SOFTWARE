package br.edu.exemplo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    // ---------------------------------------------------------------- R2

    @Test
    @DisplayName("R2 — Adicionar Teclado de R$ 100,00 deve resultar em total R$ 100,00")
    void r2DeveAdicionarUmProduto() {
        // Arrange
        Carrinho carrinho = new Carrinho();
        Produto teclado = new Produto("Teclado", 100.00);

        // Act
        carrinho.adicionar(teclado);

        // Assert
        assertEquals(100.00, carrinho.getTotal(), DELTA);
    }

    // ---------------------------------------------------------------- R3

    @Test
    @DisplayName("R3 — Teclado R$ 100,00 + Mouse R$ 50,00 deve totalizar R$ 150,00")
    void r3DeveSomarVariosProdutos() {
        // Arrange
        Carrinho carrinho = new Carrinho();

        // Act
        carrinho.adicionar(new Produto("Teclado", 100.00));
        carrinho.adicionar(new Produto("Mouse", 50.00));

        // Assert
        assertEquals(150.00, carrinho.getTotal(), DELTA);
    }

    @Test
    @DisplayName("R3 — Valores com centavos devem ser somados com precisão de centavo")
    void r3DeveSomarValoresComCentavos() {
        // Arrange
        Carrinho carrinho = new Carrinho();

        // Act
        carrinho.adicionar(new Produto("Cabo", 0.10));
        carrinho.adicionar(new Produto("Adaptador", 0.20));

        // Assert
        assertEquals(0.30, carrinho.getTotal(), DELTA);
    }

    // ---------------------------------------------------------------- R4

    @Test
    @DisplayName("R4 — Remover Mouse de {Teclado, Mouse} deve deixar total R$ 100,00")
    void r4DeveRemoverProduto() {
        // Arrange
        Carrinho carrinho = new Carrinho();
        Produto teclado = new Produto("Teclado", 100.00);
        Produto mouse = new Produto("Mouse", 50.00);
        carrinho.adicionar(teclado);
        carrinho.adicionar(mouse);

        // Act
        boolean removido = carrinho.remover(mouse);

        // Assert
        assertTrue(removido);
        assertEquals(100.00, carrinho.getTotal(), DELTA);
        assertEquals(1, carrinho.getQuantidadeItens());
    }

    @Test
    @DisplayName("R4 — Remover produto que não está no carrinho retorna false e não altera o total")
    void r4RemoverProdutoInexistenteNaoAlteraTotal() {
        // Arrange
        Carrinho carrinho = new Carrinho();
        carrinho.adicionar(new Produto("Teclado", 100.00));
        Produto naoAdicionado = new Produto("Monitor", 900.00);

        // Act
        boolean removido = carrinho.remover(naoAdicionado);

        // Assert
        assertFalse(removido);
        assertEquals(100.00, carrinho.getTotal(), DELTA);
        assertEquals(1, carrinho.getQuantidadeItens());
    }

    @Test
    @DisplayName("R4 — Remover de carrinho vazio retorna false e o total continua R$ 0,00")
    void r4RemoverDeCarrinhoVazio() {
        // Arrange
        Carrinho carrinho = new Carrinho();

        // Act
        boolean removido = carrinho.remover(new Produto("Mouse", 50.00));

        // Assert
        assertFalse(removido);
        assertEquals(0.0, carrinho.getTotal(), DELTA);
        assertEquals(0, carrinho.getQuantidadeItens());
    }

    @Test
    @DisplayName("R4 — Produto adicionado duas vezes: remover retira apenas uma ocorrência")
    void r4RemoverRetiraApenasUmaOcorrencia() {
        // Arrange
        Carrinho carrinho = new Carrinho();
        Produto mouse = new Produto("Mouse", 50.00);
        carrinho.adicionar(mouse);
        carrinho.adicionar(mouse);

        // Act
        carrinho.remover(mouse);

        // Assert
        assertEquals(50.00, carrinho.getTotal(), DELTA);
        assertEquals(1, carrinho.getQuantidadeItens());
    }

    @Test
    @DisplayName("R4 — Remover todos os produtos deve zerar o total")
    void r4RemoverTodosZeraOTotal() {
        // Arrange
        Carrinho carrinho = new Carrinho();
        Produto teclado = new Produto("Teclado", 100.00);
        Produto mouse = new Produto("Mouse", 50.00);
        carrinho.adicionar(teclado);
        carrinho.adicionar(mouse);

        // Act
        carrinho.remover(teclado);
        carrinho.remover(mouse);

        // Assert
        assertEquals(0.0, carrinho.getTotal(), DELTA);
        assertEquals(0, carrinho.getQuantidadeItens());
    }
}
