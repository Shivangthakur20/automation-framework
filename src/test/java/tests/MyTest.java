package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.GooglePage;

public class MyTest extends BaseTest {

    @Test(groups = {"regression"})
    public void regressionTest_googleSearch() {
        getDriver().get("https://www.google.com");
        GooglePage googlePage = new GooglePage(getDriver());
        googlePage.searchFor("Regression Test");
        System.out.println("Regression test executed");
    }

    @Test(groups = {"smoke"})
    public void smokeTest_googleSearch() {
        getDriver().get("https://www.google.com");
        Assert.fail("Intentional failure");    }



}