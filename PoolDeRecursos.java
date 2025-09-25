import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PoolDeRecursos {

    private final int numeroDeRecursos;
    private final Semaphore semaphore;
    private final List<Recurso> recursos;
    private final Lock lockPool = new ReentrantLock(); // Lock para proteger a lista de recursos

    public PoolDeRecursos(int numeroDeRecursos) {
        this.numeroDeRecursos = numeroDeRecursos;
        // O semáforo começa com N "permits", um para cada recurso
        this.semaphore = new Semaphore(numeroDeRecursos, true); // 'true' para modo justo
        this.recursos = new ArrayList<>();
        for (int i = 1; i <= numeroDeRecursos; i++) {
            recursos.add(new Recurso(i));
        }
    }

    public void acessarRecurso(String nomeDaTarefa) throws InterruptedException {
        System.out.println(nomeDaTarefa + " está na fila, tentando adquirir um permit do semáforo...");

        // 1. Tenta adquirir um "permit". Se o semáforo estiver com 0 permits,
        // a thread ficará bloqueada aqui esperando a liberação.
        semaphore.acquire();

        Recurso recursoParaUsar = null;
        try {
            System.out.println("-> " + nomeDaTarefa + " adquiriu um permit!");
            
            // 2. Procura por um recurso livre na lista.
            // A lista é um recurso compartilhado, então precisamos de um lock para protegê-la.
            lockPool.lock();
            for (Recurso r : recursos) {
                if (r.estaLivre()) {
                    recursoParaUsar = r;
                    break;
                }
            }
            lockPool.unlock();

            // 3. Usa o recurso encontrado
            if (recursoParaUsar != null) {
                recursoParaUsar.usar(nomeDaTarefa);
            }

        } finally {
            // 4. Libera o "permit" de volta para o semáforo, permitindo
            // que outra thread que esteja esperando possa entrar.
            semaphore.release();
            System.out.println("<- " + nomeDaTarefa + " liberou seu permit.");
        }
    }
}