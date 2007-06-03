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
 * Color2GrayScale.java created in 02/03/2007 - 10:30:02 PM
 * by Thiago Nobrega ( thiagonobrega at gmail dot com )
 */
package jve.image.filters.statics.pontuals.color;

import java.awt.Color;
import java.awt.image.BufferedImage;

import jve.image.filters.FilterException;
import jve.image.filters.PointFilter;


public class Color2GrayScale implements PointFilter {

	private static String name = "Color 2 GrayScale";

	private static String group = "Pontual Color";

	private BufferedImage image1;

	public Color2GrayScale(BufferedImage image1) {
		this.image1 = image1;
	}

	public Color2GrayScale() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see image.filters.PointFilter#getGroup()
	 */
	public String getGroup() {
		return group;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see image.filters.PointFilter#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see image.filters.PointFilter#getNullConstant()
	 */
	public int getNullConstant() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see image.filters.PointFilter#process()
	 */
	public BufferedImage process() throws FilterException {
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

					int gs = ajusta(Math.round(b+r+g/3));

				saida.setRGB(c,l, new Color( gs , gs , gs ).getRGB());

			}// linhas
		}//colunas
		return saida;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see image.filters.PointFilter#setConstant(int)
	 */
	public void setConstant(int i) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see image.filters.PointFilter#setSecondImage(java.awt.image.BufferedImage)
	 */
	public void setSecondImage(BufferedImage second) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see image.filters.PointFilter#setWorkingImage(java.awt.image.BufferedImage)
	 */
	public void setWorkingImage(BufferedImage wimg) {
		image1 = wimg;

	}

	private int ajusta(int i){
		if ( i > 255 )
			return 255;
		if ( i < 0 )
			return 0;
		return i;
	}

}
