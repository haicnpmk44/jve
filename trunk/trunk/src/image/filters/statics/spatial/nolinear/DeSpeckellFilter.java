/*
 * Created on May 13, 2005 , by Thiago N�brega
 *
 */
package image.filters.statics.spatial.nolinear;

import image.filters.statics.spatials.SpatialFilter;
import image.util.Mask;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Filtro da mediana este filtro calcula a media da area e faz um suaviza��o dos contrastes diminuindo os contornos
 *
 *
 * @author Thiago N�brega <thiagonobrega@gmail.com>
 *
 */
public class DeSpeckellFilter extends SpatialFilter {


	private int desvio = 5;

	/**
	 * Cria um um novo filtro da media
	 *
	 * @param m
	 * @param image
	 */
	public DeSpeckellFilter(Mask m, BufferedImage image ) {
		super(m, image , "Despeckel Filter");
	}

	/**
	 * Cria um novo filto da media
	 * com um mascara que todos os elementos s�o 1 e a mascara e de ordedm 3
	 *
	 * @param image
	 *
	 */
	public DeSpeckellFilter(BufferedImage image,int desviopadrao ) {
		super(null, image , "Despeckel Filter" );
		Mask ma = new Mask(3);
		ma.setAll(1);
		this.setMask(ma);
		this.setDesvio(desviopadrao);
	}

	/**
	 * Processa a imagem retornanando uma nova imagem ja como as altera��es
	 *
	 * @return BufferedImage uma nova imagem
	 */
	public BufferedImage process(){

		int ordem = Math.round(this.getMask().getOrdem()/2);
		BufferedImage im = this.getOriginalImage();
		BufferedImage out = new BufferedImage(im.getWidth(),im.getHeight(),BufferedImage.TYPE_INT_RGB);

		int limiteC = im.getWidth()-ordem;
		int limiteL = im.getHeight()-ordem;

		for(int c = ordem ; c < limiteC ; c++){
			for(int l = ordem ; l < limiteL ; l++){
					Color o = new Color(im.getRGB(c,l));
					Color cor = aplicaMascara(c,l,im,this.getMask());

					if ( ( Math.abs(o.getRed() - cor.getRed()) > desvio ) || ( Math.abs(o.getBlue() - cor.getBlue()) > desvio ) || ( Math.abs(o.getGreen() - cor.getGreen()) > desvio ) )
						out.setRGB(c,l,cor.getRGB());
					else
						out.setRGB(c,l,o.getRGB());
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

		int red = 0;
		int green = 0;
		int blue = 0;

		int n = Math.round(m.getOrdem()/2);

		for (int o = -n ; o < n ; o++){
			for (int i = -n ; i < n ; i++){

				int imagemLinha = l+i;
				int imagemColuna = c+o;

				int mp = (int)Math.round(m.getPoint(n+i,n+o));

				Color cor = new Color(pic.getRGB(imagemColuna,imagemLinha));

				red += cor.getRed();
				green += cor.getGreen();
				blue += cor.getBlue();

			}//linha

		}//coluna

		Color out = new Color(ajusta(Math.round(red/Math.pow(m.getOrdem(),2))),
				ajusta(Math.round(green/Math.pow(m.getOrdem(),2))),
				ajusta(Math.round(blue/Math.pow(m.getOrdem(),2)) ));

		return out;
	}

	/**
	 * @return Returns the desvio.
	 */
	public int getDesvio() {
		return desvio;
	}

	/**
	 * @param desvio The desvio to set.
	 */
	public void setDesvio(int desvio) {
		this.desvio = desvio;
	}


}