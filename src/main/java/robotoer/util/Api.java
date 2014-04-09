package robotoer.util;

import java.util.Map;

/**
 */
public class Api {
  private final Map<String, Operation> mOperations;

  public Api(final Map<String, Operation> operations) {
    this.mOperations = operations;
  }

  public Map<String, Operation> getOperations() {
    return mOperations;
  }
}
