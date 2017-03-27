import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Kim
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebGuiTest {

    static WebDriver driver;
    private static final int WAIT_MAX = 4;

    public WebGuiTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Kim\\Desktop\\Bachelor Software Development\\test\\HandIn Selenium\\drivers\\chromedriver.exe");

        //Reset Database
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
        driver = new ChromeDriver();
        driver.get("http://localhost:3000");
    }

    @AfterClass
    public static void tearDownClass() {
        driver.quit();
        //Reset Database 
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    //Verify that page is loaded and all expected data are visible
    public void test1() throws Exception {
        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement e = d.findElement(By.tagName("tbody"));
            List<WebElement> rows = e.findElements(By.tagName("tr"));
            Assert.assertThat(rows.size(), is(5));
            return true;
        });
    }

    @Test
    //Verify the filter functionality 
    public void test2() throws Exception {
        WebElement element = driver.findElement(By.id("filter"));
        element.sendKeys("2002");
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        Assert.assertThat(rows.size(), is(2));
    }

    @Test
    //Verify the reverse filter functionality 
    public void test3() throws Exception {
        WebElement element = driver.findElement(By.id("filter"));
        element.sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE);
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        Assert.assertThat(rows.size(), is(5));
    }

    @Test
    //Verify the year sorting functionality 
    public void test4() throws Exception {
        WebElement element = driver.findElement(By.id("h_year"));
        element.click();
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));

        WebElement firstRow = rows.get(0);
        String firstRow_id = firstRow.findElement(By.tagName("td")).getText();
        WebElement lastRow = rows.get((rows.size() - 1));
        String lastRow_id = lastRow.findElement(By.tagName("td")).getText();

        Assert.assertThat(firstRow_id, is("938"));
        Assert.assertThat(lastRow_id, is("940"));
    }

    @Test
    //Verify the edit functionality 
    public void test5() throws Exception {
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));

        rows.forEach((row) -> {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            String id = columns.get(0).getText();

            if (id.equals("938")) {
                List<WebElement> actions = columns.get((columns.size() - 1)).findElements(By.tagName("a"));

                WebElement action_edit = actions.get(0);
                action_edit.click();

                WebElement id_input = driver.findElement(By.id("id"));
                Assert.assertThat(id_input.getAttribute("value"), is("938"));
            }
        });
    }

    @Test
    //Verify the new car error functionality 
    public void test6() throws Exception {
        WebElement btn_newCar = driver.findElement(By.id("new"));
        WebElement btn_saveCar = driver.findElement(By.id("save"));
        WebElement txt_err = driver.findElement(By.id("submiterr"));

        btn_newCar.click();
        btn_saveCar.click();

        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));

        Assert.assertThat(txt_err.getText(), is("All fields are required"));
        Assert.assertThat(rows.size(), is(5));
    }

    @Test
    //Verify the add new car functionality 
    public void test7() throws Exception {
        WebElement btn_newCar = driver.findElement(By.id("new"));
        WebElement btn_saveCar = driver.findElement(By.id("save"));

        btn_newCar.click();

        driver.findElement(By.id("year")).sendKeys("2017");
        driver.findElement(By.id("registered")).sendKeys("2017-1-2");
        driver.findElement(By.id("make")).sendKeys("Mercedes Benz");
        driver.findElement(By.id("model")).sendKeys("AMG C 63 S Coupe");
        driver.findElement(By.id("description")).sendKeys("Kims dyt");
        driver.findElement(By.id("price")).sendKeys("1603200");

        btn_saveCar.click();

        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        Assert.assertThat(rows.size(), is(6));
    }   
}