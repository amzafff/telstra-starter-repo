package stepDefinitions;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(
        classes = au.com.telstra.simcardactivator.SimCardActivator.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT // or RANDOM_PORT
)
public class CucumberSpringConfiguration {
}
