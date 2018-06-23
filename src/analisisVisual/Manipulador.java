package analisisVisual;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Clase manipulador.
 */
public class Manipulador {
    
	//variables de instancia
    private final String ruta = System.getenv("RUTA") + "\\tmp\\";
    private String nombrePrueba;
    private String fecha;
    private String path;

    /**
     * Constructor de Manipulador. 
     * Parametro de entrada: string con el nombre del proyecto.
     */
    public Manipulador(String nombreProyecto) {
    	
    	//se crea la carpeta tmp
        Utils.crearCarpetaTMP();
        this.nombrePrueba = nombreProyecto;
        //se crea una carpeta con el nombre de la prueba (nombre del proyecto), dentro de la carpeta tmp
        this.crearCarpeta(this.ruta, this.nombrePrueba);   
    }
    
    /**
     *  Metodo para crear una carpeta.
     *  Parametros de entrada: la ruta donde se va a crear la carpeta, 
     *  un string con el nombre del proyecto
     */
    private void crearCarpeta(String ruta, String nombreProyecto) {
    	
    	//asigna a la variable de instancia fecha un timestamp con el momento en que se llama el metodo
        this.fecha = Utils.getFecha();
        //elimina caracteres invalidos (para el nombre de un archivo) del string de entrada
        String nombrePrueba = Utils.removerCaracteresInvalidos(nombreProyecto);
        
        String titulo;
        String tituloDir;
        
        //si el nombre de prueba (variable de instancia) esta vacio
        if (nombrePrueba.isEmpty()) {
        	//se le asigna un titulo generico
            titulo = "Sin Titulo";
            //trim = saca los espacios al texto
            tituloDir = titulo.trim();
            
        //si la longitud del nombre de prueba es mayor a 50 caracteres
        } else if (nombrePrueba.length() > 50) {
        	//se recorta el nombre de la prueba
            titulo = nombrePrueba.substring(0, 47) + "...";
            //trim = saca los espacios al texto
            tituloDir = nombrePrueba.substring(0, 47).trim();
            
        //si el nombre de prueba no esta vacio y contiene menos de 50 caracteres
        } else {
        	//se asigna un titulo con el nombre de la prueba
            titulo = nombrePrueba;
            //trim = saca los espacios al texto
            tituloDir = titulo.trim();
        }
        
        //se asigna al path (variable de instancia) la concatenacion de: la ruta (variable de instancia), 
        //el string de fecha (variable de instancia) y el tituloDir recien creado
        this.path = ruta + this.fecha + " - " + tituloDir + "//";
        
        //se crea una carpeta en ese path
        Utils.crearCarpeta(path);
    }
    
    /**
     * Metodo que realiza una captura de pantalla.
     * Parametro de entrada: un objeto de tipo WebDriver.
     * Devuelve un objeto de tipo Imagen.
     */
    public Imagen capturarPantalla(WebDriver navegador) {
    	
        try {
        	//se maximiza la ventana del navegador
            navegador.manage().window().maximize(); //manipula el navegador
            
            //fija un retraso de tiempo para contemplar la carga de la pagina en el navegador
            Thread.sleep(10000);
            //busca un elemento por tagName, el body, le hace click
            navegador.findElement(By.tagName("body")).click();

            //se utiliza el robot para sacar la captura de pantalla
            Robot robot = new Robot(); //maneja mi compu
            Thread.sleep(8000);
            
            //crea una variable dimension, con el tamanio de la pantalla
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            //crea un rectangulo con la dimension recien creada
            Rectangle rectangle = new Rectangle(dimension);
            //saca una captura de pantalla del tamanio del rectangulo, la almacena en image
            BufferedImage image = robot.createScreenCapture(rectangle); //guarda la captura en la variable dinamica
            
            //se parsea navegador (parametro de entrada, de tipo WebDriver) a un RemoteWebDriver
            RemoteWebDriver navegadorRemoto = (RemoteWebDriver) navegador;
            //se obtienen las opciones del navegadorRemoto
            Capabilities cap = navegadorRemoto.getCapabilities();
            //obtiene el nombre del navegador de las opciones obtenidas anteriormente
            String nombreNavegador = cap.getBrowserName();
            
            //llama al metodo guardarImagen con la imagen image, y el nombre de navegador, la imagen sera de tipo png
            this.guardarImagen(image, nombreNavegador + ".png");
            
            //regresa un nuevo objeto de tipo Imagen, con el nombre del navegador y image (la imagen propiamente dicha)
            return new Imagen(nombreNavegador, image);
            
        //Excepciones
        } catch (AWTException ex) {
            System.out.println("No se encontro un dispositivo gráfico)");
            return null;
        } catch (Exception ex) {
            System.out.println("Error inesperado");
            return null;
        }
    }
    
    /**
     * Metodo que realiza una captura de pantalla con el 
     * navegador en modo pantalla completa (presionando F11)
     * Parametro de entrada: un objeto de tipo WebDriver.
     * Devuelve un objeto de tipo Imagen.
     */
    public Imagen capturarPantallaFullscreen(WebDriver navegador){
        
        try {
            
            //fija un retraso de tiempo para contemplar la carga de la pagina en el navegador
            Thread.sleep(4000);
            //busca un elemento por tagName, el body, le hace click
            navegador.findElement(By.tagName("body")).click();
            
            //se utiliza el robot para sacar la captura de pantalla
            Robot robot = new Robot(); //maneja mi compu
            Thread.sleep(3000);
            
            //mueve el mouse a una posicion determinada de la pagina web
            robot.mouseMove(450, 450);
            //mueve la rueda del mouse (para dejar la vista al comienzo de la pagina web)
            robot.mouseWheel(-500);
            Thread.sleep(1500);
            
            //manejo para cambiar el navegador a modo fullscreen
    		try {
    			robot = new Robot();
    			//se presiona la tecla F11 (Fullscreen)
    			robot.keyPress(KeyEvent.VK_F11);
    			robot.keyRelease(KeyEvent.VK_F11);
    			//retraso hasta que desaparezca el aviso del navegador del modo fullscreen
    			Thread.sleep(5000);
    		} catch (AWTException | InterruptedException e) {
    			e.printStackTrace();
    		}
            
            //crea una variable dimension, con el tamanio de la pantalla
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            //crea un rectangulo con la dimension recien creada
            Rectangle rectangle = new Rectangle(dimension);
            //saca una captura de pantalla del tamanio del rectangulo, la almacena en image
            BufferedImage image = robot.createScreenCapture(rectangle); //guarda la captura en la variable dinamica
            
            //se parsea navegador (parametro de entrada, de tipo WebDriver) a un RemoteWebDriver
            RemoteWebDriver navegadorRemoto = (RemoteWebDriver) navegador;
            //se obtienen las opciones del navegadorRemoto
            Capabilities cap = navegadorRemoto.getCapabilities();
            //obtiene el nombre del navegador de las opciones obtenidas anteriormente
            String nombreNavegador = cap.getBrowserName();
            
            //llama al metodo guardarImagen con la imagen image, y el nombre de navegador, la imagen sera de tipo png
            this.guardarImagen(image, nombreNavegador + ".png");
            
            //regresa un nuevo objeto de tipo Imagen, con el nombre del navegador y image (la imagen propiamente dicha)
            return new Imagen(nombreNavegador, image);
            
        //Excepciones
        } catch (AWTException ex) {
            System.out.println("No se encontro un dispositivo gráfico)");
            return null;
        } catch (Exception ex) {
            System.out.println("Error inesperado");
            return null;
        }
    }
    
    /**
     * Metodo para guardar una imagen en disco.
     * Parametros de entrada: una imagen de tipo BufferedImage, 
     * un string con el nombre de la imagen.
     * Devuelve un boolean, que sera true si se pudo guardar la imagen, caso contrario sera false.
     */
    public boolean guardarImagen(BufferedImage imagen, String nombre) {
    	
        try {
        	//llama al metodo write de la clase estatica ImageIO, para escribir la imagen en la ruta definida
            ImageIO.write(imagen, "png", new File(this.path + nombre));
            return true;
            
        //Excepcion: no se pudo guardar la imagen en disco
        } catch (IOException ex) {
            System.out.println("No se puede guardar la imagen en " + nombre);
            return false;
        }
    }
    
    /**
     * Metodo que compara dos imagenes con el algoritmo de comparacion.
     * Recibe como parametros de entrada: las imagenes de tipo Imagen: imagen1 e imagen2.
     * Devuelve un resultado de tipo Resultado.
     */
    public Resultado compararImagenes(Imagen imagen1, Imagen imagen2) {
    	
    	//crea un nuevo comparador de tipo Comparador para ejecutar el algoritmo de comparacion de imagenes
        Comparador comparador = new Comparador(0, 0);
        
        //ejecuta el algoritmo de comparacion sobre las dos imagenes recibidas como parametro de entrada
        Resultado resultado = comparador.ejecutarAlgoritmoDeComparacion(imagen1, imagen2);
        
        //llama al metodo para guardar el resultado de la comparacion en la ruta de destino definida por la variable path
        resultado.guardarResultado(this.path + "resultado.png");
        
        return resultado;
    }
    
    /**
     * Metodo que compara dos imagenes con el algoritmo de comparacion de pixeles a matriz de pixeles.
     * Recibe como parametros de entrada: las imagenes de tipo Imagen: imagen1 y imagen2,
     * un int delta para la matriz, un int toleranciaRGB para comparacion de pixeles.
     * Devuelve un resultado de tipo Resultado.
     */
    public Resultado compararImagenesPixAMatrizPix(Imagen imagen1, Imagen imagen2, int delta, int toleranciaRGB) {
    	
    	//crea un nuevo comparador de tipo Comparador para ejecutar el algoritmo de comparacion de imagenes
        Comparador comparador = new Comparador(delta, toleranciaRGB);
        
        //ejecuta el algoritmo de comparacion sobre las dos imagenes recibidas como parametro de entrada
        Resultado resultado = comparador.comparacionPixAMatrizPix(imagen1, imagen2);
        
        //llama al metodo para guardar el resultado de la comparacion en la ruta de destino definida por la variable path
        resultado.guardarResultado(this.path + "resultado.png");
        
        return resultado;
    }
    
    /**
     * Metodo para crear reportes.
     * Parametro de entrada: un objeto de tipo Resultado. 
     * El resultado es lo obtenido luego de aplicar el algoritmo de comparacion de imagenes.
     * Devuelve un boolean, segun si se puede generar el reporte correctamente o no.
     */
    public boolean crearReporte(Resultado resultado) {
        
    	//usa como plantilla del reporte el archivo html indicado
        return Utils.crearReporte(this.path + "reporte.html", 
        		resultado.getNavegador1(), 
        		resultado.getNavegador2(), 
                this.nombrePrueba, 
                this.fecha, 
                resultado.getCantPixTotal() - resultado.getCantPixDiferentes(), //cantidad de pixeles iguales
                resultado.getCantPixDiferentes(), 								//cantidad de pixeles diferentes
                resultado.getPorcentajePxDiff(),								//porcentaje de pixeles diferentes
                resultado.getDelta(),
                resultado.getToleranciaRGB());
    }

}
