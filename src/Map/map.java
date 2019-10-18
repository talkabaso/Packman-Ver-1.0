package Map;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import Geom.Point3D;

/**
 * this class load Ariel map picture and initialize upper left corner and lower right corner.
 * @author Tal and Aric
 */

public class map {

	Image image;
	int height; // pixel of the image
	int width; // pixel of the image
	Point3D start;
	Point3D end;

	// Constructor //
	public map() throws IOException {

		image= ImageIO.read(new File("Ariel.png"));
		width=image.getWidth(null);
		height=image.getHeight(null);
		start=new Point3D(32.105835, 35.202219,0);
		end=new Point3D(32.101923, 35.212451,0);
	}

	public Image getImage() {
		return image;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Point3D getStart() {
		return start;
	}

	public Point3D getEnd() {
		return end;
	}
}