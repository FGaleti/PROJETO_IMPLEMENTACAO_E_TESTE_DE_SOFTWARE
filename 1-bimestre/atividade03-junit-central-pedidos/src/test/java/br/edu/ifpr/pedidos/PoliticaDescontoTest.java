package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PoliticaDescontoTest {
    private final PoliticaDesconto politica = new PoliticaDesconto();

    @Test
    void deveAplicarDescontoBasicoConformeOTipoDoCliente() {
        assertAll(
            () -> assertEquals(1_000, politica.calcular(new Cliente(true, false, 1), 10_000, null)),
            () -> assertEquals(2_500, politica.calcular(new Cliente(false, false, 1), 50_000, "")),
            () -> assertEquals(0, politica.calcular(new Cliente(false, false, 1), 49_999, "   "))
        );
    }

    @Test
    void deveAplicarBemVindoSomenteParaPrimeiraCompraElegivel() {
        Cliente novo = new Cliente(false, false, 0);

        assertAll(
            () -> assertEquals(2_000, politica.calcular(novo, 10_000, "  bemvindo ")),
            () -> assertEquals(0, politica.calcular(novo, 9_999, "BEMVINDO")),
            () -> assertEquals(0, politica.calcular(new Cliente(false, false, 1), 10_000, "BEMVINDO"))
        );
    }

    @Test
    void deveAplicarExtra10SomenteAPartirDoLimite() {
        Cliente comum = new Cliente(false, false, 1);

        assertAll(
            () -> assertEquals(0, politica.calcular(comum, 19_999, "EXTRA10")),
            () -> assertEquals(2_000, politica.calcular(comum, 20_000, " extra10 "))
        );
    }

    @Test
    void deveLimitarDescontoAVintePorCento() {
        Cliente vipNovo = new Cliente(true, false, 0);

        assertEquals(2_000, politica.calcular(vipNovo, 10_000, "BEMVINDO"));
    }

    @Test
    void deveTruncarPercentuaisEmCentavos() {
        assertEquals(2_000,
            politica.calcular(new Cliente(false, false, 1), 20_009, "EXTRA10"));
    }

    @Test
    void deveRejeitarSubtotalNegativoECupomDesconhecido() {
        Cliente comum = new Cliente(false, false, 1);

        assertAll(
            () -> assertThrows(IllegalArgumentException.class,
                () -> politica.calcular(comum, -1, null)),
            () -> assertThrows(IllegalArgumentException.class,
                () -> politica.calcular(comum, 10_000, "NAOEXISTE"))
        );
    }
}
