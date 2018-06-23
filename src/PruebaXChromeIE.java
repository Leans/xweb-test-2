import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import analisisVisual.Imagen;
import analisisVisual.Manipulador;
import analisisVisual.Resultado;

/**
 * Prueba para Chrome y IE, con www.google.com
 */
public class PruebaXChromeIE {
	public static void main(String[] args) {

		Manipulador manipulador = new Manipulador("Prueba Google - Chrome-IE", "C:\\xtest");

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

		//IE
		System.setProperty("webdriver.ie.driver","C:\\browser-drivers\\iedriver86\\IEDriverServer.exe");
		
		//crea el WebDriver para internet explorer
		WebDriver IEDriver = new InternetExplorerDriver();
		
		IEDriver.get("https://www.google.com");
		Imagen img2 = manipulador.capturarPantallaFullscreen(IEDriver);
		
		//ejecuta la comparacion pixel a matriz de pixeles
		//se envian las dos imagenes, los valores de delta y toleranciaRGB
		Resultado resultado1 = manipulador.compararImagenesPixAMatrizPix(img1, img2, 4, 25);
		manipulador.crearReporte(resultado1);
		
		IEDriver.close();		
	}
}
