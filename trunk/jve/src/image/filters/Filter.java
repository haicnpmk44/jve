/*
 * Created on May 12, 2005 , by Thiago N�brega
 *
 */
package image.filters;

import java.awt.image.BufferedImage;

public interface Filter {

	public BufferedImage process() throws FilterException;

}
