package analisisVisual;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;

/**
 * Clase Utils
 */
public class Utils {    
    
	/**
	 * Metodo de clase que devuelve un string con la concatenacion 
	 * de valores del instante de tiempo en que se llama el metodo.
	 * Se concatenan los valores de: dia, mes, anio, hora, minuto, segundo.
	 */
    public static String getFecha() {    
            Date d = new Date();
            Calendar c = new GregorianCalendar(); 
            c.setTime(d);

            String dia, mes, annio, hora, minuto, segundo;

            dia = Integer.toString(c.get(Calendar.DATE));
            mes = Integer.toString(c.get(Calendar.MONTH));
            annio = Integer.toString(c.get(Calendar.YEAR));
            hora = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
            minuto = Integer.toString(c.get(Calendar.MINUTE));
            segundo = Integer.toString(c.get(Calendar.SECOND));
                            
            return(dia + "-" + mes +"-" + annio + " " + hora + "." + minuto + "." + segundo);
    }
    
    /**
    * Metodo de clase que crea una carpeta temporal llamada "tmp"
    * en la ruta especificada para la herramienta.
    */
    public static void crearCarpetaTMP() {
    	//se define la ruta para la carpeta del programa
        File carpeta = new File(System.getenv("RUTA"));
        
        //si no existe la carpeta en la ruta de destino
        if(!carpeta.exists()) {
        	//se crea la carpeta
            carpeta.mkdir();
        }
        
        //define la ruta de la carpeta temporal "tmp", dentro de la carpeta creada anteriormente
        File carpetaTMP = new File(carpeta + "//tmp");
        
        //si no existe la carpeta en la ruta de destino
        if(!carpetaTMP.exists()){
        	//se crea la carpeta
            carpetaTMP.mkdir();
        }
    }
    
    /**
    * Metodo de clase que crea una nueva carpeta en el destino 
    * que se define con el string recibido como parametro.
    */
    public static void crearCarpeta(String carpetaN) {
         File carpeta = new File(carpetaN);
         carpeta.mkdir();
    }
    
    /**
     * Metodo de clase para crear un reporte.
     * Parametros de entrada: un string donde se almacenara el reporte;
     * dos strings con los nombres de los dos navegadores comparados;
     * un string con el nombre de la prueba;
     * un string con la hora y fecha de ejecucion;
     * un integer, cantidad de pixeles iguales,
     * un integer, cantidad de pixeles diferentes,
     * un double porcentajePxDiff, porcentaje de pixeles diferentes.
     * Devuelve un boolean, sera true si se genera el reporte correctamente, sino devuelve false.
     * @param toleranciaRGB 
     * @param delta 
     */
    //@SuppressWarnings("deprecation")
	public static boolean crearReporte(String path, String browser1, String browser2, String nombre, 
			String fecha, int pxIgual, int pixDiff, double porcentajePxDiff, int delta, int toleranciaRGB) {
        
        try {
        	//define un string con la fecha y hora
            String fechaYhora = "Fecha: " + fecha.replaceAll(" ", " - Hora: ");
            
            String ruta = System.getenv("RUTA");
            		
            //almacena el contenido de una plantilla del reporte en un archivo File
            File templateReportes =  new File(ruta + "//utils//templateJarReportes.html");
            //almacena el contenido del archivo File en un string
            String htmlString = FileUtils.readFileToString(templateReportes, Charset.forName("utf-8"));
            
            //define el formato de porcentaje de pixeles diferentes
            DecimalFormat df = new DecimalFormat("0.00");
            
            //reemplaza expresiones del string anterior, por valores de los parametros ingresados al metodo
            //y lo almacena en un nuevo string
            String htmlReemplazado = htmlString.replaceAll("pxIgual", String.valueOf(pxIgual))
                    .replaceAll("pxIgual", String.valueOf(pxIgual))
                    .replaceAll("pxDiff", String.valueOf(pixDiff))
                    .replaceAll("browser1", browser1)
                    .replaceAll("browser2", browser2)
                    .replaceAll("nombre", nombre)
                    .replaceAll("fecha", fechaYhora)
                    .replaceAll("PorcentajePxDiff", df.format(porcentajePxDiff))
                    .replaceAll("delta", String.valueOf(delta))
                    .replaceAll("toleranciaRGB", String.valueOf(toleranciaRGB));
            
            //crea un nuevo archivo File en la ruta path especificada como parametro de entrada
            File newHtmlFile = new File(path);
            //escribe el string generado al File creado
            FileUtils.writeStringToFile(newHtmlFile, htmlReemplazado, Charset.forName("utf-8"));
            return true;
        } catch(IOException e) {
        	//no se pudo generar el reporte
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Metodo para mostrar una ventana de error con un mensaje recibido como parametro de entrada
     */
    public static void mostrarPopupError(String mensaje){
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Metodo para reemplazar caracteres invalidos en un string recibido como parametro de entrada
     */
    public static String removerCaracteresInvalidos(String nombre) {
        
        return nombre.replaceAll("ñ", "n")
                     .replaceAll("/", "")
                     .replaceAll(":", "")
                     .replaceAll("¿", "")
                     .replaceAll("|", "")
                     .replaceAll("Ñ", "N")
                     .replaceAll("@", "")
                     .replaceAll("$", "")
                     .replaceAll("%", "")
                     .replaceAll("°", "")
                     .replaceAll("<", "")
                     .replaceAll(">", "")
                     .replaceAll("\\\\", "")
                     .replaceAll("\\*", "")
                     .replaceAll("\\?", "")
                     .replaceAll("'", "")
                     .replaceAll("!", "")
                     .replaceAll("¡", "")
                     .replaceAll("\\{", "")
                     .replaceAll("}", "")
                     .replaceAll("\\+", "")
                     .replaceAll("^", "")
                     .replaceAll("~", "")
                     .replaceAll("]", "")
                     .replaceAll("\\[", "")
                     .replaceAll("´", "")
                     .replaceAll("\"", "");
    }
    
    /**
     * Metodo que devuelve un string segun si se encuentran 
     * ciertas cadenas en el string de entrada.
     * Los mensajes que devuelve hacen referencia a problemas 
     * de instalacion de los componentes basicos para que funcione el programa.
     */
    public static String obtenerBrowserInvalido(String mensaje){
    
    	//switch
        if(mensaje.toUpperCase().contains("CHROME"))
            return "El archivo Chromedriver.exe no se encuentra instalado en el sistema.";
        
        if(mensaje.toUpperCase().contains("FIREFOX"))
            return "El navegador firefox no se encuntra instalado en el sistema.";
        
        if(mensaje.toUpperCase().contains("EXPLORER") && mensaje.toUpperCase().contains("UNABLE TO FIND ELEMENT"))
            return "La versión del navegador Internet Explorer debe ser igual o inferior a la 10 ";
        
        if(mensaje.toUpperCase().contains("IEDRIVER"))
            return "El archivo iedriver.exe no se encuentra instalado en el sistema.";
        
        if(mensaje.toUpperCase().contains("OPERA"))
            return "El navegador Opera no se encuntra instalado en el sistema.";
        
        if(mensaje.toUpperCase().contains("EXPLORER"))
            return "El navegador Explorer no se encuntra instalado en el sistema.";
        
        return "";
    }
     
}
