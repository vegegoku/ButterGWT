package org.akab.buttergwt.wizard;

public class ProjectInfo
{
  private String groupId;
  private String artifactId;
  private String name;
  private String basePackage;
  private String subPackage;
  private boolean generateSampleCode = true;
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getBasePackage()
  {
    return this.basePackage;
  }
  
  public void setBasePackage(String basePackage)
  {
    this.basePackage = basePackage;
  }
  
  public String getSubPackage()
  {
    return this.subPackage;
  }
  
  public void setSubPackage(String subPackage)
  {
    this.subPackage = subPackage;
  }
  
  public boolean isGenerateSampleCode()
  {
    return this.generateSampleCode;
  }
  
  public void setGenerateSampleCode(boolean generateSampleCode)
  {
    this.generateSampleCode = generateSampleCode;
  }
  
  public String getGroupId()
  {
    return this.groupId;
  }
  
  public void setGroupId(String groupId)
  {
    this.groupId = groupId;
  }
  
  public String getArtifactId()
  {
    return this.artifactId;
  }
  
  public void setArtifactId(String artifactId)
  {
    this.artifactId = artifactId;
  }
  
  public String toString()
  {
    return "ProjectInfo [name=" + this.name + ", basePackage=" + this.basePackage + ", subPackage=" + this.subPackage + "]";
  }
}
