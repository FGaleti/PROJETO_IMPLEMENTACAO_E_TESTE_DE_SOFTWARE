package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {
    private ItemPedido item(long preco, int quantidade, int estoque, int peso, boolean fragil) {
        return new ItemPedido("ITEM", preco, quantidade, estoque, peso, fragil);
    }

    @Test
    void deveFecharPedidoDeClienteComumComFreteDoParanaEPagamentoAprovado() {
        Cliente cliente = new Cliente(false, false, 1);
        Pedido pedido = new Pedido(
            List.of(item(10_000, 1, 5, 1_000, false)), "PR", false, null);
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertAll(
            () -> assertEquals("PAGO", resultado.status()),
            () -> assertEquals(10_000, resultado.subtotalCentavos()),
            () -> assertEquals(0, resultado.descontoCentavos()),
            () -> assertEquals(1_200, resultado.freteCentavos()),
            () -> assertEquals(11_200, resultado.totalCentavos()),
            () -> assertEquals(List.of(11_200L), cobrancas)
        );
    }

    @Test
    void deveRetornarBloqueadoAntesDeAvaliarItensECupom() {
        AtomicInteger chamadas = new AtomicInteger();
        Pedido pedido = new Pedido(List.of(), "PR", false, "CUPOM-INVALIDO");
        PedidoService service = new PedidoService(total -> {
            chamadas.incrementAndGet();
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, new Cliente(false, true, 0));

        assertAll(
            () -> assertEquals(new ResultadoPedido("BLOQUEADO", 0, 0, 0, 0), resultado),
            () -> assertEquals(0, chamadas.get())
        );
    }

    @Test
    void deveRejeitarPedidoSemItensAtivosSemCobrar() {
        AtomicInteger chamadas = new AtomicInteger();
        Pedido pedido = new Pedido(
            List.of(item(1_000, 0, 0, 100, false)), "PR", false, null);
        PedidoService service = new PedidoService(total -> {
            chamadas.incrementAndGet();
            return true;
        });

        assertThrows(IllegalArgumentException.class,
            () -> service.fechar(pedido, new Cliente(false, false, 1)));
        assertEquals(0, chamadas.get());
    }

    @Test
    void deveRetornarSemEstoqueAntesDeValidarCupom() {
        AtomicInteger chamadas = new AtomicInteger();
        Pedido pedido = new Pedido(
            List.of(item(1_000, 2, 1, 100, false)), "PR", false, "INVALIDO");
        PedidoService service = new PedidoService(total -> {
            chamadas.incrementAndGet();
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, new Cliente(false, false, 1));

        assertAll(
            () -> assertEquals(new ResultadoPedido("SEM_ESTOQUE", 0, 0, 0, 0), resultado),
            () -> assertEquals(0, chamadas.get())
        );
    }

    @Test
    void devePropagarCupomInvalidoSemCobrar() {
        AtomicInteger chamadas = new AtomicInteger();
        Pedido pedido = new Pedido(
            List.of(item(10_000, 1, 1, 100, false)), "PR", false, "INVALIDO");
        PedidoService service = new PedidoService(total -> {
            chamadas.incrementAndGet();
            return true;
        });

        assertThrows(IllegalArgumentException.class,
            () -> service.fechar(pedido, new Cliente(false, false, 1)));
        assertEquals(0, chamadas.get());
    }

    @Test
    void deveEnviarPedidoExpressoDeClienteNovoParaRevisaoSemCobrar() {
        AtomicInteger chamadas = new AtomicInteger();
        Pedido pedido = new Pedido(
            List.of(item(10_000, 1, 1, 1_000, false)), "PR", true, null);
        PedidoService service = new PedidoService(total -> {
            chamadas.incrementAndGet();
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, new Cliente(false, false, 0));

        assertAll(
            () -> assertEquals("REVISAO", resultado.status()),
            () -> assertEquals(10_000, resultado.subtotalCentavos()),
            () -> assertEquals(0, resultado.descontoCentavos()),
            () -> assertEquals(2_700, resultado.freteCentavos()),
            () -> assertEquals(12_700, resultado.totalCentavos()),
            () -> assertEquals(0, chamadas.get())
        );
    }

    @Test
    void deveEnviarPedidoDeAltoValorParaRevisaoSemCobrar() {
        AtomicInteger chamadas = new AtomicInteger();
        Pedido pedido = new Pedido(
            List.of(item(1_000_000, 1, 1, 1_000, false)), "PR", false, null);
        PedidoService service = new PedidoService(total -> {
            chamadas.incrementAndGet();
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, new Cliente(false, false, 1));

        assertAll(
            () -> assertEquals("REVISAO", resultado.status()),
            () -> assertEquals(1_000_000, resultado.subtotalCentavos()),
            () -> assertEquals(50_000, resultado.descontoCentavos()),
            () -> assertEquals(0, resultado.freteCentavos()),
            () -> assertEquals(950_000, resultado.totalCentavos()),
            () -> assertEquals(0, chamadas.get())
        );
    }

    @Test
    void deveRetornarPagamentoRecusadoSemRepetirRecusaDefinitiva() {
        List<Long> cobrancas = new ArrayList<>();
        Pedido pedido = new Pedido(
            List.of(item(10_000, 1, 1, 1_000, false)), "PR", false, null);
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return false;
        });

        ResultadoPedido resultado = service.fechar(pedido, new Cliente(false, false, 1));

        assertAll(
            () -> assertEquals("PAGAMENTO_RECUSADO", resultado.status()),
            () -> assertEquals(11_200, resultado.totalCentavos()),
            () -> assertEquals(List.of(11_200L), cobrancas)
        );
    }

    @Test
    void deveRetornarPagamentoRecusadoAposTresIndisponibilidades() {
        AtomicInteger chamadas = new AtomicInteger();
        Pedido pedido = new Pedido(
            List.of(item(10_000, 1, 1, 1_000, false)), "PR", false, null);
        PedidoService service = new PedidoService(total -> {
            chamadas.incrementAndGet();
            throw new IllegalStateException();
        });

        ResultadoPedido resultado = service.fechar(pedido, new Cliente(false, false, 1));

        assertAll(
            () -> assertEquals("PAGAMENTO_RECUSADO", resultado.status()),
            () -> assertEquals(3, chamadas.get())
        );
    }

    @Test
    void deveValidarProcessadorPedidoEClienteObrigatorios() {
        PedidoService service = new PedidoService(total -> true);
        Pedido pedido = new Pedido(
            List.of(item(1_000, 1, 1, 100, false)), "PR", false, null);
        Cliente cliente = new Cliente(false, false, 1);

        assertAll(
            () -> assertThrows(NullPointerException.class, () -> new PedidoService(null)),
            () -> assertThrows(NullPointerException.class, () -> service.fechar(null, cliente)),
            () -> assertThrows(NullPointerException.class, () -> service.fechar(pedido, null))
        );
    }
}
