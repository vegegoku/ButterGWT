package org.akab.buttergwt.handlers;

import org.akab.buttergwt.wizard.ClassUtil;
import org.akab.buttergwt.wizard.presenter.NewPresenterWizard;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class NewPresenterHandler
  extends AbstractHandler
{
  public Object execute(ExecutionEvent event)
    throws ExecutionException
  {
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
    ClassUtil.SHELL = window.getShell();
    if (ClassUtil.isSelectedProjectHasEngineNature())
    {
      WizardDialog dialog = new WizardDialog(window.getShell(), new NewPresenterWizard());
      dialog.open();
    }
    else
    {
      MessageDialog.openInformation(window.getShell(), "Error", "Select an engine project");
    }
    return null;
  }
}
