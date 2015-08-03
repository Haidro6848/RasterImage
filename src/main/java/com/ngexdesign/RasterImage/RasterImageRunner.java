package com.ngexdesign.RasterImage;

import javax.swing.*;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;

import javax.imageio.*;

import com.ngexdesign.RasterImage.RasterImage.SwipeType;

@SuppressWarnings({ "serial", "unused" })
public class RasterImageRunner extends JPanel
{
	private static int width = 472;
	private static int height = 408;
	
	private Timer timer;
	private int boundaryX = 1;
	private BufferedImage image;
	private RasterImage rasterImage;
	
	public RasterImageRunner ()
	{
		rasterImage = new RasterImage(width, height);
		timer = new Timer(20, new RasterTimer());
		timer.start();
	}

	public void paintComponent(Graphics g) 
	{
		g.drawImage(image, 0, 0, null);
		repaint();
	}

	public static void main(String [] args)
	{
		JFrame f = new JFrame("Window");
		f.getContentPane().add(new RasterImageRunner());
		f.setSize(width, height);
		f.setVisible(true);
	}
	
	private class RasterTimer implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			image = rasterImage.generateMagicImage(SwipeType.HORIZONTAL, boundaryX);
			boundaryX++;
		}
	}
}
