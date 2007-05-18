/*
 * Created on May 13, 2005 , by Thiago N�brega
 *
 */
package image.filters.statics.spatial.nolinear;

import image.filters.statics.spatials.SpatialFilter;
import image.util.Mask;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.TreeSet;

/**
 * Filtro da mediana este filtro calcula a media da area e faz um suaviza��o dos
 * contrastes diminuindo os contornos
 *
 *
 * @author Thiago N�brega <thiagonobrega@gmail.com>
 *
 */
public class MedianFilter extends SpatialFilter {

	/**
	 * Cria um um novo filtro da media
	 *
	 * @param m
	 * @param image
	 */
	public MedianFilter(Mask m, BufferedImage image) {
		super(m, image, "Median Filter");
	}

	/**
	 * Cria um novo filto da media com um mascara que todos os elementos s�o 1 e
	 * a mascara e de ordedm 3
	 *
	 * @param image
	 *
	 */
	public MedianFilter(BufferedImage image) {
		super(null, image, "Median Filter");
		Mask ma = new Mask(3);
		ma.setAll(1);
		this.setMask(ma);
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

		TreeSet<Integer> red = new TreeSet<Integer>();
		TreeSet<Integer> green = new TreeSet<Integer>();
		TreeSet<Integer> blue = new TreeSet<Integer>();

		int n = Math.round(m.getOrdem() / 2);

		for (int o = -n; o < n; o++) {
			for (int i = -n; i < n; i++) {

				int imagemLinha = l + i;
				int imagemColuna = c + o;

				Color cor = new Color(pic.getRGB(imagemColuna, imagemLinha));

				red.add(cor.getRed());
				green.add(cor.getGreen());
				blue.add(cor.getBlue());

			}// linha

		}// coluna

		Object[] r = red.toArray();
		Object[] g = green.toArray();
		Object[] b = blue.toArray();
		Color out = new Color(Integer.parseInt(r[Math.round(red.size() / 2)]
				.toString()), Integer.parseInt(g[Math.round(green.size() / 2)]
				.toString()), Integer.parseInt(b[Math.round(blue.size() / 2)]
				.toString()));

		return out;
	}

}