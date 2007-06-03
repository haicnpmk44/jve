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
 * PontualMotionDetectionFilter.java created in 08/01/2007 - 3:23:44 PM
 * by Thiago Nobrega ( thiagonobrega at gmail dot com )
 */
package jve.video.filters.motion;


import java.awt.Color;
import java.awt.image.BufferedImage;

import jve.video.filters.MotionFilter;


/**
 * This Filter compares pixel to pixel from the actual image whith the antecessor image
 * and paints the diferents pixel ( yellow is the default color )
 */

public class PontualMotionDetectionFilter extends MotionFilter {



	private int contourColor = Color.yellow.getRGB();

	public PontualMotionDetectionFilter(){
		super();
		this.contourColor = Color.yellow.getRGB();

	}

	public PontualMotionDetectionFilter(Color c){
		super();
		this.contourColor = c.getRGB();
	}

	/* (non-Javadoc)
	 * @see image.filters.motion.MotionFilter#frameProcess(java.awt.image.BufferedImage, java.awt.image.BufferedImage)
	 */
	@Override
	public BufferedImage frameProcess(BufferedImage actualImage,
			BufferedImage antecessorImage) {
		int linha = actualImage.getHeight();
		int coluna = actualImage.getWidth();

		BufferedImage saida = new BufferedImage(coluna,linha,BufferedImage.TYPE_INT_RGB);

		Color color1;
		int r,g,b;

		if ( antecessorImage != null ) {
			for ( int c = 0 ; c < coluna ; c++){
				for ( int l = 0 ; l < linha ; l++ ){

					if (actualImage.getRGB(c, l) != antecessorImage.getRGB(c, l))
						saida.setRGB(c,l, contourColor );
					else
						saida.setRGB(c, l, actualImage.getRGB(c, l));

				}// linhas
			}//colunas
		} else
			saida = actualImage;

		return saida;
	}

}
