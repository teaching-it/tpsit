package volta.tpsit.teatro;

public class Platea {

    // Struttura dati condivisa: una matrice di oggetti di tipo Spettatore.
    private Spettatore[][] matrix;
    // Semaforo che regolamenta l'accesso alla struttura dati
    private MySemaphore semaphore;
    
    public Platea(int size, MySemaphore semaphore) {
        this.matrix = new Spettatore[size][size];
        this.semaphore = semaphore;
    }
    
    /*
     * Questo metodo contiene la sezione critica che è necessario "proteggere" da eventuali
     * accessi multipli mediante l'impiego di un semaforo.
     */
    public void prenota(Spettatore spettatore, int i, int j) {
        
        semaphore.P();
        
        // Il posto è libero? Se sì, è possibile assegnarlo allo spettatore.
        if ( ! (matrix[i][j] instanceof Spettatore)) {
            matrix[i][j] = spettatore;
        }
        
        semaphore.V();
    }
    
    public void printMatrix() {
        System.out.println("");
        
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {    
                if (matrix[i][j] instanceof Spettatore) {
                    Spettatore spettatore = matrix[i][j];
                    System.out.println("Posto [" + i + "," + j + "] prenotato da: " + spettatore.getNome());
                }
            }
        }
    }    
}
