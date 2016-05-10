package org.akab.buttergwt.wizard.download;

import org.eclipse.jface.wizard.Wizard;

public class NewDownloadControllerWizard
  extends Wizard
{
  private NewDownloadControllerItem newDownloadControllerItem;
  
  public NewDownloadControllerWizard()
  {
    this.newDownloadControllerItem = new NewDownloadControllerItem();
    addPage(new NewDownloadControllerWizardPage("NewDownloadController", this.newDownloadControllerItem));
  }
  
  public boolean performFinish()
  {
    return true;
  }
}
