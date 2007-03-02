/*
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
 * KeyboardReader.java created in 30/01/2007 - 7:53:27 PM
 * by Thiago Nobrega ( thiagonobrega at gmail dot com )
 */
package engine.ui.text;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.media.Codec;
import javax.media.MediaLocator;
import javax.media.NoDataSinkException;
import javax.media.NoProcessorException;
import javax.media.NotConfiguredError;
import javax.media.NotRealizedError;
import javax.media.UnsupportedPlugInException;

import video.effects.VideoEffect;
import video.effects.motion.MotionDetectEffect;
import video.filters.motion.BorderMotionDetectionFilter;
import video.util.VideoWorker;
import engine.ui.UIException;
import engine.util.Messages;
import engine.util.MessagesErros;

public class KeyboardReader implements Runnable {

	private Scanner in;

	private String[] instructions;

	private List<String> userFiltersName;
	private Map<String,VideoEffect> userFilters;

	private String[] filterList;

	private String[] colors;

	public KeyboardReader() {

		in = new Scanner(new InputStreamReader(System.in));

		in.useDelimiter("\n");

		instructions = new String[] { Messages.getString("command.exit"),
				Messages.getString("command.help") ,
				Messages.getString("command.new") ,
				Messages.getString("command.apply"),
				Messages.getString("command.list.user.filters") };

		userFilters = new HashMap<String,VideoEffect>();
		userFiltersName = new LinkedList<String>();

		filterList = new String[] { Messages.getString("filter.motion.detect") };

		colors = new String[] { Messages.getString("color.red"),
				Messages.getString("color.green"),
				Messages.getString("color.blue"),
				Messages.getString("color.black"),
				Messages.getString("color.yellow"),
				Messages.getString("color.cyan"), };
	}

	public void run() {

		while (true) {
			try {
				while (in.hasNext()) {

					String line = in.next().trim();

					StringTokenizer word = new StringTokenizer(line, " ");

					// verify if has a valid entry
					if (word.countTokens() <= 0)
						throw new UIException(MessagesErros
								.getString("error.inavlid.arguments.numbers"));

					// get the instruction
					int instruction = getInstruction(instructions, word
							.nextToken());

					switch (instruction) {

					case 0:
						System.out.println(Messages.getString("sucess.exit"));
						System.exit(0);
						break;
					case 1:
						System.out.println(Messages.getString("help.textui.usage"));
						break;
					case 2: // new insttuction.
						createNewFilter(word);
						break;
					case 3: // apply
						applyFilters(word);
						break;
					case 4: // list
						listUserFilters();
						break;
					default:
						System.out.println("?");
						break;
					}

				}

			} catch (Exception e) {
				System.err.println(e.getMessage());
			}

		}// while true

	}

	private void listUserFilters() {
		System.out.print("[ ");
		for (String s : userFiltersName ) {
			System.out.print(s + " ");
		}
		System.out.println("]");
	}

	private void applyFilters(StringTokenizer word) throws UIException, NoProcessorException, UnsupportedPlugInException, NotConfiguredError, NotRealizedError, NoDataSinkException, SecurityException, IOException {
		List<VideoEffect> tempEfxList = new LinkedList<VideoEffect>();
		File inputFile = null;
		String input = null;
		String output = null;
		String n = null;

		boolean outFlag = false;
		while ( ! outFlag ){

			n = word.nextToken();

			if (n.equalsIgnoreCase(Messages.getString("command.apply.filter"))
					|| n == null) {

				n = word.nextToken();

				VideoEffect efx = userFilters.get(n);

				if (efx == null)
					throw new UIException(MessagesErros.getString("error.filter.cantfind")+n);

				tempEfxList.add(efx);
			} else
				outFlag = true;


		}


		if ( n.equalsIgnoreCase(Messages.getString("command.input")) ){
			input = word.nextToken();
			inputFile = new File(input);
		}


		if ( word.nextToken().equalsIgnoreCase(Messages.getString("command.output")) )
			output = word.nextToken();


		//verificacoes

		if ( ! inputFile.exists() )
			throw new UIException(MessagesErros.getString("error.file.cantfind.input")+input+".");
		if ( output == null )
			throw new UIException(MessagesErros.getString("error.file.exist.output"));

		String filePrefix = "file:/";


		VideoWorker fa = new VideoWorker(new MediaLocator(filePrefix+input),new MediaLocator(filePrefix+output));

		Codec[] cs = new Codec[tempEfxList.size()];

		int i = 0;
		for (Codec c : tempEfxList ) {
			cs[i] = c;
		}

		fa.setCodec(cs);
		//FIXME inciar um thread separada

		fa.open();
	}

	private void createNewFilter(StringTokenizer word) throws UIException {

		if (!word.nextToken().equalsIgnoreCase(
				Messages.getString("command.new.filter")))
			throw new UIException(MessagesErros
					.getString("error.invalid.object"));

		// get the filter
		int filterNum = getInstruction(filterList, word.nextToken());

		String filterAlias = word.nextToken();

		switch (filterNum) {
		case 0: // motion detection
			newMotionFilter(word, filterAlias);
			break;
		default:
			System.out.println("---x---");
			break;
		}

	}

	/**
	 * create a new motion filter
	 * @param word
	 * @param filterAlias
	 * @throws UIException
	 */
	private void newMotionFilter(StringTokenizer word, String filterAlias) throws UIException {
		Color color = null;
		if (word.hasMoreTokens()) {
			String n = word.nextToken();

			if (n.equalsIgnoreCase("help")){
				System.out.println(Messages.getString("help.textui.motion.detect"));
				return ;
			}

			if (n.startsWith("-c")) {
				if (!word.hasMoreTokens())
					new UIException(MessagesErros
							.getString("error.color.null"));

				int colorNum = getInstruction(colors, word.nextToken());

				switch (colorNum) {

				case 0:
					color = Color.red;
					break;
				case 1:
					color = Color.green;
					break;
				case 2:
					color = Color.blue;
					break;
				case 3:
					color = Color.black;
					break;
				case 4:
					color = Color.yellow;
					break;
				case 5:
					color = Color.cyan;
					break;
				default:
					throw new UIException(Messages
							.getString("error.color.invalid"));
				} // switch color
			} // if from color
		} // if hasmoretokens

		BorderMotionDetectionFilter thisFilter = color == null ? new BorderMotionDetectionFilter()
				: new BorderMotionDetectionFilter(color);

		userFilters.put(filterAlias,new MotionDetectEffect(thisFilter));
		userFiltersName.add(filterAlias);

		System.out.println(Messages.getString("sucess.textui.filters.create"));
	}

	private int getInstruction(String[] instructionset, String input) {

		for (int i = 0; i < instructionset.length; i++) {
			if (input.equalsIgnoreCase(instructionset[i]))
				return i;
		}
		return -1;
	}

	public static void main(String[] args) {

		KeyboardReader kbr = new KeyboardReader();
		kbr.run();

	}

}
