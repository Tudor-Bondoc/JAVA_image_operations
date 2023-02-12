package packWork;

import java.awt.image.BufferedImage;

public class OrEditor implements ImageEditor{

	@Override
	public BufferedImage edit(BufferedImage image1, BufferedImage image2) {
		BufferedImage orImage = new BufferedImage(image2.getWidth(), image2.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < image1.getHeight(); ++y) {
	        for (int x = 0; x < image1.getWidth(); ++x) {
	           int pixelA = image1.getRGB(x, y);
	           int pixelB = image2.getRGB(x, y);
	           int pixelOR = pixelA | pixelB;
	           orImage.setRGB(x, y, pixelOR); //fac OR intre pixelii imaginilor sursa si salvez pixelul rezultat
	        }
	    }
		return orImage;
	}
}
