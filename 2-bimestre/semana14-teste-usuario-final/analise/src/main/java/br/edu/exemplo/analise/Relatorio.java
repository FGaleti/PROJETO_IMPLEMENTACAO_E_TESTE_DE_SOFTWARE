package br.edu.exemplo.analise;

import br.edu.exemplo.analise.AnalisadorUsabilidade.Proporcao;

import java.util.Locale;
import java.util.Map;
import java.util.OptionalDouble;

/** Formata o resultado da análise como texto (números no formato pt-BR). */
public final class Relatorio {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private Relatorio() {
    }

    public static String gerar(AnalisadorUsabilidade a) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Teste de usuário final — Mercado Livre (análise automatizada) ===\n");
        if (a.semDados()) {
            sb.append("Nenhuma observação coletada ainda: resultados PENDENTES DE COLETA.\n");
            sb.append("Preencha 03-planilha-observacao.csv e 04-questionario-sus.csv e rode novamente.\n");
            return sb.toString();
        }
        sb.append("Participantes observados: ").append(a.participantes().size())
                .append(' ').append(a.participantes()).append('\n');

        sb.append("\n-- Taxa de sucesso por tarefa (concluída sem ajuda) e erros --\n");
        Map<String, Integer> erros = a.errosPorTarefa();
        a.sucessoPorTarefa().forEach((tarefa, p) -> sb.append(String.format(PT_BR,
                "%-26s %s | erros: %d%n", tarefa + ":", proporcao(p), erros.get(tarefa))));
        sb.append(String.format(PT_BR, "Taxa de sucesso global (fluxo inteiro sem ajuda): %s%n",
                proporcao(a.sucessoGlobal())));
        sb.append("Total de erros: ").append(a.totalErros()).append('\n');

        sb.append("\n-- Tempo total por participante (s) --\n");
        a.tempoTotalPorParticipante().forEach((participante, t) ->
                sb.append(String.format(PT_BR, "%s: %.1f%n", participante, t)));
        sb.append("Tempo médio: ").append(segundos(a.tempoMedio()))
                .append(" | mediana: ").append(segundos(a.tempoMediano())).append('\n');

        sb.append("\n-- SUS (System Usability Scale) --\n");
        a.escoreSusPorParticipante().forEach((participante, e) ->
                sb.append(String.format(PT_BR, "%s: %.1f%n", participante, e)));
        if (!a.participantesSemRespostaSus().isEmpty()) {
            sb.append("Sem resposta SUS: ").append(a.participantesSemRespostaSus()).append('\n');
        }
        OptionalDouble sus = a.escoreSusMedio();
        sb.append("SUS médio: ").append(sus.isPresent()
                ? String.format(PT_BR, "%.1f (%s)", sus.getAsDouble(), CalculadoraSus.interpretar(sus.getAsDouble()))
                : "sem respostas").append('\n');

        sb.append("\n-- Critérios de sucesso do enunciado --\n");
        sb.append(String.format(PT_BR, "[%s] >= 90%% adicionam ao carrinho sem ajuda: %s (com ajuda incluída: %s)%n",
                a.criterioCarrinho().rotulo(), proporcao(a.sucessoCarrinho(true)), proporcao(a.sucessoCarrinho(false))));
        sb.append(String.format(PT_BR, "[%s] Tempo médio <= 240 s (4 min): %s%n",
                a.criterioTempo().rotulo(), segundos(a.tempoMedio())));
        return sb.toString();
    }

    static String proporcao(Proporcao p) {
        return String.format(PT_BR, "%.1f%% (%d/%d)", p.taxa() * 100, p.sucessos(), p.total());
    }

    static String segundos(OptionalDouble valor) {
        return valor.isPresent() ? String.format(PT_BR, "%.1f s", valor.getAsDouble()) : "sem tempos";
    }
}
