package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PagamentoServiceTest {
    @Test
    void deveAprovarNaPrimeiraTentativaEEnviarOValorCorreto() {
        List<Long> valores = new ArrayList<>();
        PagamentoService service = new PagamentoService(total -> {
            valores.add(total);
            return true;
        });

        assertAll(
            () -> assertTrue(service.pagar(12_345, 3)),
            () -> assertEquals(List.of(12_345L), valores)
        );
    }

    @Test
    void devePararImediatamenteQuandoPagamentoForRecusado() {
        AtomicInteger chamadas = new AtomicInteger();
        PagamentoService service = new PagamentoService(total -> {
            chamadas.incrementAndGet();
            return false;
        });

        assertFalse(service.pagar(1_000, 3));
        assertEquals(1, chamadas.get());
    }

    @Test
    void deveTentarNovamenteAposIndisponibilidade() {
        AtomicInteger chamadas = new AtomicInteger();
        PagamentoService service = new PagamentoService(total -> {
            if (chamadas.incrementAndGet() == 1) throw new IllegalStateException();
            return true;
        });

        assertTrue(service.pagar(1_000, 3));
        assertEquals(2, chamadas.get());
    }

    @Test
    void deveEsgotarOLimiteDeTentativas() {
        AtomicInteger chamadas = new AtomicInteger();
        PagamentoService service = new PagamentoService(total -> {
            chamadas.incrementAndGet();
            throw new IllegalStateException();
        });

        assertFalse(service.pagar(1_000, 3));
        assertEquals(3, chamadas.get());
    }

    @Test
    void devePropagarExcecaoQueNaoRepresentaIndisponibilidade() {
        PagamentoService service = new PagamentoService(total -> {
            throw new IllegalArgumentException("erro definitivo");
        });

        assertThrows(IllegalArgumentException.class, () -> service.pagar(1_000, 3));
    }

    @Test
    void deveValidarDependenciaTotalELimiteDeTentativas() {
        PagamentoService service = new PagamentoService(total -> true);

        assertAll(
            () -> assertThrows(NullPointerException.class, () -> new PagamentoService(null)),
            () -> assertThrows(IllegalArgumentException.class, () -> service.pagar(0, 1)),
            () -> assertThrows(IllegalArgumentException.class, () -> service.pagar(-1, 1)),
            () -> assertThrows(IllegalArgumentException.class, () -> service.pagar(100, 0)),
            () -> assertThrows(IllegalArgumentException.class, () -> service.pagar(100, 4))
        );
    }

    @Test
    void deveAceitarOsLimitesDeUmaETresTentativas() {
        PagamentoService service = new PagamentoService(total -> true);

        assertAll(
            () -> assertTrue(service.pagar(1, 1)),
            () -> assertTrue(service.pagar(1, 3))
        );
    }
}
