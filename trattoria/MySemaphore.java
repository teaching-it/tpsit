public class MySemaphore {

	int S;

	public MySemaphore(int S) {
		this.S = S;
	}
	
	synchronized public void P() {	
		if (S == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		S--;
	}

	synchronized public void V() {
		S++;
		notify();
	}
	
}