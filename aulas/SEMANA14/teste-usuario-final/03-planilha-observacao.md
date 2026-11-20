# 03: Planilha de observação

Arquivo de dados: [03-planilha-observacao.csv](03-planilha-observacao.csv) (UTF-8, separador `,`; o analisador também aceita `;`).

## Colunas

As colunas são **exatamente** as do modelo do professor (*Tarefa, Tempo (s), Sucesso (S/N), Erros, Observações*), com mais duas:
`participante` e `com_ajuda`.

| Coluna | Conteúdo | Valores aceitos |
|---|---|---|
| `participante` | Código anônimo | `P01`, `P02`, … |
| `Tarefa` | Linha do modelo do professor | veja abaixo |
| `Tempo (s)` | Segundos gastos na tarefa | número ≥ 0 (aceita `12,5` ou `12.5`); em branco = não cronometrado |
| `Sucesso (S/N)` | Atingiu o critério de conclusão? | `S` ou `N`. **Em branco = linha ainda não coletada (ignorada)** |
| `Erros` | Quantidade de erros (definição abaixo) | inteiro ≥ 0; em branco = 0 |
| `com_ajuda` | O avaliador deu dica? | `S` ou `N`; em branco = `N` |
| `Observações` | Falas do participante, problemas, contexto | texto livre (use aspas se tiver vírgula) |

## Linhas (tarefas)

As linhas são as 6 do modelo do professor, mais **"Retornar à tela inicial"** (tarefa 7 do enunciado, que faltava no modelo):

| Linha | Tarefas do plano |
|---|---|
| Acessar site | 1 |
| Buscar produto | 2 |
| Aplicar filtros | 3 (Full) + 4 (melhores avaliados): tempo somado; sucesso só se as duas foram feitas |
| Abrir detalhes | 5 |
| Adicionar ao carrinho | 6 (base do critério C1) |
| Simular compra | 7 |
| Retornar à tela inicial | 8 |

O CSV já traz as linhas de **P01 a P05**, com os campos de dados em branco. Para mais participantes, copie o bloco de 7 linhas e troque o código.

## Definição operacional de erro

Some **1 erro** a cada ocorrência de:

| Tipo | Exemplos |
|---|---|
| **Clique incorreto** | Clicar em um elemento que não leva ao objetivo da tarefa e precisar voltar (por exemplo, abrir um anúncio patrocinado sem querer, clicar em "Mais vendidos" para ordenar por avaliação) |
| **Perda de caminho** | Usar "Voltar" do navegador para desfazer uma navegação errada; abrir menu ou página sem relação com a tarefa; refazer a busca sem necessidade |
| **Confusão nos filtros** | Aplicar um filtro errado (por exemplo, "Frete grátis" em vez de "Full"); remover um filtro correto; escolher uma ordenação diferente da pedida |
| **Ação proibida evitada** | Começar a digitar login ou dados pessoais e ser interrompido pelo avaliador |

**Não** contam como erro: rolar a página, passar o mouse sobre elementos, fechar banners de cookies, localização ou anúncios, e lentidão de carregamento.
Esses casos vão nas Observações.

## Exemplo de preenchimento (ilustrativo, NÃO é dado real)

```csv
participante,Tarefa,Tempo (s),Sucesso (S/N),Erros,com_ajuda,Observações
PXX,Aplicar filtros,75,S,2,S,"aplicou Frete grátis antes do Full; pediu dica após 60 s"
```
