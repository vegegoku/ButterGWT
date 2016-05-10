package org.akab.buttergwt.wizard.download;

import org.akab.buttergwt.wizard.ClassUtil;
import org.akab.buttergwt.wizard.PluginUtil;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
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

public class NewDownloadControllerWizardPage
  extends WizardPage
{
  private Composite container;
  private Text controllerName;
  private Text requestImport;
  private Button browseRequest;
  private Text subPackage;
  private NewDownloadControllerItem newDownloadControllerItem;
  
  protected NewDownloadControllerWizardPage(String pageName, NewDownloadControllerItem newDownloadControllerItem)
  {
    super(pageName);
    this.newDownloadControllerItem = newDownloadControllerItem;
    setTitle("New download Controller");
    setDescription("Create new download controller and all its required classes");
  }
  
  public void createControl(Composite parent)
  {
    this.container = new Composite(parent, 0);
    GridLayout layout = new GridLayout();
    this.container.setLayout(layout);
    layout.numColumns = 3;
    GridData threeColumns = new GridData(768);
    GridData twoColumns = new GridData(4, 4, true, false);
    twoColumns.horizontalSpan = 2;
    
    Label controllerNameLabel = new Label(this.container, 0);
    controllerNameLabel.setText("Controller name * : ");
    
    this.controllerName = new Text(this.container, 2052);
    this.controllerName.setText("");
    this.controllerName.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent e) {}
      
      public void keyReleased(KeyEvent e)
      {
        if (!NewDownloadControllerWizardPage.this.controllerName.getText().isEmpty())
        {
          NewDownloadControllerWizardPage.this.validatePageCompletion();
          NewDownloadControllerWizardPage.this.newDownloadControllerItem.setName(NewDownloadControllerWizardPage.this.controllerName.getText());
        }
        else
        {
          NewDownloadControllerWizardPage.this.validatePageCompletion();
          NewDownloadControllerWizardPage.this.newDownloadControllerItem.setName(null);
        }
      }
    });
    this.controllerName.setLayoutData(twoColumns);
    
    Label requestImportLabel = new Label(this.container, 0);
    requestImportLabel.setText("Request * : ");
    
    this.requestImport = new Text(this.container, 2052);
    this.requestImport.setText("");
    this.requestImport.setEditable(false);
    
    this.requestImport.setLayoutData(threeColumns);
    
    this.browseRequest = new Button(this.container, 8);
    this.browseRequest.setText("Browse...");
    this.browseRequest.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
        try
        {
          IType selectedType = ClassUtil.browseClass(NewDownloadControllerWizardPage.this.getShell(), NewDownloadControllerWizardPage.this.getWizard(), "org.akab.engine.client.core.request.BaseRequest", "*Request", "Select Request");
          if ((NewDownloadControllerWizardPage.this.requestImport != null) && (selectedType != null))
          {
            NewDownloadControllerWizardPage.this.requestImport.setText(selectedType.getFullyQualifiedName());
            NewDownloadControllerWizardPage.this.newDownloadControllerItem.setRequestImport(NewDownloadControllerWizardPage.this.requestImport.getText());
          }
          NewDownloadControllerWizardPage.this.validatePageCompletion();
        }
        catch (JavaModelException e1)
        {
          e1.printStackTrace();
          PluginUtil.showErrorMessage(e1);
        }
      }
      
      public void widgetDefaultSelected(SelectionEvent e)
      {
        try
        {
          IType selectedType = ClassUtil.browseClass(NewDownloadControllerWizardPage.this.getShell(), NewDownloadControllerWizardPage.this.getWizard(), "org.akab.engine.client.core.request.BaseRequest", "*Request", "Select Request");
          if ((NewDownloadControllerWizardPage.this.requestImport != null) && (selectedType != null))
          {
            NewDownloadControllerWizardPage.this.requestImport.setText(selectedType.getFullyQualifiedName());
            NewDownloadControllerWizardPage.this.newDownloadControllerItem.setRequestImport(NewDownloadControllerWizardPage.this.requestImport.getText());
          }
        }
        catch (JavaModelException e1)
        {
          e1.printStackTrace();
          PluginUtil.showErrorMessage(e1);
        }
        NewDownloadControllerWizardPage.this.validatePageCompletion();
      }
    });
    Label subPackageLabel = new Label(this.container, 0);
    subPackageLabel.setText("Sub package : ");
    
    this.subPackage = new Text(this.container, 2052);
    this.subPackage.setText("");
    this.subPackage.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent e) {}
      
      public void keyReleased(KeyEvent e)
      {
        NewDownloadControllerWizardPage.this.newDownloadControllerItem.setSubPackage(NewDownloadControllerWizardPage.this.subPackage.getText());
      }
    });
    this.subPackage.setLayoutData(twoColumns);
    
    setControl(this.container);
    setPageComplete(false);
    validatePageCompletion();
  }
  
  private boolean validatePageCompletion()
  {
    boolean complete = true;
    if ((this.controllerName.getText() == null) || ("".equals(this.controllerName.getText())))
    {
      complete = false;
      setErrorMessage("Controller name connot be empty");
    }
    if ((this.requestImport.getText() == null) || ("".equals(this.requestImport.getText())))
    {
      complete = false;
      setErrorMessage("Request connot be empty.!");
    }
    if ((this.subPackage.getText() == null) || ("".equals(this.subPackage.getText())))
    {
      complete = false;
      setErrorMessage("Sub package connot be empty.!");
    }
    setPageComplete(complete);
    if (complete) {
      setErrorMessage(null);
    }
    return complete;
  }
}
