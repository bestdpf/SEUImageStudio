import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.*;
//seuImage
import seuImage.*;

public class ImageStudio extends JFrame {
private BufferedImage currentImage=null;
//file name
private String fileString=null;
//image label
//private JLabel imageLabel=null;

//bufferd image
private BufferedImage newImage;
private BufferedImage saveImage;

//history picture's pixs
private LinkedList<BufferedImage> imageStack=new LinkedList<BufferedImage>();
private LinkedList<BufferedImage> tempImageStack=new LinkedList<BufferedImage>();
//left image
private seuImage.ImagePanel leftImagePanel = new seuImage.ImagePanel();
//right image
private seuImage.ImagePanel rightImagePanel = new seuImage.ImagePanel();





public ImageStudio(String title){
    super(title);
	this.setSize(800,600);
    setExtendedState(Frame.MAXIMIZED_BOTH);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//container
	Container contentPane = getContentPane();
	Box box=Box.createHorizontalBox();
	box.add(leftImagePanel);
	box.add(rightImagePanel);
	contentPane.add(box);
    //create menubar
    JMenuBar jb=new JMenuBar();
    JMenu fileMenu=new JMenu("File");
    jb.add(fileMenu);
  
    JMenuItem openImageMenuItem=new JMenuItem("Open");
    fileMenu.add(openImageMenuItem);
    openImageMenuItem.addActionListener(new OpenListener());
	
	JMenuItem saveImageMenuItem=new JMenuItem("Save");
    fileMenu.add(saveImageMenuItem);
    saveImageMenuItem.addActionListener(new SaveListener());
	
    JMenuItem exitMenu=new JMenuItem("Exit");
    fileMenu.add(exitMenu);
    exitMenu.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
      System.exit(0);
     }
    });
	//nosie
	JMenu nosieMenu=new JMenu("Nosie");
	jb.add(nosieMenu);
	
	JMenuItem rbNosieMenuItem=new JMenuItem("rbNosie");
	nosieMenu.add(rbNosieMenuItem);
	rbNosieMenuItem.addActionListener(new rbNosieActionListener());
	
	JMenuItem randNosieMenuItem=new JMenuItem("randNosie");
	nosieMenu.add(randNosieMenuItem);
	randNosieMenuItem.addActionListener(new randNosieActionListener());
	
	JMenuItem loadSrcMenuItem=new JMenuItem("loadSrc");
	nosieMenu.add(loadSrcMenuItem);
	loadSrcMenuItem.addActionListener(new loadSrcActionListener());
	
    JMenu operateMenu=new JMenu("Pix Processing");
    jb.add(operateMenu);
  
    JMenuItem RGBtoGrayMenuItem=new JMenuItem("RGB2Gray");
    operateMenu.add(RGBtoGrayMenuItem);
    RGBtoGrayMenuItem.addActionListener(new RGBtoGrayActionListener());
  
	JMenuItem grayHistogramMenuItem=new JMenuItem("GrayHistogram");
    operateMenu.add(grayHistogramMenuItem);
    grayHistogramMenuItem.addActionListener(new GrayHistogramActionListener());
  
    JMenuItem balanceMenuItem=new JMenuItem("Equlization");
    operateMenu.add(balanceMenuItem);
    balanceMenuItem.addActionListener(new BalanceActionListener());
  
	JMenuItem horizontalMenuItem=new JMenuItem("horizonMirror");
    operateMenu.add(horizontalMenuItem);
    horizontalMenuItem.addActionListener(new HorizontalMirrorActionListener());
	
	JMenuItem verticialMenuItem=new JMenuItem("verticialMirror");
    operateMenu.add(verticialMenuItem);
    verticialMenuItem.addActionListener(new VerticialMirrorActionListener());
	
	JMenuItem cornerMenuItem=new JMenuItem("cornerMirror");
    operateMenu.add(cornerMenuItem);
    cornerMenuItem.addActionListener(new CornerMirrorActionListener());
	//inverse
	JMenuItem inverseMenuItem=new JMenuItem("Inverse");
    operateMenu.add(inverseMenuItem);
    inverseMenuItem.addActionListener(new InverseMirrorActionListener());
	//binary
	JMenuItem binaryMenuItem=new JMenuItem("Binary");
    operateMenu.add(binaryMenuItem);
    binaryMenuItem.addActionListener(new BinaryMirrorActionListener());
	
	JMenu filterMenu=new JMenu("Filter Processing");
	jb.add(filterMenu);
	JMenuItem blurItem=new JMenuItem("Blur");
	filterMenu.add(blurItem);
	blurItem.addActionListener(new BlurActionListener());
	JMenuItem sharpenItem=new JMenuItem("Sharpen");
	filterMenu.add(sharpenItem);
	sharpenItem.addActionListener(new SharpenActionListener());
	
	JMenuItem meanItem=new JMenuItem("Mean");
	filterMenu.add(meanItem);
	meanItem.addActionListener(new MeanActionListener());
	
	//mid
	JMenuItem midItem=new JMenuItem("Mid");
	filterMenu.add(midItem);
	midItem.addActionListener(new MidActionListener());
	//min
	JMenuItem minItem=new JMenuItem("Min");
	filterMenu.add(minItem);
	minItem.addActionListener(new MinActionListener());
	//max
	JMenuItem maxItem=new JMenuItem("Max");
	filterMenu.add(maxItem);
	maxItem.addActionListener(new MaxActionListener());
	//gauss
	//max
	JMenuItem gaussItem=new JMenuItem("Gauss");
	filterMenu.add(gaussItem);
	gaussItem.addActionListener(new GaussActionListener());
	//edge menu
	JMenu edgeMenu=new JMenu("Edge");
	jb.add(edgeMenu);
	JMenuItem prewitt=new JMenuItem("Prewitt");
	edgeMenu.add(prewitt);
	prewitt.addActionListener(new PrewittActionListener());
	
	JMenuItem sobel=new JMenuItem("Sobel");
	edgeMenu.add(sobel);
	sobel.addActionListener(new SobelActionListener());
	
	JMenuItem roberts=new JMenuItem("Roberts");
	edgeMenu.add(roberts);
	roberts.addActionListener(new RobertsActionListener());
	
	//trans men
	JMenu transMenu=new JMenu("Trans");
	jb.add(transMenu);
	JMenuItem fftMenu=new JMenuItem("DFT");
	transMenu.add(fftMenu);
	fftMenu.addActionListener(new DFTActionListener());

	JMenuItem dctMenu=new JMenuItem("DCT");
	transMenu.add(dctMenu);
	dctMenu.addActionListener(new DCTActionListener());

	JMenuItem testMenu=new JMenuItem("test");
	transMenu.add(testMenu);
	testMenu.addActionListener(new TestActionListener());
	//color menu
	JMenu colorMenu=new JMenu("Color");
	jb.add(colorMenu);
	JMenuItem color1Item=new JMenuItem("Color1");
	colorMenu.add(color1Item);
	color1Item.addActionListener(new Color1ActionListener());
	
	JMenuItem color2Item=new JMenuItem("Color2");
	colorMenu.add(color2Item);
	color2Item.addActionListener(new Color2ActionListener());
	
    JMenu frontAndBackMenu=new JMenu("History");
    jb.add(frontAndBackMenu);
  
    JMenuItem backMenuItem=new JMenuItem("Back");
    frontAndBackMenu.add(backMenuItem);
    backMenuItem.addActionListener(new BackActionListener());
  
    JMenuItem frontMenuItem=new JMenuItem("Foreward");
    frontAndBackMenu.add(frontMenuItem);
    frontMenuItem.addActionListener(new FrontActionListener());
	
	
	//about menu
	JMenu helpMenu=new JMenu("Help");
	jb.add(helpMenu);
	JMenuItem aboutItem=new JMenuItem("About");
	helpMenu.add(aboutItem);
	aboutItem.addActionListener(new AboutActionListener());
    this.setJMenuBar(jb);
  
    //imageLabel=new JLabel("");
    //JScrollPane pane = new    JScrollPane(imageLabel);
    //this.add(pane,BorderLayout.CENTER);
  
    this.setVisible(true);
  
}

private class OpenListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
     JFileChooser jc=new JFileChooser();
	 jc.setCurrentDirectory(new File(".")); 
	 jc.setFileFilter(new javax.swing.filechooser.FileFilter() {
				public boolean accept(File file) { //file type
					String name = file.getName().toLowerCase();
					return name.endsWith(".gif")
						|| name.endsWith(".jpg")
						|| name.endsWith(".jpeg")
						|| name.endsWith(".bmp")
						|| file.isDirectory();
				}
				public String getDescription() { //description
					return "picture files";
				}
			});
     int returnValue=jc.showOpenDialog(null);
     if (returnValue ==    JFileChooser.APPROVE_OPTION) {
      File selectedFile =    jc.getSelectedFile();                       
      if (selectedFile != null) {                          
       fileString=selectedFile.getAbsolutePath();
       try{
        newImage =ImageIO.read(new File(fileString));
		currentImage=newImage;
        imageStack.clear();
        tempImageStack.clear();
        imageStack.addLast(currentImage);
        leftImagePanel.loadImage(newImage);
      
       }catch(IOException ex){
        System.out.println(ex);
       }
     
      }                
     }
     ImageStudio.this.repaint();
    }
}

//save a file

private class SaveListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
     JFileChooser jc=new JFileChooser();
	 jc.setCurrentDirectory(new File(".")); 
	 jc.setFileFilter(new javax.swing.filechooser.FileFilter() {
				public boolean accept(File file) { //file type
					String name = file.getName().toLowerCase();
					return name.endsWith(".gif")
						|| name.endsWith(".jpg")
						|| name.endsWith(".jpeg")
						|| name.endsWith(".bmp")
						|| file.isDirectory();
				}
				public String getDescription() { //description
					return "picture files";
				}
			});
     int returnValue=jc.showSaveDialog(null);
     if (returnValue ==    JFileChooser.APPROVE_OPTION) {
      File selectedFile =    jc.getSelectedFile();                       
      if (selectedFile != null) {
		if(selectedFile.exists()){
		JOptionPane.showMessageDialog(null,"File exists!","Error",
        JOptionPane.INFORMATION_MESSAGE);
		}
		else{
       fileString=selectedFile.getAbsolutePath();
	   try{
		System.out.println(getExtensionName(selectedFile.getName()));
        ImageIO.write(currentImage,getExtensionName(selectedFile.getName()),new File(fileString));
       }catch(IOException ex){
        System.out.println(ex);
       }
     }
      }                
     }
     ImageStudio.this.repaint();
    }
}


//////////////////action listeners///////////
private class RGBtoGrayActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.RGBtoGray(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
  
}

private class BalanceActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.balance(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
  
}

private class BackActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     if(imageStack.size()<=2){
      JOptionPane.showMessageDialog(null,"No more back history","Attention",
        JOptionPane.INFORMATION_MESSAGE);
     }else{
      tempImageStack.addLast(imageStack.removeLast());
      currentImage=imageStack.getLast();
      showImage(currentImage,rightImagePanel);
     }
    }
  
}

private class FrontActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     if(tempImageStack.size()<1){
      JOptionPane.showMessageDialog(null,"No more fore history","Attention",
        JOptionPane.INFORMATION_MESSAGE);
     }else{
      currentImage=tempImageStack.removeFirst();
      imageStack.addLast(currentImage);
      showImage(currentImage,rightImagePanel);
     }
    }
  
}

//about
private class AboutActionListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      JOptionPane.showMessageDialog(null,"pengfeituan@gmail.com","Author",
        JOptionPane.INFORMATION_MESSAGE);
}
}

//show pic on picPanel 
private void showImage(Image pic,seuImage.ImagePanel picPanel){
	picPanel.loadImage(pic);
}

//flur
private class BlurActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.blur(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
  
}

//sharpen
private class SharpenActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.sharpen(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
  
}
//rbNosie
private class rbNosieActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.rbNosie(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
  
}
//randNosie
private class randNosieActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.randNosie(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
  
}
//horizontal mirror
private class HorizontalMirrorActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.horizontalMirror(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
  
}
//verticial mirror
private class VerticialMirrorActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.verticialMirror(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
  
}
//corner mirror
private class CornerMirrorActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.cornerMirror(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
  
}
//gram histogram
//corner mirror
private class GrayHistogramActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.grayHistogram(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
  
}
//get extension name
    public static String getExtensionName(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length() - 1))) {   
                return filename.substring(dot + 1);   
            }   
        }   
        return filename;   
    } 
//reload src
private class loadSrcActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     //BufferedImage resultImage=seuImage.process.randNosie(currentImage);
     imageStack.addLast(newImage);
     currentImage=newImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
  
}
//fft
private class DFTActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.FFT2D(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
	}
//dct
private class DCTActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.DCT2D(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
	}
//test
private class TestActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
      /*
     BufferedImage resultImage=seuImage.process.test(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
      */
     seuImage.process.test();
    }
	}
//color1
private class Color1ActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.color1(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
	}
	
	private class Color2ActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.color2(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
	}
	//prewitt
	private class PrewittActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.prewitt(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
	}
	//sobel
	private class SobelActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.sobel(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
	}
	//roberts
	private class RobertsActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.roberts(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
	}
	//mean filter
	private class MeanActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.mean(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
	}
	//mid filter
	private class MidActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.midFilter(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
	}
	//min filter
	private class MinActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.minFilter(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
	}
	//max filter
	private class MaxActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.maxFilter(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
	}
	//gauss filter
	private class GaussActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.gauss(currentImage,1.0);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
	}
	//inverse
	private class InverseMirrorActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.inverse(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
	}
	//binary
	private class BinaryMirrorActionListener implements ActionListener{

    public void actionPerformed(ActionEvent e){
     BufferedImage resultImage=seuImage.process.binary(currentImage);
     imageStack.addLast(resultImage);
     currentImage=resultImage;
     showImage(currentImage,rightImagePanel);
     tempImageStack.clear();
    }
	}
public static void main(String[] args) {
    new ImageStudio("SEU Image Studio");
}

}
