public class Main {
    public static void main(String[] args) {
        PoolDeRecursos pool = new PoolDeRecursos(3);
        Agendador agendador = new Agendador(pool);

    
        new Thread(agendador, "Thread-Agendador").start();

        Monitor monitor = new Monitor(agendador, pool);
        new Thread(monitor, "Thread-Monitor").start();

        System.out.println("Sistema iniciado. Gerando tarefas...");


        for (int i = 1; i <= 5; i++) {
            new Thread(new Tarefa("Leitor-" + i, agendador, TipoTarefa.LEITURA)).start();
            sleep(100); 
        }

        int escritorId = 1;
        while(true) {
            new Thread(new Tarefa("Escritor-" + escritorId, agendador, TipoTarefa.ESCRITA)).start();
            escritorId++;
            sleep(500); 
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}