package com.ecole.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumUITest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Vous devez avoir ChromeDriver installé et accessible via PATH
        driver = new ChromeDriver();
    }

    @Test
    public void testAjouterEtudiant() {
        driver.get("http://localhost:8080/etudiants/nouveau");

        WebElement nom = driver.findElement(By.name("nom"));
        WebElement prenom = driver.findElement(By.name("prenom"));
        WebElement bouton = driver.findElement(By.tagName("button"));

        nom.sendKeys("Ali");
        prenom.sendKeys("Diop");
        bouton.click();

        Assertions.assertTrue(driver.getPageSource().contains("Ali"));
        Assertions.assertTrue(driver.getPageSource().contains("Diop"));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
