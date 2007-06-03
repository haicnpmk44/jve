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
 * MainWindow.java created in 12/01/2007 - 21:02:24
 * by Thiago Nobrega ( thiagonobrega at gmail dot com )
 */

package jve.engine.ui.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JDesktopPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JToolBar;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel workspace = null;

	private JDesktopPane jDesktopPane = null;

	private JToolBar actionBar = null;

	/**
	 * This is the default constructor
	 */
	public MainWindow() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(640, 480);
		this.setPreferredSize(new Dimension(640, 480));
		this.setName("baseFrame");
		this.setContentPane(getWorkspace());
		this.setTitle(".:Tharan:.");
		this.setVisible(true);
	}

	/**
	 * This method initializes workspace
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getWorkspace() {
		if (workspace == null) {
			workspace = new JPanel();
			workspace.setLayout(new BorderLayout());
			workspace.setName("workspace");
			workspace.add(getJDesktopPane(), BorderLayout.NORTH);
			workspace.add(getActionBar(), BorderLayout.SOUTH);
		}
		return workspace;
	}

	/**
	 * This method initializes jDesktopPane
	 *
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getJDesktopPane() {
		if (jDesktopPane == null) {
			jDesktopPane = new JDesktopPane();
		}
		return jDesktopPane;
	}

	/**
	 * This method initializes actionBar
	 *
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getActionBar() {
		if (actionBar == null) {
			actionBar = new JToolBar();
			actionBar.setPreferredSize(new Dimension(18, 40));
		}
		return actionBar;
	}

}
