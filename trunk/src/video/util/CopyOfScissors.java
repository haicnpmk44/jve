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
 * Scissors.java created in 26/12/2006 - 10:32:42 AM
 * by Thiago Nobrega ( thiagonobrega at gmail dot com )
 */
package video.util;

import java.io.IOException;

import javax.media.Codec;
import javax.media.Control;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoProcessorException;
import javax.media.Owned;
import javax.media.Player;
import javax.media.Processor;
import javax.media.StopAtTimeEvent;
import javax.media.Time;
import javax.media.control.FramePositioningControl;
import javax.media.control.QualityControl;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.FileTypeDescriptor;

public class CopyOfScissors implements ControllerListener {

	// recolocar
	Object waitSync = new Object();

	boolean stateTransitionOK = true;

	public MediaLocator inML;

	public MediaLocator outML;

	public long start[];

	public long end[];

	public boolean frameMode;

	Processor p;

	// dd
	Object waitFileSync = new Object();

	boolean fileDone = false;

	boolean fileSuccess = true;

	public CopyOfScissors(MediaLocator inML, MediaLocator outML, long start[],
			long end[]) {
		this.inML = inML;
		this.outML = outML;
		this.start = start;
		this.end = end;
		frameMode = false;
	}

	public void cut() throws NoProcessorException, IOException {


		// fazer asserts
		ContentDescriptor cd = fileExtToCD(outML.getRemainder());

		p = Manager.createProcessor(inML);

		if (!waitForState(p, p.Configured)) {
			System.err.println("Failed to configure the processor.");
		}

		checkTrackFormats(p);

		if (!waitForState(p, p.Realized)) {
			System.err.println("Failed to realize the processor.");
		}

		setJPEGQuality(p, 0.5f);

		// Translate frame # into time.
		if (frameMode) {

			FramePositioningControl fpc = (FramePositioningControl) p
					.getControl("javax.media.control.FramePositioningControl");

			if (fpc == null)
				System.err.println("no fpc");
			else {
				Time t;

				for (int i = 0; i < start.length; i++) {

					t = fpc.mapFrameToTime((int) start[i]);

					if (t == FramePositioningControl.TIME_UNKNOWN) {
						fpc = null;
						break;
					} else
						start[i] = t.getNanoseconds();

					if (end[i] == Long.MAX_VALUE)
						continue;

					t = fpc.mapFrameToTime((int) end[i]);
					if (t == FramePositioningControl.TIME_UNKNOWN) {
						fpc = null;
						break;
					} else
						end[i] = t.getNanoseconds();
				}
			}
		} // fram2time if close

		// create the dataSource
		SuperScissorDataSource ds = new SuperScissorDataSource(p, inML, start, end);

		// processor to generate final output
		p = Manager.createProcessor(ds);

		//TODO EU COLOQUEI
		p.addControllerListener(this);

		// configuring de pf processor
		if (!waitForState(p, p.Configured)) {
			System.err.println("Failed to configure the processor.");
		}

		for (ContentDescriptor s : p.getSupportedContentDescriptors()) {
			if (s.toString().equalsIgnoreCase("QuickTime"))
				p.setContentDescriptor(s);

		}
		//p.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW));

		if (!waitForState(p, p.Prefetched)) {
			System.err.println("Failed to realize the processor.");
		}

		DataSink dsink;
		if ((dsink = createDataSink(p, outML)) == null) {
			System.err
					.println("Failed to create a DataSink for the given output MediaLocator: "
							+ outML);
		}

		// TODO REVER ISSO
		// dsink.addDataSinkListener(pf);

		fileDone = false;

		// OK, we can now start the actual concatenation.
		p.start();
		dsink.start();

		// Wait for EndOfStream event.
		waitForFileDone();

		// Cleanup.
		dsink.close();

	}

	/**
	 * Block until file writing is done.
	 */
	boolean waitForFileDone() {
		System.err.print("  ");
		synchronized (waitFileSync) {
			try {
				while (!fileDone) {
					waitFileSync.wait(1000);
					System.err.print(".");
				}
			} catch (Exception e) {
			}
		}
		System.err.println("");
		return fileSuccess;
	}

	/**
	 * Create the DataSink.
	 */
	DataSink createDataSink(Processor p, MediaLocator outML) {

		DataSink dsink;

		try {
			System.err.println("- Create DataSink for: " + outML);
			dsink = Manager.createDataSink(p.getDataOutput(), outML);
			dsink.open();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return dsink;
	}

	/**
	 * Convert a file name to a content type. The extension is parsed to
	 * determine the content type.
	 */
	public ContentDescriptor fileExtToCD(String name) {

		String ext;
		int p;

		// Extract the file extension.
		if ((p = name.lastIndexOf('.')) < 0)
			return null;

		ext = (name.substring(p + 1)).toLowerCase();

		String type;

		// Use the MimeManager to get the mime type from the file extension.
		if (ext.equals("mp3")) {
			type = FileTypeDescriptor.MPEG_AUDIO;
		} else {
			if ((type = com.sun.media.MimeManager.getMimeType(ext)) == null)
				return null;
			type = ContentDescriptor.mimeTypeToPackageName(type);
		}

		return new FileTypeDescriptor(type);
	}

	/**
	 * Transcode the MPEG audio to linear and video to JPEG so we can do the
	 * cutting.
	 */
	void checkTrackFormats(Processor p) {

		TrackControl tc[] = p.getTrackControls();
		VideoFormat mpgVideo = new VideoFormat(VideoFormat.MPEG);
		AudioFormat rawAudio = new AudioFormat(AudioFormat.LINEAR);

		for (int i = 0; i < tc.length; i++) {
			Format preferred = null;

			if (tc[i].getFormat().matches(mpgVideo)) {
				preferred = new VideoFormat(VideoFormat.JPEG);
			} else if (tc[i].getFormat() instanceof AudioFormat
					&& !tc[i].getFormat().matches(rawAudio)) {
				preferred = rawAudio;
			}

			if (preferred != null) {

				Format supported[] = tc[i].getSupportedFormats();
				Format selected = null;

				for (int j = 0; j < supported.length; j++) {
					if (supported[j].matches(preferred)) {
						selected = supported[j];
						break;
					}
				}

				if (selected != null) {
					tc[i].setFormat(selected);
				}

			}
		}
	}

	/**
	 * Setting the encoding quality to the specified value on the JPEG encoder.
	 * 0.5 is a good default.
	 */
	void setJPEGQuality(Player p, float val) {

		Control cs[] = p.getControls();
		QualityControl qc = null;
		VideoFormat jpegFmt = new VideoFormat(VideoFormat.JPEG);

		// Loop through the controls to find the Quality control for
		// the JPEG encoder.
		for (int i = 0; i < cs.length; i++) {

			if (cs[i] instanceof QualityControl && cs[i] instanceof Owned) {
				Object owner = ((Owned) cs[i]).getOwner();

				// Check to see if the owner is a Codec.
				// Then check for the output format.
				if (owner instanceof Codec) {
					Format fmts[] = ((Codec) owner)
							.getSupportedOutputFormats(null);
					for (int j = 0; j < fmts.length; j++) {
						if (fmts[j].matches(jpegFmt)) {
							qc = (QualityControl) cs[i];
							qc.setQuality(val);
							System.err.println("- Set quality to " + val
									+ " on " + qc);
							break;
						}
					}
				}
				if (qc != null)
					break;
			}
		}
	}

	/**
	 * Wait for processor
	 *
	 * @param p
	 * @param state
	 * @return
	 */
	boolean waitForState(Processor p, int state) {

		switch (state) {
		case Processor.Configured:
			p.configure();
			break;
		case Processor.Realized:
			p.realize();
			break;
		case Processor.Prefetched:
			p.prefetch();
			break;
		case Processor.Started:
			p.start();
			break;
		}
		synchronized (waitSync) {
			try {
				int s = p.getState();
				while (s != state && stateTransitionOK){
					s = p.getState();
					waitSync.wait(100);
				}
			} catch (Exception e) {
			}
		}
		return stateTransitionOK;
	}

	public void controllerUpdate(ControllerEvent event) {
		if (event instanceof EndOfMediaEvent){
			fileDone = true;
			p.stop();
			p.close();

		}
	}

}