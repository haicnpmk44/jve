/**
 * JGVE - J Grid Video Editor.
 *
 * Copyright (c) 2006, Thiago Nóbrega
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
 * TesteEffect.java created in 24/12/2006 - 3:56:12 PM
 * by Thiago Nobrega ( thiagonobrega at gmail dot com )
 */
package video.effects;

import image.filters.pontual.aritimetics.ADDFilter;
import image.filters.spatial.nolinear.RoadFilter;

import java.awt.image.BufferedImage;

/**
 * JGVE - J Grid Video Editor.
 *
 * Copyright (c) 2006, Thiago Nóbrega
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
 * TesteEffect.java created in 24/12/2006 - 3:56:12 PM
 * @author Thiago Nóbrega ( thiagonobrega at gmail dot com )
 */

public class TesteEffect extends VideoEffect {

	/* (non-Javadoc)
	 * @see video.effects.VideoEffect#getEffectName()
	 */
	@Override
	String getEffectName() {
		return "Efeito de Teste";
	}

	/* (non-Javadoc)
	 * @see video.effects.VideoEffect#processImage(java.awt.image.BufferedImage)
	 */
	@Override
	BufferedImage processImage(BufferedImage img) {
		BufferedImage b = new RoadFilter(img).process();
		//BufferedImage b = new DetectFilter(img).process();
		BufferedImage a = new ADDFilter(img,b).process();
		return a;

	}

}
