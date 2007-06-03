/*
 * Created on May 9, 2005 , by Thiago N�brega
 *
 */
package jve.image.util;

import java.awt.Color;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ComponentColorModel;
import java.io.IOException;

public class ColorConvert {
	
	private static final String CMYKFILE = "/home/thiago/workspace/photocarpet/lib/icc/CMYK.pf";
	private static final int MAX_COLOR = 255;
	private static final int MIN_COLOR = 0;
	
	/**
	 * Converte um cor RGB para uma cor CMY , utilizando o algoritimo
	 * <p><bold>R = 1.0 - C</bold></p>
	 * <p><bold>G = 1.0 - M</bold></p>
	 * <p><bold></bold>B = 1.0 - Y</p>
	 * <p>Enconrado no livro de William K. Pratt , Digital Imagein Processing Third Edtion</p>
	 * @param in
	 * @return
	 */
	public static Color rgb2cmy(Color in){
		/**
		nt k = r > g ? r : g;
		k = k > b ? k : b;

		k = (255-k);

		int c = r != 0 ? ((255-r)-k) : 255;
		int m = g != 0 ? ((255-g)-k) : 255;
		int y = b != 0 ? ((255-b)-k) : 255;
		**/
		
		int newred = in.getRed(); // r = 1.0 - c 
		int newgreen = in.getGreen(); //m
		int newblue = in.getBlue(); //y		
		int k = Math.min(Math.min(newred,newgreen),newblue);
		
		int c = newred != 0 ? ((255-newred)-k) : 255;
		int m = newgreen != 0 ? ((255-newgreen)-k) : 255;
		int y = newblue != 0 ? ((255-newblue)-k) : 255;
		
		newred = MAX_COLOR - newred-k;
		newgreen = MAX_COLOR - newgreen -k;
		newblue = MAX_COLOR - newblue -k;
		return new Color(c,m,y,k);
		//return new Color(ajusta(newred),ajusta(newgreen),ajusta(newblue)); 
	}
	
	public static BufferedImage w(BufferedImage image, int k){
		int coluna = image.getWidth();
		int linha = image.getHeight();
		
		BufferedImage out = new BufferedImage(coluna,linha,BufferedImage.TYPE_INT_RGB );
		Color color;
		for ( int c = 0 ; c < coluna ; c++){
			for ( int l = 0 ; l < linha ; l++ ){		
				color =  new Color(image.getRGB(c,l));
				
				int r = color.getRed();
				int g = color.getGreen();
				int b =color.getBlue();
				
				int i = Integer.parseInt("" + ( (r+g+b)/3 ));
				
				int temp = r+g+b == 0 ? 1 : r+g+b;
				double s = 255 - ( ( 3 / (temp) ) * ( Math.min(Math.min(r,g),b) ) );
				
				double h = Math.acos( ( ( r-g ) + (r-b)/2 )/Math.sqrt(Math.pow((r-g),2) + (r-b)*(g-b)));
				r = (3*i + k) - ( g + b );
				g = (3*i + k) - ( r + b);
				b = (3*i + k) - (g + r);
				
				out.setRGB(c,l,new Color(ajusta(r),ajusta(g),ajusta(b)) .getRGB());				
				//.setRGB(c,l, rgb2cmy(color).getRGB());
				
			}// linhas			
		}//colunas
		
		return out;
	}
	
	
	
	public static BufferedImage t( BufferedImage image ) throws IOException{
		
		// carrega o cmyk profile
		ICC_Profile cmykProfile = ICC_Profile.getInstance(CMYKFILE);
		
		ICC_ColorSpace cmykColorSpace = new ICC_ColorSpace(cmykProfile);
		ColorConvertOp cmykConversion = new ColorConvertOp(image.getColorModel().getColorSpace(),
														   cmykColorSpace,	null);

		ComponentColorModel colorModel = new ComponentColorModel(cmykColorSpace,true,
											 //image.isAlphaPremultiplied(),
											 true,
											 image.getColorModel().getTransparency(),
											 image.getRaster().getDataBuffer().getDataType());
		//  dados da imagem da saida
		int coluna = image.getWidth();
		int linha = image.getHeight();
		
		BufferedImage out = cmykConversion.createCompatibleDestImage( image , colorModel );
		//BufferedImage compImage = cmykConversion.createCompatibleDestImage(image1, colorModel);
		//BufferedImage convertedImage = cmykConversion.filter(image1, temp);
		Color color;
		//BufferedImage o = new BufferedImage(coluna,linha, out.getColorModel());
		for ( int c = 0 ; c < coluna ; c++){
			for ( int l = 0 ; l < linha ; l++ ){		
				color =  new Color(image.getRGB(c,l));
				Color tc = new Color(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha()-100);
				
				out.setRGB(c,l,tc.getRGB());				
				//.setRGB(c,l, rgb2cmy(color).getRGB());
				
			}// linhas			
		}//colunas
		
		return out;
		
	}
	
	private static int ajusta(int i){
		if ( i > 255 )
			return 255;
		if ( i < 0 )
			return 0;
		return i;
	}


}
