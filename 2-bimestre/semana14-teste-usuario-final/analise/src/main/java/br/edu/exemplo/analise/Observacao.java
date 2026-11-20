package br.edu.exemplo.analise;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Uma linha da planilha de observação: um participante executando uma tarefa.
 * Colunas do modelo do professor (Tarefa, Tempo (s), Sucesso (S/N), Erros,
 * Observações) + participante e com_ajuda.
 *
 * @param tempoSegundos null quando o tempo não foi cronometrado
 */
public record Observacao(String participante, String tarefa, Double tempoSegundos,
                         boolean sucesso, int erros, boolean comAjuda, String observacoes) {

    public static final String COL_PARTICIPANTE = "participante";
    public static final String COL_TAREFA = "Tarefa";
    public static final String COL_TEMPO = "Tempo (s)";
    public static final String COL_SUCESSO = "Sucesso (S/N)";
    public static final String COL_ERROS = "Erros";
    public static final String COL_AJUDA = "com_ajuda";
    public static final String COL_OBSERVACOES = "Observações";

    /** Linhas da planilha (modelo do professor + retorno à tela inicial), na ordem. */
    public static final List<String> TAREFAS = List.of("Acessar site", "Buscar produto", "Aplicar filtros",
            "Abrir detalhes", "Adicionar ao carrinho", "Simular compra", "Retornar à tela inicial");

    private static final Map<String, String> ALIASES = Map.ofEntries(
            Map.entry("participante", COL_PARTICIPANTE),
            Map.entry("tarefa", COL_TAREFA),
            Map.entry("tempo (s)", COL_TEMPO),
            Map.entry("tempo", COL_TEMPO),
            Map.entry("segundos", COL_TEMPO),
            Map.entry("sucesso (s/n)", COL_SUCESSO),
            Map.entry("sucesso", COL_SUCESSO),
            Map.entry("ok", COL_SUCESSO),
            Map.entry("erros", COL_ERROS),
            Map.entry("com_ajuda", COL_AJUDA),
            Map.entry("ajuda", COL_AJUDA),
            Map.entry("observações", COL_OBSERVACOES),
            Map.entry("observacoes", COL_OBSERVACOES),
            Map.entry("obs", COL_OBSERVACOES));

    private static final Set<String> SIM = Set.of("S", "SIM");
    private static final Set<String> NAO = Set.of("N", "NAO", "NÃO");

    /** Sucesso que conta para a métrica do enunciado: concluiu e sem ajuda. */
    public boolean sucessoSemAjuda() {
        return sucesso && !comAjuda;
    }

    /**
     * Converte um registro do CSV. Linhas ainda não coletadas (Sucesso em branco)
     * resultam em Optional.empty(), o que permite versionar a planilha vazia.
     *
     * @throws IllegalArgumentException se algum valor preenchido for inválido
     */
    public static Optional<Observacao> de(Map<String, String> original) {
        Map<String, String> registro = normalizar(original);
        String sucesso = valor(registro, COL_SUCESSO);
        if (sucesso.isEmpty()) {
            return Optional.empty();
        }
        String participante = obrigatorio(registro, COL_PARTICIPANTE);
        String tarefa = nomeDaTarefa(obrigatorio(registro, COL_TAREFA), participante);
        return Optional.of(new Observacao(
                participante,
                tarefa,
                tempo(valor(registro, COL_TEMPO), participante, tarefa),
                simNao(sucesso, COL_SUCESSO, participante, tarefa),
                erros(valor(registro, COL_ERROS), participante, tarefa),
                simNao(valorOuPadrao(registro, COL_AJUDA, "N"), COL_AJUDA, participante, tarefa),
                valor(registro, COL_OBSERVACOES)));
    }

    /**
     * Converte cabeçalhos alternativos (planilha simplificada do celular, sem
     * diferenciar maiúsculas) para os nomes das colunas do modelo do professor.
     */
    static Map<String, String> normalizar(Map<String, String> registro) {
        Map<String, String> normalizado = new HashMap<>();
        registro.forEach((coluna, v) ->
                normalizado.put(ALIASES.getOrDefault(coluna.trim().toLowerCase(Locale.ROOT), coluna), v));
        return normalizado;
    }

    /** Aceita o nome da tarefa ou o número da linha da planilha (1 a 7). */
    static String nomeDaTarefa(String tarefa, String participante) {
        if (!tarefa.matches("\\d+")) {
            return tarefa;
        }
        int numero = Integer.parseInt(tarefa);
        if (numero < 1 || numero > TAREFAS.size()) {
            throw invalido(COL_TAREFA, tarefa, "use 1 a " + TAREFAS.size(), participante, tarefa);
        }
        return TAREFAS.get(numero - 1);
    }

    private static String valor(Map<String, String> registro, String coluna) {
        return registro.getOrDefault(coluna, "").trim();
    }

    private static String valorOuPadrao(Map<String, String> registro, String coluna, String padrao) {
        String v = valor(registro, coluna);
        return v.isEmpty() ? padrao : v;
    }

    private static String obrigatorio(Map<String, String> registro, String coluna) {
        String v = valor(registro, coluna);
        if (v.isEmpty()) {
            throw new IllegalArgumentException("Coluna '" + coluna + "' obrigatória em linha com Sucesso preenchido");
        }
        return v;
    }

    private static boolean simNao(String v, String coluna, String participante, String tarefa) {
        String resposta = v.toUpperCase(Locale.ROOT);
        if (SIM.contains(resposta)) {
            return true;
        }
        if (NAO.contains(resposta)) {
            return false;
        }
        throw invalido(coluna, v, "use S/N ou Sim/Não", participante, tarefa);
    }

    private static Double tempo(String v, String participante, String tarefa) {
        if (v.isEmpty()) {
            return null;
        }
        double segundos = numero(v, COL_TEMPO, participante, tarefa);
        if (segundos < 0) {
            throw invalido(COL_TEMPO, v, "tempo não pode ser negativo", participante, tarefa);
        }
        return segundos;
    }

    private static int erros(String v, String participante, String tarefa) {
        if (v.isEmpty()) {
            return 0;
        }
        double quantidade = numero(v, COL_ERROS, participante, tarefa);
        if (quantidade < 0 || quantidade != Math.floor(quantidade)) {
            throw invalido(COL_ERROS, v, "use um inteiro >= 0", participante, tarefa);
        }
        return (int) quantidade;
    }

    private static double numero(String v, String coluna, String participante, String tarefa) {
        try {
            return Double.parseDouble(v.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw invalido(coluna, v, "não é um número", participante, tarefa);
        }
    }

    private static IllegalArgumentException invalido(String coluna, String v, String motivo,
                                                     String participante, String tarefa) {
        return new IllegalArgumentException(String.format(
                "Valor inválido em '%s' = '%s' (%s) [participante %s, tarefa %s]",
                coluna, v, motivo, participante, tarefa));
    }
}
