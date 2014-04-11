package robotoer.util;

import robotoer.ast.java.JavaBaseVisitor;
import robotoer.ast.java.JavaParser;

public class JavaPrettyPrinter3 extends JavaBaseVisitor<String> {
  public static String prettyPrint(
      final JavaParser parser
  ) {
    return new JavaPrettyPrinter2(parser).visit(parser.compilationUnit());
  }

  private final JavaParser mParser;

  public JavaPrettyPrinter3(final JavaParser parser) {
    mParser = parser;
  }

  public JavaParser getParser() {
    return mParser;
  }

  @Override
  public String visitPackageDeclaration(
      final JavaParser.PackageDeclarationContext ctx
  ) {
    ctx.toStringTree()
    String.format(
        "package %s;"
    );
    return visitChildren(ctx);
  }
}
