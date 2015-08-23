package com.ngexdesign.RasterImage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RasterImage 
{
	public enum SwipeType
	{
		HORIZONTAL,
		VERTICAL,
		SWEEP;
	}

	public static BufferedImage bImage1, bImage2, finalImage1;
	private int width, height;

	public RasterImage(int width, int height)
	{
		super();
		this.width = width;
		this.height = height;
		try 
		{                
			bImage1 = ImageIO.read(new File("image1.png"));
			bImage2 = ImageIO.read(new File("image2.png"));
		} 
		catch (IOException e)
		{
			//Not handled.
		}
	}

	public BufferedImage generateRotatingSweepImage(double angle)
	{
		BufferedImage bImg = new BufferedImage(bImage1.getWidth(), bImage1.getHeight(), bImage1.getType());
		
		for(int i=0; i<bImg.getWidth(); i++) 
        {
            for(int j=0; j<bImg.getHeight(); j++) 
            {
                // Get pixel
            	BufferedImage source = getSourceImage(angle, i, j);
            	Color c;
            	if(source ==null)
            	{
            		c = Color.GREEN;
            	}
            	else c = new Color(source.getRGB(i,j));
                int newPixel = colorToRGB(c.getAlpha(), c.getRed(), c.getGreen(), c.getBlue());
                bImg.setRGB(i, j, newPixel); 
            }
        }
		return bImg;
	}
	
	private BufferedImage getSourceImage(double angle, int i, int j)
    {
	    double deltaJ = bImage1.getHeight() - j;
	    double deltaI = i - bImage1.getWidth() / 2;
	    
	    double targetAngle = Math.atan2(deltaJ, deltaI);
	    
	    if (Math.abs(targetAngle - angle) <= Math.PI/180 / 5)
	    {
	    	return null;
	    }
	    else if (targetAngle - angle > Math.PI/180 / 5)
	    {
	    	return bImage1;
	    }
	    
	    else return bImage2;
    }

	// Convert R, G, B, Alpha to standard 8 bit
    private static int colorToRGB(int alpha, int red, int green, int blue) {
 
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
 
        return newPixel;
 
    }
	
	public BufferedImage generateMagicImage(SwipeType swipeType, double parameter)
	{
		switch(swipeType)
		{
		case HORIZONTAL:
		case VERTICAL:
			return generateSwipeImage(swipeType, (int)parameter);
		case SWEEP:
			return generateRotatingSweepImage(parameter);
		}
		return null;
	}
	
	
	public BufferedImage generateSwipeImage(SwipeType swipeType, int boundary) 
	{
		if (swipeType == SwipeType.HORIZONTAL)
		{
			// -------------- Image 1 as Background -------------------
			int arrSize = bImage1.getWidth() * bImage1.getHeight();
			int pixels1[] = new int[arrSize];
			PixelGrabber grabber = new PixelGrabber(bImage1, 0, 0, bImage1.getWidth(), bImage1.getHeight(), pixels1, 0, bImage1.getWidth());
			try
			{
				grabber.grabPixels();
			}
			catch (InterruptedException e1) { }
			DataBufferInt buffer = new DataBufferInt(pixels1, arrSize);
			int bitMask[] = new int[] { 0xff0000, 0xff00, 0xff, 0xff000000 };
			SinglePixelPackedSampleModel sampleModel = new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT, bImage1.getWidth(), bImage1.getHeight(), bitMask);
			WritableRaster writeRaster = Raster.createWritableRaster(sampleModel, buffer, null);

			// -------------- Image 2 ------------------
			int pixels2[] = new int[boundary * bImage2.getHeight()];
			PixelGrabber grabber2 = new PixelGrabber(bImage2, 0, 0, boundary, bImage2.getHeight(), pixels2, 0, boundary);
			try
			{
				grabber2.grabPixels();
			}
			catch (InterruptedException e1) { }
			DataBufferInt buffer2 = new DataBufferInt(pixels2, (boundary * bImage1.getHeight()));
			SinglePixelPackedSampleModel sampleModel2 = new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT, boundary, bImage1.getHeight(), bitMask);
			WritableRaster writeRaster2 = Raster.createWritableRaster(sampleModel2, buffer2, null);
			
			// Final Image
			int pixels3[] = new int[4*boundary * bImage2.getHeight()];
			writeRaster2.getPixels(0, 0, boundary, bImage2.getHeight(), pixels3);
			writeRaster.setPixels(0, 0, boundary, bImage2.getHeight(), pixels3);

			finalImage1 = new BufferedImage(ColorModel.getRGBdefault(), writeRaster, false, null);
			return finalImage1;
		}
		else return null;
	}

	public static BufferedImage rotateImage(BufferedImage src, double degrees) 
	{
		double radians = Math.toRadians(degrees);

		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();

		double sin = Math.abs(Math.sin(radians));
		double cos = Math.abs(Math.cos(radians));
		int newWidth = (int) Math.floor(srcWidth * cos + srcHeight * sin);
		int newHeight = (int) Math.floor(srcHeight * cos + srcWidth * sin);

		BufferedImage result = new BufferedImage(newWidth, newHeight, src.getType());
		Graphics2D g = result.createGraphics();
		g.translate((newWidth - srcWidth) / 2, (newHeight - srcHeight) / 2);
		g.rotate(radians, srcWidth / 2, srcHeight / 2);
		g.drawRenderedImage(src, null);

		return result;
	}

}
