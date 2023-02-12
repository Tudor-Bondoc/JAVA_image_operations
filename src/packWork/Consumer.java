package packWork;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Consumer extends Thread {
	
	private Buffer buffer1;
	private Buffer buffer2;
	private Producer producer; //am nevoie de Producer pt obtinerea informatiilor despre dimensiunea imaginilor de intrare
	private DataOutputStream out1, out2, out3; //outputStream-urile trimise prin Pipe catre WriterResult (pt cele 3 imagini destinatie)
	private XorEditor xorEditor;
	private AndEditor andEditor;
	private OrEditor orEditor;
	public long startTime, endTime, dif1, dif2, dif3, sum;
	/*---------Constructor Consumer---------*/
	public Consumer(Buffer buffer1, Buffer buffer2, Producer producer, DataOutputStream out1, DataOutputStream out2, DataOutputStream out3) {
		this.buffer1 = buffer1;
		this.buffer2 = buffer2;
		this.producer = producer;
		this.out1 = out1;
		this.out2 = out2;
		this.out3 = out3;
		this.xorEditor = new XorEditor();
		this.andEditor = new AndEditor();
		this.orEditor = new OrEditor();
	}
	
	/*---------Metoda RUN---------*/
	@Override
	public void run () {
		int ct = 0;
		/*---------In image si image2 salvez cele 2 imagini sursa dupa ce se termina Producer-Consumer---------*/
		BufferedImage image = null;
		BufferedImage image2 = null;
		/*---------In cele 2 outputStream-uri concatenez sferturile primite, pentru a reconstitui imaginile initiale---------*/
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
		
		while (true){
			/*---------Chunk = cate o bucata de un sfert (1/4) care vine din Producer---------*/
			byte[] chunk = buffer1.get();
			byte[] chunk2 = buffer2.get();
			try {
				outputStream.write(chunk); //adaug cate o bucata in outputStream, pe masura ce vine din Producer
				outputStream2.write(chunk2); //in acest fel, la final am imaginea reconstituita in outputStream
				
				if (outputStream.size() == producer.sourceImage1.length) { //daca am bagat toate sferturile in outputStream
					byte[] fullImage = outputStream.toByteArray(); //convertesc outputStream-ul in vector de octeti
					ByteArrayInputStream in = new ByteArrayInputStream(fullImage);
					image = ImageIO.read(in); //optin prima imagine sursa sub forma de BufferedImage
				}
				/*---------Analog cu if-ul precedent---------*/
				if (outputStream2.size() == producer.sourceImage2.length) {
					byte[] fullImage2 = outputStream2.toByteArray();
					ByteArrayInputStream in2 = new ByteArrayInputStream(fullImage2);
					image2 = ImageIO.read(in2);
					
					/*---------Acum am cele 2 imagini sursa la dispozitie - image si image2---------*/
					
					/*---------Incep procesarea XOR---------*/
					startTime = System.currentTimeMillis();
					BufferedImage xorImage = xorEditor.edit(image, image2);
					endTime = System.currentTimeMillis();
					dif1 = endTime - startTime;
					//trimit width si height catre WriterResult - pentru a sti si acolo cand termin cate un sfert de informatie
					int width = xorImage.getWidth();
		            int height = xorImage.getHeight();
		            out1.writeInt(width);
		            out1.writeInt(height);
		            int pixels = width * height;
		            int segmentSize = pixels / 4;
		            int count = 0;
		            /*---------Trimit pixel cu pixel prin Pipe si numar cand ajung la cate un sfert---------*/
		            for (int i = 0; i < height; i++) {
		                for (int j = 0; j < width; j++) {
		                    out1.writeInt(xorImage.getRGB(j, i)); //aici trimit pixel-ul
		                    count++; //sa stiu cati pixeli am trimis
		                    if (count % segmentSize == 0) { //daca am ajuns la un sfert
		                        System.out.println("Consumer trimite sfertul " + count / segmentSize + " din Imaginea XOR");
		                    }
		                }
		            }
		            out1.close();
					
					//incep procesarea AND
		            startTime = System.currentTimeMillis();
		            BufferedImage andImage = andEditor.edit(image, image2);
		            endTime = System.currentTimeMillis();
		            dif2 = endTime - startTime;
		            /*---------Trimit prin PIPE la fel ca la xor---------*/
					count = 0;
					for (int i = 0; i < height; i++) {
		                for (int j = 0; j < width; j++) {
		                    out2.writeInt(andImage.getRGB(j, i));
		                    count++;
		                    if (count % segmentSize == 0) {
		                    	System.out.println("Consumer trimite sfertul " + count / segmentSize + " din Imaginea AND");
		                    }
		                }
		            }
		            out2.close();
					
					//incep procesarea OR
		            startTime = System.currentTimeMillis();
		            BufferedImage orImage = orEditor.edit(image, image2);
		            endTime = System.currentTimeMillis();
		            dif3 = endTime - startTime;
		            /*---------Trimit prin PIPE la fel ca la xor si and---------*/
					count = 0;
					for (int i = 0; i < height; i++) {
		                for (int j = 0; j < width; j++) {
		                    out3.writeInt(orImage.getRGB(j, i));
		                    count++;
		                    if (count % segmentSize == 0) {
		                    	System.out.println("Consumer trimite sfertul " + count / segmentSize + " din Imaginea OR");
		                    }
		                }
		            }
		            out3.close();
					break; //ies din while(true) - am terminat
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("Consumatorul a preluat sfertul " + ct + " din imaginea Lenna");
			System.out.println("Consumatorul a preluat sfertul " + ct + " din imaginea Scarlett");
			ct++; //sa stiu la ce sfert am ajuns
			try {
				sleep(1000); //pentru a evidentia etapele comunicarii
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Eroare: Thread-ul Consumator a fost intrerupt.");
			}
		}
	}
}
