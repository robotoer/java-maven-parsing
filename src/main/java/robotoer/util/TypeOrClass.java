package robotoer.util;

import java.util.Map;

public interface TypeOrClass {
  Map<String, ApiSerDe> getConverters();

  public static StringType STRING = new StringType();
  public static IntegerType INT = new IntegerType();
  public static LongType LONG = new LongType();
  public static FloatType FLOAT = new FloatType();
  public static DoubleType DOUBLE = new DoubleType();
  public static CharacterType CHAR = new CharacterType();

//  public static MapType map(
//      final TypeOrClass keyType,
//      final TypeOrClass valueType
//  ) {
//    return new MapType(keyType, valueType);
//  }

  public static class StringType implements TypeOrClass {
    @Override
    public Map<String, ApiSerDe> getConverters() {
      return null;
    }
  }

  public static class IntegerType implements TypeOrClass {
    @Override
    public Map<String, ApiSerDe> getConverters() {
      return null;
    }
  }

  public static class LongType implements TypeOrClass {
    @Override
    public Map<String, ApiSerDe> getConverters() {
      return null;
    }
  }

  public static class FloatType implements TypeOrClass {
    @Override
    public Map<String, ApiSerDe> getConverters() {
      return null;
    }
  }

  public static class DoubleType implements TypeOrClass {
    @Override
    public Map<String, ApiSerDe> getConverters() {
      return null;
    }
  }

  public static class CharacterType implements TypeOrClass {
    @Override
    public Map<String, ApiSerDe> getConverters() {
      return null;
    }
  }

  public static class MapType implements TypeOrClass {
    private final TypeOrClass mKeyType;
    private final TypeOrClass mValueType;

    public MapType(final TypeOrClass keyType, final TypeOrClass valueType) {
      mValueType = keyType;
      mKeyType = keyType;
    }

    @Override
    public Map<String, ApiSerDe> getConverters() {
      return null;
    }

    public TypeOrClass getKeyType() {
      return mKeyType;
    }

    public TypeOrClass getValueType() {
      return mValueType;
    }
  }

  public static class Optional implements TypeOrClass {
    private final TypeOrClass mOptionalType;

    public Optional(final TypeOrClass optionalType) {
      mOptionalType = optionalType;
    }

    public TypeOrClass getOptionalType() {
      return mOptionalType;
    }

    @Override
    public Map<String, ApiSerDe> getConverters() {
      return null;
    }
  }
}
