public class Tarefa implements Runnable {

    private final String nome;
    private final Agendador agendador;
    private final TipoTarefa tipo;
    

    private final int prioridadeBase;
    private long tempoDeEspera;


    public Tarefa(String nome, Agendador agendador, TipoTarefa tipo, int prioridadeBase) {
        this.nome = nome;
        this.agendador = agendador;
        this.tipo = tipo;
        this.prioridadeBase = prioridadeBase;
        this.tempoDeEspera = 0;
    }

    public String getNome() { return nome; }
    public TipoTarefa getTipo() { return tipo; }
    

    public void envelhecer() {
        this.tempoDeEspera++;
    }

    public long getPrioridadeEfetiva() {
        return this.prioridadeBase + this.tempoDeEspera;
    }
    
    public String getStatus() {
        return String.format("%s(%s,P:%d)", nome, tipo, getPrioridadeEfetiva());
    }


    @Override
    public void run() {
        agendador.adicionarTarefa(this);
    }
}