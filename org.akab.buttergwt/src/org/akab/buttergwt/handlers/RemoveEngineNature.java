package org.akab.buttergwt.handlers;

import org.akab.buttergwt.wizard.ClassUtil;
import org.akab.buttergwt.wizard.PluginUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class RemoveEngineNature
  extends AbstractHandler
{
  public Object execute(ExecutionEvent event)
    throws ExecutionException
  {
    try
    {
      IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
      ClassUtil.SHELL = window.getShell();
      IProject project = ClassUtil.getCurrentProject();
      
      IProjectDescription description = project.getDescription();
      
      String[] natures = description.getNatureIds();
      boolean natureExists = false;
      String[] arrayOfString1;
      int j = (arrayOfString1 = natures).length;
      for (int i = 0; i < j; i++)
      {
        String nature = arrayOfString1[i];
        if ("org.akab.buttergwt.enginenature".equals(nature)) {
          natureExists = true;
        }
      }
      if (natureExists)
      {
        String[] newNatures = new String[natures.length - 1];
        int source = -1;
        int destination = -1;
        String[] arrayOfString2;
        int m = (arrayOfString2 = natures).length;
        for (int k = 0; k < m; k++)
        {
          String nature = arrayOfString2[k];
          if ("org.akab.buttergwt.enginenature".equals(nature)) {
            source++;
          }
          source++;
          destination++;
          if (destination < newNatures.length) {
            newNatures[destination] = natures[source];
          }
        }
        description.setNatureIds(newNatures);
        project.setDescription(description, null);
        
        IFile file = project.getFile("engineproject.properties");
        file.delete(true, null);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      PluginUtil.showErrorMessage(ex);
    }
    return null;
  }
}
