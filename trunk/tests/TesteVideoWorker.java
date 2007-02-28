/*
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
 * TesteVideoWorker.java created in 27/12/2006 - 2:32:43 AM
 * @author Thiago Nóbrega ( thiagonobrega at gmail dot com )
 */
import java.io.IOException;

import javax.media.Codec;
import javax.media.MediaLocator;
import javax.media.NoDataSinkException;
import javax.media.NoProcessorException;
import javax.media.NotConfiguredError;
import javax.media.NotRealizedError;
import javax.media.UnsupportedPlugInException;

import video.effects.MotionDetectEffect;
import video.util.VideoWorker;

public class TesteVideoWorker {
	/**
	 * Main program
	 */
	public static void main(String[] args) {

//		String url ="file://home/thiago/workspace/java/jgve/resources/scirrhous2.mpg";
//		String url ="file://home/thiago/workspace/java/jgve/resources/clip01.mpg";
//		String url ="file://home/thiago/MeusDocumentos/ufcg/smultimidia/bluescreen2.mov";
//		String url ="file://home/thiago/workspace/java/jgve/resources/UFO.avi";
//		String url ="file://home/thiago/workspace/java/jgve/resources/l.avi";
		String url ="file://home/thiago/workspace/java/jgve/resources/01.avi";
//		String url ="file://home/thiago/workspace/java/jgve/resources/sky.mpg";
//		String url ="file://home/thiago/workspace/java/jgve/resources/PariRollerVideo.avi";
		//String url ="file://home/thiago/workspace/java/jgve/resources/sampleN01.avi";
//		String url = "file://home/thiago/MeusDocumentos/ufcg/smultimidia/pinguin.mpg";
//		String url = "file://home/thiago/MeusDocumentos/ufcg/smultimidia/s.mpg";



		String out = "file://home/thiago/Desktop/out.avi";



		VideoWorker fa = new VideoWorker(new MediaLocator(url),new MediaLocator(out));
		Codec codec[] = {new MotionDetectEffect()};
		fa.setCodec(codec);

		// TODO PROBLEMAS DE SICRONIZAÇÃO
		try {
			fa.open();
		} catch (NoProcessorException e) {
			// TODO Auto-generated catch block
			System.out.println("!!!");
			e.printStackTrace();
		} catch (UnsupportedPlugInException e) {
			// TODO Auto-generated catch block
			System.out.println("!!!");
			e.printStackTrace();
		} catch (NotConfiguredError e) {
			// TODO Auto-generated catch block
			System.out.println("!!!");
			e.printStackTrace();
		} catch (NotRealizedError e) {
			// TODO Auto-generated catch block
			System.out.println("!!!");
			e.printStackTrace();
		} catch (NoDataSinkException e) {
			// TODO Auto-generated catch block
			System.out.println("!!!");
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			System.out.println("!!!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("!!!");
			e.printStackTrace();
		}

		System.exit(0);

	}

}
