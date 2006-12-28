/*
 * Created on May 7, 2005 , by Thiago N�brega
 *
 */
package image.filters.pontual.logical;

import image.filters.PointFilter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ORFilter implements PointFilter {
	private static String name = "Filtro OR";
	private static String group = "Pontual L�gico";
	private BufferedImage image1;
	private int or;

	/**
	 * Build a filter tha add two images and one constant
	 *
	 * @param image1
	 * @param image2
	 * @param cons
	 */
	public ORFilter(BufferedImage image1, int orconstant ){
		this.or = orconstant;
		this.image1 = image1;
	}

	public BufferedImage process() {

		int linha = image1.getHeight();
		int coluna = image1.getWidth();




		System.out.println("C "+ coluna+"\nL "+linha+"\n");

		BufferedImage saida = new BufferedImage(coluna,linha,BufferedImage.TYPE_INT_RGB);

		Color color1;

		int b;
		int r;
		int g;

		for ( int c = 0 ; c < coluna ; c++){
			for ( int l = 0 ; l < linha ; l++ ){
					color1 =  new Color(image1.getRGB(c,l));
					b = color1.getBlue();
					r = color1.getRed();
					g = color1.getGreen();

				saida.setRGB(c,l, new Color( r | or , g | or , b | or ).getRGB());

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
		this.or = i;

	}

}