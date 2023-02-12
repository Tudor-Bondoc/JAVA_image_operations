package packWork;

import java.awt.image.BufferedImage;

/*---------Interfata pentru aplicarea unei Binary Operation intre 2 imagini---------*/
/*---------Este implementata de XorEditor, AndEditor si OrEditor---------*/

public interface ImageEditor {
	
	public BufferedImage edit(BufferedImage image1, BufferedImage image2);
	
}
