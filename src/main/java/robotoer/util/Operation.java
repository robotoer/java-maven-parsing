package robotoer.util;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class Operation {
  private final String mName;
  private final Map<String, TypeOrClass> mParameterTypes;

  public Operation(
      final String name,
      final Map<String, TypeOrClass> parameterTypes
  ) {
    mName = name;
    mParameterTypes = parameterTypes;
  }

  public String getName() {
    return mName;
  }

  public Map<String, TypeOrClass> getParameterTypes() {
    return mParameterTypes;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Operation statement(final String name) {
    return new Operation(name, ImmutableMap.<String, TypeOrClass>builder().build());
  }

  public static class Builder {
    private String mName;
    private ImmutableMap.Builder<String, TypeOrClass> mParameterTypesBuilder;

    public Builder() { }

    public String getName() {
      return mName;
    }

    public ImmutableMap.Builder<String, TypeOrClass> getParameterTypes() {
      return mParameterTypesBuilder;
    }

    public Builder setName(String name) {
      this.mName = name;
      return this;
    }

    public Builder setParameterTypes(ImmutableMap.Builder<String, TypeOrClass> parameterTypesBuilder) {
      this.mParameterTypesBuilder = parameterTypesBuilder;
      return this;
    }

    public Builder addParameter(
        final String name,
        final TypeOrClass parameterType
    ) {
      mParameterTypesBuilder.put(name, parameterType);
      return this;
    }

    public Operation build() {
      return new Operation(mName, mParameterTypesBuilder.build());
    }
  }
}
