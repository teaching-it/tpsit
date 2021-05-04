package volta.tpsit.teatro;

public class Teatro {
    
    public static void main(String[] args) throws InterruptedException {
        
        MySemaphore semaphore = new MySemaphore(1);
        
        Platea platea = new Platea(5, semaphore);
        
        Spettatore RossiMario = new Spettatore("Rossi", "Mario", "rossimario@gmail.com", "12345");
        RossiMario.prenota(platea, 1, 2);
        
        Spettatore VerdiTommaso = new Spettatore("Verdi", "Tommaso", "verditommaso@gmail.com", "12345");
        VerdiTommaso.prenota(platea, 1, 2);
        
        Spettatore BianchiAntonio = new Spettatore("Bianchi", "Antornio", "bianchiantonio@gmail.com", "12345");
        BianchiAntonio.prenota(platea, 1, 2);
        
        RossiMario.start();
        /* 
         * Effettuo dei print intermedi per verificare la corretta gestione della prenotazione, per puri fini diagnostici.
         * ATTENZIONE: l'accesso in lettura alla struttura dati condivisa, effettuata in forma concorrente,
         * potrebbe generare un output NON rappresentativo dell'attuale situazione della platea.
         * Questo perché l'accesso in lettura NON è regolamentata da un semaforo.
         */
        platea.printMatrix();
        
        VerdiTommaso.start();
        platea.printMatrix();
        
        BianchiAntonio.start();
        platea.printMatrix();
    
        RossiMario.join();
        VerdiTommaso.join();
        BianchiAntonio.join();
        
        // Prima di stampare a video la situazione DEFINITIVA della platea, attende che tutti
        // i thread abbiamo terminato (join).
        platea.printMatrix();
        
    }
}