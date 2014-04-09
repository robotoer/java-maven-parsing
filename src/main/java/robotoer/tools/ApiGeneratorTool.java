package robotoer.tools;

import robotoer.util.Api;
import robotoer.util.Operation;
import robotoer.util.TypeOrClass;

public class ApiGeneratorTool {
  public static void main(String[] args) {
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
