/*
 * Created on May 7, 2005 , by Thiago N�brega
 *
 */
package engine.ui.gui;

import image.filters.PointFilter;
import image.filters.statics.pontuals.aritimetics.ADDFilter;
import image.filters.statics.pontuals.aritimetics.DIVFilter;
import image.filters.statics.pontuals.aritimetics.MAXFilter;
import image.filters.statics.pontuals.aritimetics.MINFilter;
import image.filters.statics.pontuals.aritimetics.MULTFilter;
import image.filters.statics.pontuals.aritimetics.SUBFilter;
import image.filters.statics.pontuals.logical.ANDFilter;
import image.filters.statics.pontuals.logical.IGSFilter;
import image.filters.statics.pontuals.logical.NOTFilter;
import image.filters.statics.pontuals.logical.ORFilter;
import image.filters.statics.pontuals.logical.XORFilter;
import image.filters.statics.spatial.nolinear.DeSpeckellFilter;
import image.filters.statics.spatial.nolinear.MedianFilter;
import image.filters.statics.spatial.nolinear.RoadFilter;
import image.filters.statics.spatials.SpatialFilter;
import image.filters.statics.spatials.linear.BorderDetectFilter;
import image.filters.statics.spatials.linear.KernelFilter;
import image.filters.statics.spatials.linear.LaplacianGaussinanFilter;
import image.util.Mask;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MainWindowE extends JFrame {

	private File imagefile = null;
	private MainWindowE mw;
	private final String filelocal = "/home/thiago/luanapiovani.jpg";
	private JDesktopPane DesktopPane = null;
	private JMenuBar menu = null;
	private JMenu Arquivo = null;
	private JMenuItem jMenuOpen = null;
	private JMenuItem jMenuSair = null;
	private JMenu filtros = null;
	private JMenu pontuais1 = null;
	private JMenuItem soma = null;
	private JMenu aritimeticos = null;
	private JMenuItem sub = null;
	private JMenu logicos = null;
	private JMenuItem and = null;
	private JMenuItem or = null;
	private JMenuItem not = null;
	private JMenuItem xor = null;
	private JMenuItem IGS = null;
	private JMenu espacias = null;
	private JMenuItem mult = null;
	private JMenuItem max = null;
	private JMenuItem min = null;
	private JMenuItem div = null;
	private JMenu linear = null;
	private JMenuItem border = null;
	private JMenuItem bordera = null;
	private JMenuItem log = null;
	private JMenuItem loga = null;
	private JMenuItem kernelf = null;
	private JMenuItem sharp = null;
	private JMenu nolinear = null;
	private JMenuItem media = null;
	private JMenuItem despeckl = null;
	private JMenuItem road = null;
	/**
	 * This is the default constructor
	 */
	public MainWindowE() {
		super();
		mw = this;
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(640, 480);
		this.setTitle("JFrame");
		this.setPreferredSize(new java.awt.Dimension(200,200));
		this.setLocation(50, 50);
		this.setContentPane(getDesktopPane());
		this.setJMenuBar(getMenu());
	}

	/**
	 * This method initializes jDesktopPane
	 *
	 * @return javax.swing.JDesktopPane
	 */
	private JDesktopPane getDesktopPane() {
		if (DesktopPane == null) {
			DesktopPane = new JDesktopPane();
		}
		return DesktopPane;
	}

	/**
	 * This method initializes jJMenuBar
	 *
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getMenu() {
		if (menu == null) {
			menu = new JMenuBar();
			menu.add(getArquivo());
			menu.add(getFiltros());
		}
		return menu;
	}

	/**
	 * This method initializes jMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getArquivo() {
		if (Arquivo == null) {
			Arquivo = new JMenu();
			Arquivo.setText("Arquivo");
			Arquivo.add(getJMenuOpen());
			Arquivo.add(getJMenuSair());
		}
		return Arquivo;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuOpen() {
		if (jMenuOpen == null) {
			jMenuOpen = new JMenuItem();
			jMenuOpen.setText("Abrir");
			jMenuOpen.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//DesktopPane.add(null);
				}
			});
		}
		return jMenuOpen;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuSair() {
		if (jMenuSair == null) {
			jMenuSair = new JMenuItem();
			jMenuSair.setText("Sair");
			jMenuSair.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return jMenuSair;
	}

	/**
	 * This method initializes jMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getFiltros() {
		if (filtros == null) {
			filtros = new JMenu();
			filtros.setText("Filtros");
			filtros.add(getPontuais1());
			filtros.add(getEspacias());
		}
		return filtros;
	}

	/**
	 * This method initializes jMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getPontuais1() {
		if (pontuais1 == null) {
			pontuais1 = new JMenu();
			pontuais1.setText("Pontuais");
			pontuais1.add(getLogicos());
			pontuais1.add(getAritimeticos());
			pontuais1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return pontuais1;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSoma() {
		if (soma == null) {
			soma = new JMenuItem();
			soma.setText("SOMA");
			soma.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return soma;
	}

	/**
	 * This method initializes jMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getAritimeticos() {
		if (aritimeticos == null) {
			aritimeticos = new JMenu();
			aritimeticos.setText("Aritimeticos");
			aritimeticos.add(getMin());
			aritimeticos.add(getMax());
			aritimeticos.add(getSub());
			aritimeticos.add(getSoma());
			aritimeticos.add(getDiv());
			aritimeticos.add(getMult());
		}
		return aritimeticos;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSub() {
		if (sub == null) {
			sub = new JMenuItem();
			sub.setText("SUBTRACAO");
			sub.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return sub;
	}

	/**
	 * This method initializes jMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getLogicos() {
		if (logicos == null) {
			logicos = new JMenu();
			logicos.setText("Logicos");
			logicos.add(getIGS());
			logicos.add(getXor());
			logicos.add(getNot());
			logicos.add(getOr());
			logicos.add(getAnd());
		}
		return logicos;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAnd() {
		if (and == null) {
			and = new JMenuItem();
			and.setText("AND");
			and.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return and;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getOr() {
		if (or == null) {
			or = new JMenuItem();
			or.setText("OR");
			or.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return or;
	}

	/**
	 * This method initializes jMenuItem1
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getNot() {
		if (not == null) {
			not = new JMenuItem();
			not.setText("NOT");
			not.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return not;
	}

	/**
	 * This method initializes jMenuItem2
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getXor() {
		if (xor == null) {
			xor = new JMenuItem();
			xor.setText("XOR");
			xor.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return xor;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getIGS() {
		if (IGS == null) {
			IGS = new JMenuItem();
			IGS.setText("IGS");
			IGS.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return IGS;
	}

	/**
	 * This method initializes espacias
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getEspacias() {
		if (espacias == null) {
			espacias = new JMenu();
			espacias.setText("Espacias");
			espacias.add(getNolinear());
			espacias.add(getLinear());
		}
		return espacias;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMult() {
		if (mult == null) {
			mult = new JMenuItem();
			mult.setText("MULTIPLICACAO");
			mult.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return mult;
	}

	/**
	 * This method initializes jMenuItem1
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMax() {
		if (max == null) {
			max = new JMenuItem();
			max.setText("MAX");
			max.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return max;
	}

	/**
	 * This method initializes jMenuItem2
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMin() {
		if (min == null) {
			min = new JMenuItem();
			min.setText("MIN");
			min.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return min;
	}

	/**
	 * This method initializes jMenuItem3
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getDiv() {
		if (div == null) {
			div = new JMenuItem();
			div.setText("DIVISAO");
			div.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return div;
	}

	/**
	 * This method initializes jMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getLinear() {
		if (linear == null) {
			linear = new JMenu();
			linear.setText("Linear");
			linear.add(getSharp());
			linear.add(getKernelf());
			linear.add(getBordera());
			linear.add(getBorder());
			linear.add(getLog());
			linear.add(getLoga());
		}
		return linear;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getBorder() {
		if (border == null) {
			border = new JMenuItem();
			border.setText("Detectar Bordas 1 ( Sobel )");
			border.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return border;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getBordera() {
		if (bordera == null) {
			bordera = new JMenuItem();
			bordera.setText("Detectar Bordas 2 ( Sobel )");
			bordera.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return bordera;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getLog() {
		if (log == null) {
			log = new JMenuItem();
			log.setText("Laplacian Of Gaussian");
			log.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {


				}
			});
		}
		return log;
	}

	/**
	 * This method initializes jMenuItem1
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getLoga() {
		if (loga == null) {
			loga = new JMenuItem();
			loga.setText("Laplacian Of Gaussian 2");
			loga.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return loga;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getKernelf() {
		if (kernelf == null) {
			kernelf = new JMenuItem();
			kernelf.setText("Kernel Filter");
			kernelf.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return kernelf;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSharp() {
		if (sharp == null) {
			sharp = new JMenuItem();
			sharp.setText("Sharpenig");
			sharp.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return sharp;
	}

	/**
	 * This method initializes jMenu
	 *
	 * @return javax.swing.JMenu
	 */
	private JMenu getNolinear() {
		if (nolinear == null) {
			nolinear = new JMenu();
			nolinear.setText("Nao Linear");
			nolinear.add(getRoad());
			nolinear.add(getDespeckl());
			nolinear.add(getMedia());
		}
		return nolinear;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMedia() {
		if (media == null) {
			media = new JMenuItem();
			media.setText("Filtro da Media");
			media.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return media;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getDespeckl() {
		if (despeckl == null) {
			despeckl = new JMenuItem();
			despeckl.setText("DeSpeckell");
			despeckl.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return despeckl;
	}

	/**
	 * This method initializes jMenuItem
	 *
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getRoad() {
		if (road == null) {
			road = new JMenuItem();
			road.setText("Road");
			road.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {


				}
			});
		}
		return road;
	}
 }
