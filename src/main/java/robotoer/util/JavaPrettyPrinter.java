package robotoer.util;

import org.antlr.v4.runtime.tree.TerminalNode;
import robotoer.ast.java.JavaBaseListener;
import robotoer.ast.java.JavaParser;

public class JavaPrettyPrinter extends JavaBaseListener {
  public static final String TAB_STRING = "  ";

  private final StringBuilder mCodeBuilder = new StringBuilder();
  private final JavaParser mParser;

  private int mDepth = 0;

  public JavaPrettyPrinter(JavaParser parser) {
    mParser = parser;
  }

  protected String getLeftMargin() {
    final StringBuilder marginBuilder = new StringBuilder();
    for (int i = 0; i < mDepth; i++) {
      marginBuilder.append(TAB_STRING);
    }
    return marginBuilder.toString();
  }
  @Override
  public void enterImportDeclaration(final JavaParser.ImportDeclarationContext ctx) {
    System.out.println();
  }

//  @Override
//  public void enterImportDeclaration(final JavaParser.ImportDeclarationContext ctx) {
//    final StringBuilder qualifiedNameBuilder = new StringBuilder();
//    for (TerminalNode nameComponent : ctx.qualifiedName().Identifier()) {
//      qualifiedNameBuilder.append(".");
//      qualifiedNameBuilder.append(nameComponent.toString());
//    }
//    final String code = String.format("import %s;", qualifiedNameBuilder.toString());
//    System.out.println(String.format("%s%s", getLeftMargin(), code));
//  }
//
//
//  @Override
//  public void enterInterfaceDeclaration(final JavaParser.InterfaceDeclarationContext ctx) {
//    final String code = String.format(
//        "%s interface %s {",
//        "public",
//        ctx.Identifier().toString()
//    );
//    System.out.println(String.format("%s%s", getLeftMargin(), code));
//  }
//
//  @Override
//  public void exitInterfaceDeclaration(final JavaParser.InterfaceDeclarationContext ctx) {
//    final String code = "}";
//    System.out.println(String.format("%s%s", getLeftMargin(), code));
//  }
//
//
//  @Override
//  public void enterInterfaceMethodDeclaration(
//      final JavaParser.InterfaceMethodDeclarationContext ctx
//  ) {
//    final String code = String.format(
//        "%s %s %s;",
//        ctx.type().toStringTree(mParser),
//        ctx.Identifier().toStringTree(mParser),
//        ctx.formalParameters().toStringTree(mParser)
//    );
//    System.out.println(String.format("%s%s", getLeftMargin(), code));
//  }
//
//
//  @Override
//  public void enterInterfaceBodyDeclaration(
//      final JavaParser.InterfaceBodyDeclarationContext ctx
//  ) {
//    final String code = "body {";
//    System.out.println(String.format("%s%s", getLeftMargin(), code));
//    mDepth++;
//  }
//  @Override
//  public void exitInterfaceBodyDeclaration(
//      final JavaParser.InterfaceBodyDeclarationContext ctx
//  ) {
//    mDepth--;
//    final String code = "}";
//    System.out.println(String.format("%s%s", getLeftMargin(), code));
//  }
//
//
//  @Override public void enterGenericInterfaceMethodDeclaration(final JavaParser.GenericInterfaceMethodDeclarationContext ctx) { }
//  @Override public void exitGenericInterfaceMethodDeclaration(final JavaParser.GenericInterfaceMethodDeclarationContext ctx) { }
//  @Override public void enterClassOrInterfaceModifier(final JavaParser.ClassOrInterfaceModifierContext ctx) { }
//  @Override public void exitClassOrInterfaceModifier(final JavaParser.ClassOrInterfaceModifierContext ctx) { }
//  @Override public void enterInterfaceBody(final JavaParser.InterfaceBodyContext ctx) { }
//  @Override public void exitInterfaceBody(final JavaParser.InterfaceBodyContext ctx) { }
//  @Override public void enterClassOrInterfaceType(final JavaParser.ClassOrInterfaceTypeContext ctx) { }
//  @Override public void exitClassOrInterfaceType(final JavaParser.ClassOrInterfaceTypeContext ctx) { }
//  @Override public void enterInterfaceMemberDeclaration(final JavaParser.InterfaceMemberDeclarationContext ctx) { }
//  @Override public void exitInterfaceMemberDeclaration(final JavaParser.InterfaceMemberDeclarationContext ctx) { }

  @Override
  public void visitTerminal(
      final TerminalNode node
  ) {
    final String nodeType = mParser.getTokenNames()[node.getSymbol().getType()];
    final String code = node.getSymbol().getText();
    System.out.println(String.format("%s%s(%s)", getLeftMargin(), nodeType, code));
  }

  public static String format(JavaParser.CompilationUnitContext compilationUnit) {
    throw new UnsupportedOperationException("Not implemented yet.");
  }
}
