public class Applicazione {
	
	public static void main(String[] args) throws InterruptedException {
		
		MySemaphore semaphore = new MySemaphore(1);
		
		Trattoria trattoria = new Trattoria(15, semaphore);
		
		Cliente RossiMario = new Cliente("Rossi", "Mario", "rossimario@gmail.com", "055 123 66 77");
		// Mario vuole prenotare 3 posti in trattoria
		int[] postiMario = { 0, 1, 2 };
		RossiMario.prenota(trattoria, postiMario);
		
		Cliente VerdiTommaso = new Cliente("Verdi", "Tommaso", "verditommaso@protonmain.com", "347 192 77 19");
		// Tommaso vuole prenotare 4 posti in trattoria
		int[] postiTommaso = { 2, 3, 4, 5 };
		VerdiTommaso.prenota(trattoria, postiTommaso);
		
		Cliente RiccardoBianchi = new Cliente("Bianchi", "Riccardo", "riccardobianchi@inventati.org", "338 181 91 10");
		// RiccardoBianchi vuole prenotare 4 posti in trattoria
		int[] postiRiccardo = { 0, 1, 5, 6 };
		RiccardoBianchi.prenota(trattoria, postiRiccardo);		
		
		RossiMario.start();
		VerdiTommaso.start();
		RiccardoBianchi.start();

		RossiMario.join();
		VerdiTommaso.join();
		RiccardoBianchi.join();
		
		// Prima di stampare a video la situazione definitiva dei posti prenotati, attende che tutti
		// i thread abbiamo terminato (join).
		trattoria.print();
		
	}
}