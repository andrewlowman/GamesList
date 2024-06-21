package com.lowman.gameslist.Utility;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HLTB {

    private WebDriver driver;
    private List<WebElement> gamesList;
    private String gameName;
    WebDriverWait wait;
    public HLTB(){

        driver = new FirefoxDriver();
        driver.manage().window().maximize();


    }

    public void searchGameName(String name){
        driver.get("https://howlongtobeat.com/");
        gameName = name;

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class='MainNavigation_search_box__UUnYc back_form']")));
        WebElement searchTextBox = driver.findElement(By.xpath("//input[@class='MainNavigation_search_box__UUnYc back_form']"));
        searchTextBox.sendKeys(gameName);
        //click triggers the page to update
        searchTextBox.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='GameCard_search_list_image__B2uLH']")));
        gamesList = driver.findElements(By.xpath("//div[@class='GameCard_search_list_image__B2uLH']"));
        gamesList.get(0).click();
    }

    public String getGameRating(){
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='text_primary']//h5[@style='margin-top: -87px; text-align: center;']")));
        WebElement rating = driver.findElement(By.xpath("//a[@class='text_primary']//h5[@style='margin-top: -87px; text-align: center;']"));
        //System.out.println(rating.getText());
        //trimming the results so it is just numbers and getting a rating out of ten
        float numberRating = Float.parseFloat(rating.getText().replaceAll("[^0-9]",""));
        numberRating = numberRating/10;
        return String.valueOf(numberRating);
    }

    public String getGameHLTB(){
        //wait.until(ExpectedConditions.presenceOfElementLocated())
        WebElement time = driver.findElement(By.xpath("//td[text()='Main + Extras']/following-sibling::td[3]"));
       /* wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[@class='GameStats_short__tSJ6I time_60']//h5")));
        WebElement time = driver.findElement(By.xpath("//li[@class='GameStats_short__tSJ6I time_60']//h5"));*/
        //System.out.println(time.getText());
        return time.getText().substring(0,time.getText().indexOf("h"));
    }
}
