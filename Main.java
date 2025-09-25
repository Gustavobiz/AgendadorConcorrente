public class Main {
    public static void main(String[] args) {
    
        PoolDeRecursos pool = new PoolDeRecursos(3);

        System.out.println("Criando 10 tarefas para competir por 3 recursos...");


        for (int i = 1; i <= 10; i++) {
            Tarefa tarefa = new Tarefa("Tarefa-" + i, pool);
            Thread thread = new Thread(tarefa);
            thread.start();
        }
    }
}