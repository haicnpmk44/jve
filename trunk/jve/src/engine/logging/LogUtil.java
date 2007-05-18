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
 * LoggingUtil.java created in 11/01/2007 - 3:45:24 PM
 * by Thiago Nobrega ( thiagonobrega at gmail dot com )
 */
package engine.logging;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogUtil {

	public static Logger getLog(Class c){
		 PropertyConfigurator.configure("logging.properties");
		 return Logger.getLogger(c);
	}

	public static void main(String[] args) {
		Logger l = getLog(LogUtil.class);
		l.debug("Ola mundo");
	}

}
