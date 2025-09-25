public class Recurso {

    private final int id;
    private String donoAtual; // Para sabermos quem está usando o recurso

    public Recurso(int id) {
        this.id = id;
        this.donoAtual = "Livre";
    }

    public void usar(String nomeDaTarefa) throws InterruptedException {
        this.donoAtual = nomeDaTarefa;
        System.out.println(">>> Tarefa " + nomeDaTarefa + " começou a usar o Recurso " + this.id);

        // Simula o tempo que a tarefa leva para usar o recurso
        Thread.sleep(2000); // 2 segundos

        System.out.println("<<< Tarefa " + nomeDaTarefa + " terminou de usar o Recurso " + this.id);
        this.donoAtual = "Livre";
    }

    public int getId() {
        return id;
    }

    public String getDonoAtual() {
        return donoAtual;
    }

    public boolean estaLivre() {
        return donoAtual.equals("Livre");
    }
}