/*
 * Created on May 7, 2005 , by Thiago N�brega
 *
 */
package image.filters.pontual.logical;

import image.filters.PointFilter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class IGSFilter implements PointFilter {
	private static String name = "Filtro IGS";
	private static String group = "Pontual L�gico";
	private BufferedImage image1;



	/**
	 * Build a filter that add a constant to one image
	 * @param image1
	 * @param cons
	 */
	public IGSFilter(BufferedImage image1 ){
		this.image1 = image1;
	}

	public BufferedImage process() {

		int linha = image1.getHeight();
		int coluna = image1.getWidth();

		System.out.println("C "+ coluna+"\nL "+linha+"\n");

		BufferedImage saida = new BufferedImage(coluna,linha,BufferedImage.TYPE_INT_RGB);

		Color color1;

		int b = 0;
		int r = 0;
		int g = 0;

		for ( int c = 0 ; c < coluna ; c++){
			for ( int l = 0 ; l < linha ; l++ ){
					color1 =  new Color(image1.getRGB(c,l));

					r = color1.getRed()  + (r & 25);
		            g = color1.getGreen() + (g & 25);
		            b = color1.getBlue() + (b & 25);



				saida.setRGB(c,l, new Color( r & 230 , g & 230 , b & 230 ).getRGB());

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

	}

}