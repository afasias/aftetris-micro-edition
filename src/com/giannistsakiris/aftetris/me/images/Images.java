package com.giannistsakiris.aftetris.me.images;

import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.giannistsakiris.aftetris.me.math.MyDouble;

public class Images
{
	public static Image loadImage( String resource )
	{
		try
		{
			return Image.createImage(resource);
		}
		catch (IOException ex)
		{
			Image image = Image.createImage(64,64);
			Graphics g = image.getGraphics();
			g.setGrayScale(255);
			g.fillRect(0,0,image.getWidth(),image.getHeight());
			g.setGrayScale(0);
			g.drawString(resource,0,0,Graphics.TOP|Graphics.LEFT);
			return image;
		}
	}

	public static Image resizeImage( Image src, int width, int height )
	{
		Image dst = Image.createImage(width,height);
		imageCopyResampled(dst, src, 0, 0, 0, 0, width, height, src.getWidth(), src.getHeight());
		return dst;
	}

	public static void imageCopyResampled( Image dst, Image src, int dstX, int dstY, int srcX, int srcY, int dstW, int dstH, int srcW, int srcH)
	{
		int[] srcPixels = new int[srcW*srcH];
		src.getRGB( srcPixels, 0, srcW, srcX, srcY, srcW, srcH );
		
		int[] dstPixels = new int[dstW*dstH];
		
		MyDouble yStep = new MyDouble(srcH);
		yStep.divideBy(dstH);
		
		MyDouble xStep = new MyDouble(srcW);
		xStep.divideBy(dstW);
		
//		double yStep = (double)srcH / (double)dstH;
//		double xStep = (double)srcW / (double)dstW;
		
		MyDouble y1 = new MyDouble(0);
		MyDouble y2 = new MyDouble(yStep);
//		double y1 = 0.0;
//		double y2 = yStep;
		
		for (long y = 0; y < dstH; y++)
		{
			long iy1 = y1.longFloor();
//			int iy1 = (int)Math.floor(y1);
			if (iy1 < 0) iy1 = 0;

			long iy2 = y2.longCeil();
//			int iy2 = (int)Math.ceil(y2);
			if (iy2 > srcH)
				iy2 = srcH;
			
			MyDouble x1 = new MyDouble(0);
//			double x1 = 0.0;
			MyDouble x2 = new MyDouble(xStep);
//			double x2 = xStep;
			
			for (long x = 0; x < dstW; x++)
			{
				long ix1 = x1.longFloor();
//				int ix1 = (int)Math.floor(x1);
				if (ix1 < 0)
					ix1 = 0;
	
				long ix2 = x2.longCeil();
//				int ix2 = (int)Math.ceil(x2);
				if (ix2 > srcW)
					ix2 = srcW;
	
				MyDouble alpha = new MyDouble(0);
				MyDouble red = new MyDouble(0);
				MyDouble green = new MyDouble(0);
				MyDouble blue = new MyDouble(0);
//				double alpha = 0.0;
//				double red = 0.0;
//				double green = 0.0;
//				double blue = 0.0;

				MyDouble total = new MyDouble(0);
//				double total = 0.0;
				
				for (long i = iy1; i < iy2; i++)
				{
					MyDouble yportion = new MyDouble();
//					double yportion;
					
					if (i < y1.integerPart() && (i+1) > y1.integerPart())
					{
						yportion.assign(i);
						yportion.add(1);
						yportion.subtract(y1);
//						yportion = i+1-y1;
					}
					else if ((i+1) > y2.integerPart() && i < y2.integerPart())
					{
						yportion.assign(y2);
						yportion.subtract(i);
//						yportion = y2-i;
					}
					else
					{
						yportion.assign(1);
//						yportion = 1;
					}
					
					for (long k = ix1; k < ix2; k++)
					{
						MyDouble xportion = new MyDouble();
//						double xportion;
						
						if (k < x1.integerPart() && (k+1) > x1.integerPart())
						{
							xportion.assign(k);
							xportion.add(1);
							xportion.subtract(x1);
//							xportion = k+1-x1;
						}
						else if ((k+1) > x2.integerPart() && k < x2.integerPart())
						{
							xportion.assign(x2);
							xportion.subtract(k);
//							xportion = x2-k;
						}
						else
						{
							xportion.assign(1);
//							xportion = 1;
						}

						int color = srcPixels[(int)(k+i*srcW)];
						
						MyDouble portion = new MyDouble(xportion);
						portion.multiplyBy(yportion);
						
						MyDouble colorByte = new MyDouble();
						
						colorByte.assign((color>>24)&0xff);
						colorByte.multiplyBy(portion);
						alpha.add(colorByte);
						
						colorByte.assign((color>>16)&0xff);
						colorByte.multiplyBy(portion);
						red.add(colorByte);
						
						colorByte.assign((color>>8)&0xff);
						colorByte.multiplyBy(portion);
						green.add(colorByte);
						
						colorByte.assign(color&0xff);
						colorByte.multiplyBy(portion);
						blue.add(colorByte);
						
//						red += ((color>>16)&0xff)*xportion*yportion;
//						green += ((color>>8)&0xff)*xportion*yportion;
//						blue += (color&0xff)*xportion*yportion;
						
						total.add(portion);
//						total += xportion*yportion;
					}
				}
				
				alpha.divideBy(total);
				red.divideBy(total);
				green.divideBy(total);
				blue.divideBy(total);
//				alpha /= total;
//				red /= total;
//				green /= total;
//				blue /= total;
				
				dstPixels[(int)(x+y*dstW)] = (int)((alpha.integerPart()<<24)+(red.integerPart()<<16)+(green.integerPart()<<8)+blue.integerPart());

				x1.assign(x2);
				x2.add(xStep);
//				x1 = x2;
//				x2 += xStep;
			}
			
			y1.assign(y2);
			y2.add(yStep);
//			y1 = y2;
//			y2 += yStep;
		}
		
		Image image = Image.createRGBImage( dstPixels, dstW, dstH, false );
		dst.getGraphics().drawImage( image, dstX, dstY, Graphics.TOP|Graphics.LEFT );
	}
}
