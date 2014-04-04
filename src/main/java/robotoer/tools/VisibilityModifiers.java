package robotoer.tools;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import robotoer.ast.java.JavaParser;
import robotoer.project.Module;

public class VisibilityModifiers {
  private static final class TerminalNodeToText implements Function<TerminalNode, String> {

    @Override
    public String apply(final TerminalNode input) {
      return input.getText();
    }

    @Override
    public boolean equals(final Object object) {
      return object.getClass().equals(this.getClass());
    }
  }

  private static final class ClassAnnotationsTuple {
    private final String mClassName;
    private final String mClassOrInterface;
    private final String mApiAudience;
    private final String mApiStability;
    private final String mInheritance;
    private final String mVisibility;
    private final String mExtensibility;

    private ClassAnnotationsTuple(
        final String className,
        final String classOrInterface,
        final String apiAudience,
        final String apiStability,
        final String inheritance,
        final String visibility,
        final String extensibility
    ) {
      mClassName = className;
      mClassOrInterface = classOrInterface;
      mApiAudience = apiAudience;
      mApiStability = apiStability;
      mInheritance = inheritance;
      mVisibility = visibility;
      mExtensibility = extensibility;
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(ClassAnnotationsTuple.class)
          .add("ClassName", mClassName)
          .add("ClassOrInterface", mClassOrInterface)
          .add("ApiAudience", mApiAudience)
          .add("ApiStability", mApiStability)
          .add("Inheritance", mInheritance)
          .add("Visibility", mVisibility)
          .add("Extensibility", mExtensibility)
          .toString();
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(
          mClassName,
          mClassOrInterface,
          mApiAudience,
          mApiStability,
          mInheritance,
          mVisibility,
          mExtensibility);
    }

    @Override
    public boolean equals(
        final Object obj
    ) {
      if (null == obj || !Objects.equal(ClassAnnotationsTuple.class, obj.getClass())) {
        return false;
      }
      final ClassAnnotationsTuple other = (ClassAnnotationsTuple) obj;
      return Objects.equal(this.mClassName, other.mClassName)
          && Objects.equal(this.mClassOrInterface, other.mClassOrInterface)
          && Objects.equal(this.mApiAudience, other.mApiAudience)
          && Objects.equal(this.mApiStability, other.mApiStability)
          && Objects.equal(this.mInheritance, other.mInheritance)
          && Objects.equal(this.mVisibility, other.mVisibility)
          && Objects.equal(this.mExtensibility, other.mExtensibility);
    }
  }

  private static final List<String> VISIBILITY_MODIFIERS =
      Lists.newArrayList("public", "private", "protected");
  private static final List<String> EXTENSIBILITY_MODIFIERS =
      Lists.newArrayList("abstract", "final");
  private static ClassAnnotationsTuple getAnnotationsTuple(
      final String fullyQualifiedClassName,
      final String classOrInterface,
      final List<String> annotations,
      final List<String> modifiers
  ) {
    String audience = null;
    String stability = null;
    String inheritance = null;
    for (String annotation : annotations) {
      if (annotation.startsWith("ApiAudience")) {
        audience = annotation.substring("ApiAudience.".length());
      } else if (annotation.startsWith("ApiStability.")) {
        stability = annotation.substring("ApiStability.".length());
      } else if (annotation.startsWith("Inheritance.")) {
        inheritance = annotation.substring("Inheritance.".length());
      }
    }

    String visibility = null;
    String extensibility = null;
    for (String modifier : modifiers) {
      if (VISIBILITY_MODIFIERS.contains(modifier)) {
        visibility = modifier;
      } else if (EXTENSIBILITY_MODIFIERS.contains(modifier)) {
        extensibility = modifier;
      }
    }
    visibility = (null != visibility) ? visibility : "package private";
    extensibility = (null != extensibility) ? extensibility : "extensible";


    return new ClassAnnotationsTuple(
        fullyQualifiedClassName,
        classOrInterface,
        audience,
        stability,
        inheritance,
        visibility,
        extensibility);
  }

  private static List<String> getAnnotations(
      final JavaParser.ClassOrInterfaceModifierContext classOrInterfaceModifierContext
  ) {
    final JavaParser.AnnotationContext annotationContext = classOrInterfaceModifierContext.annotation();
    if (null != annotationContext) {
      return Lists.newArrayList(Joiner.on('.').join(Lists.transform(
          annotationContext.annotationName().qualifiedName().Identifier(),
          new TerminalNodeToText())));
    } else {
      return Lists.newArrayList();
    }
  }

  private static List<String> getModifiers(
      final JavaParser.ClassOrInterfaceModifierContext classOrInterfaceModifierContext
  ) {
    final List<String> modifiers = Lists.newArrayList();
    for (ParseTree parseTree : classOrInterfaceModifierContext.children) {
      if (parseTree instanceof TerminalNode) {
        modifiers.add(((TerminalNode) parseTree).getSymbol().getText());
      }
    }
    return modifiers;
  }

  private static Set<ClassAnnotationsTuple> recurse(
      final String relativePath,
      final JavaParser.ClassBodyContext classBodyContext
  ) {
    final Set<ClassAnnotationsTuple> classAnnotations = Sets.newHashSet();

    final List<String> annotations = Lists.newArrayList();
    final List<String> modifiers = Lists.newArrayList();
    for (JavaParser.ClassBodyDeclarationContext classBodyDeclarationContext
        : classBodyContext.classBodyDeclaration()) {
      for (JavaParser.ModifierContext modifierContext : classBodyDeclarationContext.modifier()) {
        final JavaParser.ClassOrInterfaceModifierContext classOrInterfaceModifierContext =
            modifierContext.classOrInterfaceModifier();
        if (null != classOrInterfaceModifierContext) {
          annotations.addAll(getAnnotations(classOrInterfaceModifierContext));
          modifiers.addAll(getModifiers(classOrInterfaceModifierContext));
        }
      }
      final JavaParser.MemberDeclarationContext memberDeclarationContext =
          classBodyDeclarationContext.memberDeclaration();
      if (null != memberDeclarationContext) {
        final JavaParser.ClassDeclarationContext innerClassDeclarationContext =
            memberDeclarationContext.classDeclaration();
        final JavaParser.InterfaceDeclarationContext innerInterfaceDeclarationContext =
            memberDeclarationContext.interfaceDeclaration();
        if (null != innerClassDeclarationContext) {
          final String classString =
              relativePath + "." + innerClassDeclarationContext.Identifier();
          classAnnotations.add(getAnnotationsTuple(classString, "class", annotations, modifiers));
          classAnnotations.addAll(
              recurse(classString, innerClassDeclarationContext.classBody()));
        } else if (null != innerInterfaceDeclarationContext) {
          final String interfaceString =
              relativePath + "." + innerInterfaceDeclarationContext.Identifier();
          classAnnotations.add(
              getAnnotationsTuple(interfaceString, "interface", annotations, modifiers));
          classAnnotations.addAll(
              recurse(interfaceString, innerInterfaceDeclarationContext.interfaceBody()));
        }
      }
    }
    return classAnnotations;
  }

  private static Set<ClassAnnotationsTuple> recurse(
      final String relativePath,
      final JavaParser.InterfaceBodyContext interfaceBodyContext
  ) {
    final Set<ClassAnnotationsTuple> classAnnotations = Sets.newHashSet();

    for (JavaParser.InterfaceBodyDeclarationContext interfaceBodyDeclarationContext
        : interfaceBodyContext.interfaceBodyDeclaration()) {
      final List<String> annotations = Lists.newArrayList();
      final List<String> modifiers = Lists.newArrayList();
      for (JavaParser.ModifierContext modifierContext : interfaceBodyDeclarationContext.modifier()) {
        final JavaParser.ClassOrInterfaceModifierContext classOrInterfaceModifierContext =
            modifierContext.classOrInterfaceModifier();
        if (null != classOrInterfaceModifierContext) {
          annotations.addAll(getAnnotations(classOrInterfaceModifierContext));
          modifiers.addAll(getModifiers(classOrInterfaceModifierContext));
        }
      }
      final JavaParser.InterfaceMemberDeclarationContext interfaceMemberDeclarationContext =
          interfaceBodyDeclarationContext.interfaceMemberDeclaration();
      if (null != interfaceMemberDeclarationContext) {
        final JavaParser.ClassDeclarationContext innerClassDeclarationContext =
            interfaceMemberDeclarationContext.classDeclaration();
        final JavaParser.InterfaceDeclarationContext innerInterfaceDeclarationContext =
            interfaceMemberDeclarationContext.interfaceDeclaration();
        if (null != innerClassDeclarationContext) {
          final String classString =
              relativePath + "." + innerClassDeclarationContext.Identifier();
          classAnnotations.add(getAnnotationsTuple(classString, "class",annotations, modifiers));
          classAnnotations.addAll(
              recurse(classString, innerClassDeclarationContext.classBody()));
        } else if (null != innerInterfaceDeclarationContext) {
          final String interfaceString =
              relativePath + "." + innerInterfaceDeclarationContext.Identifier();
          classAnnotations.add(
              getAnnotationsTuple(interfaceString, "interface", annotations, modifiers));
          classAnnotations.addAll(
              recurse(interfaceString, innerInterfaceDeclarationContext.interfaceBody()));
        }
      }
    }
    return classAnnotations;
  }

  public static void main(String[] args) throws IOException {
    final List<String> projects = Lists.newArrayList(
        "kiji-schema/kiji-schema",
        "kiji-mapreduce/kiji-mapreduce",
        "kiji-rest/kiji-rest",
        "kiji-scoring",
        "kiji-model-repository/kiji-model-repository",
        "kiji-mapreduce-lib/kiji-mapreduce-lib",
        "kiji-hive-adapter/kiji-hive-adapter",
        "kiji-hive-adapter/kiji-hive-tools",
        "kiji-delegation",
        "kiji-checkin",
        "kiji-common-flags"
    );
    for (String project : projects) {
      final Module module = new Module("/home/ajprax/src/kiji/" + project);
      final List<JavaParser.CompilationUnitContext> contexts = module.getCompilationUnits();
      final Set<ClassAnnotationsTuple> classAnnotations = Sets.newHashSet();
      for (JavaParser.CompilationUnitContext compilationUnitContext : contexts) {
        final String packageString = Joiner.on('.').join(Lists.transform(
            compilationUnitContext.packageDeclaration().qualifiedName().Identifier(),
            new TerminalNodeToText()));

        for (JavaParser.TypeDeclarationContext typeDeclarationContext
            : compilationUnitContext.typeDeclaration()) {
          final List<String> annotations = Lists.newArrayList();
          final List<String> modifiers = Lists.newArrayList();
          for (JavaParser.ClassOrInterfaceModifierContext classOrInterfaceModifierContext
              : typeDeclarationContext.classOrInterfaceModifier()) {
            annotations.addAll(getAnnotations(classOrInterfaceModifierContext));
            modifiers.addAll(getModifiers(classOrInterfaceModifierContext));
          }
          final JavaParser.ClassDeclarationContext classDeclarationContext =
              typeDeclarationContext.classDeclaration();
          final JavaParser.InterfaceDeclarationContext interfaceDeclarationContext =
              typeDeclarationContext.interfaceDeclaration();

          if (null != classDeclarationContext) {
            final String classString = packageString + "." + classDeclarationContext.Identifier();
            classAnnotations.add(getAnnotationsTuple(classString, "class", annotations, modifiers));
            classAnnotations.addAll(recurse(classString, classDeclarationContext.classBody()));
          } else if (null != interfaceDeclarationContext) {
            final String interfaceString =
                packageString + "." + interfaceDeclarationContext.Identifier();
            classAnnotations.add(
                getAnnotationsTuple(interfaceString, "interface", annotations, modifiers));
            classAnnotations.addAll(
                recurse(interfaceString, interfaceDeclarationContext.interfaceBody()));
          }
        }
      }

      final File out =
          new File(String.format("/home/ajprax/tmp/annotations/%s.csv", project.replace('/','.')));
      final PrintWriter pw = new PrintWriter(out);
      try {
        pw.println("JavaVisibility,JavaExtensibility,ClassOrInterface,FullyQualifiedName,"
            + "@ApiAudience,@ApiStability,@Inheritance");
        for (ClassAnnotationsTuple t : classAnnotations) {
          final String apiAudience = null != t.mApiAudience ? t.mApiAudience : "";
          final String apiStability = null != t.mApiStability ? t.mApiStability : "";
          final String inheritance = null != t.mInheritance ? t.mInheritance : "";
          final String line = String.format("%s,%s,%s,%s,%s,%s,%s",
              t.mVisibility,
              t.mExtensibility,
              t.mClassOrInterface,
              t.mClassName,
              apiAudience,
              apiStability,
              inheritance);
          pw.println(line);
        }
      } finally {
        pw.close();
      }
    }
  }
}
