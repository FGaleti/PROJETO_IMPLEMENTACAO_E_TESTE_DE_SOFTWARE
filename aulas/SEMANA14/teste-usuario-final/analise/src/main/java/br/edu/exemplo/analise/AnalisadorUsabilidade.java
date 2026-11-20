package br.edu.exemplo.analise;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Calcula as métricas do teste de usuário final a partir das observações e do SUS.
 * Sucesso = tarefa concluída SEM ajuda (métrica "taxa de sucesso" do enunciado).
 */
public final class AnalisadorUsabilidade {

    public static final String TAREFA_CARRINHO = "Adicionar ao carrinho";
    /** Critério 1: ao menos 90% dos usuários adicionam o produto ao carrinho. */
    public static final double META_CARRINHO = 0.90;
    /** Critério 2: tempo médio para concluir o teste de até 4 minutos. */
    public static final double META_TEMPO_SEGUNDOS = 240.0;

    private final List<Observacao> observacoes;
    private final List<RespostaSus> respostasSus;

    public AnalisadorUsabilidade(List<Observacao> observacoes, List<RespostaSus> respostasSus) {
        this.observacoes = List.copyOf(observacoes);
        this.respostasSus = List.copyOf(respostasSus);
    }

    public boolean semDados() {
        return observacoes.isEmpty();
    }

    public TreeSet<String> participantes() {
        TreeSet<String> participantes = new TreeSet<>();
        observacoes.forEach(o -> participantes.add(o.participante()));
        return participantes;
    }

    /** Tarefas na ordem em que aparecem na planilha, com sua proporção de sucesso sem ajuda. */
    public Map<String, Proporcao> sucessoPorTarefa() {
        Map<String, Proporcao> porTarefa = new LinkedHashMap<>();
        for (Observacao o : observacoes) {
            porTarefa.merge(o.tarefa(), Proporcao.de(o.sucessoSemAjuda()), Proporcao::somar);
        }
        return porTarefa;
    }

    /** Participantes que concluíram TODAS as suas tarefas sem ajuda. */
    public Proporcao sucessoGlobal() {
        Map<String, Boolean> fluxoCompleto = new TreeMap<>();
        for (Observacao o : observacoes) {
            fluxoCompleto.merge(o.participante(), o.sucessoSemAjuda(), Boolean::logicalAnd);
        }
        int sucessos = (int) fluxoCompleto.values().stream().filter(Boolean::booleanValue).count();
        return new Proporcao(sucessos, fluxoCompleto.size());
    }

    /** Participantes que adicionaram o produto ao carrinho; {@code semAjuda} define se ajuda invalida o sucesso. */
    public Proporcao sucessoCarrinho(boolean semAjuda) {
        Proporcao resultado = new Proporcao(0, 0);
        for (Observacao o : observacoes) {
            if (o.tarefa().equalsIgnoreCase(TAREFA_CARRINHO)) {
                resultado = resultado.somar(Proporcao.de(semAjuda ? o.sucessoSemAjuda() : o.sucesso()));
            }
        }
        return resultado;
    }

    public Map<String, Integer> errosPorTarefa() {
        Map<String, Integer> erros = new LinkedHashMap<>();
        observacoes.forEach(o -> erros.merge(o.tarefa(), o.erros(), Integer::sum));
        return erros;
    }

    public int totalErros() {
        return observacoes.stream().mapToInt(Observacao::erros).sum();
    }

    /** Soma dos tempos cronometrados de cada participante (tempo total do fluxo). */
    public Map<String, Double> tempoTotalPorParticipante() {
        Map<String, Double> tempos = new TreeMap<>();
        for (Observacao o : observacoes) {
            if (o.tempoSegundos() != null) {
                tempos.merge(o.participante(), o.tempoSegundos(), Double::sum);
            }
        }
        return tempos;
    }

    public OptionalDouble tempoMedio() {
        return tempoTotalPorParticipante().values().stream().mapToDouble(Double::doubleValue).average();
    }

    public OptionalDouble tempoMediano() {
        return mediana(new ArrayList<>(tempoTotalPorParticipante().values()));
    }

    public Map<String, Double> escoreSusPorParticipante() {
        Map<String, Double> escores = new TreeMap<>();
        for (RespostaSus r : respostasSus) {
            if (!r.semResposta()) {
                escores.put(r.participante(), r.escore());
            }
        }
        return escores;
    }

    public List<String> participantesSemRespostaSus() {
        return respostasSus.stream().filter(RespostaSus::semResposta).map(RespostaSus::participante).sorted().toList();
    }

    public OptionalDouble escoreSusMedio() {
        return escoreSusPorParticipante().values().stream().mapToDouble(Double::doubleValue).average();
    }

    /** Critério 1 (≥ 90% adicionam ao carrinho sem ajuda); vazio se não há dados da tarefa. */
    public Criterio criterioCarrinho() {
        Proporcao carrinho = sucessoCarrinho(true);
        if (carrinho.total() == 0) {
            return Criterio.PENDENTE;
        }
        return Criterio.de(carrinho.taxa() >= META_CARRINHO);
    }

    /** Critério 2 (tempo médio ≤ 240 s); pendente se não há tempos. */
    public Criterio criterioTempo() {
        OptionalDouble medio = tempoMedio();
        if (medio.isEmpty()) {
            return Criterio.PENDENTE;
        }
        return Criterio.de(medio.getAsDouble() <= META_TEMPO_SEGUNDOS);
    }

    static OptionalDouble mediana(List<Double> valores) {
        if (valores.isEmpty()) {
            return OptionalDouble.empty();
        }
        valores.sort(Double::compare);
        int meio = valores.size() / 2;
        if (valores.size() % 2 == 1) {
            return OptionalDouble.of(valores.get(meio));
        }
        return OptionalDouble.of((valores.get(meio - 1) + valores.get(meio)) / 2.0);
    }

    /** Proporção "sucessos / total". */
    public record Proporcao(int sucessos, int total) {

        static Proporcao de(boolean sucesso) {
            return new Proporcao(sucesso ? 1 : 0, 1);
        }

        Proporcao somar(Proporcao outra) {
            return new Proporcao(sucessos + outra.sucessos, total + outra.total);
        }

        /** Taxa entre 0 e 1 (0 quando não há registros). */
        public double taxa() {
            return total == 0 ? 0.0 : (double) sucessos / total;
        }
    }

    public enum Criterio {
        ATENDIDO("ATENDIDO"), NAO_ATENDIDO("NÃO ATENDIDO"), PENDENTE("PENDENTE");

        private final String rotulo;

        Criterio(String rotulo) {
            this.rotulo = rotulo;
        }

        public String rotulo() {
            return rotulo;
        }

        static Criterio de(boolean atendido) {
            return atendido ? ATENDIDO : NAO_ATENDIDO;
        }
    }
}
