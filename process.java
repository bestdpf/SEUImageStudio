//for picture processing function class
//from src buffered image to dst buffered image
package seuImage;

import java.lang.Math;
import java.awt.image.*;
import java.util.Arrays;
import seuImage.*;

public class process{ 
//////////////////get the pix array/////////
public static int[]getPixArray(BufferedImage im){
		int w=im.getWidth();
		int h=im.getHeight();
       int[] pix=new int[w*h];
       PixelGrabber pg=null;
       try{
         pg = new PixelGrabber(im, 0, 0, w, h, pix, 0, w);
         if(pg.grabPixels()!=true)
           try{
             throw new java.awt.AWTException("pg error"+pg.status());
           }catch(Exception eq){
                   eq.printStackTrace();
           }
       } catch(Exception ex){
               ex.printStackTrace();

       }
      return pix;
    }

   //dct 
public static BufferedImage DCT2D(BufferedImage srcImage){
      BufferedImage grayImage=RGBtoGray(srcImage);
      int w=srcImage.getWidth();
      int h=srcImage.getHeight();
      int imgType=srcImage.getType();
      int[]grayArray=getPixArray(grayImage);
      double[] dArray=new double[w*h];
	int[]destArray=new int[w*h];
      int i,j;
      int pixW=0;//row
     double pixH=0.0;//column 
	//double pW,pH;
      //seuImage.Complex[] mirror=new Complex[w*h];//to storage the output
      double[] row=new double[w];
      double[] column=new double[h];
      //Complex pixH;//column
      //seuImage.Complex[] column=new Complex[h];
      //seuImage.Complex fftCom=new seuImage.Complex();
      for(i=0;i<h;i++){//rows fft
	 for(j=0;j<w;j++){
	    pixW=0xff&grayArray[i*w+j];//get row vector
	    row[j]= (double)pixW;
	}
	 row=seuImage.Complex.DCT(row);
	 for(j=0;j<w;j++){
	    dArray[i*w+j]=row[j];
	}
	}
	//dump
	/*
	  for(i=0;i<h;i++)
	    for(j=0;j<w;j++){
		mirror[j*h+i].print();
	      }
	    */
	 for(j=0;j<w;j++){//columns fft
	 for(i=0;i<h;i++){
	    column[i]=dArray[j*h+i];//get column vector, reduce it to 1/sqrt(w);
	}
	 column=seuImage.Complex.DCT(column);
	 for(i=0;i<h;i++){
	    dArray[j*h+i]=column[i];
	}
	}
	for(i=0;i<h;i++)
	   for(j=0;j<w;j++){
	      double tmp=dArray[i*w+j];
		//int pix=(int)tmp;
	      int pix=(int)(tmp);
	      //System.out.printf("index: %d\t pix: %d\n",i*w+j,pix);
	      //mirror[i*w+j].print();
	      if(pix>255)pix=255;
	      if(pix<0)pix=0;
	      destArray[i*w+j]=0xff<<24|pix<<16|pix<<8|pix;
		//destArray[i*w+j]=pix;
	    }
	BufferedImage pic=new BufferedImage(w,h,imgType);
	pic.setRGB(0,0,w,h,destArray,0,w);
	return pic;
}

   //fft 
public static BufferedImage FFT2D(BufferedImage srcImage){
      BufferedImage grayImage=RGBtoGray(srcImage);
      int w=srcImage.getWidth();
      int h=srcImage.getHeight();
      int imgType=srcImage.getType();
      int[]grayArray=getPixArray(grayImage);
      int[]destArray=new int[w*h];
      int i,j;
      int pixW=0;//row
      //double pW,pH;
      seuImage.Complex[] mirror=new Complex[w*h];//to storage the output
      seuImage.Complex[] row=new Complex[w];
      Complex pixH;//column
      seuImage.Complex[] column=new Complex[h];
      //seuImage.Complex fftCom=new seuImage.Complex();
      for(i=0;i<h;i++){//rows fft
	 for(j=0;j<w;j++){
	    pixW=0xff&grayArray[i*w+j];//get row vector
	    row[j]= seuImage.Complex.int2Complex(pixW);
	}
	 row=seuImage.Complex.FFT(row);
	 for(j=0;j<w;j++){
	    mirror[i*w+j]=row[j];
	}
	}
	//dump
	/*
	  for(i=0;i<h;i++)
	    for(j=0;j<w;j++){
		mirror[j*h+i].print();
	      }
	    */
	 for(j=0;j<w;j++){//columns fft
	 for(i=0;i<h;i++){
	    pixH=mirror[j*h+i];//get column vector, reduce it to 1/sqrt(w);
	    //column[i]=new Complex(pixH.mul(new seuImage.Complex(1.0/Math.sqrt(w),0.0)));
	    column[i]= new Complex(pixH);
	}
	 column=seuImage.Complex.FFT(column);
	 for(i=0;i<h;i++){
	    mirror[j*h+i]=column[i];
	}
	}
	for(i=0;i<h;i++)
	   for(j=0;j<w;j++){
	      double tmp=mirror[i*w+j].length();
		//int pix=(int)tmp;
	      int pix=(int)(20.0*Math.log(tmp));
	      //System.out.printf("index: %d\t pix: %d\n",i*w+j,pix);
	      //mirror[i*w+j].print();
	      if(pix>255)pix=255;
	      if(pix<0)pix=0;
	      destArray[i*w+j]=0xff<<24|pix<<16|pix<<8|pix;
		//destArray[i*w+j]=pix;
	    }
	BufferedImage pic=new BufferedImage(w,h,imgType);
	pic.setRGB(0,0,w,h,destArray,0,w);
	return pic;
}
//test
public static void test(){
		Complex[] duan=new Complex[16];
		//Complex d=new Complex();
		//duan={Complex(1.0,0.0),Complex(2.0,0.0),Complex(3.0,0.0),Complex(4.0,0.0)};
		duan[0]=new Complex(1.0,2.0);
		duan[1]=new Complex(2.0,-3.0);
		duan[2]=new Complex(1.0,0.0);
		duan[3]=new Complex(1.0,0.0);
		duan[4]=new Complex(4.0,0.0);
		duan[5]=new Complex(3.0,0.0);
		duan[6]=new Complex(2.0,0.0);
		duan[7]=new Complex(1.0,0.0);
		duan[8]=new Complex(1.0,2.0);
		duan[9]=new Complex(2.0,-3.0);
		duan[10]=new Complex(1.0,1.0);
		duan[11]=new Complex(1.0,1.0);
		duan[12]=new Complex(4.0,4.0);
		duan[13]=new Complex(3.0,3.0);
		duan[14]=new Complex(2.0,2.0);
		duan[15]=new Complex(1.0,1.0);
		for(int i=0;i<duan.length;i++){
			System.out.printf("before FFT\nindex %d: \n%f +j*%f\n",i,duan[i].re,duan[i].im);
		}
		duan=seuImage.Complex.FFT(duan);
		for(int i=0;i<duan.length;i++){
			System.out.printf("index %d: \n%f +j*%f\n",i,duan[i].re,duan[i].im);
		}
}
//////////////////RGB2Gray///////////
public static BufferedImage RGBtoGray(BufferedImage srcImage){
	int imgType=srcImage.getType();
	int[]srcArray=getPixArray(srcImage);
	int w=srcImage.getWidth();
	int h=srcImage.getHeight();
    int[]grayArray=new int[h*w];
    ColorModel colorModel=ColorModel.getRGBdefault();
    int i ,j,k,r,g,b;
    for(i = 0; i < h;i++){
     for(j = 0;j < w;j++){
      k = i*w+j;  
      r = colorModel.getRed(srcArray[k]);
      g = colorModel.getGreen(srcArray[k]);
      b = colorModel.getBlue(srcArray[k]);
      int gray=(int)(r*0.3+g*0.59+b*0.11);
      r=g=b=gray;
      grayArray[i*w+j]=(255 << 24) | (r << 16) | (g << 8 )| b;
     }
    }
	BufferedImage pic=new BufferedImage(w,h,imgType);
	pic.setRGB(0,0,w,h,grayArray,0,w);
	return pic;
}
//inverse
public static BufferedImage inverse(BufferedImage srcImage){
	int imgType=srcImage.getType();
	int[]srcArray=getPixArray(srcImage);
	int w=srcImage.getWidth();
	int h=srcImage.getHeight();
    int[]destArray=new int[h*w];
    ColorModel colorModel=ColorModel.getRGBdefault();
    int i ,j,k,r,g,b;
    for(i = 0; i < h;i++){
     for(j = 0;j < w;j++){
      k = i*w+j;  
      r = 255-colorModel.getRed(srcArray[k]);
      g = 255-colorModel.getGreen(srcArray[k]);
      b = 255-colorModel.getBlue(srcArray[k]);
      //int gray=(int)(r*0.3+g*0.59+b*0.11);
      //r=g=b=gray;
      destArray[i*w+j]=(255 << 24) | (r << 16) | (g << 8 )| b;
     }
    }
	BufferedImage pic=new BufferedImage(w,h,imgType);
	pic.setRGB(0,0,w,h,destArray,0,w);
	return pic;
}

//binary
public static BufferedImage binary(BufferedImage srcImage){
	int imgType=srcImage.getType();
	int[]srcArray=getPixArray(srcImage);
	int w=srcImage.getWidth();
	int h=srcImage.getHeight();
    int[]grayArray=new int[h*w];
    ColorModel colorModel=ColorModel.getRGBdefault();
    int i ,j,k,r,g,b;
    for(i = 0; i < h;i++){
     for(j = 0;j < w;j++){
      k = i*w+j;  
      r = colorModel.getRed(srcArray[k]);
      g = colorModel.getGreen(srcArray[k]);
      b = colorModel.getBlue(srcArray[k]);
      int gray=(int)(r*0.3+g*0.59+b*0.11);
	  if(gray<128)gray=0;
	  else gray=255;
	  r=g=b=gray;
      grayArray[i*w+j]=(255 << 24) | (r << 16) | (g << 8 )| b;
     }
    }
	BufferedImage pic=new BufferedImage(w,h,imgType);
	pic.setRGB(0,0,w,h,grayArray,0,w);
	return pic;
}


////////////////balance///////////
public static BufferedImage balance(BufferedImage srcImage){
		int imgType=srcImage.getType();
		int[]srcArray=getPixArray(srcImage);
		int w=srcImage.getWidth();
		int h=srcImage.getHeight();
		int[] histogramR=new int[256];
		int[] histogramG=new int[256];
		int[] histogramB=new int[256];
		int[] dinPixArray=new int[w*h];
			
      for(int i=0;i<h;i++){
       for(int j=0;j<w;j++){
        int b=srcArray[i*w+j]&0xff;
		int g=(srcArray[i*w+j]&0x0000ff00)>>8;
		int r=(srcArray[i*w+j]&0x00ff0000)>>16;
        histogramB[b]++;
		histogramG[g]++;
		histogramR[r]++;
       }
      }
      double a=(double)255/(w*h);
      double[] cR=new double[256];
	  double[] cG=new double[256];
	  double[] cB=new double[256];
      cR[0]=(a*histogramR[0]);
	  cG[0]=(a*histogramG[0]);
	  cB[0]=(a*histogramB[0]);
      for(int i=1;i<256;i++){
       cR[i]=cR[i-1]+(int)(a*histogramR[i]);
	   cG[i]=cG[i-1]+(int)(a*histogramG[i]);
	   cB[i]=cB[i-1]+(int)(a*histogramB[i]);
      }
      for(int i=0;i<h;i++){
       for(int j=0;j<w;j++){
		int r=(srcArray[i*w+j]&0xff0000)>>16;
		int g=(srcArray[i*w+j]&0x00ff00)>>8;
        int b=srcArray[i*w+j]&0x0000ff;
        int histR=(int)cR[r];
		int histG=(int)cG[g];
		int histB=(int)cB[b];
        dinPixArray[i*w+j]=255<<24|histR<<16|histG<<8|histB;
       }
      }
     BufferedImage pic=new BufferedImage(w,h,imgType);
	pic.setRGB(0,0,w,h,dinPixArray,0,w);
	return pic;
	  }
	public static BufferedImage grayHistogram(BufferedImage srcImage){
		BufferedImage grayImage=RGBtoGray(srcImage);
		int imgType=grayImage.getType();
		int[]grayArray=getPixArray(grayImage);
		int w=grayImage.getWidth();
		int h=grayImage.getHeight();
		int[] histogramB=new int[256];
		int[] dinPixArray=new int[256*2*400];
			
      for(int i=0;i<h;i++){
       for(int j=0;j<w;j++){
        int b=grayArray[i*w+j]&0xff;
        histogramB[b]++;
       }
      }
	  for(int i=0;i<400;i++)
		for(int j=0;j<256*2;j++){
				dinPixArray[i*256*2+j]=0xffffffff;//set white background
		}
	  for(int i=0;i<256*2;i++,i++){
		double p=((double)(histogramB[i/2]))/((double)(w*h));
		p=p*30000;
		//System.out.println(p);
		histogramB[i/2]=(int)p;
		//System.out.println(histogramB[i]);
		for(int j=399;j>=0&&histogramB[i/2]>0;j--,histogramB[i/2]=histogramB[i/2]-1){
		dinPixArray[j*256*2+i]=0xff000000;//set black
		dinPixArray[j*256*2+i+1]=0xff000000;
		}
	  }
		BufferedImage pic=new BufferedImage(256*2,400,imgType);
		pic.setRGB(0,0,256*2,400,dinPixArray,0,256*2);
		return pic;
		}
	    //filter
	    public static BufferedImage applyFilter(BufferedImage srcImage,float[] data,int dataWidth,int dataHeight) {
			if (srcImage == null)
				return null; //return null if input is null
			Kernel kernel = new Kernel(dataWidth, dataHeight, data);  
			ConvolveOp imageOp=new ConvolveOp(kernel,ConvolveOp.EDGE_NO_OP, null);	//create op
			BufferedImage filteredBufImage = new BufferedImage(srcImage.getWidth(),srcImage.getHeight(),srcImage.getType());	//creat buffer image
			imageOp.filter(srcImage, filteredBufImage);//filter
			return filteredBufImage; 
		}
		//blur the image
	 	public static BufferedImage blur(BufferedImage srcImage) {
			if (srcImage == null)
				return null;
			float[] data = {
					0.0625f, 0.125f, 0.0625f,
					0.125f,	0.025f, 0.125f,
					0.0625f, 0.125f, 0.0625f 
			};
			return applyFilter(srcImage,data,3,3);
		}
		//sharpen the image
		public static BufferedImage sharpen(BufferedImage srcImage) {
			if (srcImage == null)
				return null;
			float[] data = { 
			        -1.0f, -1.0f, -1.0f,
			        -1.0f, 9.0f, -1.0f,
			        -1.0f, -1.0f, -1.0f 
			};
			return applyFilter(srcImage,data,3,3);
		}
		//mean
		public static BufferedImage mean(BufferedImage srcImage) {
			if (srcImage == null)
				return null;
			float[] data = { 
			        0.11f, 0.11f, 0.11f,
			        0.11f, 0.11f, 0.11f,
			        0.11f, 0.11f, 0.11f 
			};
			return applyFilter(srcImage,data,3,3);
		}
		//prewitt
		public static BufferedImage prewitt(BufferedImage srcImage) {
			if (srcImage == null)
				return null;
			float[] data1 = { 
			        1.0f, 1.0f, 1.0f,
			        0.0f, 0.0f, 0.0f,
			        -1.0f, -1.0f, -1.0f 
			};
			float[] data2 = {
					-1.0f,0.0f,1.0f,
					-1.0f,0.0f,1.0f,
					-1.0f,0.0f,1.0f
			};
			BufferedImage img1=applyFilter(srcImage,data1,3,3);
			BufferedImage img2=applyFilter(srcImage,data2,3,3);
			return max(img1,img2);
		}
		// max 
		public static BufferedImage max(BufferedImage img1,BufferedImage img2){
			int w=img1.getWidth();
			int h=img1.getHeight();
			int imgType=img1.getType();
			int[]src1=getPixArray(img1);
			int[]src2=getPixArray(img1);
			int[]dest=new int[w*h];
			ColorModel colorModel=ColorModel.getRGBdefault();
			int i,j,r,g,b,k;
			for(i=0;i<h;i++)
				for(j=0;j<w;j++){
					k=i*w+j;
					  r=Math.max(colorModel.getRed(src1[k]),colorModel.getRed(src2[k]));
					  g=Math.max(colorModel.getGreen(src1[k]),colorModel.getGreen(src2[k]));
					  b=Math.max(colorModel.getBlue(src1[k]),colorModel.getBlue(src2[k]));
					  dest[k]=0xff<<24|r<<16|g<<8|b;
				}
				BufferedImage pic=new BufferedImage(w,h,imgType);
				pic.setRGB(0,0,w,h,dest,0,w);
				return pic;
		}
		//average power
		public static BufferedImage averagePower(BufferedImage img1,BufferedImage img2){
			int w=img1.getWidth();
			int h=img1.getHeight();
			int imgType=img1.getType();
			int[]src1=getPixArray(img1);
			int[]src2=getPixArray(img1);
			int[]dest=new int[w*h];
			ColorModel colorModel=ColorModel.getRGBdefault();
			int i,j,r,g,b,k;
			for(i=0;i<h;i++)
				for(j=0;j<w;j++){
					k=i*w+j;
					  int r1=colorModel.getRed(src1[k]);
					  int r2=colorModel.getRed(src2[k]);
					  int g1=colorModel.getGreen(src1[k]);
					  int g2=colorModel.getGreen(src2[k]);
					  int b1=colorModel.getBlue(src1[k]);
					  int b2=colorModel.getBlue(src2[k]);
					  r=(int)Math.hypot(r1,r2);
					  g=(int)Math.hypot(g1,g2);
					  b=(int)Math.hypot(b1,b2);
					  if(r>255)r=255;
					  if(b>255)b=255;
					  if(g>255)g=255;
					  dest[k]=0xff<<24|r<<16|g<<8|b;
				}
				BufferedImage pic=new BufferedImage(w,h,imgType);
				pic.setRGB(0,0,w,h,dest,0,w);
				return pic;
		}
		//sobel
		public static BufferedImage sobel(BufferedImage srcImage) {
			if (srcImage == null)
				return null;
			float[] data1 = { 
			        -1.0f,0.0f, 1.0f,
			        -2.0f, 0.0f, 2.0f,
			        -1.0f, 0.0f, 1.0f 
			};
			float[] data2 = {
					1.0f,2.0f,1.0f,
					0.0f,0.0f,0.0f,
					-1.0f,-2.0f,-1.0f
			};
			BufferedImage img1=applyFilter(srcImage,data1,3,3);
			BufferedImage img2=applyFilter(srcImage,data2,3,3);
			return averagePower(img1,img2);
		}
		//roberts
		public static BufferedImage roberts(BufferedImage srcImage) {
			if (srcImage == null)
				return null;
			float[] data1 = { 
			        1.0f,0.0f,0.0f,-1.0f
			};
			float[] data2 = {
					0.0f,1.0f,-1.0f,0.0f
			};
			BufferedImage img1=applyFilter(srcImage,data1,2,2);
			BufferedImage img2=applyFilter(srcImage,data2,2,2);
			return averagePower(img1,img2);
		}
		//gauss filter
		public static BufferedImage gauss(BufferedImage srcImage,double sigma) {
			if (srcImage == null)
				return null;
			//make gauss kernel
			int center=(int)(3.0*sigma);
			float []kernel=new float[2*center+1];
			double sigma2=sigma*sigma;
			for(int i=0;i<kernel.length;i++){
				double r=center-i;
				kernel[i]=(float)Math.exp(-0.5*(r*r)/sigma2);
			}
			return applyFilter(srcImage,kernel,2*center+1,1);
		}
		//mid filter
		public static BufferedImage midFilter(BufferedImage srcImage){
			int imgType=srcImage.getType();
			int[]srcArray=getPixArray(srcImage);
			int w=srcImage.getWidth();
			int h=srcImage.getHeight();
			int[]destArray=new int[h*w];
			ColorModel colorModel=ColorModel.getRGBdefault();
			int i ,j,k,r,g,b;
			for(i = 1; i < h-1;i++){//no operation at borders
			for(j = 1;j < w-1;j++){
			int m,n;
			int[]rArray=new int[9];
			int[]gArray=new int[9];
			int[]bArray=new int[9];
			//push nums
			for(m=-1;m<2;m++)//3x3height
			for(n=-1;n<2;n++){//3x3 width
			k = (i+m)*w+(j+n);  
			r = colorModel.getRed(srcArray[k]);
			g = colorModel.getGreen(srcArray[k]);
			b = colorModel.getBlue(srcArray[k]);
			rArray[(m+1)*3+n+1]=r;
			gArray[(m+1)*3+n+1]=g;
			bArray[(m+1)*3+n+1]=b;
			}
			//list in order,big in front
			rArray=listInOrder(rArray);
			gArray=listInOrder(gArray);
			bArray=listInOrder(bArray);
			//set pix
			destArray[i*w+j]=0xff<<24|rArray[4]<<16|gArray[4]<<8|bArray[4];
     }
			}
			BufferedImage pic=new BufferedImage(w,h,imgType);
			pic.setRGB(0,0,w,h,destArray,0,w);
			return pic;
		}
		//max
			public static BufferedImage maxFilter(BufferedImage srcImage){
			int imgType=srcImage.getType();
			int[]srcArray=getPixArray(srcImage);
			int w=srcImage.getWidth();
			int h=srcImage.getHeight();
			int[]destArray=new int[h*w];
			ColorModel colorModel=ColorModel.getRGBdefault();
			int i ,j,k,r,g,b;
			for(i = 1; i < h-1;i++){//no operation at borders
			for(j = 1;j < w-1;j++){
			int m,n;
			int[]rArray=new int[9];
			int[]gArray=new int[9];
			int[]bArray=new int[9];
			//push nums
			for(m=-1;m<2;m++)//3x3height
			for(n=-1;n<2;n++){//3x3 width
			k = (i+m)*w+(j+n);  
			r = colorModel.getRed(srcArray[k]);
			g = colorModel.getGreen(srcArray[k]);
			b = colorModel.getBlue(srcArray[k]);
			rArray[(m+1)*3+n+1]=r;
			gArray[(m+1)*3+n+1]=g;
			bArray[(m+1)*3+n+1]=b;
			}
			//list in order,big in front
			rArray=listInOrder(rArray);
			gArray=listInOrder(gArray);
			bArray=listInOrder(bArray);
			//set pix
			destArray[i*w+j]=0xff<<24|rArray[0]<<16|gArray[0]<<8|bArray[0];
     }
			}
			BufferedImage pic=new BufferedImage(w,h,imgType);
			pic.setRGB(0,0,w,h,destArray,0,w);
			return pic;
		}
			//min
			public static BufferedImage minFilter(BufferedImage srcImage){
			int imgType=srcImage.getType();
			int[]srcArray=getPixArray(srcImage);
			int w=srcImage.getWidth();
			int h=srcImage.getHeight();
			int[]destArray=new int[h*w];
			ColorModel colorModel=ColorModel.getRGBdefault();
			int i ,j,k,r,g,b;
			for(i = 1; i < h-1;i++){//no operation at borders
			for(j = 1;j < w-1;j++){
			int m,n;
			int[]rArray=new int[9];
			int[]gArray=new int[9];
			int[]bArray=new int[9];
			//push nums
			for(m=-1;m<2;m++)//3x3height
			for(n=-1;n<2;n++){//3x3 width
			k = (i+m)*w+(j+n);  
			r = colorModel.getRed(srcArray[k]);
			g = colorModel.getGreen(srcArray[k]);
			b = colorModel.getBlue(srcArray[k]);
			rArray[(m+1)*3+n+1]=r;
			gArray[(m+1)*3+n+1]=g;
			bArray[(m+1)*3+n+1]=b;
			}
			//list in order,big in front
			rArray=listInOrder(rArray);
			gArray=listInOrder(gArray);
			bArray=listInOrder(bArray);
			//set pix
			destArray[i*w+j]=0xff<<24|rArray[8]<<16|gArray[8]<<8|bArray[8];
     }
			}
			BufferedImage pic=new BufferedImage(w,h,imgType);
			pic.setRGB(0,0,w,h,destArray,0,w);
			return pic;
		}
		//list in order
		public static int[] listInOrder(int[] array){
			int length=array.length;
			int i,tmp;
			boolean lastChanged=true;
			while(lastChanged){
				lastChanged=false;
				for(i=0;i<length-1;i++){
				if(array[i]<array[i+1]){
				//swap to make bigger in front
				tmp=array[i+1];
				array[i+1]=array[i];
				array[i]=tmp;
				lastChanged=true;
				}
				}
			}
			return array;
		}

		//noise
		public static BufferedImage rbNosie(BufferedImage srcImage){
			int imgType=srcImage.getType();
			int[]srcArray=getPixArray(srcImage);
			int w=srcImage.getWidth();
			int h=srcImage.getHeight();
			int i,j;
			double randomDouble1,randomDouble2;
			//int randomInt;
			for(i=0;i<h;i++)
				for(j=0;j<w;j++){
					randomDouble1=Math.random();
					if(randomDouble1<0.005){//the probility of nosie
					randomDouble2=Math.random();
					if(randomDouble2<0.5){
						srcArray[i*w+j]=0xff000000;
					}
					else{
						srcArray[i*w+j]=0xffffffff;
					}
					}
				}
				BufferedImage pic=new BufferedImage(w,h,imgType);
				pic.setRGB(0,0,w,h,srcArray,0,w);
				return pic;
		}
		//noise
		public static BufferedImage randNosie(BufferedImage srcImage){
			int imgType=srcImage.getType();
			int[]srcArray=getPixArray(srcImage);
			int w=srcImage.getWidth();
			int h=srcImage.getHeight();
			int i,j;
			double randomDouble1,randomDouble2;
			int randomInt;
			for(i=0;i<h;i++)
				for(j=0;j<w;j++){
					randomDouble1=Math.random();
					if(randomDouble1<0.005){//the probility of nosie
					randomDouble2=Math.random();
					randomInt=(int)(16777215*randomDouble2+0.5);
					//System.out.println(randomInt);
					srcArray[i*w+j]=(255<<24)|randomInt;
					}
				}
				BufferedImage pic=new BufferedImage(w,h,imgType);
				pic.setRGB(0,0,w,h,srcArray,0,w);
				return pic;
		}
//mirror horizontial
		public static BufferedImage horizontalMirror(BufferedImage srcImage){
			int imgType=srcImage.getType();
			int[]srcArray=getPixArray(srcImage);
			int w=srcImage.getWidth();
			int h=srcImage.getHeight();
			int[]destArray=new int[w*h];
			int i,j;
			double randomDouble1,randomDouble2;
			int randomInt;
			for(i=0;i<h;i++)
				for(j=0;j<w;j++){
					destArray[i*w+j]=srcArray[i*w+w-1-j];
				}
				BufferedImage pic=new BufferedImage(w,h,imgType);
				pic.setRGB(0,0,w,h,destArray,0,w);
				return pic;
		}
//mirror vertical
		public static BufferedImage verticialMirror(BufferedImage srcImage){
			int imgType=srcImage.getType();
			int[]srcArray=getPixArray(srcImage);
			int w=srcImage.getWidth();
			int h=srcImage.getHeight();
			int[]destArray=new int[w*h];
			int i,j;
			double randomDouble1,randomDouble2;
			int randomInt;
			for(i=0;i<h;i++)
				for(j=0;j<w;j++){
					destArray[i*w+j]=srcArray[(h-1-i)*w+j];
				}
				BufferedImage pic=new BufferedImage(w,h,imgType);
				pic.setRGB(0,0,w,h,destArray,0,w);
				return pic;
		}
//mirror corner
		public static BufferedImage cornerMirror(BufferedImage srcImage){
			int imgType=srcImage.getType();
			int[]srcArray=getPixArray(srcImage);
			int w=srcImage.getWidth();
			int h=srcImage.getHeight();
			int[]destArray=new int[w*h];
			int i,j;
			double randomDouble1,randomDouble2;
			int randomInt;
			for(i=0;i<h;i++)
				for(j=0;j<w;j++){
					destArray[i*w+j]=srcArray[(h-1-i)*w+(w-1-j)];
				}
				BufferedImage pic=new BufferedImage(w,h,imgType);
				pic.setRGB(0,0,w,h,destArray,0,w);
				return pic;
		}
		//pseudo color1
		public static BufferedImage color1(BufferedImage srcImage){
		BufferedImage grayImage=RGBtoGray(srcImage);
		int imgType=grayImage.getType();
		int[]grayArray=getPixArray(grayImage);
		int w=grayImage.getWidth();
		int h=grayImage.getHeight();
		int[]dinPixArray=new int[w*h];
		int i,j,r,g,b;
		for(i=0;i<h;i++)
			for(j=0;j<w;j++){
			b=grayArray[i*w+j]&0xff;
			if(b<64){
				b=255;
				g=4*b;
				r=0;
			}
			else if(b<128){
				b=(127-b)*4;
				g=255;
				r=0;
			}
			else if(b<192){
				b=0;
				g=255;
				r=(b-128)*4;
			}
			else{
				b=0;
				g=(255-b)*4;
				r=255;
			}
			dinPixArray[i*w+j]=127<<24|r<<16|g<<8|b;
			}
		BufferedImage pic=new BufferedImage(w,h,imgType);
		pic.setRGB(0,0,w,h,dinPixArray,0,w);
		return pic;
		}
		//psf2
		public static int psf2(int x){
			double x1=x;
			return (int)((5-x1/42.5)*x1);
		}
		//pseudo color2
		public static BufferedImage color2(BufferedImage srcImage){
		BufferedImage grayImage=RGBtoGray(srcImage);
		int imgType=grayImage.getType();
		int[]grayArray=getPixArray(grayImage);
		int w=grayImage.getWidth();
		int h=grayImage.getHeight();
		int[]dinPixArray=new int[w*h];
		int i,j,r,g,b;
		for(i=0;i<h;i++)
			for(j=0;j<w;j++){
			b=grayArray[i*w+j]&0xff;
			if(b<85){
				b=psf2(84-b);
				g=psf2(b);
				r=0;
			}
			else if(b<170){
				b=0;
				g=psf2(169-b);
				r=psf2(b-85);
			}
			else{
				b=psf2(b-170);
				g=0;
				r=psf2(255-b);
			}
			dinPixArray[i*w+j]=127<<24|r<<16|g<<8|b;
			}
		BufferedImage pic=new BufferedImage(w,h,imgType);
		pic.setRGB(0,0,w,h,dinPixArray,0,w);
		return pic;
		}
		//test public
		/*
		public static void main(String argv[]){
			int[]duan={1,2,3,4,5,6,7};
			duan=listInOrder(duan);
			System.out.println(Arrays.toString(duan));
		}
		*/
}
