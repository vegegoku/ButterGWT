package org.akab.buttergwt;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class EngineNature
  implements IProjectNature
{
  public static final String NATURE_ID = "org.akab.buttergwt.enginenature";
  private IProject project;
  
  public void configure()
    throws CoreException
  {}
  
  public void deconfigure()
    throws CoreException
  {}
  
  public IProject getProject()
  {
    return this.project;
  }
  
  public void setProject(IProject project)
  {
    this.project = project;
  }
}
