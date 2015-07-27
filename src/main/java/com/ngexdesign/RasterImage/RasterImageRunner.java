package com.ngexdesign.RasterImage;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;


public class RasterImageRunner {
	
	protected JFrame mFrame;
	protected JPanel mPanel;
	protected JButton mButton;
	protected JLabel mLabel;
	
	static Timer timer;
	
	public RasterImageRunner()
	{
		this.createGUI();
	}

	private void createGUI() 
	{
		mFrame = new JFrame("RasterImage");
		mFrame.setVisible(true);
		mFrame.setSize(460, 470);
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mPanel = new JPanel();
		mPanel.setBackground(Color.WHITE);
		
		mButton = new JButton("Push");
		mLabel = new JLabel("Just a label");
		
		mPanel.add(mButton);
		mPanel.add(mLabel);
		
		mFrame.add(mPanel);
	}

	public static void main(String[] args) 
	{
		new RasterImageRunner();
		
		timer = new Timer(50, new RasterTimer());
	    timer.start();
	}

}

class RasterTimer implements ActionListener 
{
	public void actionPerformed(ActionEvent e) 
	{
		int i = 0;
		System.out.println(i + "asdf");
		i++;
	}
}

