package video.util;

import java.io.IOException;

import javax.media.Codec;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoDataSinkException;
import javax.media.NoProcessorException;
import javax.media.NotConfiguredError;
import javax.media.NotRealizedError;
import javax.media.Processor;
import javax.media.UnsupportedPlugInException;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;

import org.apache.log4j.Logger;

import video.effects.TesteEffect;
import engine.logging.LogUtil;

/**
 * Sample program to access individual video frames by using a "pass-thru"
 * codec. The codec is inserted into the data flow path. As data pass through
 * this codec, a callback is invoked for each frame of video data.
 */
public class VideoWorker implements ControllerListener {


	private Logger logger = LogUtil.getLog(VideoWorker.class);
	private MediaLocator in, out;
	private DataSink ds;
	private boolean fileDone;
	private Processor p;

	Object waitSync = new Object();
	Object waitFileSync = new Object();

	boolean stateTransitionOK = true;



	public VideoWorker(MediaLocator in, MediaLocator out) {
		this.in = in;
		this.out = out;
		this.fileDone = false;
	}

	public void open() throws NoProcessorException, IOException,
			UnsupportedPlugInException, NotConfiguredError, NotRealizedError, NoDataSinkException, SecurityException {

		p = Manager.createProcessor(in);
		initME(p);

		// Obtain the track controls.
		TrackControl videoTrack = extractVideoTrack();

		System.out.println("Video format: " + videoTrack.getFormat());

		// setup the codecs in the track
		Codec codec[] = { new TesteEffect() };
		videoTrack.setCodecChain(codec);

		// realize the processor
		if (!waitForState(p, p.Prefetched))
			throw new NotConfiguredError("Unable to prefetch the processor!");

		// creating the data sink
		ds = createDataSink(p,out);


		//TODO stop time, to stop the pĺayer
		//p.setStopTime(p.getDuration());

		// Start the processor.
		p.start();
		waitForFileDone();
		// STOPING PLAYER
		p.stop();
		p.close();
		// STOPING DATASINK
		ds.stop();
		ds.close();
	}

	/**
	 * Configure the processor<br>
	 * Set the contentDescripto to if avaliable , if not set to RAW
	 *
	 * @return
	 * @throws NotConfiguredError
	 */
	private void initME(Processor p) throws NotConfiguredError {

		if (!waitForState(p, p.Configured))
			throw new NotConfiguredError("Unable to configure the processor!");

		for (ContentDescriptor s : p.getSupportedContentDescriptors()) {
			 //System.out.println(s);
			if (s.toString().equalsIgnoreCase("AVI"))
				p.setContentDescriptor(s);
		}


		if (p.getContentDescriptor() == null)
			p.setContentDescriptor(new ContentDescriptor(
							ContentDescriptor.RAW));

		p.addControllerListener(this);
	}

	/**
	 * Set processor state and wait for the state
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
				while (s != state && stateTransitionOK) {
					s = p.getState();
					waitSync.wait(100);
				}
			} catch (Exception e) {
			}
		}
		return stateTransitionOK;
	}

	/**
	 * Extracts de Video Track Control
	 *
	 * @return the video track control
	 * @throws NotConfiguredError
	 */
	private TrackControl extractVideoTrack() throws NotConfiguredError {
		TrackControl tc[] = p.getTrackControls();

		TrackControl videoTrack = null;

		for (int i = 0; i < tc.length; i++) {
			if (tc[i].getFormat() instanceof VideoFormat) {
				videoTrack = tc[i];
			}
		}

		return videoTrack;
	}

	/**
	 * Extracts de Audio Track Control
	 *
	 * @return the audio track control
	 * @throws NotConfiguredError
	 */
	private TrackControl extractAudioTrack() throws NotConfiguredError {
		TrackControl tc[] = p.getTrackControls();

		TrackControl audioTrack = null;

		for (int i = 0; i < tc.length; i++) {

			if (tc[i].getFormat() instanceof AudioFormat) {
				audioTrack = tc[i];
			}
		}
		return audioTrack;
	}

	/**
	 * Create a new DataSink, and start it.
	 * @throws IOException
	 * @throws SecurityException
	 *
	 **/
	private DataSink createDataSink(Processor p, MediaLocator output) throws NotRealizedError, NoDataSinkException, SecurityException, IOException {

		DataSource d1 = p.getDataOutput();
		this.ds = Manager.createDataSink(d1, output);
		ds.open();
		ds.start();

		return ds;
	}

	/**
	 * TODO verfica se ainda e necessario
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
		return true;
	}

	public void controllerUpdate(ControllerEvent event) {

		logger.debug(event.toString());
		if (event instanceof EndOfMediaEvent) {
			logger.debug("End of media found");
			fileDone = true;
		}

	}

}
