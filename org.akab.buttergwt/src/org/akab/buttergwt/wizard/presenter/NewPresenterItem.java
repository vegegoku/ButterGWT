package org.akab.buttergwt.wizard.presenter;

public class NewPresenterItem
{
  private String presenterName;
  private String presenterDescription;
  
  public String getPresenterName()
  {
    return this.presenterName;
  }
  
  public void setPresenterName(String presenterName)
  {
    this.presenterName = presenterName;
  }
  
  public String getPresenterDescription()
  {
    return this.presenterDescription;
  }
  
  public void setPresenterDescription(String presenterDescription)
  {
    this.presenterDescription = presenterDescription;
  }
  
  public String toString()
  {
    return 
      "NewPresenterItem [presenterName=" + this.presenterName + ", presenterDescription=" + this.presenterDescription + "]";
  }
}
