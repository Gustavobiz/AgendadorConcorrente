import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Agendador implements Runnable {

    private final PoolDeRecursos pool;
    private final Queue<Tarefa> filaDeTarefas = new LinkedList<>();
    private final Lock lockFila = new ReentrantLock();
    private final Condition temTarefa = lockFila.newCondition();
    private final long cicloDeEnvelhecimento = 500; 

    public Agendador(PoolDeRecursos pool) {
        this.pool = pool;
    }

    public void adicionarTarefa(Tarefa tarefa) {
        lockFila.lock();
        try {
            filaDeTarefas.add(tarefa);
            System.out.println("++ Agendador: " + tarefa.getNome() + " (" + tarefa.getTipo() + ") entrou na fila.");
            temTarefa.signalAll(); 
        } finally {
            lockFila.unlock();
        }
    }

    public String getStatusFila() {
        lockFila.lock();
        try {
            if (filaDeTarefas.isEmpty()) {
                return "Fila vazia.";
            }
            StringBuilder status = new StringBuilder();
            filaDeTarefas.stream()
                .sorted(Comparator.comparing(Tarefa::getPrioridadeEfetiva).reversed())
                .forEach(t -> status.append(t.getStatus()).append(" "));
            return status.toString();
        } finally {
            lockFila.unlock();
        }
    }
    

    public void iniciarEnvelhecimento() {
        Thread threadEnvelhecimento = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(cicloDeEnvelhecimento);
                    lockFila.lock();
                    try {
                        for (Tarefa t : filaDeTarefas) {
                            t.envelhecer();
                        }
                        temTarefa.signal(); 
                    } finally {
                        lockFila.unlock();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });
        threadEnvelhecimento.setDaemon(true); 
        threadEnvelhecimento.start();
    }


    @Override
    public void run() {
        iniciarEnvelhecimento(); 
        while (true) {
            lockFila.lock();
            Tarefa proximaTarefa = null;
            try {
                while (filaDeTarefas.isEmpty()) {
                    try {
                        temTarefa.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }


                proximaTarefa = filaDeTarefas.stream()
                        .max(Comparator.comparing(Tarefa::getPrioridadeEfetiva))
                        .orElse(null);
                
                if (proximaTarefa != null) {
                    filaDeTarefas.remove(proximaTarefa);
                }

            } finally {
                lockFila.unlock();
            }

            if (proximaTarefa != null) {
                try {
                    pool.acessarRecurso(proximaTarefa);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}