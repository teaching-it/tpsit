package volta.tpsit.teatro;

public class Spettatore extends Thread {
    
    private String nome, cognome, email, telefono;
    private Platea platea;
    private int i, j; 
    
    public Spettatore(String cognome, String nome, String email, String telefono) {
        this.cognome = cognome;
        this.nome = nome;
        this.email = email;
        this.telefono = telefono;
    }
    
    public String getNome() {
        return cognome + " " + nome;
    }
    
    /*
     * Il metodo start() invocato su un oggetto di tipo Spettatore attiva il metodo run()
     */
    public void run() {
        platea.prenota(this, i, j);
    }
    
    public void prenota(Platea platea, int i, int j) {
        this.platea = platea;
        this.i = i;
        this.j = j;
    }
}
