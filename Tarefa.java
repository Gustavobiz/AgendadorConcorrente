public class Tarefa implements Runnable {

    private final String nome;
    private final Agendador agendador; 
    private final TipoTarefa tipo;

    public Tarefa(String nome, Agendador agendador, TipoTarefa tipo) { 
        this.nome = nome;
        this.agendador = agendador; 
        this.tipo = tipo;
    }

    public String getNome() { return nome; }
    public TipoTarefa getTipo() { return tipo; }

    @Override
    public void run() {
        agendador.adicionarTarefa(this); 
    }
}