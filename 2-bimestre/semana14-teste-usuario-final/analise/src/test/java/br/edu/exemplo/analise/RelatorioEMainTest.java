package br.edu.exemplo.analise;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("Relatorio e Main — saída da análise")
class RelatorioEMainTest {

    private static String executarFixtures() throws Exception {
        return Main.executar(LeitorCsvTest.fixture("FICTICIO-observacao.csv"), LeitorCsvTest.fixture("FICTICIO-sus.csv"));
    }

    @Test
    @DisplayName("Relatório completo com as fixtures fictícias")
    void relatorioCompleto() throws Exception {
        String saida = executarFixtures();
        assertTrue(saida.contains("Participantes observados: 3 [F01, F02, F03]"), saida);
        assertTrue(saida.contains("Aplicar filtros:           66,7% (2/3) | erros: 3"), saida);
        assertTrue(saida.contains("Taxa de sucesso global (fluxo inteiro sem ajuda): 33,3% (1/3)"), saida);
        assertTrue(saida.contains("Total de erros: 4"), saida);
        assertTrue(saida.contains("Tempo médio: 233,3 s | mediana: 250,0 s"), saida);
        assertTrue(saida.contains("Sem resposta SUS: [F03]"), saida);
        assertTrue(saida.contains("SUS médio: 75,0 (Bom (acima da média))"), saida);
        assertTrue(saida.contains("[ATENDIDO] >= 90% adicionam ao carrinho sem ajuda: 100,0% (3/3)"), saida);
        assertTrue(saida.contains("[ATENDIDO] Tempo médio <= 240 s (4 min): 233,3 s"), saida);
    }

    @Test
    @DisplayName("CSV real ainda vazio (só cabeçalho/linhas sem dados) → resultados PENDENTES DE COLETA")
    void semDadosColetados(@TempDir Path dir) throws IOException {
        Path obs = Files.writeString(dir.resolve("obs.csv"),
                "participante,Tarefa,Tempo (s),Sucesso (S/N),Erros,com_ajuda,Observações\nP01,Acessar site,,,,,\n");
        Path sus = Files.writeString(dir.resolve("sus.csv"), "participante,Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10\n");
        String saida = Main.executar(obs, sus);
        assertTrue(saida.contains("PENDENTES DE COLETA"), saida);
    }

    @Test
    @DisplayName("Sem SUS, sem tempos e todos com SUS respondido: mensagens alternativas")
    void ramosAlternativos(@TempDir Path dir) throws IOException {
        Path obs = Files.writeString(dir.resolve("obs.csv"),
                "participante,Tarefa,Sucesso (S/N)\nP01,Adicionar ao carrinho,N\n");
        Path susVazio = Files.writeString(dir.resolve("sus.csv"), "participante,Q1\n");
        String saida = Main.executar(obs, susVazio);
        assertTrue(saida.contains("Tempo médio: sem tempos | mediana: sem tempos"), saida);
        assertTrue(saida.contains("SUS médio: sem respostas"), saida);
        assertFalse(saida.contains("Sem resposta SUS"), saida);
        assertTrue(saida.contains("[NÃO ATENDIDO] >= 90% adicionam ao carrinho sem ajuda: 0,0% (0/1)"), saida);
        assertTrue(saida.contains("[PENDENTE] Tempo médio <= 240 s"), saida);
    }

    @Test
    @DisplayName("Caminhos: padrão (CSVs da pasta pai) e explícitos")
    void caminhos() {
        Path[] padrao = Main.caminhos(new String[0]);
        assertEquals(Path.of("../03-planilha-observacao.csv"), padrao[0]);
        assertEquals(Path.of("../04-questionario-sus.csv"), padrao[1]);
        Path[] soPlanilha = Main.caminhos(new String[]{"a.csv"});
        assertEquals(List.of(Path.of("a.csv"), Path.of("../04-questionario-sus.csv")), List.of(soPlanilha));
        Path[] ambos = Main.caminhos(new String[]{"a.csv", "b.csv"});
        assertEquals(List.of(Path.of("a.csv"), Path.of("b.csv")), List.of(ambos));
    }

    @Test
    @DisplayName("main() imprime o relatório no console")
    void mainImprime() throws Exception {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            Main.main(new String[]{
                    LeitorCsvTest.fixture("FICTICIO-observacao.csv").toString(),
                    LeitorCsvTest.fixture("FICTICIO-sus.csv").toString()});
        } finally {
            System.setOut(original);
        }
        assertEquals(executarFixtures(), buffer.toString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Os CSVs reais do repositório têm formato válido para o analisador")
    void csvsReaisSaoValidos() {
        Path planilha = Path.of(Main.PLANILHA_PADRAO);
        Path sus = Path.of(Main.SUS_PADRAO);
        assumeTrue(Files.exists(planilha) && Files.exists(sus), "CSVs reais não encontrados a partir de " + Path.of("").toAbsolutePath());
        String saida = assertDoesNotThrow(() -> Main.executar(planilha, sus));
        assertTrue(saida.startsWith("=== Teste de usuário final"), saida);
    }
}
