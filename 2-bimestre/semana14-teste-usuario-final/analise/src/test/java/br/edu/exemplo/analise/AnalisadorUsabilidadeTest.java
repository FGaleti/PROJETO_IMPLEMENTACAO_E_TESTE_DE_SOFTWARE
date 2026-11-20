package br.edu.exemplo.analise;

import br.edu.exemplo.analise.AnalisadorUsabilidade.Criterio;
import br.edu.exemplo.analise.AnalisadorUsabilidade.Proporcao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Usa somente as fixtures FICTÍCIAS de src/test/resources/fixtures. */
@DisplayName("AnalisadorUsabilidade — métricas e critérios (dados fictícios)")
class AnalisadorUsabilidadeTest {

    private static final double DELTA = 0.001;
    private AnalisadorUsabilidade analisador;

    @BeforeEach
    void carregarFixtures() throws Exception {
        List<Observacao> obs = LeitorCsv.ler(LeitorCsvTest.fixture("FICTICIO-observacao.csv")).stream()
                .map(Observacao::de).flatMap(Optional::stream).toList();
        List<RespostaSus> sus = LeitorCsv.ler(LeitorCsvTest.fixture("FICTICIO-sus.csv")).stream()
                .map(RespostaSus::de).flatMap(Optional::stream).toList();
        analisador = new AnalisadorUsabilidade(obs, sus);
    }

    private static Observacao obs(String participante, String tarefa, Double tempo, boolean sucesso, boolean ajuda) {
        return new Observacao(participante, tarefa, tempo, sucesso, 0, ajuda, "");
    }

    @Test
    @DisplayName("Participantes: F01–F03 (a linha não coletada de F04 é ignorada)")
    void participantes() {
        assertFalse(analisador.semDados());
        assertEquals(List.of("F01", "F02", "F03"), new ArrayList<>(analisador.participantes()));
    }

    @Test
    @DisplayName("Taxa de sucesso por tarefa, na ordem da planilha, contando só sucesso sem ajuda")
    void sucessoPorTarefa() {
        Map<String, Proporcao> porTarefa = analisador.sucessoPorTarefa();
        assertEquals(List.of("Acessar site", "Buscar produto", "Aplicar filtros", "Abrir detalhes",
                "Adicionar ao carrinho", "Simular compra", "Retornar à tela inicial"), new ArrayList<>(porTarefa.keySet()));
        assertEquals(new Proporcao(3, 3), porTarefa.get("Acessar site"));
        assertEquals(new Proporcao(2, 3), porTarefa.get("Aplicar filtros"), "F02 teve ajuda");
        assertEquals(new Proporcao(2, 3), porTarefa.get("Simular compra"), "F03 não concluiu");
    }

    @Test
    @DisplayName("Taxa global: só F01 concluiu o fluxo inteiro sem ajuda → 1/3")
    void sucessoGlobal() {
        Proporcao global = analisador.sucessoGlobal();
        assertEquals(new Proporcao(1, 3), global);
        assertEquals(1.0 / 3, global.taxa(), DELTA);
    }

    @Test
    @DisplayName("Erros por tarefa e total")
    void erros() {
        assertEquals(3, analisador.errosPorTarefa().get("Aplicar filtros"));
        assertEquals(1, analisador.errosPorTarefa().get("Simular compra"));
        assertEquals(0, analisador.errosPorTarefa().get("Acessar site"));
        assertEquals(4, analisador.totalErros());
    }

    @Test
    @DisplayName("Tempo total por participante, média (233,3 s) e mediana (250 s)")
    void tempos() {
        assertEquals(Map.of("F01", 180.0, "F02", 250.0, "F03", 270.0), analisador.tempoTotalPorParticipante());
        assertEquals(700.0 / 3, analisador.tempoMedio().getAsDouble(), DELTA);
        assertEquals(250.0, analisador.tempoMediano().getAsDouble(), DELTA);
    }

    @Test
    @DisplayName("SUS: F01 = 50, F02 = 100, F03 sem resposta, média 75")
    void sus() {
        assertEquals(Map.of("F01", 50.0, "F02", 100.0), analisador.escoreSusPorParticipante());
        assertEquals(List.of("F03"), analisador.participantesSemRespostaSus());
        assertEquals(75.0, analisador.escoreSusMedio().getAsDouble(), DELTA);
    }

    @Test
    @DisplayName("Critérios com as fixtures: carrinho 3/3 e tempo médio 233,3 s → ambos atendidos")
    void criteriosAtendidos() {
        assertEquals(new Proporcao(3, 3), analisador.sucessoCarrinho(true));
        assertEquals(Criterio.ATENDIDO, analisador.criterioCarrinho());
        assertEquals(Criterio.ATENDIDO, analisador.criterioTempo());
    }

    @Test
    @DisplayName("Critérios não atendidos: carrinho 8/10 (< 90%) e tempo médio 241 s (> 240)")
    void criteriosNaoAtendidos() {
        List<Observacao> obs = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            obs.add(obs("P" + i, "Adicionar ao carrinho", 241.0, i <= 8, false));
        }
        AnalisadorUsabilidade a = new AnalisadorUsabilidade(obs, List.of());
        assertEquals(Criterio.NAO_ATENDIDO, a.criterioCarrinho());
        assertEquals(Criterio.NAO_ATENDIDO, a.criterioTempo());
    }

    @Test
    @DisplayName("Fronteiras exatas dos critérios: 9/10 = 90% e média = 240 s → atendidos")
    void fronteirasDosCriterios() {
        List<Observacao> obs = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            obs.add(obs("P" + i, "Adicionar ao carrinho", 240.0, i <= 9, false));
        }
        AnalisadorUsabilidade a = new AnalisadorUsabilidade(obs, List.of());
        assertEquals(Criterio.ATENDIDO, a.criterioCarrinho());
        assertEquals(Criterio.ATENDIDO, a.criterioTempo());
    }

    @Test
    @DisplayName("Carrinho com ajuda: conta só na métrica 'com ajuda incluída'; nome da tarefa sem diferenciar maiúsculas")
    void carrinhoComAjuda() {
        AnalisadorUsabilidade a = new AnalisadorUsabilidade(List.of(
                obs("P1", "ADICIONAR AO CARRINHO", 30.0, true, true),
                obs("P2", "Adicionar ao carrinho", 30.0, true, false)), List.of());
        assertEquals(new Proporcao(1, 2), a.sucessoCarrinho(true));
        assertEquals(new Proporcao(2, 2), a.sucessoCarrinho(false));
    }

    @Test
    @DisplayName("Sem dados: tudo vazio e critérios PENDENTES")
    void semDados() {
        AnalisadorUsabilidade a = new AnalisadorUsabilidade(List.of(), List.of());
        assertTrue(a.semDados());
        assertTrue(a.tempoMedio().isEmpty());
        assertTrue(a.tempoMediano().isEmpty());
        assertTrue(a.escoreSusMedio().isEmpty());
        assertEquals(new Proporcao(0, 0), a.sucessoGlobal());
        assertEquals(0.0, a.sucessoGlobal().taxa(), DELTA);
        assertEquals(Criterio.PENDENTE, a.criterioCarrinho());
        assertEquals(Criterio.PENDENTE, a.criterioTempo());
    }

    @Test
    @DisplayName("Tarefa de carrinho ausente e tempos não cronometrados → critérios PENDENTES")
    void dadosParciais() {
        AnalisadorUsabilidade a = new AnalisadorUsabilidade(
                List.of(obs("P1", "Acessar site", null, true, false)), List.of());
        assertTrue(a.tempoTotalPorParticipante().isEmpty());
        assertEquals(Criterio.PENDENTE, a.criterioCarrinho());
        assertEquals(Criterio.PENDENTE, a.criterioTempo());
    }

    @Test
    @DisplayName("Mediana de quantidade ímpar, par e unitária")
    void mediana() {
        assertEquals(OptionalDouble.of(2.0), AnalisadorUsabilidade.mediana(new ArrayList<>(List.of(3.0, 1.0, 2.0))));
        assertEquals(OptionalDouble.of(2.5), AnalisadorUsabilidade.mediana(new ArrayList<>(List.of(4.0, 1.0, 3.0, 2.0))));
        assertEquals(OptionalDouble.of(7.0), AnalisadorUsabilidade.mediana(new ArrayList<>(List.of(7.0))));
        assertEquals(OptionalDouble.empty(), AnalisadorUsabilidade.mediana(new ArrayList<>(Collections.emptyList())));
    }

    @Test
    @DisplayName("Rótulos dos critérios")
    void rotulos() {
        assertEquals("ATENDIDO", Criterio.ATENDIDO.rotulo());
        assertEquals("NÃO ATENDIDO", Criterio.NAO_ATENDIDO.rotulo());
        assertEquals("PENDENTE", Criterio.PENDENTE.rotulo());
    }
}
