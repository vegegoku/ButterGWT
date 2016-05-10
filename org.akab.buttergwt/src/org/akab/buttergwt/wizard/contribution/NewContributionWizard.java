package org.akab.buttergwt.wizard.contribution;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.akab.buttergwt.wizard.ClassUtil;
import org.akab.buttergwt.wizard.PluginUtil;
import org.akab.buttergwt.wizard.TemplateUtil;
import org.akab.buttergwt.wizard.plugin.PluginSettings;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.Wizard;

public class NewContributionWizard
  extends Wizard
{
  private NewContributionItem newContributionItem;
  
  public NewContributionWizard()
  {
    this.newContributionItem = new NewContributionItem();
    addPage(new NewContributionPage(this.newContributionItem));
  }
  
  public boolean performFinish()
  {
    Map<String, String> params = new HashMap();
    
    PluginSettings pluginSettings = ClassUtil.getPluginSettings();
    
    params.put("PACKAGE", pluginSettings.asPackage());
    params.put("NAME", this.newContributionItem.getContributionName());
    
    params.put("CONTRIBUTION_DESCRIPTION", this.newContributionItem.getContributionDescription());
    params.put("CONTRIBUTION_CONTEXT_IMPORT", this.newContributionItem.getContextImport());
    params.put("EXTENSION_POINT_IMPORT", this.newContributionItem.getExtensionPointImport());
    params.put("CONTRIBUTION_CONTEXT_NAME", 
      ClassUtil.getSimpleClassNameFromFullClassName(this.newContributionItem.getContextImport()));
    params.put("EXTENSION_POINT_NAME", 
      ClassUtil.getSimpleClassNameFromFullClassName(this.newContributionItem.getExtensionPointImport()));
    
    IProject project = ClassUtil.getCurrentProject();
    try
    {
      createContribution(project, pluginSettings, params);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      PluginUtil.showErrorMessage(ex);
    }
    return true;
  }
  
  private void createContribution(IProject project, PluginSettings pluginSettings, Map<String, String> params)
    throws CoreException
  {
    String template = TemplateUtil.getTemplateString("contribution/contributionTemplate.txt");
    String templateString = TemplateUtil.replaceTemplateParameters(template, params);
    
    IFile file = project.getFile("src/main/java/" + pluginSettings.asPackagePath() + 
      "/client/impl/contribution/" + (String)params.get("NAME") + "Contribution.java");
    
    InputStream source = new ByteArrayInputStream(templateString.getBytes());
    file.create(source, true, null);
  }
}
