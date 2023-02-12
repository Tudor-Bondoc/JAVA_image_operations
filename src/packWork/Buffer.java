package packWork;

import java.util.Arrays;

public class Buffer {
	
	public boolean available = false;
	private byte[] chunk; //in buffer trimit cate o bucata de 1/4 din imagine
	
	/*---------Metoda PUT---------*/
	public synchronized void put (byte[] chunk) {
		while (available) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
			this.chunk = Arrays.copyOf(chunk, chunk.length);
			available = true;
			notifyAll();
	}
	/*---------Metoda GET---------*/
	public synchronized byte[] get() {
		while (!available) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		available = false;
		notifyAll();
		return chunk;
	}

}
