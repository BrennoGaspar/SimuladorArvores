# Simulador de Árvores 🌳

> Um simulador visual 2D focado na aplicação prática de Estruturas de Dados, que permite inserir, remover e percorrer nós em árvores de busca e comparar como cada uma se organiza e se equilibra.

## ✨ Objetivo

O **Simulador de Árvores** foi criado para traduzir conceitos fundamentais de Ciência da Computação para um ambiente interativo. O usuário constrói a própria árvore inserindo chaves, vê cada nó deslizar até o seu lugar quando a estrutura se reorganiza e compara como a **ABB** (Árvore Binária de Busca), a **AVL** e a **VP** (Vermelha e Preta) reagem às mesmas operações.

## 🚀 Funcionalidades

* ➕ **Inserção e Remoção:** Inserção por clique ou tecla e remoção com o clique direito diretamente sobre o nó (detecção de colisão).
* 🔍 **Busca com Destaque:** O nó encontrado é destacado na árvore.
* 🎲 **Geração Aleatória:** Insere 5 valores aleatórios, sem repetição, para testar rapidamente.
* 🚶 **Percursos Animados:** Em ordem, pré-ordem e pós-ordem, com o nó atual destacado a cada meio segundo.
* 🎞️ **Animação de Rotações:** Os nós se movem suavemente da posição antiga para a nova, em vez de pular, o que deixa as rotações visíveis.
* 🔄 **Modo Sincronizado:** As três árvores recebem as mesmas operações ao mesmo tempo, e é possível alternar entre elas com as setas do teclado.
* 🔎 **Zoom e Arrastar:** Roda do mouse para dar zoom e botão do meio para mover a árvore.
* 📊 **Informações em Tempo Real:** Cabeçalho com quantidade de nós, níveis e zoom, além do fator de balanceamento exibido sobre cada nó da AVL.
* 🎯 **Árvore Sempre Centralizada:** A árvore é reposicionada horizontalmente a cada alteração, com a raiz no topo.

## 🧠 Estruturas de Dados & Arquitetura (Foco do Projeto)

O grande diferencial deste projeto é que a visualização é gerada diretamente a partir das estruturas de dados clássicas, sem nenhuma biblioteca pronta de árvores. Cada algoritmo foi implementado do zero:

* 🌲 **Árvore Binária de Busca (ABB):**
  * **Onde:** `ABB.java`
  * **Como:** Inserção e remoção recursivas. Na remoção de um nó com dois filhos, ele é substituído pelo seu sucessor (o menor nó da subárvore direita).
  * **No mundo real:** É a base de índices, dicionários e conjuntos ordenados. Porém, sem balanceamento, inserir chaves em ordem crescente transforma a árvore em uma lista (`O(n)`), o que fica evidente no modo sincronizado.

* ⚖️ **Árvore AVL:**
  * **Onde:** `AVL.java`
  * **Como:** Guarda a altura de cada nó e calcula o fator de balanceamento (`altura da esquerda - altura da direita`). Quando ele sai do intervalo `[-1, 1]`, aplica rotação simples (esquerda ou direita) ou dupla (esquerda-direita ou direita-esquerda), tanto na inserção quanto na remoção.
  * **No mundo real:** Garante busca, inserção e remoção em `O(log n)` e é indicada para estruturas com muitas leituras.

* 🔴 **Árvore Vermelha e Preta (VP):**
  * **Onde:** `VP.java`
  * **Como:** Variante *left-leaning*. O equilíbrio é mantido por cores e por três operações: rotação à esquerda, rotação à direita e inversão de cores. A remoção usa as operações de mover um nó vermelho para a esquerda ou para a direita, e a raiz é sempre preta.
  * **No mundo real:** Estruturas parecidas sustentam o `TreeMap` e o `TreeSet` do Java e o escalonador de processos do kernel Linux.

## 🖼️ Comparativo das Árvores

As três árvores recebem as mesmas chaves, mas se organizam de formas diferentes:

| Árvore | Critério de Equilíbrio | Altura no Pior Caso | Rotações | Destaque Visual |
| :--- | :--- | :--- | :--- | :--- |
| **🔵 ABB** | Nenhum | `O(n)` | Não usa | Nós com anel azul |
| **🟢 AVL** | Fator de balanceamento entre -1 e +1 | `O(log n)`, bem equilibrada | Simples e duplas | Nós com anel verde e fator de balanceamento sobre cada nó |
| **🔴 VP** | Regras de cor (sem dois vermelhos seguidos e mesma altura negra) | `O(log n)`, até 2 vezes a altura mínima | Esquerda, direita e inversão de cores | Nós vermelhos e pretos, com arestas vermelhas até os nós vermelhos |

## 🎮 Controles

| Ação | Como fazer |
| :--- | :--- |
| Escolher a árvore | `1` (ABB), `2` (AVL), `3` (VP) ou `4` (sincronizadas) |
| Alternar entre as telas | `←` e `→` |
| Inserir uma chave | Clique esquerdo ou `I` |
| Remover um nó | Clique direito sobre o nó |
| Buscar uma chave | `F` |
| Gerar 5 valores aleatórios | `R` |
| Percurso em ordem, pré-ordem e pós-ordem | `O`, `P` e `S` |
| Mover a árvore | Segurar o botão do meio do mouse e arrastar |
| Zoom | Roda do mouse |
| Voltar ao menu (limpa as árvores) | `Backspace` |

## 🛠️ Tecnologias Utilizadas

* ☕ **Java**
* 🎮 **JSGE** (Java Simple Game Engine)
* 🪟 **Java Swing** (`JOptionPane` para a entrada das chaves)
* 📐 **Organização em Pacotes** (`simulador.ABB`, `simulador.AVL`, `simulador.VP`, além de `No` e `Main`, separando as estruturas de dados da interface)

## 💡 Ideia do Projeto

O projeto foi desenvolvido com foco em:
* 🖥️ Sair da teoria e aplicar Estruturas de Dados em um contexto interativo.
* 🔬 Enxergar o que acontece dentro de cada algoritmo de balanceamento, com rotações, recolorações e percursos animados.
* 📈 Comparar na prática o desempenho e o formato de ABB, AVL e VP com as mesmas chaves.
* 📁 Desenvolver um projeto organizado, modular e com código limpo visando boas práticas de engenharia de software.

## 👨‍💻 Autores

*  Brenno Gaspar Pinto
*  Victor Altran Soares
