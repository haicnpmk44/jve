package image.filters.statics.pontuals.aritimetics;

import image.filters.PointFilter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class MAXFilter implements PointFilter {

	private BufferedImage image1;
	private BufferedImage image2;
	private int constant;


	private static String name = "Filtro Multiplicativo";
	private static String group = "Pontual Aritim�tico";

	public MAXFilter (BufferedImage i, int RGB) {
		image1 = i;
		constant = RGB;
	}

	public MAXFilter (BufferedImage i1, BufferedImage i2) {
		image1 = i1;
		image2 = i2;
		constant = 0;
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


				if ( l < linesImage1 && c < columImage1 ){
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


				saida.setRGB(c,l, new Color(Math.max(Math.max(r1,r2),constant),Math.max(Math.max(g1,g2),constant),Math.max(Math.max(g1,g2),constant)).getRGB());

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

	public int getNullConstant() {
		return 0;
	}

	public void setSecondImage(BufferedImage second) {
		this.image2 = second;
	}

	public void setConstant(int i) {
		this.constant = i;

	}

	private int ajusta(int i){
		if ( i > 255 )
			return 255;
		if ( i < 0 )
			return 0;
		return i;
	}

}
