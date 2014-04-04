package robotoer.project;

/**
 */
public class TestUtils {
  public static class TestEnvironment {
    public <T> T createInstance(final Class<T> type)
        throws IllegalAccessException, InstantiationException {
      return type.newInstance();
    }
  }

  public static TestEnvironment generic() {
    return new TestEnvironment();
  }
}
