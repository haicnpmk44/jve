/*
 * Created on May 13, 2005 , by Thiago N�brega
 *
 */
package image.filters.statics.spatials.linear;

import image.filters.statics.pontuals.aritimetics.ADDFilter;
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
public class BorderDetectFilter extends SpatialFilter {

	private int constant;
	private boolean add = false;
	/**
	 * Cria um um novo filtro da media
	 *
	 * @param m
	 * @param image
	 */
	public BorderDetectFilter(Mask m, BufferedImage image) {
		super(m, image, "Border Detect");
		constant = 50;
	}

	/**
	 * Cria um novo filto da media
	 * com um mascara que todos os elementos s�o 1 e a mascara e de ordedm 3
	 * com uma constante de 50
	 *
	 * @param image
	 *
	 */
	public BorderDetectFilter(BufferedImage image) {
		super(null, image,"Border Detect");
		Mask ma = new Mask(3);

		ma.setPoint(0,0,-1);
		ma.setPoint(0,1,-2);
		ma.setPoint(0,2,-1);
		ma.setPoint(1,0,0);
		ma.setPoint(1,1,0);
		ma.setPoint(1,2,0);
		ma.setPoint(2,0,1);
		ma.setPoint(2,1,2);
		ma.setPoint(2,2,1);

		this.setMask(ma);

		constant = 50;
	}

	/**
	 * Cria um novo filto com uma determinada contante
	 * com um mascara de sobel de ordem = 3
	 *
	 * @param image
	 * @param constant
	 */
	public BorderDetectFilter(BufferedImage image, int constant) {
		super(null, image,"Border Detect");
		Mask ma = new Mask(3);

		ma.setPoint(0,0,-1);
		ma.setPoint(0,1,-2);
		ma.setPoint(0,2,-1);
		ma.setPoint(1,0,0);
		ma.setPoint(1,1,0);
		ma.setPoint(1,2,0);
		ma.setPoint(2,0,1);
		ma.setPoint(2,1,2);
		ma.setPoint(2,2,1);

		this.setMask(ma);
		this.constant = constant;
	}

	/**
	 * Processa a imagem retornanando uma nova imagem ja como as altera��es
	 *
	 * @return BufferedImage uma nova imagem
	 */
	public BufferedImage process(){

		if ( add ){
			return new ADDFilter(this.getOriginalImage(),processImage()).process();
		} else
		return processImage();
	}

	/**
	 * @return
	 */
	private BufferedImage processImage() {
		int ordem = Math.round(this.getMask().getOrdem()/2);
		BufferedImage im = this.getOriginalImage();
		BufferedImage out = new BufferedImage(im.getWidth(),im.getHeight(),BufferedImage.TYPE_INT_RGB);
		int limiteC = im.getWidth()-ordem;
		int limiteL = im.getHeight()-ordem;
		for(int c = ordem ; c < limiteC ; c++){
			for(int l = ordem ; l < limiteL ; l++){
					Color cor = aplicaMascara(c,l,im,this.getMask());
					out.setRGB(c,l,cor.getRGB());
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
				red = red + (cor.getRed() * mp);
				green = green + (cor.getGreen() * mp);
				blue = blue + (cor.getBlue() * mp);

			}//linha

		}//coluna


		Color out = new Color(ajusta((red/3)+constant),
				ajusta(Math.round(green/3)+constant),
				ajusta(Math.round(blue/3)+constant));

		return out;
	}

	/**
	 * @return Returns the constant.
	 */
	public int getConstant() {
		return constant;
	}

	/**
	 * @param constant The constant to set.
	 */
	public void setConstant(int constant) {
		this.constant = constant;
	}

	/**
	 * Seta se a imagem sera adivionada na original
	 *
	 */
	public void setadd(boolean b){
		this.add = b;
	}


}