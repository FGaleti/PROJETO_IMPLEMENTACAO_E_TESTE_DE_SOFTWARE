# 07: Avaliação heurística (Nielsen) do fluxo de compra do Mercado Livre

> **Tipo de estudo:** avaliação **de especialista** (inspeção), **não** é teste com usuários.
> Ela antecipa problemas prováveis e prepara o que observar nas sessões, mas **não substitui** a execução com participantes reais.
>
> **Limitação importante:** a inspeção foi feita a partir do conhecimento da interface pública do site **web** do Mercado Livre, sem automação
> (o site bloqueia robôs, e o plano proíbe automatizá-lo). Como a interface muda com frequência, **cada achado é uma hipótese a confirmar**
> no piloto e nas sessões (risco R2 do [plano](01-plano-de-teste.md)). A coluna "Confirmar nas sessões" diz o que observar.

## Método

- **Heurísticas:** as 10 heurísticas de usabilidade de Nielsen (1994).
- **Escopo:** o fluxo das tarefas do enunciado: página inicial → busca → filtro Full → ordenação → anúncio → carrinho → início do checkout → volta à página inicial.
- **Severidade (0–4, Nielsen):** 0 não é problema · 1 cosmético · 2 pequeno · 3 grande · 4 catástrofe.
  A severidade abaixo é **preliminar** e será revista com a frequência real observada nas sessões.

## Achados preliminares

| # | Tarefa | Problema provável | Heurística violada | Sev. prelim. | Confirmar nas sessões |
|---|---|---|---|:-:|---|
| H1 | 4: ordenar pelos melhores avaliados | A ordenação da lista de resultados costuma oferecer critérios como "Mais relevantes" e preço, **sem uma opção explícita de "melhores avaliados"**. O usuário procura uma opção que talvez não exista, e a avaliação só aparece como estrelas em cada item | H2: correspondência com o mundo real; H7: flexibilidade e eficiência | **3** | Quais opções de ordenação aparecem no dia? O participante desiste ou escolhe outra? (registrar como erro de "confusão nos filtros") |
| H2 | 3: filtrar entrega Full | "Full" é um termo da própria plataforma, apresentado como selo ou ícone. Quem não conhece o termo pode confundi-lo com "Frete grátis" ou com "Chegará amanhã" | H2: correspondência com o mundo real; H6: reconhecer em vez de lembrar | **2** | O participante distingue Full de Frete grátis? Precisou de ajuda? |
| H3 | 3: filtros | Em telas menores ou no celular, os filtros ficam recolhidos num botão ou numa coluna lateral longa, e o filtro de entrega pode exigir rolagem | H8: estética e design minimalista; H7: eficiência | **2** | Tempo até encontrar o filtro; quantas rolagens e cliques errados |
| H4 | 2 e 3: resultados | Anúncios **patrocinados** misturados aos resultados orgânicos podem fazer o participante abrir um item que não atende ao filtro ou à ordenação | H8: design minimalista; H1: visibilidade do status (qual filtro está ativo) | **2** | Abriu anúncio patrocinado sem querer? Percebeu os filtros ativos? |
| H5 | 6: adicionar ao carrinho | A página do anúncio destaca **"Comprar agora"** ao lado de "Adicionar ao carrinho". O participante pode clicar no botão principal e ir direto para o checkout | H5: prevenção de erros; H4: consistência | **2** | Clicou em "Comprar agora" por engano? |
| H6 | 6 e 7: carrinho e checkout | Sem conta logada, o site pode pedir **login ou cadastro** ao adicionar ao carrinho ou ao iniciar a compra. Isso interrompe o fluxo, e o plano proíbe fazer login | H3: controle e liberdade do usuário; H1: visibilidade do status | **3** (para o teste) | Em que passo o login é pedido? Se for ao adicionar ao carrinho, o critério C1 depende disso. Registrar como observação e encerrar a tarefa como definido no plano |
| H7 | 1: acessar o site | Banner de cookies e pedido de CEP ou localização logo na entrada competem com a tarefa | H8: design minimalista | **1** | Quanto tempo se perde com banners? (não conta como erro) |
| H8 | 8: voltar ao início | O retorno pelo logo é convenção conhecida, mas no checkout a navegação pode ser reduzida (cabeçalho simplificado) | H3: controle e liberdade; H4: padrões | **1** | Como o participante voltou (logo, botão voltar, nova aba)? |
| H9 | 5: abrir detalhes | A página do anúncio é longa (fotos, variações, frete, avaliações, perguntas), e as avaliações ficam abaixo da dobra | H8: design minimalista; H6: reconhecer em vez de lembrar | **1** | O participante consulta as avaliações antes de decidir? |

## Implicações para o teste com usuários

1. **Fazer um piloto antes da 1ª sessão** (risco R2) e anotar o nome exato das opções de ordenação e do filtro de entrega disponíveis no dia.
   Se não houver "melhores avaliados", aplicar a regra do [plano](01-plano-de-teste.md#3-tarefas-e-critério-operacional-de-conclusão)
   (usar a ordenação mais próxima e registrar isso no relatório).
2. **H1 e H2 devem concentrar erros e pedidos de ajuda** na linha "Aplicar filtros" da planilha. Se isso se confirmar, entram como achados de alta severidade no [relatório](06-relatorio-final.md).
3. **H6 pode afetar o critério C1** (≥ 90% adicionam ao carrinho). Se o site exigir login para adicionar ao carrinho, isso precisa ser registrado e discutido no relatório como limitação do cenário (o teste não permite login).

## Referências

- Nielsen, J. (1994). *10 Usability Heuristics for User Interface Design*. Nielsen Norman Group.
- Nielsen, J. (1994). *Severity Ratings for Usability Problems*. Nielsen Norman Group.
