/*
 *
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
 * MotionDetectFilter.java created in 07/01/2007 - 3:01:11 PM
 * @author Thiago Nóbrega ( thiagonobrega at gmail dot com )
 */
package jve.video.filters;


import java.awt.image.BufferedImage;

import jve.video.util.VideoFilter;
import jve.video.util.VideoFilterException;

public abstract class MotionFilter implements VideoFilter {


	protected static String name = "Generic Motion Filter";
	protected static String group = "Motion Filter";

	private BufferedImage workImage;
	private BufferedImage antecessor;

	/**
	 * Build a filter that add a constant to one image
	 * @param image1
	 * @param cons
	 */
	public MotionFilter(BufferedImage image1){
		this.workImage = image1;
	}

	public MotionFilter() {
	}

	/*
	 * (non-Javadoc)
	 * @see image.filters.Filter#process()
	 */
	public BufferedImage process() throws VideoFilterException {
		verifyFrameSize();
		BufferedImage out = frameProcess(workImage,antecessor);
		antecessor = workImage;
		return out;
	}

	/**
	 *
	 * @param actualImage
	 * @param antecessorImage
	 * @return
	 */
	public abstract BufferedImage frameProcess(BufferedImage actualImage , BufferedImage antecessorImage );

	private void verifyFrameSize() throws VideoFilterException {
		if ( antecessor != null && ( antecessor.getHeight() != workImage.getHeight() || antecessor.getWidth() != workImage.getWidth() ) )
			throw new VideoFilterException("Diferent images size");
	}


	/*
	 * Gets the filter name;
	 */
	public String getName() {
		return name;
	}

	/*
	 * Gets the filter groups
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * Sets the image that actual, to be processed.
	 * @param second
	 */
	public void setWorkingImage(BufferedImage second) {
		this.workImage = second;
	}

	public static void setGroup(String group) {
		MotionFilter.group = group;
	}

	public static void setName(String name) {
		MotionFilter.name = name;
	}

}