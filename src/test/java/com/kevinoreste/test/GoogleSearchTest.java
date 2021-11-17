package com.kevinoreste.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class GoogleSearchTest {

	private WebDriver driver;
	By contactoLinkLocator = By.linkText("Contáctenos");
	By contactoPageLocator = By.xpath("//h1[@class='entry-title']");

	By nombreUsuario = By.name("your-name");
	By mailUsuario = By.name("your-email");
	By asuntoUsuario = By.name("your-subject");
	By mensajeUsuario = By.name("your-message");
	By captchaUsuario = By.name("captcha-636");

	By botonEnviarForm = By.xpath("//input[@type='submit' and @value='Enviar']");

	@Before
	public void setUp() {

		System.setProperty("webdriver.chrome.driver", "./src/test/resources/ChromeDriver/chromedriver.exe");
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		driver.get("https://www.consultoriaglobal.com.ar/cgweb/");

	}

	@Test
	public void contactoCG() throws InterruptedException, IOException {

		ExtentReports extent = new ExtentReports();
		ExtentSparkReporter spark = new ExtentSparkReporter("index.html");
		
		spark.config().setEncoding("utf-8");
		
		spark.config().setTheme(Theme.DARK);
		spark.config().setDocumentTitle("Reporte");
		spark.config().setReportName("Automatización");
		extent.attachReporter(spark);
		
		
		ExtentTest prueba = extent.createTest("Ejercicio de Automatización");
		mostrarMensaje("Ejercicio de Automatización ha comenzado", prueba);
		mostrarMensaje("Abrimos la página de 'Consultoria Global'", prueba);
			
		driver.findElement(contactoLinkLocator).click();
		Thread.sleep(2000);
		mostrarMensaje("Abrimos la seccion de Contacto", prueba);
		

		Assert.assertTrue(driver.findElement(contactoPageLocator).isDisplayed());

		driver.findElement(nombreUsuario).sendKeys("Kevin");
		mostrarMensaje("Se ingreso el nombre", prueba);
		driver.findElement(mailUsuario).sendKeys("kevinorestehotmail");
		mostrarMensaje("Se ingreso el e-mail", prueba);
		driver.findElement(asuntoUsuario).sendKeys("Ejercicio");
		mostrarMensaje("Se ingreso el asunto", prueba);
		driver.findElement(mensajeUsuario).sendKeys("Automatización en JAVA");
		mostrarMensaje("Se ingreso el mensaje", prueba);
		driver.findElement(captchaUsuario).sendKeys("KA2E");
		mostrarMensaje("Se ingreso el captcha", prueba);
		

		driver.findElement(botonEnviarForm).click();
		mostrarMensaje("Se envio el formulario", prueba);
		
		Thread.sleep(2000);
		
		WebElement emailInvalido = driver.findElement(By.xpath("//span[text() = 'La dirección e-mail parece inválida.']"));
		
		Assert.assertEquals("La dirección e-mail parece inválida.", emailInvalido.getText());
		
		File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		
		byte[] fileContent = Files.readAllBytes(screenshotFile.toPath());

		prueba.log(Status.PASS,"Se detectó el mensaje de error de e-mail invalido",MediaEntityBuilder.createScreenCaptureFromBase64String(Base64.getEncoder().encodeToString(fileContent)).build());
		
		extent.flush();
		
	}
	
	public void mostrarMensaje(String mensaje, ExtentTest prueba) {
		
		System.out.println(mensaje);
		prueba.info(mensaje);
		
	}
	

	@After
	public void tearDown() {

		driver.quit();

	}

}
