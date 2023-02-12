package packWork;

import java.awt.image.BufferedImage;

public class XorEditor implements ImageEditor{

	@Override
	public BufferedImage edit(BufferedImage image1, BufferedImage image2) {
		BufferedImage xorImage = new BufferedImage(image2.getWidth(), image2.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < image1.getHeight(); ++y) {
	        for (int x = 0; x < image1.getWidth(); ++x) {
	           int pixelA = image1.getRGB(x, y);
	           int pixelB = image2.getRGB(x, y);
	           int pixelXOR = pixelA ^ pixelB;
	           xorImage.setRGB(x, y, pixelXOR); //fac XOR intre pixelii imaginilor sursa si salvez pixelul rezultat
	        }
	    }
		return xorImage;
	}
}
