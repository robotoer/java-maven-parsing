package robotoer.tools;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import robotoer.ast.java.JavaLexer;
import robotoer.ast.java.JavaParser;
import robotoer.util.Api;
import robotoer.util.JavaPrettyPrinter;
import robotoer.util.Operation;
import robotoer.util.TypeOrClass;

public class ApiGeneratorTool {
  public static void main(String[] args) throws IOException {
    // Parse a java file and print it back out using JavaPrettyPrinter.
    final String kijiInterfacePath =
        "/home/robert/kiji/kiji-schema/kiji-schema/src/main/java/org/kiji/schema/Kiji.java";
    final Lexer lexer = new JavaLexer(new ANTLRFileStream(kijiInterfacePath));
    final CommonTokenStream tokens = new CommonTokenStream(lexer);
    final JavaParser parser = new JavaParser(tokens);
    final JavaParser.CompilationUnitContext kijiInterface = parser.compilationUnit();

    ParseTreeWalker.DEFAULT.walk(new JavaPrettyPrinter(parser), kijiInterface);
//    System.out.println(kijiInterface.toStringTree(parser));

    // Load an API.
    Operation.builder()
        .addParameter("baseDir", TypeOrClass.STRING)
        .addParameter("port", TypeOrClass.INT)
        .addParameter("numAcceptors", TypeOrClass.INT)
        .build();
    Api.builder()
        .setName("scoring-server")
        .addOperation(
            Operation.builder()
                .setName("initialize")
                .addParameter("baseDir", TypeOrClass.STRING)
                .addParameter("port", TypeOrClass.INT)
                .addParameter("numAcceptors", TypeOrClass.INT)
                .build()
        )
        .addOperation(Operation.statement("start"))
        .addOperation(Operation.statement("stop"))
        .addOperation(
            Operation.builder()
                .setName("deploy")
                .addParameter("scoreFunctionId", new TypeOrClass.Optional(TypeOrClass.STRING))
                .addParameter("scoreFunctionClass", TypeOrClass.STRING)
                .addParameter("columnUri", TypeOrClass.STRING)
                .addParameter("parameters", new TypeOrClass.MapType(TypeOrClass.STRING, TypeOrClass.STRING))
                .build()
        )
        .addOperation(
            Operation.builder()
                .setName("undeploy")
                .addParameter("scoreFunctionId", TypeOrClass.STRING)
                .build()
        )
        .addOperation(Operation.statement("list"))
        .addOperation(Operation.statement("status"))
        .build();

    Api.builder()
        .setName("scoring-servlet")
        .addOperation(Operation.statement("score"))
        .addOperation(Operation.statement("status"))
        .build();


    Api.builder()
        .setName("wibidata-tools")
        .addOperation(Operation.statement("mail-helpdesk"))
        .build();


    // Generate the transformed API.
    // Save the generated text.
  }
}
