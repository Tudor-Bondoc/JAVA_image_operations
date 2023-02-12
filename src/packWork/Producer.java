package packWork;

import java.util.Arrays;

public class Producer extends Thread {
	
	/*---------Cele 2 buffere pt cele 2 imagini sursa---------*/
	private Buffer buffer1;
	private Buffer buffer2;
	/*---------Cei 2 vectori de octeti (cele 2 imagini sursa initiale)---------*/
	public byte[] sourceImage1;
	public byte[] sourceImage2;
	
	public long startTime;
	
	/*---------Constructor Producer---------*/
	public Producer (Buffer buffer1, Buffer buffer2, byte[] sourceImage1, byte[] sourceImage2) {
		this.buffer1 = buffer1;
		this.buffer2 = buffer2;
		this.sourceImage1 = sourceImage1;
		this.sourceImage2 = sourceImage2;
	}
	/*---------Metoda RUN---------*/
	@Override
	public void run() {
		int sfert = 0; //numar la ce sfert am ajuns
		for (int i=0; i<sourceImage1.length; i+=(sourceImage1.length/4)) {
			/*---------Pun in buffer cate un sfert din imaginea sursa---------*/
			buffer1.put(Arrays.copyOfRange(sourceImage1, i, Math.min(i + sourceImage1.length/4, sourceImage1.length)));
			System.out.println("Producatorul a pus sfertul " + sfert + " din poza Lenna");
			buffer2.put(Arrays.copyOfRange(sourceImage2, i, Math.min(i + sourceImage2.length/4, sourceImage2.length)));
			System.out.println("Producatorul a pus sfertul " + sfert + " din poza Scarlett");
			sfert++; 
			try {
				sleep(1000); //pentru a evidentia etapele comunicarii
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Eroare: Thread-ul Producator a fost intrerupt.");
			}
		}
	}
}
