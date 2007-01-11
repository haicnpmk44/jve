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
 * SuperCutDataSource.java created in 26/12/2006 - 12:47:59 PM
 * by Thiago Nobrega ( thiagonobrega at gmail dot com )
 */
package video.util;

import javax.media.Control;
import javax.media.MediaLocator;
import javax.media.Processor;
import javax.media.Time;
import javax.media.control.TrackControl;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;


public class SuperScissorDataSource extends PushBufferDataSource {

	Processor p;

	MediaLocator ml;

	PushBufferDataSource ds;

	SuperScissorStream streams[];

	public SuperScissorDataSource(Processor p, MediaLocator ml, long start[],
			long end[]) {
		this.p = p;
		this.ml = ml;
		this.ds = (PushBufferDataSource) p.getDataOutput();

		TrackControl tcs[] = p.getTrackControls();
		PushBufferStream pbs[] = ds.getStreams();

		streams = new SuperScissorStream[pbs.length];

		for (int i = 0; i < pbs.length; i++) {
			streams[i] = new SuperScissorStream(tcs[i], pbs[i], start, end);
		}
	}

	public void connect() throws java.io.IOException {
	}

	public PushBufferStream[] getStreams() {
		return streams;
	}

	public void start() throws java.io.IOException {
		p.start();
		ds.start();
	}

	public void stop() throws java.io.IOException {
	}

	public Object getControl(String name) {
		// No controls
		return null;
	}

	public Object[] getControls() {
		// No controls
		return new Control[0];
	}

	public Time getDuration() {
		return ds.getDuration();
	}

	public void disconnect() {
	}

	public String getContentType() {
		return ContentDescriptor.RAW;
	}

	public MediaLocator getLocator() {
		return ml;
	}

	public void setLocator(MediaLocator ml) {
		System.err.println("Not interested in a media locator");
	}

}
