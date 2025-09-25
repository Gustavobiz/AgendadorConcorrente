# AgendadorConcorrente

## Descrição

Este é um simulador de um servidor de agendamento de recursos concorrentes, desenvolvido em Java. O projeto demonstra a gestão de um pool de recursos limitados que são disputados por múltiplas threads (tarefas). A simulação implementa e resolve problemas clássicos da programação concorrente, como o **Problema dos Leitores e Escritores** e a **Inanição (Starvation)**.

## Contexto do Projeto

Este trabalho foi desenvolvido para a disciplina de Programação Concorrente, ministrada pelo Prof. Dr. Everton Cavalcante. O objetivo é aplicar em um projeto prático os conceitos teóricos sobre threads, sincronização, e problemas de concorrência.

## Conceitos Abordados

A simulação implementa e explora os seguintes conceitos de programação concorrente:

- **Pool de Recursos e Semáforos (`Semaphore`)**: O sistema gerencia um pool com `N` recursos, um semáforo é utilizado para controlar o número máximo de threads que podem acessar o pool simultaneamente, garantindo o balanceamento de carga.
- **Problema dos Leitores e Escritores**: As tarefas são divididas em duas categorias:
  - **Leitores**: Podem acessar os recursos de forma compartilhada, múltiplos leitores ao mesmo tempo.
  - **Escritores**: Exigem acesso exclusivo ao pool, bloqueando todos os outros leitores e escritores.
- **Inanição (Starvation)**: O projeto é configurado para demonstrar um cenário onde tarefas de baixa prioridade (ou leitores) são constantemente adiadas por um fluxo de tarefas de alta prioridade (escritores), nunca conseguindo acesso ao recurso.
- **Solução com "Aging" (Envelhecimento)**: Para resolver a inanição, foi implementado um algoritmo de agendamento justo. A prioridade de uma tarefa aumenta dinamicamente quanto mais tempo ela passa na fila de espera, garantindo que eventualmente todas as tarefas sejam executadas.
- **Exclusão Mútua (`ReentrantLock`)**: Locks são utilizados para proteger seções críticas do código, como a manipulação da lista de recursos e os contadores de leitores/escritores, evitando condições de corrida.
- **Monitoramento em Tempo Real**: Uma thread `Monitor` dedicada imprime o estado do sistema no console a cada segundo, permitindo a visualização em tempo real da fila de espera, do estado dos recursos e da prioridade dinâmica das tarefas.

## Estrutura do Projeto

O código está organizado nas seguintes classes principais:

- `Main.java`: Ponto de entrada da aplicação. Configura o cenário da simulação, cria e inicia as threads das tarefas e do monitor.
- `PoolDeRecursos.java`: Gerencia o conjunto de recursos, utilizando um `Semaphore` para limitar o acesso e `Locks` para a lógica de Leitores e Escritores.
- `Agendador.java`: Atua como o "cérebro" do sistema. Mantém a fila de tarefas e implementa o algoritmo de "aging" para decidir qual tarefa será a próxima a ser executada.
- `Tarefa.java`: Representa uma unidade de trabalho (`Runnable`) que será executada por uma thread. Possui um tipo (Leitura/Escrita) e uma prioridade.
- `Recurso.java`: Um objeto simples que representa um recurso individual dentro do pool.
- `Monitor.java`: Uma thread `Runnable` responsável por imprimir o estado do sistema no console em intervalos regulares.

## Como Executar

1.  Clone este repositório:
2.  Abra o projeto em sua IDE Java.
3.  Execute o arquivo `Main.java`.

## Fases da Simulação

O projeto foi projetado para demonstrar claramente o problema e a solução.

### 1. Cenário de Starvation

Para visualizar a inanição, basta desativar o algoritmo de "aging" no `Agendador.java` e fazer com que ele sempre dê preferência a tarefas de `ESCRITA`. Ao executar a simulação com um fluxo constante de escritores, você observará que as tarefas de `LEITURA` ficarão presas na fila indefinidamente.

### 2. Cenário com Solução (Aging)

Executando o código como está, o algoritmo de "aging" está ativo. O `Monitor` mostrará a prioridade efetiva das tarefas na fila aumentando com o tempo. Mesmo com um fluxo constante de tarefas de alta prioridade, você verá que as tarefas de baixa prioridade eventualmente "envelhecem" o suficiente para ganhar acesso ao recurso, provando que a inanição foi resolvida.

## Autor

<a href="https://github.com/Gustavobiz">Gustavo Sousa Bernardes</a><br>
