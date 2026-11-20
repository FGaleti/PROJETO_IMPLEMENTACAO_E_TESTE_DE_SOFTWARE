package br.edu.exemplo.analise;

import br.edu.exemplo.analise.AnalisadorUsabilidade.Proporcao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Formatos de coleta de menor esforço: CSV exportado do Google Forms (SUS) e
 * planilha simplificada preenchida no celular (Google Sheets). Usa só fixtures FICTÍCIAS.
 */
@DisplayName("Formatos alternativos: Google Forms e planilha simplificada")
class FormatosAlternativosTest {

    private static final double DELTA = 0.001;

    @Test
    @DisplayName("CSV do Google Forms: carimbo de data/hora ignorado, cabeçalhos longos mapeados para Q1..Q10")
    void susDoGoogleForms() throws Exception {
        List<RespostaSus> respostas = LeitorCsv.ler(LeitorCsvTest.fixture("FICTICIO-google-forms-sus.csv")).stream()
                .map(RespostaSus::de).flatMap(Optional::stream).toList();
        assertEquals(2, respostas.size());
        assertEquals("F01", respostas.get(0).participante());
        assertEquals(50.0, respostas.get(0).escore(), DELTA);
        assertEquals(75.0, respostas.get(1).escore(), DELTA);
    }

    @Test
    @DisplayName("Títulos '1. …', 'Q01) …' e 'q 2 - …' são reconhecidos; outras colunas são ignoradas")
    void variacoesDeTituloDoForms() {
        Map<String, String> m = RespostaSus.normalizar(Map.of(
                "Carimbo de data/hora", "2026/11/21",
                "Seu participante", "P09",
                "1. Eu acho que gostaria…", "5",
                "Q01) duplicado do item 1", "5",
                "q 2 - Eu achei…", "1",
                "Comentário livre", "gostei",
                "123 numero longo", "x"));
        assertEquals("P09", m.get("participante"));
        assertEquals("5", m.get("Q1"));
        assertEquals("1", m.get("Q2"));
        assertEquals(3, m.size(), m.toString());
    }

    @Test
    @DisplayName("Planilha simplificada: tarefa por número, 'segundos', 'ok', 'ajuda', Sim/Não")
    void planilhaSimplificada() throws Exception {
        List<Observacao> obs = LeitorCsv.ler(LeitorCsvTest.fixture("FICTICIO-observacao-simplificada.csv")).stream()
                .map(Observacao::de).flatMap(Optional::stream).toList();
        assertEquals(7, obs.size(), "a linha de F02 sem 'ok' é ignorada");
        assertEquals(new Observacao("F01", "Aplicar filtros", 70.0, true, 2, true,
                "aplicou Frete grátis antes do Full"), obs.get(2));
        AnalisadorUsabilidade a = new AnalisadorUsabilidade(obs, List.of());
        assertEquals(new Proporcao(1, 1), a.sucessoCarrinho(true), "linha 5 = Adicionar ao carrinho");
        assertEquals(new Proporcao(0, 1), a.sucessoPorTarefa().get("Simular compra"), "linha 6 = Simular compra");
        assertEquals(171.0, a.tempoMedio().getAsDouble(), DELTA);
    }

    @ParameterizedTest(name = "tarefa ''{0}'' é inválida")
    @ValueSource(strings = {"0", "8"})
    @DisplayName("Número de tarefa fora de 1–7 é rejeitado")
    void numeroDeTarefaInvalido(String tarefa) {
        Map<String, String> linha = Map.of("participante", "F01", "tarefa", tarefa, "ok", "S");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Observacao.de(linha));
        assertTrue(e.getMessage().contains("use 1 a 7"), e.getMessage());
    }

    @Test
    @DisplayName("Menos de 5 sessões: relatório marca resultados como PARCIAIS; com 5 ou mais, não")
    void resultadosParciais() {
        List<Observacao> quatro = List.of(
                obs("P1"), obs("P2"), obs("P3"), obs("P4"));
        AnalisadorUsabilidade parcial = new AnalisadorUsabilidade(quatro, List.of());
        assertTrue(parcial.resultadosParciais());
        assertTrue(Relatorio.gerar(parcial).contains(
                "ATENÇÃO: só 4 sessão(ões) realizada(s), menos que as 5 planejadas: resultados PARCIAIS."));

        List<Observacao> cinco = List.of(obs("P1"), obs("P2"), obs("P3"), obs("P4"), obs("P5"));
        AnalisadorUsabilidade completo = new AnalisadorUsabilidade(cinco, List.of());
        assertFalse(completo.resultadosParciais());
        assertFalse(Relatorio.gerar(completo).contains("PARCIAIS"));
    }

    private static Observacao obs(String participante) {
        return new Observacao(participante, "Adicionar ao carrinho", 100.0, true, 0, false, "");
    }
}
