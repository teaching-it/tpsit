
/*
 * Questa classe si occupa di gestire l'avvio dei thread creati a partire dalla classe BasicThreadV2
 * (thread creati implementando l'interfaccia Runnable).
 */

public class ThreadControllerV2 {
	
	/*
	 * Nb. in Java l'esecuzione del metodo main comporta l'implicita ed automatica generazione di un thread.
	 */
	public static void main(String[] args) {
		
		/*
		 *  Passiamo in rassegna 3 differenti "formule" per la creazione di thread a partire dalla classe BasicThreadV2.
		 *  In linea di principio, dato che BasicThreadV2 non eredita dalla classe Thread, non è possibile invocare direttamente
		 *  il metodo start sugli oggetti istanziati.
		 *  
		 *  Prima si procedere alla creazione di un oggetto BasicThreadV2;
		 *  successivamente si crea un oggetto di tipo Thread passando come parametro al costruttore l'oggetto BasicThreadV2.
		 */
		
		/*
		 * 1.
		 */
		BasicThreadV2 basicThreadObject = new BasicThreadV2("thread A");
		Thread threadA = new Thread(basicThreadObject);
		// Il metodo start invoca automaticamente il metodo run all'interno di BasicThreadV2.
		threadA.start();
		
		/*
		try {
			threadA.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
		
		/*
		 * 2.
		 */
		Thread threadB = new Thread(new BasicThreadV2("thread B"));
		threadB.start();
		
		/*
		 * 3.
		 */
		// new Thread(new BasicThreadV2("thread C")).start();
		
		/*
		 * Si noti che l'esecuzione parallela dei due thread produce un output non deterministico.
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