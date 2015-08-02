package com.ngexdesign.RasterImage;

import javax.swing.*;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;

import javax.imageio.*;

@SuppressWarnings({ "serial", "unused" })
public class RasterImageRunner extends JPanel
{
	public static BufferedImage bImage1, bImage2, finalImage1;

	public static Timer timer;

	public int c = 0;

	public RasterImageRunner ()
	{
		super();
		try 
		{                
			bImage1 = ImageIO.read(new File("image1.png"));
			bImage2 = ImageIO.read(new File("image2.png"));
		} 
		catch (IOException e)
		{
			//Not handled.
		}

		timer = new Timer(50, new RasterTimer());
		timer.start();
	}
	
	public void actionPerformed(ActionEvent ae) 
	{
		System.out.println("One");
	}

	public void paintComponent(Graphics g) 
	{
		g.drawImage(finalImage1, 0, 0, null);
		repaint();
	}

	public static void main(String [] args)
	{
		JFrame f = new JFrame("Window");
		f.getContentPane().add(new RasterImageRunner());
		f.setSize(460, 470);
		f.setVisible(true);
	}
	
	private class RasterTimer implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			// -------------- Image 1 -------------------
			int W1 = 472;
			int H1 = 408;
			int pixels1[] = new int[W1 * H1];

			PixelGrabber grabber = new PixelGrabber(bImage1, 0, 0, W1, H1, pixels1, 0, W1);
			try
			{
				grabber.grabPixels();
			}
			catch (InterruptedException e1) { }

			DataBufferInt buffer = new DataBufferInt(pixels1, (W1 * H1));

			int bitMask[] = new int[] { 0xff0000, 0xff00, 0xff, 0xff000000 };

			SinglePixelPackedSampleModel sampleModel = new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT, W1, H1, bitMask);

			Raster raster = Raster.createRaster(sampleModel, buffer, null);
			WritableRaster writeRaster = Raster.createWritableRaster(sampleModel, buffer, null);

			// -------------- Image 2 -------------------
			int W2 = 472;
			int H2 = 408;
			int pixels2[] = new int[W2 * H2];

			PixelGrabber grabber2 = new PixelGrabber(bImage2, 0, 0, W2, H2, pixels2, 0, W2);
			try
			{
				grabber2.grabPixels();
			}
			catch (InterruptedException e1) { }

			DataBufferInt buffer2 = new DataBufferInt(pixels2, (W2 * H1));

			int bitMask2[] = new int[] { 0xff0000, 0xff00, 0xff, 0xff000000 };

			SinglePixelPackedSampleModel sampleModel2 = new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT, W1, H1, bitMask);

			Raster raster2 = Raster.createRaster(sampleModel2, buffer2, null);
			WritableRaster writeRaster2 = Raster.createWritableRaster(sampleModel, buffer2, null);

			// -------------- Replacing Images -------------------
			for (int i=0; i<W1-200; i++)
			{
				for (int j=0; j<H1-200; j++)
				{
					writeRaster2.setPixel(c, j, pixels1);
				}
			}

			finalImage1 = new BufferedImage(ColorModel.getRGBdefault(), writeRaster2, false, null);
			c++;
		}
	}
}
