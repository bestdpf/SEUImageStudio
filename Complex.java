//complex
package seuImage;
import java.lang.Math;

public class Complex extends Object{
	public double im;
	public double re;
	public  Complex(){};
	public  Complex(double r,double i){
		im=i;
		re=r;
	};
	public  Complex(Complex a){
		re=a.re;
		im=a.im;
	}
	//exp(jseta), seta is in radians
	public  Complex(double seta){
		re=Math.cos(seta);
		im=Math.sin(seta);
	}
	//convert integer to complex
	public static Complex int2Complex(int x){
		Complex ret=new Complex();
		ret.re=(double)x;
		ret.im=0.0;
		return ret;
	}
	//return WN(k),ie. exp(-k*j*(2*pi/N));
	public  static Complex wnk(int N,int k){
		Complex ret=new Complex();
		double kD=(double)k;
		double ND=(double)N;
		double seta=-1.0*kD*2.0*Math.PI/ND;
		ret =new Complex(seta);
		return ret;
	}
	//return length
	public  double length(){
		return Math.hypot(re,im);
	}
	//real part
	public double real(){
		 return re;
	}
	//img part
	public double image(){
		 return im;
	}
	//return ang
	public  double angle(){
		return Math.atan(im/re);
	}
	public  Complex add(Complex c){
		Complex ret=new Complex();
		ret.im=im+c.im;
		ret.re=re+c.re;
		return ret;
	}
	public  Complex minus(Complex c){
		Complex ret=new Complex();
		ret.im=im-c.im;
		ret.re=re-c.re;
		return ret;
	}
	
	public  Complex mul(Complex c){
		Complex ret=new Complex();
		ret.im=re*c.im+im*c.re;
		ret.re=re*c.re-im*c.im;
		return ret;
	}
	//FFT
	public static  Complex[] FFT(Complex[] input){
		//get log2(complexLength)
		int complexLength=input.length;
		int tmp,i,level=0,less=0,length=0;//level is fft level num,less is number of added number,length is real length
		//double id;
		for(i=0;i<32;i++){//suppose lagest level is 32 to avoid error
			tmp=pow2(i);
			if(tmp>=complexLength){
				length=pow2(i);
				level=i;
				less=tmp-complexLength;
				break;//stop
			}
		}
		Complex []newIn=new Complex[complexLength+less];
		Complex []output=new Complex[complexLength];
		//System.out.println("dump input[]");
		for(i=0;i<complexLength;i++){
			newIn[i]=input[i];
			//dump input[];
			//input[i].print();
		}
		for(;i<complexLength+less;i++){
			newIn[i]=new Complex(0.0,0.0);
		}
		//dump test
		/*
		for(i=0;i<complexLength;i++){
			System.out.println("dump newIn[]");
			newIn[i].print();
		}
		*/
		//System.out.printf("complexLength: %d\n",complexLength);
		//System.out.printf("level: %d\n",level);
		//fft flow
		int j,k,step;
		Complex com1=new Complex();
		Complex com2=new Complex();
		for(i=0;i<level;i++){//i is level, left to right
			for(j=0,k=0;j<length;/*see below for j*/){//j is the input order, up to down
					// x chart
					//set k
					step=length/pow2(i+1);
					//k=j>>(level-i);
					//k=bitRevise(k,level);
					//k=k+pow2(i);
					//System.out.printf("level: %d\tindex: %d\nstep: %d\tk: %d\nnumIndex: %d\t%d\n",i,j,step,k,j,j+step);
					/*newIn[j]*/com1=xChart(newIn[j],newIn[j+step],true,step,j);
					/*newIn[j+step]*/com2=xChart(newIn[j],newIn[j+step],false,step,j);
					newIn[j]=com1;
					newIn[j+step]=com2;
					j++;
					//k=k+pow2(i);
					//System.out.printf("j: %d\npow2(level-i-1): %d\n",j,pow2(level-i-1));
					if(j%step==0){j=j+step;//j jump if j==k*2^(length-i-1)
												//k=0;//and set k=0
												//System.out.printf("j: %d\n",j);
											}
			}
		}
		//rearrangement,bit revise
		//dump output
		//System.out.println("dump output");
		for(i=0;i<complexLength;i++){
		output[i]=newIn[bitRevise(i,level)];
		//System.out.printf("input index: %d\t out index: %d\n",i,bitRevise(i,level));
		//output[i].print();
		}
		return output;
		
	}
	//bit revise of an integer with bitWidth width
	public  static int bitRevise(int in,int bitWidth){
		int []bitArray =new int[bitWidth];
		int i;
		for(i=0;i<bitWidth;i++)bitArray[i]=0;//init
		for(i=0;true;i++){
			bitArray[i]=in%2;
			in/=2;
			if(in==0)break;
		}
		int ret=0;
		for(i=bitWidth-1;i>=0;i--){
			if(bitArray[i]!=0)ret+=pow2(bitWidth-1-i);
		}
		return ret;
	}
	//2^p;p>=0;
	public static  int pow2(int p){
		/*
		if(p<0)return -1;
		int ret=1;
		for(int i=0;i<p;i++)ret=ret*2;
		return ret;
		  */
		return 1<<p;
	}
	//x chart,in1=in1+in2;in2=(in1-in2)*Wn(k);
	public  static Complex xChart(Complex in1,Complex in2,boolean isFirst,int n,int k){
		Complex ret1,ret2;
		ret1=new Complex(in1.add(in2));
		ret2=new Complex(in1.minus(in2).mul(wnk(n,k)));
		  /*
		if(isFirst){
		System.out.printf("dump xChat: n: %d  k:  %d\n",n,k);
		in1.print();
		in2.print();
		ret1.print();
		ret2.print();
		System.out.println("ok");
		}*/
		if(isFirst)return ret1;
		else return ret2;
	}
	//dump
	public void print(){
		System.out.printf("%f +j*%f\n",re,im);
	}
      public static double[] DCT(double[]in){
	    int inLength=in.length;
		double [] out=new double[inLength];
	    Complex[] inCom=new Complex[inLength*2];
	    for(int i=0;i<inLength;i++){
			  inCom[i]=new Complex(in[i],0.0);
		  }
	    for(int i=inLength;i<2*inLength;i++){
			  inCom[i]=new Complex(0.0,0.0);
		  }
	    inCom=FFT(inCom);
	    out[0]=0;
	    for(int i=0;i<inLength;i++){
		      out[0]+=in[i];
		  }
	    double length=(double)inLength;
	    double k=Math.sqrt(2.0/length);
	    out[0]*=k/Math.sqrt(2.0);
	    for(int i=1;i<inLength;i++){
		    Complex tmpCom=new Complex();
		    tmpCom=wnk(4*inLength,i).mul(inCom[i]);
		    out[i]=k*tmpCom.length();
		  }
	    return out;
      }
	/*
	//test main
	public static void main(String argv[]){
		Complex[] duan=new Complex[8];
		Complex d=new Complex();
		//duan={Complex(1.0,0.0),Complex(2.0,0.0),Complex(3.0,0.0),Complex(4.0,0.0)};
		duan[0]=new Complex(1.0,2.0);
		duan[1]=new Complex(2.0,-3.0);
		duan[2]=new Complex(1.0,0.0);
		duan[3]=new Complex(1.0,0.0);
		duan[4]=new Complex(4.0,0.0);
		duan[5]=new Complex(3.0,0.0);
		duan[6]=new Complex(2.0,0.0);
		duan[7]=new Complex(1.0,0.0);
		for(int i=0;i<duan.length;i++){
			System.out.printf("before FFT\nindex %d: \n%f +j*%f\n",i,duan[i].re,duan[i].im);
		}
		duan=d.FFT(duan);
		for(int i=0;i<duan.length;i++){
			System.out.printf("index %d: \n%f +j*%f\n",i,duan[i].re,duan[i].im);
		}
	}
	*/
	
}
