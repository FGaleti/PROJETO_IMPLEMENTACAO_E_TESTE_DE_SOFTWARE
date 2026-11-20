# 01: Plano do teste de usuário final (Mercado Livre)

Base: enunciado do professor ([enunciado/TESTE USUARIO FINAL.pdf](enunciado/TESTE%20USUARIO%20FINAL.pdf)).
O documento original passa da seção 3 direto para a 6. As **seções 4 e 5** abaixo (perfil dos participantes e procedimento) foram completadas pela equipe.

## 1. Objetivo

Avaliar a **facilidade de uso, a eficiência e a clareza** da interface do site Mercado Livre nas etapas de **busca, comparação e simulação de compra**
de um produto. Observamos usuários finais reais e aplicamos conceitos de IHC e teste de usabilidade.

## 2. Escopo e cenário de uso

- **Dentro do escopo:** página inicial, busca, filtros e ordenação da lista de resultados, página de detalhes do produto, carrinho e início do checkout.
- **Fora do escopo:** login, cadastro, pagamento, conclusão de compra, pós-venda e aplicativo móvel. Também está fora qualquer automação do site.
- **Cenário apresentado ao participante:**
  > "Você está procurando um **fone de ouvido sem fio** para uso diário e quer comprar pelo Mercado Livre.
  > Seu objetivo é encontrar um modelo com **boa avaliação**, comparar opções e **simular a compra até o carrinho**, sem finalizar o pagamento."

## 3. Tarefas e critério operacional de conclusão

O enunciado lista 7 tarefas. Para medir melhor, separamos a tarefa 3 ("usar os filtros") em duas: filtrar por Full e ordenar.
O resultado são **8 tarefas**. Na planilha de observação, as tarefas 3 e 4 ficam juntas na linha **"Aplicar filtros"** do modelo do professor
(veja [03-planilha-observacao.md](03-planilha-observacao.md)).

| # | Tarefa | Linha na planilha | Considerada **concluída** quando… |
|---|---|---|---|
| 1 | Acessar https://www.mercadolivre.com.br | Acessar site | A página inicial do Mercado Livre está carregada na tela |
| 2 | Pesquisar "fone de ouvido Bluetooth" | Buscar produto | A lista de resultados da busca para esse termo (ou equivalente) está exibida |
| 3 | Filtrar só produtos com entrega **Full** | Aplicar filtros | O filtro "Full" está ativo (aparece como filtro aplicado ou na URL) |
| 4 | Ordenar pelos **melhores avaliados** | Aplicar filtros | Está selecionada a ordenação mais próxima de "melhores avaliados" que o site oferecer no dia (veja o risco R2). Uma ordenação diferente conta como erro, e a tarefa só é concluída quando o participante seleciona a opção correta |
| 5 | Escolher um produto e abrir os detalhes | Abrir detalhes | A página do anúncio (título, preço, avaliações) está aberta |
| 6 | Adicionar ao carrinho | Adicionar ao carrinho | A confirmação de "adicionado ao carrinho" aparece, ou o carrinho mostra o item |
| 7 | Simular o início da compra **sem pagar** | Simular compra | O participante clica em "Comprar"/"Continuar a compra" e chega à primeira tela do checkout ou à tela que pede login. **Ele para aí:** não faz login, não informa dados e não paga |
| 8 | Voltar à tela inicial | Retornar à tela inicial | A página inicial está exibida de novo (pelo logo, pelo menu ou pelo botão voltar) |

**Encerramento de uma tarefa:** a tarefa termina quando o critério acima é atingido (sucesso), quando o participante desiste ou quando passam **3 minutos** nela (insucesso).
No insucesso, o avaliador leva o participante ao ponto de partida da tarefa seguinte e registra o fato.

## 4. Perfil e número de participantes *(seção completada pela equipe)*

- **Número:** no mínimo **5 participantes**. Com 5 usuários costuma aparecer a maior parte dos problemas graves de usabilidade (Nielsen, 2000).
  Se houver tempo, fazer até 8 sessões.
- **Perfil:** estudantes ou colegas com 18 anos ou mais que usam a internet todo dia. Buscamos variedade:
  pelo menos 1 pessoa que **nunca ou raramente** compra no Mercado Livre e pelo menos 1 que compra com frequência.
- **Exclusão:** quem trabalha ou trabalhou com o Mercado Livre e os próprios autores deste plano.
- **Identificação:** só por código (`P01`, `P02`, …). Não coletamos nome, RA, e-mail nem imagem do rosto.
- **Dados de perfil (opcionais e não identificáveis):** faixa de idade (18–24, 25–34, 35+) e frequência de compra online (nunca, mensal, semanal).
  Esses dados ficam na coluna Observações da primeira linha de cada participante.

## 5. Procedimento da sessão *(seção completada pela equipe)*

O roteiro detalhado está em [02-roteiro-sessao.md](02-roteiro-sessao.md). Resumo:

1. Preparação do ambiente: janela anônima no navegador **do próprio participante**, carrinho vazio, cronômetro zerado (cerca de 2 min).
2. Leitura do termo de consentimento ([05-termo-de-consentimento.md](05-termo-de-consentimento.md)) e concordância verbal.
3. *Briefing* padronizado e explicação do protocolo *think-aloud* (pensar em voz alta).
4. Execução das 8 tarefas, uma por vez, com o avaliador cronometrando e registrando a observação.
5. Questionário SUS ([04-questionario-sus.md](04-questionario-sus.md)), logo após a última tarefa.
6. Breve conversa de encerramento (do que gostou, o que foi difícil) e agradecimento.

Duração estimada: **15 a 20 minutos por sessão**.

## 6. Métricas (dados coletados)

| Métrica | Definição operacional | Fonte |
|---|---|---|
| Tempo de execução | Segundos de cada tarefa, do fim da leitura do enunciado até o critério de conclusão. O **tempo total** do participante é a soma das tarefas | Cronômetro → planilha, coluna `Tempo (s)` |
| Taxa de sucesso | Proporção de participantes que concluíram a tarefa **sem ajuda** (`Sucesso = S` e `com_ajuda = N`). A **taxa global** considera quem concluiu **todas** as tarefas sem ajuda | Planilha |
| Erros cometidos | Quantidade de erros por tarefa, conforme a definição em [03-planilha-observacao.md](03-planilha-observacao.md): clique incorreto, perda de caminho, confusão nos filtros | Planilha, coluna `Erros` |
| Satisfação percebida | Escore SUS (0–100) por participante e médio | [04-questionario-sus.csv](04-questionario-sus.csv) |

Todas as métricas são calculadas pelo analisador automatizado ([analise/](analise/)), sem conta manual.

## 7. Critérios de sucesso

| # | Critério (do enunciado) | Como é avaliado |
|---|---|---|
| C1 | **≥ 90%** dos usuários conseguem adicionar o produto ao carrinho | Taxa de sucesso sem ajuda na linha "Adicionar ao carrinho". O relatório também mostra a taxa com a ajuda incluída |
| C2 | Tempo médio para concluir o teste **≤ 4 minutos (240 s)** | Média dos tempos totais por participante |

## 8. Papéis

| Papel | Quem | Responsabilidade |
|---|---|---|
| Usuário final | Colega participante | Executar as tarefas como comprador, pensando em voz alta |
| Avaliador | Integrante da equipe | Conduzir a sessão pelo roteiro, cronometrar e registrar tempos, erros e comentários. Não induzir o participante |
| Instrutor | Prof. João Choma | Orientar e supervisionar a atividade |

**Ferramentas:** navegador (Chrome, Firefox ou Edge) em janela anônima; cronômetro do celular; planilha de observação (este CSV, em papel ou no Google Planilhas);
questionário SUS (impresso ou Google Forms com as mesmas 10 perguntas).

## 9. Riscos e mitigação

| # | Risco | Mitigação |
|---|---|---|
| R1 | Verificação de conta, CAPTCHA ou pedido de login antes do checkout | É esperado. A tarefa 7 termina **ao chegar** à tela de login ou de checkout. Nunca fazer login nem tentar contornar o CAPTCHA |
| R2 | O site muda durante o período de testes (nome dos filtros, ordenações disponíveis, *layout*) | Registrar a data e o navegador de cada sessão. Antes da 1ª sessão, um integrante faz um piloto e anota os nomes exatos. Se não existir a ordenação "melhores avaliados", usar a mais próxima e registrar isso no relatório |
| R3 | Conexão lenta ou instável | Usar uma rede estável. Se uma página demorar mais de 10 s para carregar, pausar o cronômetro e anotar |
| R4 | Anúncios, pop-ups de cookies ou de localização | Não contam como erro do participante. Registrar nas Observações |
| R5 | Efeito de aprendizado (participante já viu outra sessão) | Fazer as sessões em lugares separados e pedir que ninguém comente as tarefas |
| R6 | Avaliador induzir o participante | Seguir o roteiro ao pé da letra. Toda ajuda é registrada como `com_ajuda = S` |

## 10. Ética

- Participação **voluntária**, com **consentimento** registrado ([05-termo-de-consentimento.md](05-termo-de-consentimento.md)). O participante pode desistir a qualquer momento.
- **Anonimato:** só códigos P01…Pn. Não coletamos dado pessoal identificável. Prints de tela não podem mostrar nomes, e-mails, endereços nem rostos.
- **Sem login real, sem dados pessoais e sem pagamento.** A sessão para antes de qualquer dado de conta ou de pagamento.
- **Sem automação do site.** O Mercado Livre tem verificação anti-bot, e um teste de usuário precisa de **pessoas reais**.
  Nenhum robô (Playwright, Selenium etc.) é usado.
- O que avaliamos é a **interface**, não a pessoa. Isso deve ficar claro para o participante.
