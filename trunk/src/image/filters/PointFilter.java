package image.filters;

import java.awt.image.BufferedImage;

/**
 *
 * @author Thiago N�brega <thiagonobrega@gmail.com>
 *
 */

public interface PointFilter extends Filter{

    /**
     * The main method from the PlugIn. What it will really do.
     * @throws FilterException
     */
    public BufferedImage process() throws FilterException;

    /**
     * A name to be shown of the filter
     * @return name of the filter
     */
    public String getName();

    public String getGroup();

	public void setConstant(int i);

	public int getNullConstant();

	public void setSecondImage(BufferedImage second);

}