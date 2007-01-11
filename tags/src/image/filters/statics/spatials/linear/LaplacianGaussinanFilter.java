/*
 * Created on May 13, 2005 , by Thiago N�brega
 *
 */
package image.filters.statics.spatials.linear;

import image.filters.statics.spatials.SpatialFilter;
import image.util.Mask;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * @author Thiago N�brega <thiagonobrega@gmail.com>
 *
 */
public class LaplacianGaussinanFilter extends SpatialFilter {

	private double sigma;
	private int masksize;

	/**
	 * Cria um um novo filtro gausiano
	 *
	 * @param m
	 * @param image
	 * @param mascara tamanho da mascara
	 */
	public LaplacianGaussinanFilter(BufferedImage image,int mascara) {
		super(null, image, "Laplacian of Gausian Filter" );
		sigma = 0.9;
		masksize = mascara;
	}

	/**
	 * Cria um novo filto gausiano
	 *
	 * @param m
	 * @param image
	 * @param sigma
	 * @param mascara tamanho da mascara
	 */
	public LaplacianGaussinanFilter(BufferedImage image, double sigma, int mascara ) {
		super(null, image,"Gausian Filter");
		this.sigma = sigma;
		masksize = mascara;
	}

	/**
	 * Processa a imagem retornanando uma nova imagem ja como as altera��es
	 *
	 * @return BufferedImage uma nova imagem
	 */
	public BufferedImage process(){
		this.setMask(Mask.getinstanceOfLaplacianGaussian(masksize,sigma));

		int ordem = Math.round(this.getMask().getOrdem()/2);
		BufferedImage im = this.getOriginalImage();
		BufferedImage out = new BufferedImage(im.getWidth(),im.getHeight(),BufferedImage.TYPE_INT_RGB);

		int limiteC = im.getWidth()-ordem;
		int limiteL = im.getHeight()-ordem;
		for(int c = ordem ; c < limiteC ; c++){
			for(int l = ordem ; l < limiteL ; l++){
					out.setRGB(c,l,aplicaMascara(c,l,im,this.getMask()).getRGB());
			}//linha
		}//coluna

		return out;
	}

	/**
	 * Aplica a mascara em um ponto da imagem
	 *
	 * @param l a linha da imagem
	 * @param c a coluna da imagem
	 * @param im imagem
	 * @param m a mascara
	 * @return o novo valor
	 */
	private Color aplicaMascara(int c, int l,  BufferedImage pic , Mask m){

		double red = 0;
		double green = 0;
		double blue = 0;

		int n = Math.round(m.getOrdem()/2);

		for (int o = -n ; o < n ; o++){
			for (int i = -n ; i < n ; i++){

				int imagemLinha = l+i;
				int imagemColuna = c+o;

				double mp = m.getPoint(n+i,n+o);

				Color cor = new Color(pic.getRGB(imagemColuna,imagemLinha));

				red += mp * cor.getRed();
				green +=  mp * cor.getGreen();
				blue += mp * cor.getBlue();

			}//linha

		}//coluna

		Color out = new Color(ajusta(Math.round(red)),
				ajusta(Math.round(green)),
				ajusta(Math.round(blue)) );

		return out;
	}


}