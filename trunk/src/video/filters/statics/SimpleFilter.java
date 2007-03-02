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
 * SimpleFilter.java created in 02/03/2007 - 9:28:21 AM
 * by Thiago Nobrega ( thiagonobrega at gmail dot com )
 */
package video.filters.statics;

import image.filters.FilterException;
import image.filters.PointFilter;

import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;

import video.filters.StaticFilter;
import video.util.VideoWorker;
import engine.logging.LogUtil;

public class SimpleFilter extends StaticFilter {

	private Logger logger = LogUtil.getLog(VideoWorker.class);
	private PointFilter imageFilter;

	public SimpleFilter(BufferedImage img , PointFilter imgFilter ) {
		super(img);
		imageFilter = imgFilter;
	}

	@Override
	public BufferedImage frameProcess(BufferedImage actualImage) {
		imageFilter.setWorkingImage(actualImage);
		BufferedImage b = null;
		try {
			b = imageFilter.process();
		} catch (FilterException e) {
			logger.error("FilterExpetion was throw : \n " + e.getMessage());
			//FIXME: REVER ISSO
		}

		return b;
	}

}
