public class Main {
    public static void main(String[] args) {
        PoolDeRecursos pool = new PoolDeRecursos(3);

        System.out.println("Iniciando simulação de Leitores e Escritores...");


        for (int i = 1; i <= 5; i++) {
            Tarefa tarefa = new Tarefa("Escritor-" + i, pool, TipoTarefa.ESCRITA);
            new Thread(tarefa).start();
        }

        for (int i = 1; i <= 10; i++) {
            Tarefa tarefa = new Tarefa("Leitor-" + i, pool, TipoTarefa.LEITURA);
            new Thread(tarefa).start();
        }
    }
}