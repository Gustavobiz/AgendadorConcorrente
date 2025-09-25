public class Tarefa implements Runnable {

    private final String nome;
    private final PoolDeRecursos pool;

    public Tarefa(String nome, PoolDeRecursos pool) {
        this.nome = nome;
        this.pool = pool;
    }

    @Override
    public void run() {
        try {
            pool.acessarRecurso(this.nome);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Tarefa " + nome + " foi interrompida.");
        }
    }
}