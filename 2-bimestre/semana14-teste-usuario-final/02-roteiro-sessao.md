# 02: Roteiro da sessão (para o avaliador)

Siga este roteiro **ao pé da letra** em todas as sessões. Assim as sessões ficam comparáveis entre si.

## A. Preparação (antes de chamar o participante)

- [ ] Abra uma **janela anônima/privativa** no navegador **do próprio participante** (o computador ou o celular que ele costuma usar).
      Em uma janela anônima não há login, histórico nem carrinho anterior.
- [ ] Deixe a janela em uma aba em branco, **sem** o Mercado Livre aberto (abrir o site é a tarefa 1).
- [ ] Tenha à mão: cronômetro (celular), a planilha [03-planilha-observacao.csv](03-planilha-observacao.csv) (impressa ou aberta)
      e o questionário SUS ([04-questionario-sus.md](04-questionario-sus.md)).
- [ ] Anote no topo da folha: **código do participante** (P01, P02…), **data**, **navegador** e **dispositivo**.
- [ ] Desative notificações do seu próprio celular, exceto o cronômetro.

## B. Consentimento e *briefing* (leia em voz alta)

1. Leia o [termo de consentimento](05-termo-de-consentimento.md) e pergunte: *"Você concorda em participar?"*. Só siga em frente com um "sim".
2. *Briefing* padronizado:

> "Obrigado por participar. Hoje vamos avaliar o **site do Mercado Livre**, não você. Não existe resposta certa ou errada.
> Se algo for difícil, é um problema do site, e é justamente isso que queremos descobrir.
>
> Vou ler algumas tarefas, uma de cada vez. Faça como faria em casa. Enquanto usa o site, **pense em voz alta**:
> diga o que está procurando, o que espera que aconteça e o que acha estranho.
>
> Você **não** vai fazer login, **não** vai digitar nenhum dado pessoal e **não** vai pagar nada.
> Se aparecer uma tela pedindo login ou pagamento, é ali que paramos.
>
> Eu vou cronometrar e anotar, mas não vou poder ajudar muito durante as tarefas. Pode começar quando eu terminar de ler cada uma. Tudo bem?"

3. Leia o cenário:

> "Você está procurando um **fone de ouvido sem fio** para uso diário e quer comprar pelo Mercado Livre.
> Seu objetivo é encontrar um modelo **bem avaliado**, comparar opções e **simular a compra até o carrinho**, sem pagar."

## C. Execução das tarefas

Para cada tarefa: **leia o enunciado**, **comece a cronometrar** quando terminar de ler e **pare** ao atingir o critério de conclusão
(veja o [plano, seção 3](01-plano-de-teste.md#3-tarefas-e-critério-operacional-de-conclusão)). Anote na planilha logo depois.

| # | Leia exatamente | Linha da planilha |
|---|---|---|
| 1 | "Acesse o site do Mercado Livre." | Acessar site |
| 2 | "Pesquise por **fone de ouvido Bluetooth**." | Buscar produto |
| 3 | "Mostre só os produtos com entrega **Full**." | Aplicar filtros (inicie o cronômetro) |
| 4 | "Agora ordene os resultados pelos **melhores avaliados**." | Aplicar filtros (pare o cronômetro só aqui; o tempo das tarefas 3 e 4 é somado) |
| 5 | "Escolha um produto que te interesse e abra os detalhes dele." | Abrir detalhes |
| 6 | "Adicione esse produto ao carrinho." | Adicionar ao carrinho |
| 7 | "Comece a compra, mas **pare antes de qualquer login ou pagamento**." | Simular compra |
| 8 | "Volte para a página inicial do site." | Retornar à tela inicial |

### Protocolo *think-aloud*

- Se o participante ficar em silêncio por mais de **10 segundos**, diga apenas: *"O que você está pensando agora?"*.
  **Não** dê dicas e **não** aponte para a tela.
- Anote frases literais importantes nas Observações, entre aspas (por exemplo: "cadê o filtro de frete?").

### Quando e como ajudar

| Situação | Ação | Registro |
|---|---|---|
| O participante pergunta "onde fica X?" | Responda: *"Onde você procuraria?"* (isso não é ajuda) | Erro, se ele estiver perdido |
| Travado há **60 s** na mesma tela, ou pede ajuda pela 2ª vez | Dê **uma dica mínima** (por exemplo: "olhe a coluna da esquerda") | `com_ajuda = S` |
| Tentou digitar dados pessoais ou fazer login | **Interrompa imediatamente**: "não precisa, pode parar aí" | Observações |
| Passaram **3 min** na tarefa ou ele desistiu | Encerre a tarefa e leve-o ao início da seguinte | `Sucesso = N` |

**Regra de registro:** uma tarefa concluída **com** dica conta `Sucesso = S` e `com_ajuda = S`, e **não** entra na taxa de sucesso sem ajuda.

### O que contar como erro

Veja a definição operacional em [03-planilha-observacao.md](03-planilha-observacao.md#definição-operacional-de-erro).
Some 1 na coluna `Erros` a cada ocorrência.

## D. Encerramento

1. Entregue o **questionário SUS** e peça que responda sozinho, com sinceridade, logo após a última tarefa.
2. Pergunte: *"O que foi mais fácil? E o mais difícil? Você mudaria alguma coisa no site?"* e anote nas Observações.
3. **Feche a janela anônima** (isso descarta o carrinho e o histórico).
4. Agradeça e peça que não comente as tarefas com outros participantes.
5. Tire os prints combinados (sem dados pessoais) e salve em `evidencias/` com o nome `P01-tarefa6-carrinho.png`, por exemplo.
6. Passe as anotações para o CSV no mesmo dia.
