package packTest;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import packWork.Producer;
import packWork.WriterResult;
import packWork.Buffer;
import packWork.Consumer;

public class Test {

	public static void main(String[] args) throws IOException {
		
		/*---------Cele 2 buffer-uri pentru cele 2 imagini sursa---------*/
		Buffer buffer = new Buffer();
		Buffer buffer2 = new Buffer();
		/*---------Path-urile pentru fisierele sursa/destinatie---------*/
		String path1 = null;
		String path2 = null;
		String pathXOR = null;
		String pathAND = null;
		String pathOR = null;
		/*---------variabile pentru timpii de executie---------*/
		long startTime, endTime, dif1, dif2, dif3;
		/*---------Definirea input/output stream-urilor pentru comunicatia PIPES (3 imagini destinatie => 3 fluxuri)---------*/
		PipedOutputStream pipeOut1 = new PipedOutputStream();
		PipedInputStream pipeIn1 = new PipedInputStream(pipeOut1);
		DataOutputStream out1 = new DataOutputStream(pipeOut1);
		DataInputStream in1 = new DataInputStream(pipeIn1);
		
		PipedOutputStream pipeOut2 = new PipedOutputStream();
		PipedInputStream pipeIn2 = new PipedInputStream(pipeOut2);
		DataOutputStream out2 = new DataOutputStream(pipeOut2);
		DataInputStream in2 = new DataInputStream(pipeIn2);
		
		PipedOutputStream pipeOut3 = new PipedOutputStream();
		PipedInputStream pipeIn3 = new PipedInputStream(pipeOut3);
		DataOutputStream out3 = new DataOutputStream(pipeOut3);
		DataInputStream in3 = new DataInputStream(pipeIn3);
		/*---------Introudcerea path-urilor pt cele 2 imagini sursa si cele 3 imagini destinatie---------*/
		startTime = System.currentTimeMillis();
		System.out.println("Introduceti path-ul pentru prima imagine sursa: ");
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
		try {
			path1 = reader1.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("Introduceti path-ul pentru a doua imagine sursa: ");
		BufferedReader reader2 = new BufferedReader(new InputStreamReader(System.in));
		try {
			path2 = reader2.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("Introduceti path-ul pentru imaginea procesata XOR: ");
		BufferedReader reader3 = new BufferedReader(new InputStreamReader(System.in));
		try {
			pathXOR = reader3.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("Introduceti path-ul pentru imaginea procesata AND: ");
		BufferedReader reader4 = new BufferedReader(new InputStreamReader(System.in));
		try {
			pathAND = reader4.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("Introduceti path-ul pentru imaginea procesata OR: ");
		BufferedReader reader5 = new BufferedReader(new InputStreamReader(System.in));
		try {
			pathOR = reader5.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		endTime = System.currentTimeMillis();
		dif3 = endTime - startTime;
		/*---------Stocarea celor 2 imagini sursa in vectori de octeti ce urmeaza sa fie trimisi in bucati de 1/4 prin Producer Consumer---------*/
		byte[] dateImagine, dateImagine2;
		try {
		    BufferedImage imagine = ImageIO.read(new File(path1));
		    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    ImageIO.write(imagine, "bmp", outputStream);
		    dateImagine = outputStream.toByteArray();
		} catch (IOException e) {
		    e.printStackTrace();
		    dateImagine = null;
		}
		if (dateImagine==null)
			System.out.println("dateImagine este NULL!!!");
		
		try {
		    BufferedImage imagine2 = ImageIO.read(new File(path2));
		    ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
		    ImageIO.write(imagine2, "bmp", outputStream2);
		    dateImagine2 = outputStream2.toByteArray();
		} catch (IOException e) {
		    e.printStackTrace();
		    dateImagine2 = null;
		}
		if (dateImagine2==null)
			System.out.println("dateImagine2 este NULL!!!");
		
		/*---------Instantierea Producer, Consumer si WriterResult---------*/
		Producer producer = new Producer(buffer, buffer2, dateImagine, dateImagine2);
		Consumer consumer = new Consumer(buffer, buffer2, producer, out1, out2, out3);
		WriterResult writer = new WriterResult(in1, in2, in3, pathXOR, pathAND, pathOR);
		
		/*---------Start THREADS---------*/
		
		startTime = System.currentTimeMillis();
		producer.start();
		consumer.start();
		while(producer.isAlive()) {
			//astept ca Producer sa se termine
		}
		endTime = System.currentTimeMillis();
		dif1 = endTime - startTime;
		startTime = System.currentTimeMillis();
		writer.start();
		while(writer.isAlive()) {
			//astept ca writer sa se termine
		}
		endTime = System.currentTimeMillis();
		dif2 = endTime - startTime;
		System.out.println("Citirea path-urilor a durat " + dif3 + " milisecunde");
		System.out.println("Citirea prin Producer-Consumer a durat " + dif1 + " milisecunde");
		System.out.println("Procesarea imaginii XOR a durat " + consumer.dif1 + " milisecunde");
		System.out.println("Procesarea imaginii AND a durat " + consumer.dif2 + " milisecunde");
		System.out.println("Procesarea imaginii OR a durat " + consumer.dif3 + " milisecunde");
		System.out.println("Scrierea in fisier prin WriterResult a durat " + dif2 + " milisecunde");
	}
}
