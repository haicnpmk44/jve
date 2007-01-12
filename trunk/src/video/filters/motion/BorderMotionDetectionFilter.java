/*
 * JGVE - J Grid Video Editor.
 *
 * Copyright (c) 2007, Thiago Nóbrega
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 2
 * of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 *
 * BorderMotionDetectionFilter.java created in 08/01/2007 - 4:01:12 PM
 * by Thiago Nobrega ( thiagonobrega at gmail dot com )
 */
package video.filters.motion;

import image.util.Mask;

import java.awt.Color;
import java.awt.image.BufferedImage;

import video.filters.MotionFilter;


public class BorderMotionDetectionFilter extends MotionFilter {

	private int contourColor = Color.yellow.getRGB();

	private int constant;

	// mask
	private Mask mask;

	public BorderMotionDetectionFilter() {
		constant = 50;
		setName("Border Detection Motion Filter");
		contourColor = Color.yellow.getRGB();
		setUpMask();
	}

	public BorderMotionDetectionFilter(Color c) {
		constant = 50;
		setName("Border Detection Motion Filter");
		contourColor = c.getRGB();
		setUpMask();
	}

	/**
	 * Setup the kernel mask
	 */
	private void setUpMask() {
		Mask ma = new Mask(3);

		ma.setPoint(0, 0, -1);
		ma.setPoint(0, 1, -2);
		ma.setPoint(0, 2, -1);
		ma.setPoint(1, 0, 0);
		ma.setPoint(1, 1, 0);
		ma.setPoint(1, 2, 0);
		ma.setPoint(2, 0, 1);
		ma.setPoint(2, 1, 2);
		ma.setPoint(2, 2, 1);

		mask = ma;
	}

	/**
	 * Apply the mask in on image
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
	private int aplicaMascara(int c, int l, BufferedImage pic, Mask m) {

		int red = 0;
		int green = 0;
		int blue = 0;

		int n = Math.round(m.getOrdem() / 2);

		for (int o = -n; o < n; o++) {
			for (int i = -n; i < n; i++) {

				int imagemLinha = l + i;
				int imagemColuna = c + o;

				int mp = (int) Math.round(m.getPoint(n + i, n + o));

				Color cor = new Color(pic.getRGB(imagemColuna, imagemLinha));
				red = red + (cor.getRed() * mp);
				green = green + (cor.getGreen() * mp);
				blue = blue + (cor.getBlue() * mp);

			}// linha

		}// coluna

		return red + green + blue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see image.filters.motion.MotionFilter#frameProcess(java.awt.image.BufferedImage,
	 *      java.awt.image.BufferedImage)
	 */
	@Override
	public BufferedImage frameProcess(BufferedImage actualImage,
			BufferedImage antecessorImage) {

		int ordem = Math.round(mask.getOrdem() / 2);

		int linha = actualImage.getHeight();
		int coluna = actualImage.getWidth();

		BufferedImage saida = new BufferedImage(coluna, linha,
				BufferedImage.TYPE_INT_RGB);

		Color color1;
		int r, g, b;

		if (antecessorImage != null) {

			for (int c = 0; c < coluna; c++) {
				for (int l = 0; l < linha; l++) {

					if (c < ordem || c > (coluna - ordem) || l < ordem	|| l > (linha - ordem)) {
						saida.setRGB(c, l, actualImage.getRGB(c, l));
					} else {
						//put a thread to each aplicaMascara
						int delta1 = aplicaMascara(c, l, actualImage, mask);
						int delta2 = aplicaMascara(c, l, antecessorImage, mask);

						int var = Math.abs(delta1 - delta2) - constant;

						if (var <= 0)
							saida.setRGB(c, l, actualImage.getRGB(c, l));
						else
							saida.setRGB(c, l, contourColor);
					}

				}// linhas
			}// colunas
		} else
			saida = actualImage;

		return saida;
	}

	/**
	 * Ajusta a cor para ficar entre o intervalo
	 *
	 * @param l
	 *            a cor
	 * @return a nova cor
	 */
	public int ajusta(long l) {
		if (l > 255)
			return 255;
		if (l < 0)
			return 0;
		return (int) l;
	}

}
