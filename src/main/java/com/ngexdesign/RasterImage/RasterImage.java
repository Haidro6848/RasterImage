package com.ngexdesign.RasterImage;

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
		VERTICAL;
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
	 
	public BufferedImage generateMagicImage(SwipeType swipeType, int boundary) 
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
			
			writeRaster.setPixels(0, 0, boundary, bImage2.getHeight(), sampleModel2.getPixels(0, 0, boundary, bImage2.getHeight(), pixels2, buffer2));
			
			finalImage1 = new BufferedImage(ColorModel.getRGBdefault(), writeRaster, false, null);
			return finalImage1;
		}
		else return null;
	}

}
