/*
 * Created on 11/05/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package image.util;


/**
 * @author thiagopdn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Mask {
	
	
	private int ordem;
	private double[][] mask;
	
	/**
	 * @param mask
	 */
	public Mask(double[][] mask) {		
		this.mask = mask;
	}

	/**
	 * Cria um mascara , com todos os valores = 0;
	 *  
	 * 
	 * @param ordem
	 */
	public Mask(int ordem){
		mask = new double[ordem][ordem];		
		this.ordem = ordem;
		setAll(0);		
	}
	
	/**
	 * Seta todos os valore da mascara com o mesmo valor
	 * 
	 * @param i
	 */
	public void setAll(int i){		
		for ( int l = 0 ; l < ordem ; l++ ){
			for ( int c = 0 ; c < ordem ; c++ ){
				mask[l][c] = i;
			}
		}
	}	

	/**
	 * @return Returns the mask.
	 */
	public double[][] getMask() {
		return mask;
	}	
	
	/**
	 * Retorna um ponto da mascara
	 * caso a estejam fora do mascara retorna 0
	 * 
	 * @param linha
	 * @param coluna
	 * @return
	 */
	public double getPoint(int linha , int coluna){
		if ( linha >= ordem || coluna >= ordem )
			return 0;
		return mask[linha][coluna];
	}
	
	/**
	 * Seta um valor para um ponto da mascara
	 * 
	 * @param linha
	 * @param coluna
	 * @param valor
	 * @return caso o valor estea setado para fora da mascara
	 */
	public void setPoint(int linha , int coluna , double valor){
		if ( linha >= ordem || coluna >= ordem )
			return ;
		mask[linha][coluna] = valor;
	}

	/**
	 * @return Returns the ordem.
	 */
	public int getOrdem() {
		return ordem;
	}
	
	
	/**
	 * Soma os valores da mascara
	 * 
	 * @return int com a soma dos elementos
	 */	
	public double somaEletementos(){
		
		double out = 0;
	
		for ( int l = 0 ; l < ordem ; l++ ){
			for ( int c = 0 ; c < ordem ; c++ ){
				out += mask[l][c];
			}
		}
		return out;		
	}
	
	/**
	 * Retorna um filtro laplaciano de ordem 3 , com uma vizinha�a guadri conectada 
	 * 
	 * @return mask o filtro
	 */
	public static Mask getinstanceOfLapalacian34(){
		Mask k = new Mask(3);
		k.setPoint(0,0,0);
		k.setPoint(0,1,1);
		k.setPoint(0,2,0);
		k.setPoint(1,0,1);
		k.setPoint(1,1,4);
		k.setPoint(1,2,1);
		k.setPoint(2,0,0);
		k.setPoint(2,1,1);
		k.setPoint(2,2,0);
		
		return k;
	}
	
	/**
	 * Retorna um filtro laplaciano de ordem 3 , com uma vizinha�a 8-conectada 
	 * 
	 * @return mask o filtro
	 */
	public static Mask getinstanceOfLapalacian38(){
		Mask k = new Mask(3);
		k.setPoint(0,0,1);
		k.setPoint(0,1,1);
		k.setPoint(0,2,1);
		k.setPoint(1,0,1);
		k.setPoint(1,1,8);
		k.setPoint(1,2,1);
		k.setPoint(2,0,1);
		k.setPoint(2,1,1);
		k.setPoint(2,2,1);
		
		return k;
	}
	
	/**
	 * Cria um kernel gaussiano
	 * 
	 * @param size a ordem ex 3 = M3x3
	 * @param sigma
	 * @return
	 */
	public static Mask getinstanceOfGaussian(int size, double sigma){
		Mask kernel = new Mask(size);
		for ( int l = 0 ; l < size ; l++ ){
			for ( int c = 0 ; c < size ; c++ ){
				kernel.setPoint(l,c,calculaGaussian(c,l,sigma));
			}
		}
		
		return kernel;
	}
	
	/**
	 * Retorna uma mascara para aplicar um filtro laplaciano e gaussiano, ambos ao mesmo tempo.
	 * 
	 * @param size
	 * @param sigma
	 * @return
	 */
	public static Mask getinstanceOfLaplacianGaussian(int size, double sigma){
		Mask kernel = new Mask(size);
		for ( int l = 0 ; l < size ; l++ ){
			for ( int c = 0 ; c < size ; c++ ){
				kernel.setPoint(l,c,calculaLaplacianGaussian(c,l,sigma));
			}
		}
		
		return kernel;
	}
	
	/**
	 * Retorna um filtro para sharpening ded ordem 3 
	 * 
	 * @param alfa 
	 * @return
	 */
	public static Mask getinstanceOfSharpenig(int alfa){
		
		Mask k = new Mask(3);
		k.setPoint(0,0,0);
		k.setPoint(0,1,-alfa);
		k.setPoint(0,2,0);
		k.setPoint(1,0,-alfa);
		k.setPoint(1,1,1+4*alfa);
		k.setPoint(1,2,-alfa);
		k.setPoint(2,0,0);
		k.setPoint(2,1,-alfa);
		k.setPoint(2,2,0);
		
		return k;
		
	}
	
	/**
	 * Faz o calcula da transformada de gaus
	 * 
	 * @param linha
	 * @param coluna
	 * @param sigma 
	 * @return
	 */
	private static double calculaGaussian(int c , int l, double sigma ){
		return   Math.pow( Math.E, -( ( Math.pow(l,2) + Math.pow(c,2) ) / (2*Math.pow(sigma,2)) ) ) / ( 2*Math.PI*Math.pow(sigma,2) ) ;
	}
	
	/**
	 * Calcula um pondo da mascara para um filtro laplaciano e gaussiano
	 * 
	 * @param coluna
	 * @param linha
	 * @param sigma o desvio padr�o
	 * @return o valor do pondo na mascara
	 */
	private static double calculaLaplacianGaussian(int c , int l, double sigma ){
		double f = ( Math.pow(c,2)+Math.pow(l,2)- Math.pow(sigma,2) )/Math.pow(sigma,4);
		double t = Math.pow( Math.E,-(( Math.pow(c,2)+Math.pow(l,2) )/2*Math.pow(sigma,2)) );
		
		return -t*f;
	}
	

}
