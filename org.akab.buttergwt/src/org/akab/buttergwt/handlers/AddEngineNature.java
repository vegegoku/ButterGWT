package org.akab.buttergwt.handlers;

import org.akab.buttergwt.wizard.ClassUtil;
import org.akab.buttergwt.wizard.nature.AddEngineNatureWizard;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class AddEngineNature extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IProject project = ClassUtil.getCurrentProject();
		ClassUtil.SHELL = window.getShell();
		if (ClassUtil.isProjectHasEngineNature(project)) {
			MessageDialog.openInformation(window.getShell(), "Nature already added",
					"Selected project is an engine project");
			return null;
		}
		WizardDialog dialog = new WizardDialog(window.getShell(), new AddEngineNatureWizard());
		dialog.open();

		return null;
	}
}
