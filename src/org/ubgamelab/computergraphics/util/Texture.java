package org.ubgamelab.computergraphics.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.newdawn.slick.util.ResourceLoader;

/**
 * Implementation based on Kevin Glass and Brian Matzon works (in SlickUtil
 * Library)
 * 
 * @author funbyte
 */
public class Texture {

	/** The colour model including alpha for the GL image */
	private ColorModel glAlphaColorModel = new ComponentColorModel(
			ColorSpace.getInstance(ColorSpace.CS_sRGB),
			new int[] { 8, 8, 8, 8 }, true, false,
			ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
	/** The colour model for the GL image */
	private ColorModel glColorModel = new ComponentColorModel(
			ColorSpace.getInstance(ColorSpace.CS_sRGB),
			new int[] { 8, 8, 8, 0 }, false, false, ComponentColorModel.OPAQUE,
			DataBuffer.TYPE_BYTE);

	private ByteBuffer imageData;
	
	private int width;
	
	private int height;

	/**
	 * Convert the buffered image to a texture
	 * 
	 * @param bufferedImage
	 *            The image to convert to a texture
	 * @return A buffer containing the data
	 */
	private ByteBuffer convertImageData(BufferedImage bufferedImage) {
		ByteBuffer imageBuffer;
		WritableRaster raster;
		BufferedImage texImage;

		int texWidth = bufferedImage.getWidth();
		int texHeight = bufferedImage.getHeight();

		// find the closest power of 2 for the width and height
		// of the produced texture
		/*while (texWidth < bufferedImage.getWidth()) {
			texWidth *= 2;
		}
		while (texHeight < bufferedImage.getHeight()) {
			texHeight *= 2;
		}*/
		

		// create a raster that can be used by OpenGL as a source
		// for a texture
		if (bufferedImage.getColorModel().hasAlpha()) {
			raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
					texWidth, texHeight, 4, null);
			texImage = new BufferedImage(glAlphaColorModel, raster, false,
					new Hashtable<String, Object>());
		} else {
			raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
					texWidth, texHeight, 3, null);
			texImage = new BufferedImage(glColorModel, raster, false,
					new Hashtable<String, Object>());
		}

		// copy the source image into the produced image
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -bufferedImage.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		bufferedImage = op.filter(bufferedImage, null);
		Graphics g = texImage.getGraphics();
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, texWidth, texHeight);
		g.drawImage(bufferedImage, 0, 0, null);

		// set width and height
		this.width = texWidth;
		this.height = texHeight;
		
		// build a byte buffer from the temporary image
		// that be used by OpenGL to produce a texture.
		byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer())
				.getData();
		imageBuffer = ByteBuffer.allocateDirect(data.length);
		imageBuffer.order(ByteOrder.nativeOrder());
		imageBuffer.put(data, 0, data.length);
		imageBuffer.flip();

		return imageBuffer;
	}

	public boolean load(String ref) {
		URL url = ResourceLoader.getResource(ref);
		
		if (url == null) {
			return false;
		}

		// due to an issue with ImageIO and mixed signed code
		// we are now using good old fashioned ImageIcon to load
		// images and the paint it on top of a new BufferedImage
		// Image img = new ImageIcon(url).getImage();
		// BufferedImage bufferedImage = new BufferedImage(img.getWidth(null),
		// img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		// Graphics g = bufferedImage.getGraphics();
		// g.drawImage(img, 0, 0, null);
		// g.dispose();
		
		try {
			BufferedImage bufferedImage = ImageIO.read(url);
			imageData = convertImageData(bufferedImage);
		} catch (IOException e) {
			return false;
		}

		return true;
	}
	
	/**
	 * Get Image Data 
	 * @return
	 */
	public ByteBuffer getImageData() {
		return imageData;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	
}
