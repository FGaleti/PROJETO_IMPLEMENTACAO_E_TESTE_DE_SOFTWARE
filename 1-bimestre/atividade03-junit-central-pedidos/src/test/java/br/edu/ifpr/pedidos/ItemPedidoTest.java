package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {
    @Test
    void deveCalcularTotalEInformarDisponibilidade() {
        ItemPedido disponivel = new ItemPedido("CADERNO", 1_250, 2, 2, 300, false);
        ItemPedido indisponivel = new ItemPedido("CANETA", 200, 3, 2, 20, false);

        assertAll(
            () -> assertEquals(2_500, disponivel.totalCentavos()),
            () -> assertTrue(disponivel.disponivel()),
            () -> assertFalse(indisponivel.disponivel())
        );
    }

    @Test
    void naoDeveAceitarSkuNuloOuEmBranco() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido(null, 100, 1, 1, 1, false)),
            () -> assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("   ", 100, 1, 1, 1, false))
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, 1_000_001})
    void naoDeveAceitarPrecoInvalido(long preco) {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("ITEM", preco, 1, 1, 1, false));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 101})
    void naoDeveAceitarQuantidadeInvalida(int quantidade) {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("ITEM", 100, quantidade, 1, 1, false));
    }

    @Test
    void naoDeveAceitarEstoqueNegativo() {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("ITEM", 100, 1, -1, 1, false));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 100_001})
    void naoDeveAceitarPesoInvalido(int peso) {
        assertThrows(IllegalArgumentException.class,
            () -> new ItemPedido("ITEM", 100, 1, 1, peso, false));
    }

    @Test
    void deveAceitarValoresNosLimites() {
        assertAll(
            () -> assertDoesNotThrow(() -> new ItemPedido("MIN", 1, 0, 0, 1, false)),
            () -> assertDoesNotThrow(() -> new ItemPedido("MAX", 1_000_000, 100, 100, 100_000, true))
        );
    }
}
