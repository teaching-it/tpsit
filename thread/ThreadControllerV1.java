
/*
 * Questa classe si occupa di gestire l'avvio dei thread creati a partire dalla classe BasicThreadV1
 * (thread implementati estendendo la classe Thread).
 */

public class ThreadControllerV1 {
        
    /*
     * Nb. in Java l'esecuzione del metodo main comporta l'implicita ed automatica generazione di un thread.
     */
    public static void main(String[] args) {
        
        /*
         * Creazione oggetti istanza della classe BasicThread
         * (ovvero, generazione di due thread; e con i due seguenti siamo 3 totali:
         * il main, più le due istanze della classe BasicThread)
         */
        BasicThreadV1 threadA = new BasicThreadV1("thread A");
        BasicThreadV1 threadB = new BasicThreadV1("thread B");
        
        /*
         * L'invocazione del metodo start comporta l'immediata esecuzione del metodo run contenuto nella classe BasicThread
         * (il metodo run non può essere invocato direttamente).
         */
        threadA.start();
        threadB.start();
        
        /*
         * Si noti che l'esecuzione concorrente dei due thread produce un output non deterministico.
         * In altre parole, gli effetti della schedulazione sui thread non è calcolabile a priori.
         */
    
        /*
         * Decommentando le seguenti istruzioni, si può notare (attraverso l'osservazione dell'output) che anche il main
         * risulta esso stesso un ulteriore thread.

        String threadName = Thread.currentThread().getName();
        System.out.println("Ciao, sono " + threadName);
        
        for (int i = 0; i < 10; i++) {
            System.out.println(threadName + " "  + i);
        }
        */
    }
}