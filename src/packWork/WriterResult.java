package packWork;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class WriterResult extends Thread {

	private DataInputStream in1, in2, in3; //cele 3 imagini procesate care vin din Consumer
	private final String fileNameXOR, fileNameAND, fileNameOR; //path-urile unde salvez cele 3 imagini destinatie

	/*---------Constructor WriterResult---------*/
	public WriterResult(DataInputStream in1, DataInputStream in2, DataInputStream in3, String fileNameXOR, String fileNameAND, String fileNameOR) {
		this.in1 = in1;
		this.in2 = in2;
		this.in3 = in3;
		this.fileNameXOR = fileNameXOR;
		this.fileNameAND = fileNameAND;
		this.fileNameOR = fileNameOR;
	}
	
	/*---------Metoda RUN---------*/
	@Override
	public void run() {
		try {
		int width = in1.readInt();
        int height = in1.readInt(); //am extras width si height - erau primele 2 in inputStream
        int pixels = width * height; //dimensiunea totala
        int segmentSize = pixels / 4; //un sfert din dimensiunea totala
        int count = 0;
        
        /*---------Primesc si scriu imaginea XOR---------*/
        BufferedImage xorImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                xorImage.setRGB(j, i, in1.readInt()); //populez xorImage cu pixelii veniti din fluxul 1
                count++;
                if (count % segmentSize == 0) { //verific daca am ajuns la un sfert
                    System.out.println("WriterResult a primit sfertul  " + count / segmentSize + " din imaginea XOR");
                }
            }
        }
        ImageIO.write(xorImage, "bmp", new File(fileNameXOR)); //scriu imaginea in fisier
        System.out.println("WriterResult a terminat de scris imaginea XOR");
        in1.close();
        
        /*---------Primesc si scriu imaginea AND---------*/
        count = 0;
        BufferedImage andImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                andImage.setRGB(j, i, in2.readInt());
                count++;
                if (count % segmentSize == 0) {
                	System.out.println("WriterResult a primit sfertul  " + count / segmentSize + " din imaginea AND");
                }
            }
        }
        ImageIO.write(andImage, "bmp", new File(fileNameAND));
        System.out.println("WriterResult a terminat de scris imaginea AND");
        in2.close();
        
        /*---------Primesc si scriu imaginea OR---------*/
        count = 0;
        BufferedImage orImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                orImage.setRGB(j, i, in3.readInt());
                count++;
                if (count % segmentSize == 0) {
                	System.out.println("WriterResult a primit sfertul  " + count / segmentSize + " din imaginea OR");
                }
            }
        }
        ImageIO.write(orImage, "bmp", new File(fileNameOR));
        System.out.println("WriterResult a terminat de scris imaginea OR");
        in3.close();
        
        
		} catch (IOException e) {
            e.printStackTrace();
        }
	}

}
