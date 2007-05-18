package video.effects;
/**
 *
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
 * VideoEffect.java created in 24/12/2006 - 11:19:29 AM
 * @author Thiago Nóbrega ( thiagonobrega at gmail dot com )
 */
import image.filters.Filter;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Effect;
import javax.media.Format;
import javax.media.PlugIn;
import javax.media.ResourceUnavailableException;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;

import org.apache.log4j.Logger;

import video.util.VideoWorker;

import com.sun.media.codec.video.colorspace.JavaRGBConverter;

import engine.logging.LogUtil;

/**
 *
 * This is de basic Video Effect, All Video effect will extentds this
 *  class.
 *
 * VideoEffect.java created in 24/12/2006 - 1:25:14 PM
 * @author Thiago Nóbrega ( thiagonobrega at gmail dot com )
 */
public abstract class VideoEffect implements Effect {

	private Logger logger = LogUtil.getLog(VideoEffect.class);

	private String efxName;

	private Format inputFormat;
	private Format outputFormat;

	private Format[] inputFormats;
	private Format[] outputFormats;

	private Codec converterIn;
	private Codec converterOut;

	private BufferedImage bimg;

	private Buffer tempBuffer;

	public VideoEffect() {

		logger.debug("Video Efect created");

		inputFormats = new Format[] { new RGBFormat(null, Format.NOT_SPECIFIED,
				Format.byteArray, Format.NOT_SPECIFIED, 24, 3, 2, 1, 3,
				Format.NOT_SPECIFIED, Format.TRUE, Format.NOT_SPECIFIED) };

		outputFormats = new Format[] { new RGBFormat(null,
				Format.NOT_SPECIFIED, Format.byteArray, Format.NOT_SPECIFIED,
				24, 3, 2, 1, 3, Format.NOT_SPECIFIED, Format.TRUE,
				Format.NOT_SPECIFIED) };

		efxName = getEffectName();

		tempBuffer = new Buffer();
	}

	public int process(Buffer inBuffer, Buffer outBuffer) {

		// fill's the output buffer if it's empty
		if (outBuffer.getData() == null)
			outBuffer.setData(new byte[((RGBFormat) outputFormat).getMaxDataLength()]);

		// setup the output bnuffer with input buffer specs
		outBuffer.setLength(inBuffer.getLength());
		outBuffer.setFormat(outputFormat);
		outBuffer.setFlags(inBuffer.getFlags());
		outBuffer.setTimeStamp(inBuffer.getTimeStamp());
		outBuffer.setSequenceNumber(inBuffer.getSequenceNumber());

		//get the dimensions of the video
		Dimension sizeIn = ((RGBFormat) inBuffer.getFormat()).getSize();
		Dimension sizeOut = ((RGBFormat) outBuffer.getFormat()).getSize();

		//validate de dimensions of the buffers
		if (sizeIn.width != sizeOut.width || sizeIn.height != sizeOut.height)
			return BUFFER_PROCESSED_FAILED;

		byte[] inData = (byte[]) inBuffer.getData();
		byte[] outData = (byte[]) outBuffer.getData();

		if (inBuffer.isDiscard() || inData == null)
			return BUFFER_PROCESSED_FAILED;

		//magic starts here
		boolean ok = processFrame(inBuffer, outBuffer, sizeIn.width, sizeIn.height);

		if (ok)
			return BUFFER_PROCESSED_OK;
		else
			return BUFFER_PROCESSED_FAILED;
	}

	public boolean processFrame(Buffer inBuffer, Buffer outBuffer, int srcWidth, int srcHeight) {

		if (converterIn == null || converterOut == null) {
			setUpConverters((VideoFormat) inBuffer.getFormat(), srcWidth,
					srcHeight);
		}

		logger.info("Frame #"+inBuffer.getSequenceNumber()+" processed!");

		if (converterIn.process(inBuffer, tempBuffer) != PlugIn.BUFFER_PROCESSED_OK)
			return false;

		bimg.setRGB(0, 0, srcWidth, srcHeight,
				(int[]) tempBuffer.getData(), 0, srcWidth);

		//makes the magic whith one image
		BufferedImage timg = processImage(bimg);
		bimg = timg;


		bimg.getRGB(0, 0, srcWidth, srcHeight,
				(int[]) tempBuffer.getData(), 0, srcWidth);

		if (converterOut.process(tempBuffer, outBuffer) != PlugIn.BUFFER_PROCESSED_OK)
			return false;

		return true;
	}

	/**
	 * Setup the convertes
	 *
	 * @param format
	 * @param width
	 * @param height
	 */
	private void setUpConverters(VideoFormat format, int width,
			int height) {
		Dimension size = format.getSize();
		RGBFormat imgFormat = new RGBFormat(size, width * height,
				Format.intArray, -1, 32, 0x00FF0000, 0x0000FF00, 0x000000FF);

		JavaRGBConverter rgbCodec;

		rgbCodec = new JavaRGBConverter();
		rgbCodec.setInputFormat(format);
		rgbCodec.setOutputFormat(imgFormat);
		try {
			rgbCodec.open();
		} catch (Exception e) {
		}
		converterIn = rgbCodec;

		rgbCodec = new JavaRGBConverter();
		rgbCodec.setInputFormat(imgFormat);
		rgbCodec.setOutputFormat(format);
		try {
			rgbCodec.open();
		} catch (Exception e) {
		}
		converterOut = rgbCodec;

		bimg = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Process a individual Image
	 *
	 * @param img
	 * @return
	 */
	protected abstract BufferedImage processImage(BufferedImage img);

	/**
	 * Return the efect name
	 *
	 * @return name of the effect
	 */
	public abstract String getEffectName();

	/*
	 * by Andy Dyble
	 */
	Format matches(Format in, Format outs[]) {
		for (int i = 0; i < outs.length; i++) {
			if (in.matches(outs[i]))
				return outs[i];
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.media.Codec#getSupportedInputFormats()
	 */
	public Format[] getSupportedInputFormats() {
		return inputFormats;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.media.Codec#getSupportedOutputFormats(javax.media.Format)
	 */
	public Format[] getSupportedOutputFormats(Format input) {
		if (input == null) {
			return outputFormats;
		}

		if (matches(input, inputFormats) != null) {
			return new Format[] { outputFormats[0].intersects(input) };
		} else {
			return new Format[0];
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.media.Codec#setInputFormat(javax.media.Format)
	 */
	public Format setInputFormat(Format input) {
		inputFormat = input;
		return input;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.media.Codec#setOutputFormat(javax.media.Format)
	 */
	public Format setOutputFormat(Format output) {
		if (output == null || matches(output, outputFormats) == null)
			return null;

		RGBFormat incoming = (RGBFormat) output;

		Dimension size = incoming.getSize();
		int maxDataLength = incoming.getMaxDataLength();
		int lineStride = incoming.getLineStride();
		float frameRate = incoming.getFrameRate();
		int flipped = incoming.getFlipped();
		int endian = incoming.getEndian();

		if (size == null)
			return null;
		if (maxDataLength < size.width * size.height * 3)
			maxDataLength = size.width * size.height * 3;
		if (lineStride < size.width * 3)
			lineStride = size.width * 3;
		if (flipped != Format.FALSE)
			flipped = Format.FALSE;

		outputFormat = outputFormats[0].intersects(new RGBFormat(size,
				maxDataLength, null, frameRate, Format.NOT_SPECIFIED,
				Format.NOT_SPECIFIED, Format.NOT_SPECIFIED,
				Format.NOT_SPECIFIED, Format.NOT_SPECIFIED, lineStride,
				Format.NOT_SPECIFIED, Format.NOT_SPECIFIED));

		return outputFormat;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see javax.media.PlugIn#getName()
	 */
	public String getName() {
		return efxName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.media.Controls#getControl(java.lang.String)
	 */
	public Object getControl(String controlType) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.media.Controls#getControls()
	 */
	public Object[] getControls() {
		return null;
	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void open() throws ResourceUnavailableException {
		// TODO Auto-generated method stub

	}

	public void reset() {
		// TODO Auto-generated method stub
	}

}