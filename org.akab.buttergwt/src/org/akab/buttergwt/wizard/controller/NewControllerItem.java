package org.akab.buttergwt.wizard.controller;

public class NewControllerItem
{
  private String controllerName;
  private String controllerPackage;
  private String controllerConstantName;
  private String priority;
  private String controllerDescription;
  private boolean handlesHistory;
  private String initialContent = "";
  private String extraImports = "";
  
  public String getControllerName()
  {
    return this.controllerName;
  }
  
  public void setControllerName(String controllerName)
  {
    this.controllerName = controllerName;
  }
  
  public String getControllerPackage()
  {
    if (this.controllerPackage == null) {
      return "";
    }
    if (this.controllerPackage.startsWith(".")) {
      return this.controllerPackage;
    }
    return "." + this.controllerPackage;
  }
  
  public void setControllerPackage(String controllerPackage)
  {
    if (controllerPackage.startsWith(".")) {
      this.controllerPackage = controllerPackage;
    } else if ((controllerPackage != null) && (!"".equals(controllerPackage))) {
      this.controllerPackage = ("." + controllerPackage);
    } else {
      this.controllerPackage = "";
    }
  }
  
  public String getControllerConstantName()
  {
    return this.controllerConstantName;
  }
  
  public void setControllerConstantName(String controllerConstantName)
  {
    this.controllerConstantName = controllerConstantName;
  }
  
  public String getPriority()
  {
    return this.priority;
  }
  
  public void setPriority(String priority)
  {
    this.priority = priority;
  }
  
  public String getControllerDescription()
  {
    return this.controllerDescription;
  }
  
  public void setControllerDescription(String controllerDescription)
  {
    this.controllerDescription = controllerDescription;
  }
  
  public boolean isHandlesHistory()
  {
    return this.handlesHistory;
  }
  
  public void setHandlesHistory(boolean handlesHistory)
  {
    this.handlesHistory = handlesHistory;
  }
  
  public String getInitialContent()
  {
    return this.initialContent;
  }
  
  public void setInitialContent(String initialContent)
  {
    this.initialContent = initialContent;
  }
  
  public String getExtraImports()
  {
    return this.extraImports;
  }
  
  public void setExtraImports(String extraImports)
  {
    this.extraImports = extraImports;
  }
  
  public String toString()
  {
    return 
    
      "NewControllerItem [controllerName=" + this.controllerName + ", controllerPackage=" + this.controllerPackage + ", controllerConstantName=" + this.controllerConstantName + ", priority=" + this.priority + ", controllerDescription=" + this.controllerDescription + ", handlesHistory=" + this.handlesHistory + "]";
  }
}
