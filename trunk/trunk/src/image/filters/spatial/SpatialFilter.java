/*
 * Created on 11/05/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package image.filters.spatial;

import image.filters.Filter;
import image.util.Mask;

import java.awt.image.BufferedImage;

/**
 *
 * @author Thiago N�brega <thiagonobrega@gmail.com>
 *
 */
public class SpatialFilter implements Filter {

	private Mask mask;
	private BufferedImage image;
	private String name;

	/**
	 * Cria um novo Filtro
	 * @param mascara
	 * @param image
	 */
	public SpatialFilter(Mask m, BufferedImage image1, String name){
		this.mask = m;
		this.image = image1;
		this.name = name;
	}

	/**
	 * processa uma i
	 */
	public BufferedImage process() {
		return image;
	}

	/**
	 * Recupear a mascara do filtro
	 *
	 * @return
	 */
	public Mask getMask(){
		return mask;
	}

	/**
	 * Seta a mascara do filtro
	 *
	 * @param newM
	 */
	public void setMask(Mask newM){
		this.mask = newM;
	}

	/**
	 * Recupera a imagem original
	 * @return
	 */
	public BufferedImage getOriginalImage(){
		return image;
	}

	/**
	 * Ajusta a cor para ficar entre o intervalo
	 *
	 * @param l a cor
	 * @return a nova cor
	 */
	public int ajusta(long l){
		if ( l > 255 )
			return 255;
		if ( l < 0 )
			return 0;
		return (int)l;
	}

	public String getName() {
		return name;
	}

}