package analisisVisual;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

/**
 *Clase Comparador.
 */
public class Comparador {    
    
	/**
	 * Metodo para ejecutar el algoritmo de comparacion de imagenes. Compara dos imagenes.
	 * Se recibe como parametros de entrada las dos imagenes, de tipo Imagen.
	 * Devuelve un resultado de tipo Resultado.
	 */
    public Resultado ejecutarAlgoritmoDeComparacion(Imagen img1, Imagen img2) {
    	
    	//se almacenan las dos imagenes recibidas como parametro de entrada en variables de tipo BufferedImage
        BufferedImage imagen1 = img1.getBufferedImage();
        BufferedImage imagen2 = img2.getBufferedImage();
        
        //se definen variables para usar en el algoritmo de comparacion de pixeles
        boolean resultado = true;
        Resultado resultadoFinal;
        //cont cuenta la cantidad de pixeles comparados
        int cont = 0;
        //pxDiff cuenta la cantidad de pixeles diferentes encontrados en la comparacion de pixeles
        int pxDiff = 0;
        BufferedImage mapaDeCalor = null;
        
        //si el ancho y alto de las imagenes a comparar son iguales, se realiza la comparacion
        if (imagen1.getWidth() == imagen2.getWidth() && imagen1.getHeight() == imagen2.getHeight()) {
            
        	//se crea un objeto mapaDeCalor de tipo BufferedImage, con el tamaño de las imagenes a comparar
        	mapaDeCalor = new BufferedImage(imagen1.getWidth(), imagen1.getHeight(), BufferedImage.TYPE_INT_RGB);
        	
        	//bucle que comparara los pixeles de las imagenes 1 y 2, de a pares
        	//recorre la imagen1 en ancho
        	int imagen1Ancho = imagen1.getWidth();
            for (int x = 0; x < imagen1Ancho; x++) {
            	//recorre la imagen1 en alto
            	int imagen1Alto = imagen1.getHeight();
                for (int y = 0; y < imagen1Alto; y++) {
                	
                	//si el pixel actual en la imagen1 es distinto del pixel en la misma coordenada en la imagen2
                    if (imagen1.getRGB(x, y) != imagen2.getRGB(x, y)) {
                    	
                    	//la comparacion ha fallado, existe al menos un pixel diferente entre las imagenes
                        resultado = false;
                        
                        //crea el mapa de calor, pinta el pixel con la coordenada que se comparo y resulto diferente
                        mapaDeCalor = this.crearMapaDeCalor(mapaDeCalor, x, y);
                        
                        //incrementa el contador de pixeles diferentes entre ambas imagenes
                        pxDiff++;
                    }
                    //incrementa el contador de pixeles comparados
                    cont++;
                }
            }
        //si no son iguales el ancho y alto de las imagenes a comparar, no puede llevarse a cabo el procesamiento
        } else {
            resultado = false;
        }
        
        //se crea un objeto resultadoFinal de tipo Resultado, 
        //con la informacion obtenida por el algoritmo de comparacion
        resultadoFinal = new Resultado(resultado, pxDiff, cont, img1.getNombreNavegador(), 
        		img2.getNombreNavegador(), mapaDeCalor);
        
        return resultadoFinal;
    }
    
    /**
	 * Metodo para crear el mapa de calor.
	 * Contiene todos los pixeles (pintados en naranja) que resultaron diferentes 
	 * en la comparacion realizada por el algoritmo.
	 * Parametro de entrada:una imagen de tipo BufferedImage, dos integer, 
	 * que representan las coordenadas del punto que resulto diferente en la comparacion 
	 * de las dos imagenes.
	 * Devuelve una imagen de tipo BufferedImage (el mapa de calor propiamente dicho)
	 */
    public BufferedImage crearMapaDeCalor(BufferedImage imagen, int x, int y) {
    	
    	//pinta de naranja el pixel en la coordenada dada por (x, y) en la imagen recibida por parametro
        imagen.setRGB(x, y, Color.ORANGE.getRGB());
        
        return imagen;
    }
    
	/**
	 * Metodo para ejecutar el algoritmo de comparacion de imagenes. Compara dos imagenes.
	 * Se recibe como parametros de entrada las dos imagenes, de tipo Imagen.
	 * Devuelve un resultado de tipo Resultado.
	 */
    public Resultado comparacionPixAMatrizPix(Imagen img1, Imagen img2) {
    	
    	//se almacenan las dos imagenes recibidas como parametro de entrada en variables de tipo BufferedImage
        BufferedImage imagen1 = img1.getBufferedImage();
        BufferedImage imagen2 = img2.getBufferedImage();
        
        //se definen variables para usar en el algoritmo de comparacion de pixeles
        boolean resultado = true;
        Resultado resultadoFinal;
        //la cantidad de pixeles comparados
        int pxTotal = 0;
        //pxDiff cuenta la cantidad de pixeles diferentes encontrados en la comparacion de pixeles
        int pxDiff = 0;
        BufferedImage mapaDeCalor = null;
        int imagen1Ancho = imagen1.getWidth();
        int imagen1Alto = imagen1.getHeight();
        
        //si el ancho y alto de las imagenes a comparar son iguales, se procede a realizar la comparacion
        if (imagen1Ancho == imagen2.getWidth() && imagen1Alto == imagen2.getHeight()) {
            
        	//se crea un objeto mapaDeCalor de tipo BufferedImage, con el tamaño de las imagenes a comparar
        	mapaDeCalor = new BufferedImage(imagen1Ancho, imagen1Alto, BufferedImage.TYPE_INT_RGB);
        	
        	//variables para el manejo del recorrido de la submatriz de imagen2
        	int i; 			//punto inicial (i;j)
        	int j;
        	int imax;		//ancho de la submatriz
        	int jmax;		//alto de la submatriz
        	//margen de tolerancia, afecta el tamaño de la submatriz
        	int delta = Integer.valueOf(System.getenv("DELTA"));        	
        	//usado en la comparacion de pixeles, ajusta los valores de canales RGB
        	int toleranciaRGB = Integer.valueOf(System.getenv("TOLERANCIA_RGB"));
        	boolean exito;
        	pxTotal = imagen1Ancho*imagen1Alto;
        	
        	//bucle que comparara los pixeles de las imagenes 1 y 2, de a pares
        	//recorre la imagen1 en ancho
            for (int x = 0; x < imagen1Ancho; x++) {
            	//recorre la imagen1 en alto
                for (int y = 0; y < imagen1Alto; y++) {
                	i = x-delta;
                	j = y-delta;
                	imax = x+delta;
                	jmax = y+delta;
                	exito = false;
                	//recorre la submatriz de imagen2, del punto inicial (i;j), mientras no se encuentre un punto igual
                	while (i<=imax) {
                		while (j<=jmax) {
                			//verifica que el punto (i;j) se encuentre dentro del indice de la matriz de la imagen
                			if ((i>=0 && i<imagen1Ancho)  && (j>=0 && j<imagen1Alto)) {
                				
                				//se hace la comparacion de pixel a pixel
                				Pixel pixel1 = new Pixel(imagen1.getRGB(x, y));
                				Pixel pixel2 = new Pixel(imagen2.getRGB(i, j));
                				//si el pixel (x;y) en imagen1 es igual del pixel (i;j) en la submatriz de imagen2
                				if (pixel1.compararParPixeles(pixel2, toleranciaRGB)) {
                					//se ha encontrado un pixel igual al pixel (x;y) en la submatriz
                					exito = true;
                				}
                			}
                			j++;
                		} //end while j
                		i++;
                	} //end while i
                	
                	//si no se encontro ningun pixel igual al pixel de imagen1 en (x;y) en la submatriz
                	if (!exito) {
                		//la comparacion ha fallado, existe al menos un pixel diferente entre las imagenes
    					resultado = false;
    					//crea el mapa de calor, pinta el pixel con la coordenada (x;y)
    					mapaDeCalor = this.crearMapaDeCalor(mapaDeCalor, x, y);
    					//incrementa el contador de pixeles diferentes entre ambas imagenes
    					pxDiff++;
                	}
                } //for imagen1Alto
            } //for imagen1Ancho
        } else {
        	//si no son iguales el ancho y alto de las imagenes, no pueden compararse
            resultado = false;
        }

        //obtiene el porcentaje de pixeles diferentes en la comparacion
        double porcentajePixDiff = (double)pxDiff*100/(double)pxTotal;
        
        //se crea un objeto resultadoFinal de tipo Resultado, 
        //con la informacion obtenida por el algoritmo de comparacion
        resultadoFinal = new Resultado(resultado, pxDiff, pxTotal, img1.getNombreNavegador(), 
        		img2.getNombreNavegador(), mapaDeCalor, porcentajePixDiff);
        
        return resultadoFinal;
    }
    
    /**
	 * Metodo de clase para comparar dos pixeles.
	 * Parametros de entrada: recibe dos enteros que corresponden a cada pixel
	 * (obtenidos mediante el metodo getRGB),
	 * y un entero que sera un valor tolerancia en la comparacion.
	 * Devuelve un boolean segun si los pixeles resultan iguales dentro del valor de tolerancia.
	 */
    public static boolean compararParPixeles(int pixel1, int pixel2, int toleranciaRGB) {
    	
    	boolean comparacion = false;
    	
    	//obtiene los valores RGB del pixel1
    	int blue1 = pixel1 & 0xff;
    	int green1 = (pixel1 & 0xff00) >> 8;
    	int red1 = (pixel1 & 0xff0000) >> 16;
    	
    	//obtiene los valores RGB minimos y maximos del pixel2 segun la tolerancia
    	int blue2max = pixel2 & 0xff + toleranciaRGB;
    	int blue2min = pixel2 & 0xff - toleranciaRGB;
    	int green2max = (pixel2 & 0xff00) >> 8 + toleranciaRGB;
    	int green2min = (pixel2 & 0xff00) >> 8 - toleranciaRGB;
    	int red2max = (pixel2 & 0xff0000) >> 16 + toleranciaRGB;
    	int red2min = (pixel2 & 0xff0000) >> 16 - toleranciaRGB;
    	
    	//si el valor del canal azul del pixel1 se encuentra dentro del valor tolerado de pixel2 
    	if (blue1 <= blue2max && blue1 >= blue2min) {
    		//si el valor del canal verde del pixel1 se encuentra dentro del valor tolerado de pixel2 
    		if (green1 <= green2max && green1 >= green2min) {
    			//si el valor del canal rojo del pixel1 se encuentra dentro del valor tolerado de pixel2 
    			if (red1 <= red2max && red1 >= red2min) {
    				//se considera los pixeles iguales
    				comparacion = true;
    			}
    		}
    	}    	
    	return comparacion;
    }
    
}