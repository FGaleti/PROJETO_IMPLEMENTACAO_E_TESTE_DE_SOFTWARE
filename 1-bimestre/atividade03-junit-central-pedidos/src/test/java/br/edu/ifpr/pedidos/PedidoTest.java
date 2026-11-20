package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {
    private ItemPedido item(String sku, int quantidade, int estoque, int peso, boolean fragil) {
        return new ItemPedido(sku, 1_000, quantidade, estoque, peso, fragil);
    }

    @Test
    void deveCopiarListaECalcularSubtotalEPeso() {
        List<ItemPedido> origem = new ArrayList<>();
        origem.add(item("ATIVO", 2, 2, 300, false));
        origem.add(item("INATIVO", 0, 0, 900, true));
        Pedido pedido = new Pedido(origem, "PR", false, null);

        origem.clear();

        assertAll(
            () -> assertEquals(2, pedido.itens().size()),
            () -> assertEquals(2_000, pedido.subtotalCentavos()),
            () -> assertEquals(600, pedido.pesoGramas()),
            () -> assertFalse(pedido.temFragil()),
            () -> assertThrows(UnsupportedOperationException.class,
                () -> pedido.itens().add(item("OUTRO", 1, 1, 1, false)))
        );
    }

    @Test
    void deveEncontrarItemFragilAtivoDepoisDeItensNaoFrageis() {
        Pedido pedido = new Pedido(List.of(
            item("INATIVO-FRAGIL", 0, 0, 1, true),
            item("ATIVO-NORMAL", 1, 1, 1, false),
            item("ATIVO-FRAGIL", 1, 1, 1, true)
        ), "SP", false, null);

        assertTrue(pedido.temFragil());
    }

    @Test
    void deveAvaliarEstoqueDeTodasAsLinhas() {
        Pedido suficiente = new Pedido(List.of(
            item("A", 1, 1, 1, false), item("B", 2, 3, 1, false)), "RJ", false, null);
        Pedido faltaNoFim = new Pedido(List.of(
            item("A", 1, 1, 1, false), item("B", 2, 1, 1, false)), "RJ", false, null);
        Pedido faltaNoInicio = new Pedido(List.of(
            item("A", 2, 1, 1, false), item("B", 1, 1, 1, false)), "RJ", false, null);

        assertAll(
            () -> assertTrue(suficiente.estoqueSuficiente()),
            () -> assertFalse(faltaNoFim.estoqueSuficiente()),
            () -> assertFalse(faltaNoInicio.estoqueSuficiente())
        );
    }

    @Test
    void deveAceitarListaVaziaEUfDesconhecidaBemFormada() {
        Pedido pedido = new Pedido(List.of(), "XX", true, "cupom");

        assertAll(
            () -> assertEquals(0, pedido.subtotalCentavos()),
            () -> assertEquals(0, pedido.pesoGramas()),
            () -> assertFalse(pedido.temFragil()),
            () -> assertTrue(pedido.estoqueSuficiente())
        );
    }

    @Test
    void naoDeveAceitarListaNulaComMaisDeCemItensOuElementoNulo() {
        ItemPedido item = item("ITEM", 1, 1, 1, false);

        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                () -> new Pedido(null, "PR", false, null)),
            () -> assertThrows(IllegalArgumentException.class,
                () -> new Pedido(Collections.nCopies(101, item), "PR", false, null)),
            () -> assertThrows(NullPointerException.class,
                () -> new Pedido(Collections.singletonList(null), "PR", false, null))
        );
    }

    @Test
    void naoDeveAceitarUfInvalida() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                () -> new Pedido(List.of(), null, false, null)),
            () -> assertThrows(IllegalArgumentException.class,
                () -> new Pedido(List.of(), "pr", false, null)),
            () -> assertThrows(IllegalArgumentException.class,
                () -> new Pedido(List.of(), "P", false, null)),
            () -> assertThrows(IllegalArgumentException.class,
                () -> new Pedido(List.of(), "ABC", false, null))
        );
    }
}
