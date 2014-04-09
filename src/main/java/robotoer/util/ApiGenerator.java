package robotoer.util;

public interface ApiGenerator {
  public String generateApi(final Api api);

  class Java implements ApiGenerator {
    @Override
    public String generateApi(Api api) {
      // Write out the api as a java interface.
      return null;
    }
  }

  class KijiCli implements ApiGenerator {
    @Override
    public String generateApi(Api api) {
      // Write a Kiji CLI tool.
      return null;
    }
  }

  class PythonCli implements ApiGenerator {
    @Override
    public String generateApi(Api api) {
      return null;
    }
  }

  class Http implements ApiGenerator {
    @Override
    public String generateApi(Api api) {
      return null;
    }
  }
}
