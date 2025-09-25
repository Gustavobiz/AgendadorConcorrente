public class Monitor implements Runnable {

    private final Agendador agendador;
    private final PoolDeRecursos pool;

    public Monitor(Agendador agendador, PoolDeRecursos pool) {
        this.agendador = agendador;
        this.pool = pool;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000); 
                System.out.println("\n===================== MONITOR ====================");
                System.out.println("STATUS DO POOL: " + pool.getStatusRecursos());
                System.out.println("FILA DE ESPERA: " + agendador.getStatusFila());
                System.out.println("===================================================\n");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}