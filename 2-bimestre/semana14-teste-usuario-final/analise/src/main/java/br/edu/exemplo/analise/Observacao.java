package br.edu.exemplo.analise;

import java.util.Map;
import java.util.Optional;

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
    public static Optional<Observacao> de(Map<String, String> registro) {
        String sucesso = valor(registro, COL_SUCESSO);
        if (sucesso.isEmpty()) {
            return Optional.empty();
        }
        String participante = obrigatorio(registro, COL_PARTICIPANTE);
        String tarefa = obrigatorio(registro, COL_TAREFA);
        return Optional.of(new Observacao(
                participante,
                tarefa,
                tempo(valor(registro, COL_TEMPO), participante, tarefa),
                simNao(sucesso, COL_SUCESSO, participante, tarefa),
                erros(valor(registro, COL_ERROS), participante, tarefa),
                simNao(valorOuPadrao(registro, COL_AJUDA, "N"), COL_AJUDA, participante, tarefa),
                valor(registro, COL_OBSERVACOES)));
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
        if (v.equalsIgnoreCase("S")) {
            return true;
        }
        if (v.equalsIgnoreCase("N")) {
            return false;
        }
        throw invalido(coluna, v, "use S ou N", participante, tarefa);
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
