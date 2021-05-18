public class Cliente extends Thread {
	
	private String nome, cognome, email, telefono;

	// Area di memoria condivisa, ovvero un oggetto di tipo Trattoria che incapsula la struttura dati (vettore)
	private Trattoria trattoria;

	// Un vettore di interi, corrispondete ai posti che il cliente intende prenotare, nel range [0,14]
	private int[] postiRichiesti;
	
	public Cliente(String cognome, String nome, String email, String telefono) {
		this.cognome = cognome;
		this.nome = nome;
		this.email = email;
		this.telefono = telefono;
	}
	
	public String getNome() {
		return cognome + " " + nome;
	}
	
	/*
	 * Il metodo start() invocato su un oggetto di tipo Cliente attiva il metodo run()
	 */
	public void run() {
		trattoria.prenota(this, postiRichiesti);
	}
	
	public void prenota(Trattoria trattoria, int[] postiRichiesti) {
		this.trattoria = trattoria;
		this.postiRichiesti = postiRichiesti;
	}
}
