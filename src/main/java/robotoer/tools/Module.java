package robotoer.tools;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.TerminalNode;
import robotoer.ast.java.JavaLexer;
import robotoer.ast.java.JavaParser;
import robotoer.ast.java.JavaParser.AnnotationContext;
import robotoer.ast.java.JavaParser.ClassBodyContext;
import robotoer.ast.java.JavaParser.ClassBodyDeclarationContext;
import robotoer.ast.java.JavaParser.ClassDeclarationContext;
import robotoer.ast.java.JavaParser.ClassOrInterfaceModifierContext;
import robotoer.ast.java.JavaParser.CompilationUnitContext;
import robotoer.ast.java.JavaParser.InterfaceBodyContext;
import robotoer.ast.java.JavaParser.InterfaceBodyDeclarationContext;
import robotoer.ast.java.JavaParser.InterfaceDeclarationContext;
import robotoer.ast.java.JavaParser.InterfaceMemberDeclarationContext;
import robotoer.ast.java.JavaParser.MemberDeclarationContext;
import robotoer.ast.java.JavaParser.ModifierContext;
import robotoer.ast.java.JavaParser.TypeDeclarationContext;

public class Module {
  public static final String SOURCE_DIRECTORY = "src/main/java";

  private final String mPath;
  //private final Git mVersionControl;

  public Module(
      final String path
  ) {
    mPath = path;
  }

  public List<File> findFiles(
      final File directory,
      final Pattern pattern,
      final boolean matchFiles,
      final boolean matchDirectories,
      final boolean recursive
  ) {
    final List<File> matches = Lists.newArrayList();
    for (File file : directory.listFiles()) {
      final Matcher matcher = pattern.matcher(file.getName());

      if (file.isDirectory()) {
        if (matchDirectories && matcher.matches()) {
          matches.add(file);
        }

        // Recurse into subdirectories.
        if (recursive) {
          final List<File> subdirectoryMatches = findFiles(
              file,
              pattern,
              matchFiles,
              matchDirectories,
              true
          );
          matches.addAll(subdirectoryMatches);
        }
      } else {
        if (matchFiles && matcher.matches()) {
          matches.add(file);
        }
      }
    }
    return matches;
  }

  public List<JavaParser.CompilationUnitContext> getCompilationUnits() throws IOException {
    final File rootDirectory = new File(String.format("%s/%s", mPath, SOURCE_DIRECTORY));
    final List<File> javaFiles =
        findFiles(rootDirectory, Pattern.compile(".*\\.java"), true, false, true);

    final List<JavaParser.CompilationUnitContext> parsed = Lists.newArrayList();
    for (File javaFile : javaFiles) {
      final Lexer lexer = new JavaLexer(new ANTLRFileStream(javaFile.getAbsolutePath()));
      final CommonTokenStream tokens = new CommonTokenStream(lexer);
      final JavaParser parser = new JavaParser(tokens);
      parsed.add(parser.compilationUnit());
    }
    return parsed;
  }

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
    private final String mApiAudience;
    private final String mApiStability;
    private final String mInheritance;

    private ClassAnnotationsTuple(
        final String className,
        final String apiAudience,
        final String apiStability,
        final String inheritance
    ) {
      mClassName = className;
      mApiAudience = apiAudience;
      mApiStability = apiStability;
      mInheritance = inheritance;
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(ClassAnnotationsTuple.class)
          .add("ClassName", mClassName)
          .add("ApiAudience", mApiAudience)
          .add("ApiStability", mApiStability)
          .add("Inheritance", mInheritance)
          .toString();
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(
          mClassName, mApiAudience, mApiStability, mInheritance);
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
          && Objects.equal(this.mApiAudience, other.mApiAudience)
          && Objects.equal(this.mApiStability, other.mApiStability)
          && Objects.equal(this.mInheritance, other.mInheritance);
    }
  }

  private static ClassAnnotationsTuple getAnnotationsTuple(
      final String fullyQualifiedClassName,
      final List<String> annotations
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

    return new ClassAnnotationsTuple(
        fullyQualifiedClassName, audience, stability, inheritance);
  }

  private static List<String> getAnnotations(
      final ClassOrInterfaceModifierContext classOrInterfaceModifierContext
  ) {
    final List<String> annotations = Lists.newArrayList();
    final AnnotationContext annotationContext = classOrInterfaceModifierContext.annotation();
    if (null != annotationContext) {
      annotations.add(Joiner.on('.').join(Lists.transform(
          annotationContext.annotationName().qualifiedName().Identifier(),
          new TerminalNodeToText())));
    }
    return annotations;
  }

  private static Set<ClassAnnotationsTuple> recurse(
      final String relativePath,
      final ClassBodyContext classBodyContext
  ) {
    final Set<ClassAnnotationsTuple> classAnnotations = Sets.newHashSet();

    for (ClassBodyDeclarationContext classBodyDeclarationContext
        : classBodyContext.classBodyDeclaration()) {
      for (ModifierContext modifierContext : classBodyDeclarationContext.modifier()) {
        final ClassOrInterfaceModifierContext classOrInterfaceModifierContext =
            modifierContext.classOrInterfaceModifier();
        if (null != classOrInterfaceModifierContext) {
          final List<String> annotations = getAnnotations(classOrInterfaceModifierContext);
          final MemberDeclarationContext memberDeclarationContext =
              classBodyDeclarationContext.memberDeclaration();
          if (null != memberDeclarationContext) {
            final ClassDeclarationContext innerClassDeclarationContext =
                memberDeclarationContext.classDeclaration();
            final InterfaceDeclarationContext innerInterfaceDeclarationContext =
                memberDeclarationContext.interfaceDeclaration();
            if (null != innerClassDeclarationContext) {
              final String classString =
                  relativePath + "." + innerClassDeclarationContext.Identifier();
              classAnnotations.add(getAnnotationsTuple(classString, annotations));
              classAnnotations.addAll(
                  recurse(classString, innerClassDeclarationContext.classBody()));
            } else if (null != innerInterfaceDeclarationContext) {
              final String interfaceString =
                  relativePath + "." + innerInterfaceDeclarationContext.Identifier();
              classAnnotations.add(getAnnotationsTuple(interfaceString, annotations));
              classAnnotations.addAll(
                  recurse(interfaceString, innerInterfaceDeclarationContext.interfaceBody()));
            }
          }
        }
      }
    }
    return classAnnotations;
  }

  private static Set<ClassAnnotationsTuple> recurse(
      final String relativePath,
      final InterfaceBodyContext interfaceBodyContext
  ) {
    final Set<ClassAnnotationsTuple> classAnnotations = Sets.newHashSet();

    for (InterfaceBodyDeclarationContext interfaceBodyDeclarationContext
        : interfaceBodyContext.interfaceBodyDeclaration()) {
      for (ModifierContext modifierContext : interfaceBodyDeclarationContext.modifier()) {
        final ClassOrInterfaceModifierContext classOrInterfaceModifierContext =
            modifierContext.classOrInterfaceModifier();
        if (null != classOrInterfaceModifierContext) {
          final List<String> annotations = getAnnotations(classOrInterfaceModifierContext);
          final InterfaceMemberDeclarationContext interfaceMemberDeclarationContext =
              interfaceBodyDeclarationContext.interfaceMemberDeclaration();
          if (null != interfaceMemberDeclarationContext) {
            final ClassDeclarationContext innerClassDeclarationContext =
                interfaceMemberDeclarationContext.classDeclaration();
            final InterfaceDeclarationContext innerInterfaceDeclarationContext =
                interfaceMemberDeclarationContext.interfaceDeclaration();
            if (null != innerClassDeclarationContext) {
              final String classString =
                  relativePath + "." + innerClassDeclarationContext.Identifier();
              classAnnotations.add(getAnnotationsTuple(classString, annotations));
              classAnnotations.addAll(
                  recurse(classString, innerClassDeclarationContext.classBody()));
            } else if (null != innerInterfaceDeclarationContext) {
              final String interfaceString =
                  relativePath + "." + innerInterfaceDeclarationContext.Identifier();
              classAnnotations.add(getAnnotationsTuple(interfaceString, annotations));
              classAnnotations.addAll(
                  recurse(interfaceString, innerInterfaceDeclarationContext.interfaceBody()));
            }
          }
        }
      }
    }
    return classAnnotations;
  }


  public static void main(String[] args) throws IOException {
    final Module module = new Module("/home/ajprax/src/kiji/kiji-mapreduce/kiji-mapreduce/");
    final List<JavaParser.CompilationUnitContext> contexts = module.getCompilationUnits();
    final Set<ClassAnnotationsTuple> classAnnotations = Sets.newHashSet();
    for (CompilationUnitContext compilationUnitContext : contexts) {
      final String packageString = Joiner.on('.').join(Lists.transform(
          compilationUnitContext.packageDeclaration().qualifiedName().Identifier(),
          new TerminalNodeToText()));

      for (TypeDeclarationContext typeDeclarationContext
          : compilationUnitContext.typeDeclaration()) {
        final List<String> annotations = Lists.newArrayList();
        for (ClassOrInterfaceModifierContext classOrInterfaceModifierContext
            : typeDeclarationContext.classOrInterfaceModifier()) {
          annotations.addAll(getAnnotations(classOrInterfaceModifierContext));
        }

        final ClassDeclarationContext classDeclarationContext =
            typeDeclarationContext.classDeclaration();
        final InterfaceDeclarationContext interfaceDeclarationContext =
            typeDeclarationContext.interfaceDeclaration();

        if (null != classDeclarationContext) {
          final String classString = packageString + "." + classDeclarationContext.Identifier();
          classAnnotations.add(getAnnotationsTuple(classString, annotations));
          classAnnotations.addAll(recurse(classString, classDeclarationContext.classBody()));
        } else if (null != interfaceDeclarationContext) {
          final String interfaceString =
              packageString + "." + interfaceDeclarationContext.Identifier();
          classAnnotations.add(getAnnotationsTuple(interfaceString, annotations));
          classAnnotations.addAll(
              recurse(interfaceString, interfaceDeclarationContext.interfaceBody()));
        }
      }
    }

    final File out = new File("/home/ajprax/tmp/annotations.csv");
    final PrintWriter pw = new PrintWriter(out);
    try {
      pw.println("ClassName,@ApiAudience,@ApiStability,@Inheritance");
      for (ClassAnnotationsTuple t : classAnnotations) {
        final String className = t.mClassName;
        final String apiAudience = null != t.mApiAudience ? t.mApiAudience : "";
        final String apiStability = null != t.mApiStability ? t.mApiStability : "";
        final String inheritance = null != t.mInheritance ? t.mInheritance : "";
        final String line =
            String.format("%s,%s,%s,%s", className, apiAudience, apiStability, inheritance);
        pw.println(line);
      }
    } finally {
      pw.close();
    }
  }
}
