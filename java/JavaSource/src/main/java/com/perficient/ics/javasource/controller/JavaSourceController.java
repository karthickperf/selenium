package com.perficient.ics.javasource.controller;

import java.util.*;
import java.net.*;
import java.io.*;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.perficient.ics.javasource.model.*;

public class JavaSourceController {
  public static void main( String[] args ) {
    int length = args.length;
     
    if (length != 1) {
      System.out.println("Usage: JavaSourceController <JSON File>");  
      System.out.println("JSON file should contain:\n");
      System.out.println("  {");
      System.out.println("    \"name\" :  \"myproject\"");
      System.out.println("  }");
      System.exit(-1);
    }

    String jsonFileName = args[0];

    System.out.println("JSON File: " + jsonFileName + "\n");

    ObjectMapper mapper = new ObjectMapper();
    Project project = null;
    try {
      project = mapper.readValue(new File(jsonFileName), Project.class);
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    PomCreateController         pomCreate           = new PomCreateController(project);
    ModelCreateController       modelCreate         = new ModelCreateController(project);
    PojoCreateController        pojoCreate          = new PojoCreateController(project);
    PojoPersistCreateController pojoPersistCreate   = new PojoPersistCreateController(project);

    ControllerCreateController  controllerCreate    = new ControllerCreateController(project);

    try {
      String zipFile = project.getProjectName() + ".zip";
      // Create zip file
      FileOutputStream outputStream = new FileOutputStream(zipFile);

      // Create object of ZipOutputStream from FileOutputStream
      ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

      // Create pom.xml
      zipOutputStream.putNextEntry(new ZipEntry(project.getProjectName() + "/pom.xml"));

      zipOutputStream.write(pomCreate.getSource().getBytes());
      zipOutputStream.closeEntry();

      // Create Model
      zipOutputStream.putNextEntry(new ZipEntry(project.getProjectName() + "/src/main/java/com/perficient/ics/" + project.getProjectName().toLowerCase() + "/model/Model.java"));

      zipOutputStream.write(modelCreate.getSource().getBytes());
      zipOutputStream.closeEntry();

      // Create Pojo
      zipOutputStream.putNextEntry(new ZipEntry(project.getProjectName() + "/src/main/java/com/perficient/ics/" + project.getProjectName().toLowerCase() + "/model/" + project.getClassName() + ".java"));

      zipOutputStream.write(pojoCreate.getSource().getBytes());
      zipOutputStream.closeEntry();

      // Create PojoPersist
      zipOutputStream.putNextEntry(new ZipEntry(project.getProjectName() + "/src/main/java/com/perficient/ics/" + project.getProjectName().toLowerCase() + "/model/" + project.getClassName() + "Persist.java"));

      zipOutputStream.write(pojoPersistCreate.getSource().getBytes());
      zipOutputStream.closeEntry();

      // Create Controller
      zipOutputStream.putNextEntry(new ZipEntry(project.getProjectName() + "/src/main/java/com/perficient/ics/" + project.getProjectName().toLowerCase() + "/controller/" + project.getProjectName() + "Controller.java"));

      zipOutputStream.write(controllerCreate.getSource().getBytes());
      zipOutputStream.closeEntry();
  
      zipOutputStream.flush();
      outputStream.flush();
  
      zipOutputStream.close();
      outputStream.close();
    }  
    catch(Exception ex) {
      ex.printStackTrace();
    }

  }  

}

