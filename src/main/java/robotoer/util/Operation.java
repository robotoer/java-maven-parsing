package robotoer.util;

import java.util.Map;

/**
 */
public class Operation {
  private final Map<String, String> mParameterTypes;

  public Operation(final Map<String, String> parameterTypes) {
    this.mParameterTypes = parameterTypes;
  }

  public Map<String, String> getParameterTypes() {
    return mParameterTypes;
  }
}
