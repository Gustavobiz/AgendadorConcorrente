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

    public Agendador(PoolDeRecursos pool) {
        this.pool = pool;
    }

    public void adicionarTarefa(Tarefa tarefa) {
        lockFila.lock();
        try {
            filaDeTarefas.add(tarefa);
            System.out.println("++ Agendador: " + tarefa.getNome() + " (" + tarefa.getTipo() + ") entrou na fila.");
            temTarefa.signal();
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
            for (Tarefa t : filaDeTarefas) {
                status.append(t.getNome()).append("(").append(t.getTipo()).append(") ");
            }
            return status.toString();
        } finally {
            lockFila.unlock();
        }
    }


    @Override
    public void run() {
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

                for (Tarefa t : filaDeTarefas) {
                    if (t.getTipo() == TipoTarefa.ESCRITA) {
                        proximaTarefa = t;
                        break;
                    }
                }
             
                if (proximaTarefa == null) {
                    proximaTarefa = filaDeTarefas.peek();
                }
                
                if (proximaTarefa != null) {
                    filaDeTarefas.remove(proximaTarefa);
                }
                // -----------------------------------------

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