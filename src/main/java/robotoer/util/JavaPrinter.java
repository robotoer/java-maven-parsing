package robotoer.util;

import robotoer.ast.java.JavaBaseListener;
import robotoer.ast.java.JavaParser;

public class JavaPrinter extends JavaBaseListener {
  private final StringBuilder mCodeBuilder = new StringBuilder();

  @Override
  public void enterImportDeclaration(final JavaParser.ImportDeclarationContext ctx) {
    final String importCode = String.format(
        "import %s;",
        ctx.qualifiedName().Identifier().toString()
    );
    System.out.println(importCode);
  }

  @Override
  public void enterInterfaceDeclaration(final JavaParser.InterfaceDeclarationContext ctx) {
    final String interfaceCode = String.format(
        "%s interface %s {",
        "public",
        ctx.Identifier().toString()
    );
    System.out.println(interfaceCode);
  }

  @Override
  public void exitInterfaceDeclaration(final JavaParser.InterfaceDeclarationContext ctx) {
    final String interfaceCode = "}";
    System.out.println(interfaceCode);
  }


  @Override
  public void enterInterfaceMethodDeclaration(
      final JavaParser.InterfaceMethodDeclarationContext ctx
  ) {
  }
  @Override
  public void exitInterfaceMethodDeclaration(
      final JavaParser.InterfaceMethodDeclarationContext ctx
  ) {
  }

  @Override public void enterInterfaceBodyDeclaration(final JavaParser.InterfaceBodyDeclarationContext ctx) { }
  @Override public void exitInterfaceBodyDeclaration(final JavaParser.InterfaceBodyDeclarationContext ctx) { }
  @Override public void enterGenericInterfaceMethodDeclaration(final JavaParser.GenericInterfaceMethodDeclarationContext ctx) { }
  @Override public void exitGenericInterfaceMethodDeclaration(final JavaParser.GenericInterfaceMethodDeclarationContext ctx) { }
  @Override public void enterClassOrInterfaceModifier(final JavaParser.ClassOrInterfaceModifierContext ctx) { }
  @Override public void exitClassOrInterfaceModifier(final JavaParser.ClassOrInterfaceModifierContext ctx) { }
  @Override public void enterInterfaceBody(final JavaParser.InterfaceBodyContext ctx) { }
  @Override public void exitInterfaceBody(final JavaParser.InterfaceBodyContext ctx) { }
  @Override public void enterClassOrInterfaceType(final JavaParser.ClassOrInterfaceTypeContext ctx) { }
  @Override public void exitClassOrInterfaceType(final JavaParser.ClassOrInterfaceTypeContext ctx) { }
  @Override public void enterInterfaceMemberDeclaration(final JavaParser.InterfaceMemberDeclarationContext ctx) { }
  @Override public void exitInterfaceMemberDeclaration(final JavaParser.InterfaceMemberDeclarationContext ctx) { }
}
