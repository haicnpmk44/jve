
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import java.util.StringTokenizer;

import javax.media.Codec;
import javax.media.ConfigureCompleteEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NotConfiguredError;
import javax.media.PrefetchCompleteEvent;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.ResourceUnavailableEvent;
import javax.media.StopAtTimeEvent;
import javax.media.UnsupportedPlugInException;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;

import video.effects.motion.MotionDetectEffect;

/**
 * Sample program to access individual video frames by using a "pass-thru"
 * codec. The codec is inserted into the data flow path. As data pass through
 * this codec, a callback is invoked for each frame of video data.
 */
public class TestPlayer implements ControllerListener {

	Processor p;

	Object waitSync = new Object();

	boolean stateTransitionOK = true;

	DataSink ds;

	public boolean open(MediaLocator ml) {

		try {
			p = Manager.createProcessor(ml);
		} catch (Exception e) {
			return false;
		}

		initME();

		// Obtain the track controls.
		TrackControl tc[] = p.getTrackControls();

		TrackControl videoTrack = null;
		TrackControl audioTrack = null;

		for (int i = 0; i < tc.length; i++) {
			if (tc[i].getFormat() instanceof VideoFormat) {
				videoTrack = tc[i];
			}
			if (tc[i].getFormat() instanceof AudioFormat) {
				audioTrack = tc[i];
			}
		}

		System.out.println("Video format: " + videoTrack.getFormat());
		//System.out.println("Audio format: " + audioTrack.getFormat());


		//Dimension d = parseVideoSize(videoTrack.getFormat().toString());
		try {

			Codec codec[] = { new MotionDetectEffect() };

			videoTrack.setCodecChain(codec);
		} catch (UnsupportedPlugInException e) {
			System.err.println("The process does not support effects.");
		}

		// Realize the processor.
		p.prefetch();
		if (!waitForState(p.Prefetched)) {
			System.err.println("Failed to realize the processor.");
			return false;
		}

		String dest = "file:///home/thiago/Desktop/out.mpg";
		File f = new File(dest);

		MediaLocator output = new MediaLocator(dest);

		try {
			DataSource d1 = p.getDataOutput();
			System.out.println(d1.getContentType());
			ds = Manager.createDataSink( d1, output);

			ds.open();
			ds.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// stop time
		p.setStopTime(p.getDuration());

		// Start the processor.
		p.start();

		return true;
	}

	private boolean initME() throws NotConfiguredError {
		p.configure();

		if (!waitForState(p.Configured)) {
			return false;
		}

		for (ContentDescriptor s : p.getSupportedContentDescriptors()) {
			//System.out.println(s);
			if ( s.toString().equalsIgnoreCase("AVI"))
				p.setContentDescriptor(s);
		}

		p.addControllerListener(this);

		// So I can use it as a player.
		//p.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW));

		return true;
	}


	/**
	 * Block until the processor has transitioned to the given state. Return
	 * false if the transition failed.
	 */
	boolean waitForState(int state) {
		synchronized (waitSync) {
			try {
				while (p.getState() != state && stateTransitionOK)
					waitSync.wait(100);
			} catch (Exception e) {
			}
		}
		return stateTransitionOK;
	}


	/**
	 * Main program
	 */
	public static void main(String[] args) {

		//String url = "file://home/thiago/MeusDocumentos/ufcg/smultimidia/bluescreen2.mov";
		String url = "file://home/thiago/MeusDocumentos/ufcg/smultimidia/pinguin.mpg";
		//String url = "file://home/thiago/MeusDocumentos/ufcg/smultimidia/s.mpg";
		//String url = "file://home/thiago/MeusDocumentos/ufcg/smultimidia/clip01.mpg";



		MediaLocator ml;

		if ((ml = new MediaLocator(url)) == null) {
			System.err.println("Cannot build media locator from: " + url);
			System.exit(0);
		}

		TestPlayer fa = new TestPlayer();

		//TODO PROBLEMAS DE SICRONIZAÇÃO
		fa.open(ml);

	}

	public void controllerUpdate(ControllerEvent event) {
		System.out.println( event.toString() );

		if (event instanceof StopAtTimeEvent){
			ds.close();
			p.stop();
			p.close();
		}

		if (event instanceof EndOfMediaEvent){
			System.out.println("eofMedia");
			p.stop();
		}
	}


}
