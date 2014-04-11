package robotoer.util;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import robotoer.ast.java.JavaParser;

public class JavaPrettyPrinter2 implements SimpleJavaVisitor<String> {
  private final JavaParser mParser;

  public JavaPrettyPrinter2(final JavaParser parser) {
    mParser = parser;
  }

  public String defaultPrinter(ParserRuleContext context) {
    return context.toStringTree(mParser);
  }

  public static String prettyPrint(
      final JavaParser parser
  ) {
    return new JavaPrettyPrinter2(parser).visit(parser.compilationUnit());
  }


  // ------ Overridden visitor methods ------

  @Override
  public String visit(final JavaParser.CompilationUnitContext context) {
    return defaultPrinter(context);
  }


  // ------ Not used visitor methods ------

  @Override
  public String visit(final JavaParser.InnerCreatorContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.GenericMethodDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ExpressionListContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.TypeDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ForUpdateContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.AnnotationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.EnumConstantContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ImportDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.AnnotationMethodOrConstantRestContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.EnumConstantNameContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.FinallyBlockContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.VariableDeclaratorsContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ElementValuePairsContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.InterfaceMethodDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.InterfaceBodyDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.EnumConstantsContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.CatchClauseContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ConstantExpressionContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.EnumDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ExplicitGenericInvocationSuffixContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.TypeParameterContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.EnumBodyDeclarationsContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.TypeBoundContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.StatementExpressionContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.VariableInitializerContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.BlockContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.GenericInterfaceMethodDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.LocalVariableDeclarationStatementContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.SuperSuffixContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.FieldDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.FormalParameterListContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ExplicitGenericInvocationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ParExpressionContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.SwitchLabelContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.TypeParametersContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.QualifiedNameContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ClassDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.AnnotationConstantRestContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ArgumentsContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ConstructorBodyContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.FormalParametersContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.TypeArgumentContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ForInitContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.VariableDeclaratorContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.AnnotationTypeDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ExpressionContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ResourcesContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.FormalParameterContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.TypeContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ElementValueArrayInitializerContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.AnnotationNameContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.EnhancedForControlContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.AnnotationMethodRestContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.PrimaryContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ClassBodyContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ClassOrInterfaceModifierContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.DefaultValueContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.VariableModifierContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ConstDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.CreatedNameContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.InterfaceDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.PackageDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ConstantDeclaratorContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.CatchTypeContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.TypeArgumentsContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ClassCreatorRestContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ModifierContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.StatementContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.InterfaceBodyContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ClassBodyDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.LastFormalParameterContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ForControlContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.TypeListContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.LocalVariableDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.VariableDeclaratorIdContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ElementValueContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ClassOrInterfaceTypeContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.TypeArgumentsOrDiamondContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.AnnotationTypeElementDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.BlockStatementContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.AnnotationTypeBodyContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.QualifiedNameListContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.CreatorContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.MemberDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.MethodDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.AnnotationTypeElementRestContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ResourceSpecificationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ConstructorDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ResourceContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ElementValuePairContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.MethodBodyContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ArrayInitializerContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.NonWildcardTypeArgumentsOrDiamondContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.PrimitiveTypeContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.NonWildcardTypeArgumentsContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.ArrayCreatorRestContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.InterfaceMemberDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.GenericConstructorDeclarationContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.LiteralContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final JavaParser.SwitchBlockStatementGroupContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final ParserRuleContext context) {
    return defaultPrinter(context);
  }

  @Override
  public String visit(final TerminalNode node) {
    return null;
  }

  @Override
  public String visit(final ErrorNode node) {
    return null;
  }
}
