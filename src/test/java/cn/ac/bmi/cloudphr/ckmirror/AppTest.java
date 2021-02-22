package cn.ac.bmi.cloudphr.ckmirror;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

public class AppTest {
  @Test
  public void appHasGreeting() {
    App classUnderTest = new App();
    assertNotNull(classUnderTest, "app should not be null!");
  }
}
