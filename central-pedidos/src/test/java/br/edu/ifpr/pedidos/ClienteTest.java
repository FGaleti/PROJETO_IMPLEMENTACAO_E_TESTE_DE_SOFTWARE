package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {
    @Test
    void deveCriarClienteComHistoricoValido() {
        Cliente cliente = new Cliente(true, false, 0);

        assertAll(
            () -> assertTrue(cliente.vip()),
            () -> assertFalse(cliente.bloqueado()),
            () -> assertEquals(0, cliente.comprasAnteriores())
        );
    }

    @Test
    void naoDeveAceitarHistoricoNegativo() {
        assertThrows(IllegalArgumentException.class,
            () -> new Cliente(false, false, -1));
    }
}
