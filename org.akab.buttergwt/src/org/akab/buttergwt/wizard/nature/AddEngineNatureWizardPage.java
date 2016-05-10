package org.akab.buttergwt.wizard.nature;

import org.akab.buttergwt.wizard.ProjectInfo;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class AddEngineNatureWizardPage
  extends WizardPage
{
  private Composite container;
  private Text basePackage;
  private Text subPackage;
  private Button generateSampleCode;
  private ProjectInfo projectInfo;
  
  protected AddEngineNatureWizardPage(String pageName, ProjectInfo projectInfo)
  {
    super(pageName);
    this.projectInfo = projectInfo;
    setTitle("Add Engine nature");
    setDescription("Add engine nature to the project");
  }
  
  public void createControl(Composite parent)
  {
    this.container = new Composite(parent, 0);
    GridLayout layout = new GridLayout();
    this.container.setLayout(layout);
    layout.numColumns = 2;
    GridData gd = new GridData(768);
    
    Label basePackageLabel = new Label(this.container, 0);
    basePackageLabel.setText("Base package * : ");
    
    this.basePackage = new Text(this.container, 2052);
    this.basePackage.setText("");
    this.basePackage.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent e) {}
      
      public void keyReleased(KeyEvent e)
      {
        if (!AddEngineNatureWizardPage.this.basePackage.getText().isEmpty()) {
          AddEngineNatureWizardPage.this.projectInfo.setBasePackage(AddEngineNatureWizardPage.this.basePackage.getText());
        } else {
          AddEngineNatureWizardPage.this.projectInfo.setBasePackage(null);
        }
        AddEngineNatureWizardPage.this.validatePageCompletion();
      }
    });
    this.basePackage.setLayoutData(gd);
    
    Label subPackageLabel = new Label(this.container, 0);
    subPackageLabel.setText("Sub package * : ");
    
    this.subPackage = new Text(this.container, 2052);
    this.subPackage.setText("");
    this.subPackage.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent e) {}
      
      public void keyReleased(KeyEvent e)
      {
        AddEngineNatureWizardPage.this.projectInfo.setSubPackage(AddEngineNatureWizardPage.this.subPackage.getText());
        AddEngineNatureWizardPage.this.validatePageCompletion();
      }
    });
    this.subPackage.setLayoutData(gd);
    
    Label generateSampleCodeLabel = new Label(this.container, 0);
    generateSampleCodeLabel.setText("Generate sample code : ");
    
    this.generateSampleCode = new Button(this.container, 32);
    this.generateSampleCode.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
        AddEngineNatureWizardPage.this.projectInfo.setGenerateSampleCode(AddEngineNatureWizardPage.this.generateSampleCode.getSelection());
      }
      
      public void widgetDefaultSelected(SelectionEvent e)
      {
        AddEngineNatureWizardPage.this.projectInfo.setGenerateSampleCode(AddEngineNatureWizardPage.this.generateSampleCode.getSelection());
      }
    });
    GridData twoColumns = new GridData(4, 4, true, false);
    twoColumns.horizontalSpan = 2;
    
    this.generateSampleCode.setLayoutData(gd);
    
    setControl(this.container);
    setPageComplete(false);
  }
  
  private boolean validatePageCompletion()
  {
    boolean complete = true;
    if ((this.basePackage.getText() == null) || ("".equals(this.basePackage.getText())))
    {
      complete = false;
      setErrorMessage("Base package connot be empty");
    }
    if ((this.subPackage.getText() == null) || ("".equals(this.subPackage.getText())))
    {
      complete = false;
      setErrorMessage("Sub package connot be empty");
    }
    setPageComplete(complete);
    if (complete) {
      setErrorMessage(null);
    }
    return complete;
  }
}
