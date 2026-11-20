package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnaliseRiscoTest {
    private final AnaliseRisco analise = new AnaliseRisco();

    @Test
    void deveRecusarClienteBloqueado() {
        assertEquals("RECUSADO", analise.avaliar(new Cliente(false, true, 1), 100, false));
    }

    @Test
    void deveRevisarClienteNovoPorValorOuEntregaExpressa() {
        Cliente novo = new Cliente(false, false, 0);

        assertAll(
            () -> assertEquals("APROVADO", analise.avaliar(novo, 100_000, false)),
            () -> assertEquals("REVISAO", analise.avaliar(novo, 100_001, false)),
            () -> assertEquals("REVISAO", analise.avaliar(novo, 100, true))
        );
    }

    @Test
    void deveRevisarClienteAntigoComumSomenteAcimaDeCincoMil() {
        Cliente comum = new Cliente(false, false, 1);
        Cliente vip = new Cliente(true, false, 1);

        assertAll(
            () -> assertEquals("APROVADO", analise.avaliar(comum, 500_000, false)),
            () -> assertEquals("REVISAO", analise.avaliar(comum, 500_001, false)),
            () -> assertEquals("APROVADO", analise.avaliar(vip, 500_001, false))
        );
    }

    @Test
    void naoDeveAceitarTotalNegativo() {
        assertThrows(IllegalArgumentException.class,
            () -> analise.avaliar(new Cliente(false, false, 1), -1, false));
    }
}
