/*
 * Created on May 7, 2005 , by Thiago N�brega
 *
 */
package jve.image.filters.statics.pontuals.logical;


import java.awt.Color;
import java.awt.image.BufferedImage;

import jve.image.filters.PointFilter;

public class ANDFilter implements PointFilter {
	private static String name = "Filtro AND";
	private static String group = "Pontual L�gico";
	private BufferedImage image1;
	private int and;



	/**
	 * Build a filter that add a constant to one image
	 * @param image1
	 * @param cons
	 */
	public ANDFilter(BufferedImage image1 , int andconstant ){
		this.and = andconstant;
		this.image1 = image1;
	}

	public BufferedImage process() {

		int linha = image1.getHeight();
		int coluna = image1.getWidth();

		BufferedImage saida = new BufferedImage(coluna,linha,BufferedImage.TYPE_INT_RGB);

		Color color1;

		int b;
		int r;
		int g;

		for ( int c = 0 ; c < coluna ; c++){
			for ( int l = 0 ; l < linha ; l++ ){
					color1 =  new Color(image1.getRGB(c,l));
					b = color1.getBlue() ;
					r = color1.getRed() ;
					g = color1.getGreen() ;

				saida.setRGB(c,l, new Color( r & and , g & and , b & and ).getRGB());

			}// linhas
		}//colunas
		return saida;
	}

	public String getName() {
		return name;
	}

	public String getGroup() {
		return group;
	}

	private int ajusta(int i){
		if ( i > 255 )
			return 255;
		if ( i < 0 )
			return 0;
		return i;
	}

	public int getNullConstant() {
		return 0;
	}

	public void setSecondImage(BufferedImage second) {
	}

	public void setConstant(int i) {
		this.and = i;

	}

	public void setWorkingImage(BufferedImage wimg) {
		image1 = wimg;

	}

}