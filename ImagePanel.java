package seuImage;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import java.lang.Math;

public class ImagePanel extends JPanel {
		Image image;  
		BufferedImage bufImage;  
		BufferedImage originalBufImage;  
		Graphics2D g2D;  
		//null constuctor
		public ImagePanel(){
		//this.setSize(300,200);
		}
		//load
		public void loadImage(Image image) {
			originalBufImage = new BufferedImage(image.getWidth(this),image.getHeight(this),BufferedImage.TYPE_INT_ARGB);
			g2D = originalBufImage.createGraphics();  
			g2D.drawImage(image, 0, 0, this); 
			bufImage = originalBufImage;
			repaint();  
		}
	
		public void paint(Graphics g) {
			super.paintComponent(g);
			//paint bufImage
			int orginX=0,orginY=0;
			if (bufImage != null) {
				Graphics2D g2 = (Graphics2D) g;
				//g2.drawImage(bufImage,Math.max(0,(this.getWidth() - bufImage.getWidth()) / 2),	Math.max(0,(this.getHeight() - bufImage.getHeight()) / 2),this);
				g2.drawImage(bufImage,(this.getWidth() - bufImage.getWidth()) / 2,	(this.getHeight() - bufImage.getHeight()) / 2,this);
			}
		}
	}