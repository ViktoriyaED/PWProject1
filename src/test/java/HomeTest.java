import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomeTest extends BaseTest {

    @Test
    public void testLoginNavigation() {

        assertThat(page).hasURL("https://study.traineracademy.org/home");
        assertThat(page).hasTitle("Trainer Academy");
    }
}