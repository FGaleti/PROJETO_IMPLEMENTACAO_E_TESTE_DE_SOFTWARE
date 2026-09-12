package br.edu.ifpr.pedidos;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraFreteTest {
    private final CalculadoraFrete calculadora = new CalculadoraFrete();
    private final Cliente comum = new Cliente(false, false, 1);

    private Pedido pedido(String uf, int peso, boolean expresso, boolean fragil) {
        ItemPedido item = new ItemPedido("ITEM", 10_000, 1, 1, peso, fragil);
        return new Pedido(List.of(item), uf, expresso, null);
    }

    @Test
    void deveUsarTarifaDaUf() {
        assertAll(
            () -> assertEquals(1_200, calculadora.calcular(pedido("PR", 1_000, false, false), comum, 10_000)),
            () -> assertEquals(2_000, calculadora.calcular(pedido("SP", 1_000, false, false), comum, 10_000)),
            () -> assertEquals(2_000, calculadora.calcular(pedido("RJ", 1_000, false, false), comum, 10_000)),
            () -> assertEquals(3_000, calculadora.calcular(pedido("SC", 1_000, false, false), comum, 10_000))
        );
    }

    @Test
    void deveCobrarCadaQuiloExcedenteOuFracao() {
        assertAll(
            () -> assertEquals(1_200, calculadora.calcular(pedido("PR", 2_000, false, false), comum, 10_000)),
            () -> assertEquals(1_500, calculadora.calcular(pedido("PR", 2_001, false, false), comum, 10_000)),
            () -> assertEquals(1_500, calculadora.calcular(pedido("PR", 3_000, false, false), comum, 10_000)),
            () -> assertEquals(1_800, calculadora.calcular(pedido("PR", 3_001, false, false), comum, 10_000))
        );
    }

    @Test
    void deveDarFreteGratisEmEntregaNormalAPartirDeTrezentosReais() {
        assertAll(
            () -> assertEquals(1_200, calculadora.calcular(pedido("PR", 1_000, false, false), comum, 29_999)),
            () -> assertEquals(0, calculadora.calcular(pedido("PR", 4_000, false, false), comum, 30_000)),
            () -> assertEquals(2_700, calculadora.calcular(pedido("PR", 1_000, true, false), comum, 30_000))
        );
    }

    @Test
    void deveAplicarVipAntesDosAdicionais() {
        Cliente vip = new Cliente(true, false, 1);
        Pedido expressoFragil = pedido("SP", 1_000, true, true);

        assertEquals(3_000, calculadora.calcular(expressoFragil, vip, 10_000));
    }

    @Test
    void deveCobrarFragilidadeUmaUnicaVez() {
        ItemPedido fragil1 = new ItemPedido("F1", 1_000, 1, 1, 100, true);
        ItemPedido fragil2 = new ItemPedido("F2", 1_000, 2, 2, 100, true);
        Pedido pedido = new Pedido(List.of(fragil1, fragil2), "PR", false, null);

        assertEquals(1_700, calculadora.calcular(pedido, comum, 10_000));
    }

    @Test
    void deveCobrarAdicionaisMesmoComBaseGratuita() {
        Pedido expressoFragil = pedido("PR", 1_000, true, true);

        assertEquals(3_200, calculadora.calcular(expressoFragil, comum, 30_000));
    }

    @Test
    void naoDeveAceitarValorLiquidoNegativo() {
        assertThrows(IllegalArgumentException.class,
            () -> calculadora.calcular(pedido("PR", 1_000, false, false), comum, -1));
    }
}
