package org.akab.buttergwt.wizard.controller;

import org.akab.buttergwt.wizard.ClassUtil;
import org.akab.buttergwt.wizard.ProjectInfo;
import org.eclipse.jface.wizard.Wizard;

public class NewControllerWizard
  extends Wizard
{
  private NewControllerItem newControllerItem;
  
  public NewControllerWizard()
  {
    this.newControllerItem = new NewControllerItem();
    addPage(new NewControllerWizardPage("NewController", this.newControllerItem));
  }
  
  public boolean performFinish()
  {
    ProjectInfo projectInfo = ClassUtil.getSelectedProjectInfo();
    ControllerCreator.createController(this.newControllerItem, projectInfo);
    
    return true;
  }
}
