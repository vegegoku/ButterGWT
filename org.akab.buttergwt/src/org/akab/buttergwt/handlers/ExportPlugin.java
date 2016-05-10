package org.akab.buttergwt.handlers;

import org.akab.buttergwt.wizard.PluginUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class ExportPlugin
  extends AbstractHandler
{
  public Object execute(ExecutionEvent event)
    throws ExecutionException
  {
    PluginUtil.exportPlugin();
    
    return null;
  }
}
