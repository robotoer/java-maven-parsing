package robotoer.util;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;
import robotoer.ast.java.JavaBaseVisitor;
import robotoer.ast.java.JavaParser;

public class JavaPrettyPrinter3 extends JavaBaseVisitor<String> {
  private static final String TAB_STRING = "  ";
  private int mDepth;

  public static String prettyPrint(
      final JavaParser parser
  ) {
    return new JavaPrettyPrinter3(parser).visit(parser.compilationUnit());
  }

  private final JavaParser mParser;

  public JavaPrettyPrinter3(final JavaParser parser) {
    mParser = parser;
  }

  protected String getLeftMargin() {
    final StringBuilder marginBuilder = new StringBuilder();
    for (int i = 0; i < mDepth; i++) {
      marginBuilder.append(TAB_STRING);
    }
    return marginBuilder.toString();
  }
  protected int increaseDepth() {
    return mDepth++;
  }
  protected int decreaseDepth() {
    return mDepth--;
  }

  public JavaParser getParser() {
    return mParser;
  }

  @Override
  protected String aggregateResult(
      final String aggregate,
      final String nextResult
  ) {
    if (nextResult == null) {
      return aggregate;
    } else {
      if (aggregate == null) {
        return nextResult;
      } else {
        return aggregate + nextResult;
      }
    }
  }

  @Override
  public String visitPackageDeclaration(
      final JavaParser.PackageDeclarationContext ctx
  ) {
    return String.format(
        "package %s;\n",
        super.visitPackageDeclaration(ctx)
    );
  }

  @Override
  public String visitImportDeclaration(
      final JavaParser.ImportDeclarationContext ctx
  ) {
    return String.format(
        "import %s;\n",
        super.visitImportDeclaration(ctx)
    );
  }

  @Override
  public String visitQualifiedName(
      final JavaParser.QualifiedNameContext ctx
  ) {
    final StringBuilder stringBuilder = new StringBuilder();
    final Iterator<TerminalNode> identifiers = ctx.Identifier().iterator();
    stringBuilder.append(identifiers.next().getSymbol().getText());
    while (identifiers.hasNext()) {
      stringBuilder.append(".");
      stringBuilder.append(identifiers.next().getSymbol().getText());
    }
    return stringBuilder.toString();
  }

  @Override
  public String visitTypeDeclaration(
      final JavaParser.TypeDeclarationContext ctx
  ) {
    final StringBuilder codeBuilder = new StringBuilder();
    {
      final List<JavaParser.ClassOrInterfaceModifierContext> modifierContexts =
          ctx.classOrInterfaceModifier();

      // TODO: Sort this.
      for (JavaParser.ClassOrInterfaceModifierContext context : modifierContexts) {
        codeBuilder.append(visitClassOrInterfaceModifier(context));
      }
    }

    final JavaParser.ClassDeclarationContext classDeclaration = ctx.classDeclaration();
    final JavaParser.EnumDeclarationContext enumDeclaration = ctx.enumDeclaration();
    final JavaParser.InterfaceDeclarationContext interfaceDeclaration = ctx.interfaceDeclaration();
    final JavaParser.AnnotationTypeDeclarationContext annotationDeclaration =
        ctx.annotationTypeDeclaration();

    final boolean isClass = classDeclaration != null;
    final boolean isEnum = enumDeclaration != null;
    final boolean isInterface = interfaceDeclaration != null;
    final boolean isAnnotation = annotationDeclaration != null;

    Preconditions.checkState(
        (isClass ? 1 : 0) + (isEnum ? 1 : 0) + (isInterface ? 1 : 0) + (isAnnotation ? 1 : 0) == 1
    );

    if (isClass) {
      codeBuilder.append("class ");
      codeBuilder.append(classDeclaration.Identifier().getSymbol().getText());

      // Handle 'implements' and 'extends'.
      if (classDeclaration.typeParameters() != null) {
        codeBuilder.append(" ");
        codeBuilder.append(visitTypeParameters(classDeclaration.typeParameters()));
      }
      if (classDeclaration.type() != null) {
        codeBuilder.append(" extends ");
        codeBuilder.append(visitType(classDeclaration.type()));
      }
      if (classDeclaration.typeList() != null) {
        codeBuilder.append(" implements ");
        codeBuilder.append(visitTypeList(classDeclaration.typeList()));
      }

      codeBuilder.append(" ");
      codeBuilder.append(visitClassBody(classDeclaration.classBody()));
    } else if (isEnum) {
      codeBuilder.append(visitEnumDeclaration(enumDeclaration));
    } else if (isInterface) {
      codeBuilder.append("interface ");
      codeBuilder.append(interfaceDeclaration.Identifier().getSymbol().getText());

      // Handle 'extends'.
      if (interfaceDeclaration.typeParameters() != null) {
        codeBuilder.append(" ");
        codeBuilder.append(visitTypeParameters(interfaceDeclaration.typeParameters()));
      }
      if (interfaceDeclaration.typeList() != null) {
        codeBuilder.append(" extends ");
        codeBuilder.append(visitTypeList(interfaceDeclaration.typeList()));
      }

      codeBuilder.append(" ");
      codeBuilder.append(visitInterfaceBody(interfaceDeclaration.interfaceBody()));
    } else if (isAnnotation) {
      codeBuilder.append("@annotation ");
      codeBuilder.append(visitAnnotationTypeDeclaration(annotationDeclaration));
    } else {
      throw new IllegalStateException("Unsupported type declaration");
    }

    return codeBuilder.toString();
  }

  @Override
  public String visitClassDeclaration(@NotNull JavaParser.ClassDeclarationContext ctx) {
    return super.visitClassDeclaration(ctx);
  }

  @Override
  public String visitInterfaceBody(
      @NotNull final JavaParser.InterfaceBodyContext ctx
  ) {
    final StringBuilder codeBuilder = new StringBuilder();

    codeBuilder.append(" {\n");
    increaseDepth();

    codeBuilder.append(super.visitInterfaceBody(ctx));

    decreaseDepth();
    codeBuilder.append("\n}");

    return codeBuilder.toString();
  }

  @Override
  public String visitInterfaceBodyDeclaration(
      @NotNull final JavaParser.InterfaceBodyDeclarationContext ctx
  ) {
    final StringBuilder codeBuilder = new StringBuilder();

    for (JavaParser.ModifierContext context : ctx.modifier()) {
      codeBuilder.append(visitModifier(context));
    }
    codeBuilder.append(ctx.);
    codeBuilder.append(";\n");

    return super.visitInterfaceBodyDeclaration(ctx);
  }

  @Override
  public String visitClassOrInterfaceModifier(
      @NotNull final JavaParser.ClassOrInterfaceModifierContext ctx
  ) {
    final StringBuilder codeBuilder = new StringBuilder();
    final JavaParser.AnnotationContext annotationContext = ctx.annotation();
    if (annotationContext == null) {
      codeBuilder.append(super.visitClassOrInterfaceModifier(ctx));
    } else {
      final JavaParser.ElementValueContext elementValue = annotationContext.elementValue();
      final JavaParser.ElementValuePairsContext elementValuePairs = annotationContext.elementValuePairs();
      final boolean isElementValue = elementValue != null;
      final boolean isElementValuePairs = elementValuePairs != null;

      codeBuilder.append("@");
      codeBuilder.append(visitQualifiedName(annotationContext.annotationName().qualifiedName()));
      if (isElementValue || isElementValuePairs) {
        codeBuilder.append("(");
        if (isElementValue) {
          codeBuilder.append(visitElementValue(elementValue));
        } else {
          codeBuilder.append(visitElementValuePairs(elementValuePairs));
        }
        codeBuilder.append(")");
      }
      codeBuilder.append("\n");
    }
    return codeBuilder.toString();
  }
}
