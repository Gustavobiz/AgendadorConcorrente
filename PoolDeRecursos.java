import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PoolDeRecursos {

    private final Semaphore semaphore;
    private final List<Recurso> recursos;


    private final Lock lockControle = new ReentrantLock();
    private int leitoresAtivos = 0;
    private int escritoresAtivos = 0;
    private int escritoresEsperando = 0;
    private final Condition podeLer = lockControle.newCondition();
    private final Condition podeEscrever = lockControle.newCondition();
  

    public PoolDeRecursos(int numeroDeRecursos) {
        this.semaphore = new Semaphore(numeroDeRecursos, true);
        this.recursos = new ArrayList<>();
        for (int i = 1; i <= numeroDeRecursos; i++) {
            recursos.add(new Recurso(i));
        }
    }

    public String getStatusRecursos() {
        lockControle.lock(); 
        try {
            StringBuilder status = new StringBuilder();
            for (Recurso r : recursos) {
                status.append("Recurso ").append(r.getId()).append(": ").append(r.getDonoAtual()).append(" | ");
            }
            return status.toString();
        } finally {
            lockControle.unlock();
        }
    }


    public void acessarRecurso(Tarefa tarefa) throws InterruptedException {
        if (tarefa.getTipo() == TipoTarefa.LEITURA) {
            iniciarLeitura(tarefa.getNome());
        } else {
            iniciarEscrita(tarefa.getNome());
        }
    }

    private void iniciarLeitura(String nome) throws InterruptedException {
        lockControle.lock();
        try {
       
            while (escritoresAtivos > 0 || escritoresEsperando > 0) {
                System.out.println("--- Leitor " + nome + " esperando por escritor...");
                podeLer.await();
            }
            leitoresAtivos++;
        } finally {
            lockControle.unlock();
        }

        executarTarefa(nome, TipoTarefa.LEITURA);

        lockControle.lock();
        try {
            leitoresAtivos--;
            if (leitoresAtivos == 0) {
                podeEscrever.signal();
            }
        } finally {
            lockControle.unlock();
        }
    }

    private void iniciarEscrita(String nome) throws InterruptedException {
        lockControle.lock();
        try {
            escritoresEsperando++;
            while (leitoresAtivos > 0 || escritoresAtivos > 0) {
                System.out.println("--- Escritor " + nome + " esperando por leitores/escritores...");
                podeEscrever.await();
            }
            escritoresEsperando--;
            escritoresAtivos++;
        } finally {
            lockControle.unlock();
        }

        executarTarefa(nome, TipoTarefa.ESCRITA);

        lockControle.lock();
        try {
            escritoresAtivos--;
        
            podeEscrever.signal();
            podeLer.signalAll();
        } finally {
            lockControle.unlock();
        }
    }
    
    
    private void executarTarefa(String nome, TipoTarefa tipo) throws InterruptedException {
        System.out.println(nome + " (" + tipo + ") está na fila do semáforo...");
        semaphore.acquire();
        Recurso recursoParaUsar = null;
        try {
            System.out.println("-> " + nome + " (" + tipo + ") adquiriu permit do semáforo!");
            lockControle.lock(); 
            for (Recurso r : recursos) {
                if (r.estaLivre()) {
                    recursoParaUsar = r;
                    break;
                }
            }
            lockControle.unlock();

            if (recursoParaUsar != null) {
                recursoParaUsar.usar(nome);
            }
        } finally {
            semaphore.release();
            System.out.println("<- " + nome + " (" + tipo + ") liberou permit.");
        }
    }
}