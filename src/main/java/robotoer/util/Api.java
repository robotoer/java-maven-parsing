package robotoer.util;

import java.util.List;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class Api {
  private final String mName;
  private final List<Operation> mOperations;

  public Api(final String name) {
    this(name, ImmutableList.<Operation>builder().build());
  }

  public Api(final String name, final List<Operation> operations) {
    mName = name;
    mOperations = operations;
  }

  public String getName() {
    return mName;
  }

  public List<Operation> getOperations() {
    return mOperations;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return Objects.toStringHelper(Builder.class)
        .add("name", mName)
        .add("operations", mOperations.toString())
        .toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String mName;
    private ImmutableList.Builder<Operation> mOperationsBuilder;

    public Builder() { }
    public Builder(final Builder other) {
      mName = other.mName;
      mOperationsBuilder = other.mOperationsBuilder;
    }

    public String getName() {
      return mName;
    }

    public ImmutableList.Builder<Operation> getOperationsBuilder() {
      return mOperationsBuilder;
    }

    public Builder addOperation(
        final Operation operation
    ) {
      mOperationsBuilder.add(operation);
      return this;
    }

    public Builder addOperations(
        final Iterable<Operation> operations
    ) {
      mOperationsBuilder.addAll(operations);
      return this;
    }

    public Builder setOperations(
        final Iterable<Operation> operations
    ) {
      final ImmutableList.Builder<Operation> builder = ImmutableList.builder();
      builder.addAll(operations);
      mOperationsBuilder = builder;
      return this;
    }

    public Builder setName(
        final String name
    ) {
      mName = name;
      return this;
    }

    public void validate() {
      Preconditions.checkNotNull(mName, "Api name required. Current builder: ");
    }

    public Api build() {
      validate();
      return new Api(mName, mOperationsBuilder.build());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return Objects.toStringHelper(Builder.class)
          .add("name", mName)
          .add("operations", mOperationsBuilder.toString())
          .toString();
    }
  }
}
