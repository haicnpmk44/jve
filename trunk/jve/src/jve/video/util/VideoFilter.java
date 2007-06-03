/*
 * Created on May 12, 2005 , by Thiago N�brega
 *
 */
package jve.video.util;

import java.awt.image.BufferedImage;

public interface VideoFilter {

	public BufferedImage process() throws VideoFilterException;

}
