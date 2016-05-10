package org.akab.buttergwt.wizard.plugin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.akab.buttergwt.wizard.TemplateUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public class Presenter
{
  private PluginSettings pluginSettings;
  private IProject project;
  
  public Presenter(PluginSettings pluginSettings, IProject project)
  {
    this.pluginSettings = pluginSettings;
    this.project = project;
  }
  
  public void createPresenter(String name, String description)
    throws CoreException
  {
    Map<String, String> params = new HashMap();
    
    params.put("NAME", name);
    params.put("PACKAGE", this.pluginSettings.asPackage());
    params.put("NAME_CONSTANT", this.pluginSettings.asConstant());
    params.put("DESCRIPTION", description);
    params.put("PRIORITY", "(priority=2147483647)");
    params.put("CONTROLLER_IMPORT", this.pluginSettings.asPackage() + ".client.impl.mvp.controller." + 
      this.pluginSettings.asName() + "Controller;");
    params.put("CONTROLLER_CLASS", this.pluginSettings.asName() + "Controller");
    
    createPresenter(this.project, params);
    
    createPresenterInterface(this.project, params);
    
    createViewInterface(this.project, params);
    
    createViewImpl(this.project, params);
    
    createUiBinder(this.project, params);
  }
  
  private void createPresenter(IProject project, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "presenter/presenterTemplate.txt");
    params.put("PACKAGE_PATH", "/client/impl/mvp/presenter/");
    params.put("FILE_POSTFIX", "Presenter.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, this.pluginSettings, params);
  }
  
  private void createPresenterInterface(IProject project, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "presenter/presenterInterfaceTemplate.txt");
    params.put("PACKAGE_PATH", "/client/api/mvp/presenter/");
    params.put("FILE_POSTFIX", "ViewPresenter.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, this.pluginSettings, params);
  }
  
  private void createViewInterface(IProject project, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "presenter/viewInterfaceTemplate.txt");
    params.put("PACKAGE_PATH", "/client/api/mvp/view/");
    params.put("FILE_POSTFIX", "View.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, this.pluginSettings, params);
  }
  
  private void createViewImpl(IProject project, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "presenter/viewImplementaionTemplate.txt");
    params.put("PACKAGE_PATH", "/client/impl/mvp/view/");
    params.put("FILE_POSTFIX", ".java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, this.pluginSettings, params);
  }
  
  private void createUiBinder(IProject project, Map<String, String> params)
    throws CoreException
  {
    String template = TemplateUtil.getTemplateString("presenter/binder.ui.xml");
    String templateString = TemplateUtil.replaceTemplateParameters(template, params);
    
    IFile file = project.getFile("src/main/java/" + this.pluginSettings.asPackagePath() + "/client/api/mvp/view" + 
      (String)params.get("NAME") + ".ui.xml");
    
    InputStream source = new ByteArrayInputStream(templateString.getBytes());
    file.create(source, true, null);
  }
}
