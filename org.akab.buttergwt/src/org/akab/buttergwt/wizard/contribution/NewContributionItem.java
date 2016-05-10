package org.akab.buttergwt.wizard.contribution;

public class NewContributionItem
{
  private String contributionName;
  private String contributionDescription;
  private String contextImport;
  private String extensionPointImport;
  
  public String getContributionName()
  {
    return this.contributionName;
  }
  
  public void setContributionName(String contributionName)
  {
    this.contributionName = contributionName;
  }
  
  public void setPresenterName(String contributionName)
  {
    this.contributionName = contributionName;
  }
  
  public String getContributionDescription()
  {
    return this.contributionDescription;
  }
  
  public void setContributionDescription(String contributionDescription)
  {
    this.contributionDescription = contributionDescription;
  }
  
  public String getContextImport()
  {
    return this.contextImport;
  }
  
  public void setContextImport(String contextImport)
  {
    this.contextImport = contextImport;
  }
  
  public String getExtensionPointImport()
  {
    return this.extensionPointImport;
  }
  
  public void setExtensionPointImport(String extensionPointImport)
  {
    this.extensionPointImport = extensionPointImport;
  }
  
  public String toString()
  {
    return 
    
      "NewContributionItem [contributionName=" + this.contributionName + ", contributionDescription=" + this.contributionDescription + ", contextImport=" + this.contextImport + ", extensionPointImport=" + this.extensionPointImport + "]";
  }
}
