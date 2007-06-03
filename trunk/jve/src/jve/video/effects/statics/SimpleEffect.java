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
 * SimpleFilter.java created in 02/03/2007 - 9:42:26 PM
 * by Thiago Nobrega ( thiagonobrega at gmail dot com )
 */
package jve.video.effects.statics;


import java.awt.image.BufferedImage;

import jve.engine.logging.LogUtil;
import jve.image.filters.FilterException;
import jve.image.filters.PointFilter;
import jve.video.effects.VideoEffect;

import org.apache.log4j.Logger;


public class SimpleEffect extends VideoEffect {

	private Logger logger = LogUtil.getLog(SimpleEffect.class);
	private PointFilter imageFilter;

	public SimpleEffect(PointFilter imgFilter ){
		super();
		imageFilter = imgFilter;
	}

	public SimpleEffect(){
		super();

	}

	public void setImageFilter(PointFilter p){
		imageFilter = p;
	}


	/* (non-Javadoc)
	 * @see video.effects.VideoEffect#getEffectName()
	 */
	@Override
	public String getEffectName() {
		return "SimpleEffect";
	}

	/* (non-Javadoc)
	 * @see video.effects.VideoEffect#processImage(java.awt.image.BufferedImage)
	 */
	@Override
	protected BufferedImage processImage(BufferedImage img) {
		BufferedImage b = null;
		try {
			imageFilter.setWorkingImage(img);
			b = imageFilter.process();
		} catch (FilterException e) {
			logger.error("FilterExpetion was throw : \n " + e.getMessage());
			//FIXME: REVER ISSO
		}
		return b;
	}

}
