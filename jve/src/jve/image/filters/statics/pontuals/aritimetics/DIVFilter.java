package jve.image.filters.statics.pontuals.aritimetics;


import java.awt.Color;
import java.awt.image.BufferedImage;

import jve.image.filters.PointFilter;

public class DIVFilter implements PointFilter {

	private BufferedImage image1;
	private BufferedImage image2;
	private int constant = 1;

	private static String name = "Filtro Multiplicativo";
	private static String group = "Pontual Aritim�tico";

	public DIVFilter (BufferedImage i1,BufferedImage i2,int c) {
		image1 = i1;
		image2 = i2;
		constant = c;
	}

	public DIVFilter (BufferedImage i, int c) {
		this(i,null,c);
	}

	public DIVFilter (BufferedImage i1, BufferedImage i2) {
		this(i1,i2,1);
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

				int r0;
				int g0;
				int b0;

				if ( r1 == 0 || r2 == 0 || constant == 0)
					r0 = 0;
				else
					r0 = ajusta(r1/r2/constant);

				if ( g1 == 0 || g2 == 0 || constant == 0)
					g0 = 0;
				else
					g0 = ajusta(g1/g2/constant);

				if ( b1 == 0 || b2 == 0 || constant == 0)
					b0 = 0;
				else
					b0 = ajusta(b1/b2/constant);

				saida.setRGB(c,l, new Color(r0,g0,b0).getRGB());
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

	private int ajusta(double d){
		int i = (int) Math.round(d);

		if ( i > 255 )
			return 255;
		if ( i < 0 )
			return 0;
		return i;
	}

	public int getNullConstant() {
		return 1;
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
