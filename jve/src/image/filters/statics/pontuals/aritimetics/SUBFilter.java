/*
 * Created on May 7, 2005 , by Thiago N�brega
 *
 */
package image.filters.statics.pontuals.aritimetics;

import image.filters.PointFilter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class SUBFilter implements PointFilter {
	private static String name = "Filtro Subtra��o";
	private static String group = "Pontual Aritim�tico";
	private BufferedImage image1;
	private BufferedImage image2;
	private int constant;

	/**
	 * Build a filter tha subtract two images and one constant
	 *
	 * @param image1
	 * @param image2
	 * @param cons
	 */
	public SUBFilter(BufferedImage image1 , BufferedImage image2 , int cons ){
		this.image1 = image1;
		this.image2 = image2;
		this.constant = cons;
	}

	/**
	 * Build a filter that subtract two images
	 *
	 * @param image1
	 * @param image2
	 */
	public SUBFilter(BufferedImage image1 , BufferedImage image2){
		this.image1 = image1;
		this.image2 = image2;
		this.constant = 0;
	}

	/**
	 * Build a filter that sub a constant to one image
	 * @param image1
	 * @param cons
	 */
	public SUBFilter(BufferedImage image1 , int cons){
		this.image1 = image1;
		this.image2 = null;
		this.constant = cons;
	}

	public BufferedImage process() {

		int linesImage1 = image1.getHeight();
		int columImage1 = image1.getWidth();
		int linesImage2 = 0;
		int columsIMage2 = 0;

		if ( image2 != null ){
			linesImage2 = image2.getHeight();
			columsIMage2 = image2.getWidth();
		}

		int coluna = Math.max(columImage1,columsIMage2);
		int linha = Math.max(linesImage1,linesImage2);

		System.out.println("C "+ coluna+"\nL "+linha+"\n");

		BufferedImage saida = new BufferedImage(coluna,linha,BufferedImage.TYPE_INT_RGB);

		Color color1;
		int b1,b2;
		int r1,r2;
		int g1,g2;

		for ( int c = 0 ; c < coluna ; c++){
			for ( int l = 0 ; l < linha ; l++ ){

				if (  l < linesImage1  &&  c < columImage1){
					color1 =  new Color(image1.getRGB(c,l));
					b1 = color1.getBlue();
					r1 = color1.getRed();
					g1 = color1.getGreen();
				} else{
					b1 = 0;
					r1 = 0;
					g1 = 0;
				}

				if ( (l < linesImage2  &&  c < columsIMage2) && image2 != null ) {
					color1 =  new Color(image2.getRGB(c,l));
					b2 = color1.getBlue();
					r2 = color1.getRed();
					g2 = color1.getGreen();
				} else{
					b2 = 0;
					r2 = 0;
					g2 = 0;
				}

				saida.setRGB(c,l, new Color(ajusta(r1-r2-constant),ajusta(g1-g2-constant),ajusta(g1-g2-constant)).getRGB());
			}
		}

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
		this.image2 = second;
	}

	public void setConstant(int i) {
		this.constant = i;

	}

	public void setWorkingImage(BufferedImage wimg) {
		image1 = wimg;

	}
}