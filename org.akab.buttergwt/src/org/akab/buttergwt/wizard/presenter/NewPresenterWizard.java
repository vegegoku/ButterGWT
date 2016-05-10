package org.akab.buttergwt.wizard.presenter;

import org.akab.buttergwt.wizard.ClassUtil;
import org.akab.buttergwt.wizard.PluginUtil;
import org.akab.buttergwt.wizard.plugin.PluginSettings;
import org.akab.buttergwt.wizard.plugin.Presenter;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.Wizard;

public class NewPresenterWizard
  extends Wizard
{
  private NewPresenterItem newPresenterItem;
  
  public NewPresenterWizard()
  {
    this.newPresenterItem = new NewPresenterItem();
    addPage(new NewPresenterPage(this.newPresenterItem));
  }
  
  public boolean performFinish()
  {
    IProject project = ClassUtil.getCurrentProject();
    PluginSettings pluginSettings = ClassUtil.getPluginSettings();
    
    Presenter presenter = new Presenter(pluginSettings, project);
    try
    {
      presenter.createPresenter(this.newPresenterItem.getPresenterName(), this.newPresenterItem.getPresenterDescription());
    }
    catch (CoreException e)
    {
      e.printStackTrace();
      PluginUtil.showErrorMessage(e);
    }
    return true;
  }
}
