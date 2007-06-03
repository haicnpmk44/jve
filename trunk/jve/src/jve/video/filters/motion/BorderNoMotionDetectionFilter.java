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
 * BorderNoMotionDetectionFilter.java created in 08/01/2007 - 4:01:12 PM
 * by Thiago Nobrega ( thiagonobrega at gmail dot com )
 */
package jve.video.filters.motion;


import java.awt.Color;
import java.awt.image.BufferedImage;

import jve.image.util.Mask;
import jve.video.effects.filter.MotionFilter;

/**
 * TODO describe the algorithm
 */

public class BorderNoMotionDetectionFilter extends MotionFilter {

	private int contourColor = Color.yellow.getRGB();

	private int constant;

	// mask
	private Mask mask;

	public BorderNoMotionDetectionFilter() {
		constant = 50;
		contourColor = Color.yellow.getRGB();
		setUpMask();
	}

	public BorderNoMotionDetectionFilter(Color c) {
		constant = 50;
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
							saida.setRGB(c, l, contourColor);
						else
							saida.setRGB(c, l, actualImage.getRGB(c, l));
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
