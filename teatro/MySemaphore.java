package volta.tpsit.teatro;

/*
 * Semaforo di Dijkstra
 */
public class MySemaphore {    
    int S;

    public MySemaphore(int S) {
        this.S = S;
    }

    /*
     * È imprescindibile che il seguente blocco di istruzioni sia eseguito "atomicamente",
     * ovvero senza interruzioni.
     * Il qualificatore di metodo synchronized mi offre questa certezza.
     */
    synchronized public void P() {
        if (S == 0) {
            
            try {
                wait();
            } catch(InterruptedException e) { }
        }

        S--;
    }

    synchronized public void V() {
        S++;
        // Risveglia un thread in coda (ovvero: pone un thread in stato "ready")
        notify();
    }

}
