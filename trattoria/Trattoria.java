public class Trattoria {

	// Struttura dati condivisa: una vettore di oggetti di tipo Cliente.
	private Cliente[] postiTrattoria;

	// Semaforo che regolamenta l'accesso alla sezione critica
	private MySemaphore semaphore;
	
	public Trattoria(int size, MySemaphore semaphore) {
		this.postiTrattoria = new Cliente[size];
		this.semaphore = semaphore;
	}


	/*
	 * Il seguente metodo contiene la sezione critica che è necessario "proteggere" da eventuali
	 * accessi multipli, mediante l'impiego di un semaforo.
	 */
	public void prenota(Cliente cliente, int[] postiRichiesti) {
		
		/*
		 * All'interno del vettore postiRichiesti, sono presenti i valori interi relativi ai posti che 
		 * il cliente intende prenotare, tra i 15 inizialmente disponibili
		 */
		for (int i = 0; i < postiRichiesti.length; i++) {

			int postoRichiesto = postiRichiesti[i];
			System.out.println(cliente.getNome() + " vorrebbe prenotare il posto [" + postoRichiesto + "]");

			semaphore.P();

				// Il posto è libero? Se sì, è possibile assegnarlo al cliente
				if ( ! (postiTrattoria[postoRichiesto] instanceof Cliente)) {
					postiTrattoria[postoRichiesto] = cliente;
				}
			
			semaphore.V();
		}
	}
	
	public void print() {
		System.out.println("");
		
		for (int i = 0; i < postiTrattoria.length; i++) {
			if (postiTrattoria[i] instanceof Cliente) {
				Cliente cliente = postiTrattoria[i];
				System.out.println("Posto [" + i + "] prenotato da: " + cliente.getNome());
			}
		}
	}	
}
