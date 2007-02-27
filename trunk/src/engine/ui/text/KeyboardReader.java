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

import image.filters.Filter;

import java.awt.Color;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

import video.filters.motion.BorderMotionDetectionFilter;
import engine.ui.UIException;
import engine.util.Messages;
import engine.util.MessagesErros;

public class KeyboardReader implements Runnable {

	private Scanner in;

	private String[] instructions;

	private LinkedList<Filter> userFilters;

	private String[] filterList;

	private String[] colors;

	public KeyboardReader() {

		in = new Scanner(new InputStreamReader(System.in));
		in.useDelimiter("\n");
		instructions = new String[] { Messages.getString("command.new") };
		userFilters = new LinkedList<Filter>();
		filterList = new String[] { Messages.getString("filter.motion.detect") };
		colors = new String[] { Messages.getString("color.red"),Messages.getString("color.green"),
				Messages.getString("color.blue"),Messages.getString("color.black"),
				Messages.getString("color.yellow"),Messages.getString("color.cyan"),};
	}

	public void run() {

		while (true) {
			try {
				while (in.hasNext()) {

					String line = in.next().trim();

					StringTokenizer word = new StringTokenizer(line, " ");


					// verify if has a valid entry
					if (word.countTokens() <= 3)
						throw new UIException(MessagesErros.getString("error.inavlid.arguments.numbers"));

					// get the instruction
					int instruction = getInstruction(instructions, word.nextToken());

					switch (instruction) {

					case 0: // new insttuction.
						createNewFilter(word);
						break;
					case 1:
						System.out.println("old");
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

	private void createNewFilter(StringTokenizer word) throws UIException {

		if ( ! word.nextToken().equalsIgnoreCase(Messages.getString("command.new.filter")) )
			throw new UIException(MessagesErros.getString("error.invalid.object"));

		// get The filter
		int filterNum = getInstruction(filterList,word.nextToken());

		switch (filterNum) {
			case 0: // motion detection

				Color color = null;
				String n = word.nextToken();

				if ( n.startsWith("-c") ){
					if (! word.hasMoreTokens())
						new UIException(MessagesErros.getString("error.color.null"));

					int colorNum = getInstruction(colors,word.nextToken());

					switch(colorNum){

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
					}


					MessagesErros.getString("error.color.null");




				}

				if (n.equalsIgnoreCase("help"))
					System.out.println("help");

				new BorderMotionDetectionFilter();
				System.out.println("o");
				break;
			default:
				System.out.println("---x---");
				break;
		}

		String filteralias = word.nextToken();
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
