package br.edu.exemplo.analise;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Ponto de entrada: lê a planilha de observação e o questionário SUS e imprime a análise.
 * <pre>
 *   mvn -q compile exec:java                                   (usa os CSVs da pasta pai)
 *   mvn -q compile exec:java -Dexec.args="obs.csv sus.csv"     (caminhos explícitos)
 * </pre>
 */
public final class Main {

    static final String PLANILHA_PADRAO = "../03-planilha-observacao.csv";
    static final String SUS_PADRAO = "../04-questionario-sus.csv";

    private Main() {
    }

    public static void main(String[] args) throws IOException {
        Path[] caminhos = caminhos(args);
        System.out.print(executar(caminhos[0], caminhos[1]));
    }

    static Path[] caminhos(String[] args) {
        return new Path[]{
                Path.of(args.length > 0 ? args[0] : PLANILHA_PADRAO),
                Path.of(args.length > 1 ? args[1] : SUS_PADRAO)
        };
    }

    static String executar(Path planilha, Path sus) throws IOException {
        List<Observacao> observacoes = converter(LeitorCsv.ler(planilha), Observacao::de);
        List<RespostaSus> respostas = converter(LeitorCsv.ler(sus), RespostaSus::de);
        return Relatorio.gerar(new AnalisadorUsabilidade(observacoes, respostas));
    }

    private static <T> List<T> converter(List<Map<String, String>> registros,
                                         Function<Map<String, String>, Optional<T>> conversor) {
        return registros.stream().map(conversor).flatMap(Optional::stream).toList();
    }
}
