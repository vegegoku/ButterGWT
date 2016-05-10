package org.akab.buttergwt.wizard.plugin;

import java.util.HashMap;
import java.util.Map;
import org.akab.buttergwt.wizard.TemplateUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public class ExtensionPoint
{
  private PluginSettings pluginSettings;
  private IProject project;
  
  public ExtensionPoint(PluginSettings pluginSettings, IProject project)
  {
    this.pluginSettings = pluginSettings;
    this.project = project;
  }
  
  public void createExtensionPoint(String name)
    throws CoreException
  {
    Map<String, String> params = new HashMap();
    
    params.put("PACKAGE", this.pluginSettings.asPackage());
    params.put("NAME", name);
    
    createContext(this.project, params);
    createExtensionPointClass(this.project, params);
  }
  
  private void createContext(IProject project, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "presenter/contributionContextTemplate.txt");
    params.put("PACKAGE_PATH", "/client/api/context/");
    params.put("FILE_POSTFIX", "ContributionContext.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, this.pluginSettings, params);
  }
  
  private void createExtensionPointClass(IProject project, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "presenter/extensionPointTemplate.txt");
    params.put("PACKAGE_PATH", "/client/api/extension/");
    params.put("FILE_POSTFIX", "ExtensionPoint.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, this.pluginSettings, params);
  }
}
