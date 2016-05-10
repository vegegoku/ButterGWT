package org.akab.buttergwt.wizard.extension;

import java.util.HashMap;
import java.util.Map;
import org.akab.buttergwt.wizard.ClassUtil;
import org.akab.buttergwt.wizard.PluginUtil;
import org.akab.buttergwt.wizard.plugin.ExtensionPoint;
import org.akab.buttergwt.wizard.plugin.PluginSettings;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;

public class NewExtensionPointWizard
  extends Wizard
{
  private NewExtensionPointItem newExtensionPointItem;
  
  public NewExtensionPointWizard()
  {
    this.newExtensionPointItem = new NewExtensionPointItem();
    addPage(new NewExtensionPointPage(this.newExtensionPointItem));
  }
  
  public boolean performFinish()
  {
    Map<String, String> params = new HashMap();
    
    PluginSettings pluginSettings = ClassUtil.getPluginSettings();
    
    params.put("PACKAGE", pluginSettings.asPackage());
    params.put("NAME", this.newExtensionPointItem.getName());
    
    IProject project = ClassUtil.getCurrentProject();
    try
    {
      ExtensionPoint extensionPoint = new ExtensionPoint(pluginSettings, project);
      extensionPoint.createExtensionPoint(this.newExtensionPointItem.getName());
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      PluginUtil.showErrorMessage(ex);
    }
    return true;
  }
}
