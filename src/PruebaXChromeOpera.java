import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;

import analisisVisual.Imagen;
import analisisVisual.Manipulador;
import analisisVisual.Resultado;

/**
 * Prueba para Chrome y Opera, con www.google.com
 */
public class PruebaXChromeOpera {
	public static void main(String[] args) {

		Manipulador manipulador = new Manipulador("Prueba Google - Chrome-Opera", "C:\\xtest");

		//CHROME
		System.setProperty("webdriver.chrome.driver", "C:\\browser-drivers\\chromedriver\\chromedriver.exe");
		
		//crea un options para Chrome
		ChromeOptions options = new ChromeOptions();
		//para desactivar la barra "Chrome is being controlled by automated test software"
		options.addArguments("disable-infobars");
		
		//crea el WebDriver para Chrome pasando como parametro el options creado anteriormente
		WebDriver chromeDriver = new ChromeDriver(options);
		
		//abre la pagina en chrome
		chromeDriver.get("https://www.google.com");
		//realiza la captura de pantalla
		Imagen img1 = manipulador.capturarPantallaFullscreen(chromeDriver);
		chromeDriver.close();

		//OPERA
		System.setProperty("webdriver.opera.driver","C:\\browser-drivers\\operadriver\\operadriver.exe");
		
		OperaOptions operaOptions = new OperaOptions();
		operaOptions.setBinary(new File("C:\\Program Files\\Opera\\53.0.2907.99\\opera.exe"));
		
		//crea el WebDriver para opera
		WebDriver operaDriver = new OperaDriver(operaOptions);
		
		operaDriver.get("https://www.google.com");
		Imagen img2 = manipulador.capturarPantallaFullscreen(operaDriver);
		
		//ejecuta la comparacion pixel a matriz de pixeles
		//se envian las dos imagenes, los valores de delta y toleranciaRGB
		Resultado resultado1 = manipulador.compararImagenesPixAMatrizPix(img1, img2, 4, 25);
		manipulador.crearReporte(resultado1);
		
		operaDriver.close();
	}
}
