/*
 * Created on May 13, 2005 , by Thiago N�brega
 *
 */
package jve.image.filters.statics.spatial.nolinear;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import jve.image.filters.statics.spatials.SpatialFilter;
import jve.image.util.Mask;

/**
 * Filtro road
 *
 *
 *
 * @author Thiago N�brega <thiagonobrega@gmail.com>
 *
 */
public class RoadFilter extends SpatialFilter {

	/**
	 * Cria um um novo filtro da media
	 *
	 * @param m
	 * @param image
	 */
	public RoadFilter(BufferedImage image) {
		super(new Mask(3), image, "Road Filter");
	}

	/**
	 * Cria um novo filto da media com um mascara que todos os elementos s�o 1 e
	 * a mascara e de ordedm 3
	 *
	 * @param image
	 * @param mask
	 *            o tamnho da mascara
	 */
	public RoadFilter(BufferedImage image, int mask) {
		super(new Mask(mask), image, "Road Filter");

	}

	/**
	 * Processa a imagem retornanando uma nova imagem ja como as altera��es
	 *
	 * @return BufferedImage uma nova imagem
	 */
	public BufferedImage process() {

		int ordem = Math.round(this.getMask().getOrdem() / 2);
		BufferedImage im = this.getOriginalImage();
		BufferedImage out = new BufferedImage(im.getWidth(), im.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		int limiteC = im.getWidth() - ordem;
		int limiteL = im.getHeight() - ordem;

		for (int c = ordem; c < limiteC; c++) {
			for (int l = ordem; l < limiteL; l++) {
				Color cor = aplicaMascara(c, l, im, this.getMask());
				out.setRGB(c, l, cor.getRGB());
			}// linha
		}// coluna

		return out;
	}

	/**
	 * Aplica a mascara em um ponto da imagem
	 *
	 * @param l
	 *            a linha da imagem
	 * @param c
	 *            a coluna da imagem
	 * @param im
	 *            imagem
	 * @param m
	 *            a mascara
	 * @return o novo valor
	 */
	private Color aplicaMascara(int c, int l, BufferedImage pic, Mask m) {

		List<Integer> red = new LinkedList<Integer>();
		List<Integer> green = new LinkedList<Integer>();
		List<Integer> blue = new LinkedList<Integer>();

		int n = Math.round(m.getOrdem() / 2);

		for (int o = -n; o < n; o++) {
			for (int i = -n; i < n; i++) {

				int imagemLinha = l + i;
				int imagemColuna = c + o;

				Color central = new Color(pic.getRGB(c, l));
				Color cor = new Color(pic.getRGB(imagemColuna, imagemLinha));

				// if (imagemLinha!=l && imagemColuna!=c ){
				red.add(Math.abs(cor.getRed() - central.getRed()));
				green.add(Math.abs(cor.getGreen() - central.getGreen()));
				blue.add(Math.abs(cor.getBlue() - central.getBlue()));
				// }

			}// linha

		}// coluna
		/***********************************************************************
		 * for (int i : red ){ System.out.println(i); }
		 **********************************************************************/
		Object[] r = red.toArray();
		Object[] g = green.toArray();
		Object[] b = blue.toArray();

		Arrays.sort(r);
		Arrays.sort(g);
		Arrays.sort(b);

		int vermelho = 0;
		int verde = 0;
		int azul = 0;

		for (int i = 0; i < 4; i++) {

			vermelho += Integer.parseInt(r[i].toString());
			verde += Integer.parseInt(g[i].toString());
			azul += Integer.parseInt(b[i].toString());

		}

		Color out = new Color(ajusta(vermelho), ajusta(verde), ajusta(azul));

		return out;
	}

}