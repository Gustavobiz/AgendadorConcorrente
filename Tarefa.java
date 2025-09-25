public class Tarefa implements Runnable {

    private final String nome;
    private final PoolDeRecursos pool;
    private final TipoTarefa tipo;

    public Tarefa(String nome, PoolDeRecursos pool, TipoTarefa tipo) {
        this.nome = nome;
        this.pool = pool;
        this.tipo = tipo; 
    }

    public String getNome() {
        return nome;
    }

    public TipoTarefa getTipo() {
        return tipo; 
    }

    @Override
    public void run() {
        try {
            pool.acessarRecurso(this); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Tarefa " + nome + " foi interrompida.");
        }
    }
}