package org.akab.buttergwt.wizard.controller;

import java.util.HashMap;
import java.util.Map;
import org.akab.buttergwt.wizard.ClassUtil;
import org.akab.buttergwt.wizard.PluginUtil;
import org.akab.buttergwt.wizard.ProjectInfo;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

public class ControllerCreator
{
  public static void createController(NewControllerItem newControllerItem, ProjectInfo projectInfo)
  {
    try
    {
      updateEventsTypes(newControllerItem, projectInfo);
      createControllerEventClass(newControllerItem, projectInfo);
      createControllerClass(newControllerItem, projectInfo);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      PluginUtil.showErrorMessage(ex);
    }
  }
  
  private static void updateEventsTypes(NewControllerItem newControllerItem, ProjectInfo projectInfo)
  {
    try
    {
      IPackageFragment eventsPackage = ClassUtil.createPackage(projectInfo.getName(), "src", 
        projectInfo.getBasePackage() + ".client." + projectInfo.getSubPackage() + ".event");
      ICompilationUnit eventsTypesClassFile = ClassUtil.getCompilationUnit(eventsPackage, "Events.java");
      try
      {
        if (!eventsTypesClassFile.exists())
        {
          createEventsClass(newControllerItem, projectInfo);
          eventsTypesClassFile = ClassUtil.getCompilationUnit(eventsPackage, "Events.java");
        }
        String source = eventsTypesClassFile.getSource();
        source = source.substring(0, source.lastIndexOf("}") - 1);
        source = source.trim() + "\n\tpublic static final Type<ControllerEventHandler> " + 
          newControllerItem.getControllerConstantName() + 
          "_CONTROLLER_EVENT_TYPE = new Type<ControllerEventHandler>();\n}";
        eventsPackage.createCompilationUnit("Events.java", source, true, null);
        eventsTypesClassFile.save(null, true);
      }
      catch (JavaModelException e)
      {
        e.printStackTrace();
        PluginUtil.showErrorMessage(e);
      }
      return;
    }
    catch (JavaModelException e)
    {
      e.printStackTrace();
      PluginUtil.showErrorMessage(e);
    }
  }
  
  private static void createEventsClass(NewControllerItem newControllerItem, ProjectInfo projectInfo)
    throws JavaModelException
  {
    Map<String, String> params = new HashMap();
    params.put("BASE_PACKAGE", projectInfo.getBasePackage());
    params.put("SUB_PACKAGE", projectInfo.getSubPackage());
    
    String packageName = projectInfo.getBasePackage() + ".client." + projectInfo.getSubPackage() + ".event";
    
    ClassUtil.createCompilationUnitFromTemplate("Events.java", "controller/eventsTemplate.txt", params, 
      projectInfo.getName(), "src", packageName);
  }
  
  private static void createControllerEventClass(NewControllerItem newControllerItem, ProjectInfo projectInfo)
    throws JavaModelException
  {
    Map<String, String> params = new HashMap();
    params.put("BASE_PACKAGE", projectInfo.getBasePackage());
    params.put("SUB_PACKAGE", projectInfo.getSubPackage());
    params.put("CONTROLLER_PACKAGE", newControllerItem.getControllerPackage());
    params.put("CONTROLLER_NAME", 
      newControllerItem.getControllerName() == null ? "" : newControllerItem.getControllerName());
    params.put("CONTROLLER_CONSTANT_NAME", newControllerItem.getControllerConstantName());
    
    String packageName = projectInfo.getBasePackage() + ".client." + projectInfo.getSubPackage() + ".event" + 
      newControllerItem.getControllerPackage();
    
    ClassUtil.createCompilationUnitFromTemplate(newControllerItem.getControllerName() + "ControllerEvent.java", 
      "controller/controllerEventTemplate.txt", params, projectInfo.getName(), "src", 
      packageName);
  }
  
  private static void createControllerClass(NewControllerItem newControllerItem, ProjectInfo projectInfo)
    throws JavaModelException
  {
    Map<String, String> params = new HashMap();
    
    params.put("BASE_PACKAGE", projectInfo.getBasePackage());
    params.put("SUB_PACKAGE", projectInfo.getSubPackage());
    params.put("CONTROLLER_PACKAGE", newControllerItem.getControllerPackage());
    params.put("CONTROLLER_NAME", 
      newControllerItem.getControllerName() == null ? "" : newControllerItem.getControllerName());
    params.put("CONTOLLER_PRIORITY", 
      "(priority=" + newControllerItem.getPriority() + ")");
    params.put("CONTROLLER_CONSTANT_NAME", newControllerItem.getControllerConstantName());
    params.put("CONTROLLER_DESCRIPTION", newControllerItem.getControllerDescription());
    params.put("INITIAL_CONTENT", newControllerItem.getInitialContent());
    params.put("EXTRA_IMPORTS", newControllerItem.getExtraImports());
    
    String templateName = "controller/controllerTemplate.txt";
    if (newControllerItem.isHandlesHistory()) {
      templateName = "controller/historyControllerTemplate.txt";
    }
    String packageName = projectInfo.getBasePackage() + ".client." + projectInfo.getSubPackage() + ".mvp.controller" + 
      newControllerItem.getControllerPackage();
    
    ClassUtil.createCompilationUnitFromTemplate(newControllerItem.getControllerName() + "Controller.java", 
      templateName, params, projectInfo.getName(), "src", packageName);
  }
}
