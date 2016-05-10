package org.akab.buttergwt.handlers;

import org.akab.buttergwt.wizard.PluginUtil;
import org.akab.buttergwt.wizard.plugin.NewPluginWizard;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class NewPluginHandler
  extends AbstractHandler
{
  public Object execute(ExecutionEvent event)
    throws ExecutionException
  {
    try
    {
      IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
      org.akab.buttergwt.wizard.ClassUtil.SHELL = window.getShell();
      
      WizardDialog dialog = new WizardDialog(window.getShell(), new NewPluginWizard());
      dialog.open();
    }
    catch (Exception ex)
    {
      PluginUtil.showErrorMessage(ex);
    }
    return null;
  }
}
