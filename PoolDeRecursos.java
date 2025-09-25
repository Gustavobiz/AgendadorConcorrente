import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PoolDeRecursos {

    private final int numeroDeRecursos;
    private final Semaphore semaphore;
    private final List<Recurso> recursos;
    private final Lock lockPool = new ReentrantLock(); 

    public PoolDeRecursos(int numeroDeRecursos) {
        this.numeroDeRecursos = numeroDeRecursos;
       
        this.semaphore = new Semaphore(numeroDeRecursos, true); 
        this.recursos = new ArrayList<>();
        for (int i = 1; i <= numeroDeRecursos; i++) {
            recursos.add(new Recurso(i));
        }
    }

    public void acessarRecurso(String nomeDaTarefa) throws InterruptedException {
        System.out.println(nomeDaTarefa + "Esta na fila tentando adquirir um permit do semáforo...");


        semaphore.acquire();

        Recurso recursoParaUsar = null;
        try {
            System.out.println("-> " + nomeDaTarefa + " adquiriu um permit!");
            
 
            lockPool.lock();
            for (Recurso r : recursos) {
                if (r.estaLivre()) {
                    recursoParaUsar = r;
                    break;
                }
            }
            lockPool.unlock();


            if (recursoParaUsar != null) {
                recursoParaUsar.usar(nomeDaTarefa);
            }

        } finally {

            semaphore.release();
            System.out.println("<- " + nomeDaTarefa + " liberou seu permit.");
        }
    }
}