import com.microsoft.playwright.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.nio.file.Paths;


public class BaseTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    protected Page page;

    @BeforeClass
    protected void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()//.setHeadless(false)
        );
    }

    @BeforeMethod
    protected void createContextAndPage() {
        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setBaseURL(System.getenv("BASE_URL"))
        );

        context.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );

        page = context.newPage();
        login();
    }

    @AfterMethod
    protected void closeContext(Method method, ITestResult testResult) {
        Tracing.StopOptions tracingStopOptions = null;
        String classMethodName = this.getClass().getName() + method.getName();
        if (!testResult.isSuccess()) {
            tracingStopOptions = new Tracing.StopOptions()
                    .setPath(Paths.get("testTracing/" + classMethodName + ".zip"));
        }
        context.tracing().stop(
                tracingStopOptions
        );

        context.close();
    }

    @AfterClass
    protected void closeBrowser() {
        browser.close();
        playwright.close();
    }

    private void login () {
        String username = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");

        System.out.println(username);
        System.out.println(password);

        page.navigate("/");
        page.locator("//span[text()='Email']/../div/input").fill(username);
        page.locator("//input[@type='password']").fill(password);
        page.locator("//button[@type='submit']").click();
    }
}
