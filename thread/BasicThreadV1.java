
/*
 * 1^ modalità di creazione di thread in Java:
 * 		definizione di una sottoclasse estesa (che eredita) a partire dalla classe Thread.
 * 
 * E' necessario effettuare l'override del metodo run (vuoto nella classe padre).
 * Il metodo run (e quindi le istruzioni in esso contenute) verrà eseguito concorrentemente agli altri thread.
 * 
 * https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html
 * 
 */

public class BasicThreadV1 extends Thread {
	
	public BasicThreadV1(String name) {
		setName(name);
	}
	
	/*
	 * Il metodo run rappresenta il "corpo" del thread e contiene tutte le istruzioni che verranno eseguite quando lo scheduler 
	 * porrà in stato RUNNING il thread corrente.
	 */
	public void run() {
		// La seguente istruzione mi consente di accedere al nome del thread
		String threadName = Thread.currentThread().getName();
		System.out.println("Ciao, sono " + threadName);
		
		for (int i = 0; i < 10; i++) {
			System.out.println(threadName + " "  + i);
		}
	}
}
