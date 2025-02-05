package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@RunWith(Cucumber.class)
@IncludeEngines("cucumber")
@SelectClasspathResource("/features")
@CucumberOptions(
        glue = "cucumber.stepdefinitions"
)
@ConfigurationParameter(
        key = "cucumber.plugin",
        value = "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm," +
                "pretty," +
                "html:target/cucumber-report/cucumber.html"
)
public class CucumberTestsRunner {
}
