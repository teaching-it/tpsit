
/*
 * 2^ modalità di creazione di thread in Java: implementazione dell'interfaccia Runnable.
 * 
 * E' necessario implementare il metodo run.
 * Il metodo run (e quindi le istruzioni in esso contenute) verrà eseguito concorrentemente agli altri thread.
 * 
 * https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html
 * 
 * Utile riepilogo interfacce in Java
 * https://riptutorial.com/java/example/409/implementing-multiple-interfaces
 */

public class BasicThreadV2 implements Runnable {
	
	private String threadName;
	public BasicThreadV2(String name) {
		this.threadName = name;
	}
	
	/*
	 * Il metodo run rappresenta il "corpo" del thread e contiene tutte le istruzioni che verranno eseguite quando lo scheduler 
	 * porrà in stato RUNNING il thread corrente.
	 */
	public void run() {
		System.out.println("Ciao, sono " + threadName);
		
		for (int i = 0; i < 100; i++) {
			
			// caso 2. (decommentare all'occorrenza)
			/*
			 * Per ottenere un'alternanza corretta nella visualizzazione dei thread abbiamo inserito un ritardo pari a 1000ms (ovvero 1s);
			 * questo approccio non è un vero e proprio meccanismo di sincronizzazione bensì un "artificio" per forzare una schedulazione alternata
			 * dei thread.
			 * 
			 * Il metodo sleep interrompe l'esecuzione del thread (stato wait) senza sprecare cicli del processore (ovvero senza "attesa attiva").
			 * 
			 * https://stackoverflow.com/questions/1036754/difference-between-wait-and-sleep
			 * sleep(n) says "I’m done with my timeslice, and please don’t give me another one for at least n milliseconds."
			 * The OS doesn’t even try to schedule the sleeping thread until requested time has passed.
			/*
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/
			
			// caso 3. (decommentare all'occorrenza)
			/*
			if (this.threadName.equals("thread B")) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}					
			*/
			
			// caso base.
			System.out.println(threadName + " "  + i);
		}
		
	}
}