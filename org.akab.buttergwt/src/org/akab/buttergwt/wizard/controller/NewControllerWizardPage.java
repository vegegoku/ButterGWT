package org.akab.buttergwt.wizard.controller;

import org.akab.buttergwt.wizard.NameUtil;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewControllerWizardPage
  extends WizardPage
{
  private Composite container;
  private Text controllerName;
  private Text controllerPackage;
  private Text priority;
  private Text controllerDesciption;
  private Button handlesHistory;
  private NewControllerItem newControllerItem;
  
  protected NewControllerWizardPage(String pageName, NewControllerItem newControllerItem)
  {
    super(pageName);
    this.newControllerItem = newControllerItem;
    setTitle("New Controller");
    setDescription("Create new controller and all its required classes");
  }
  
  public void createControl(Composite parent)
  {
    this.container = new Composite(parent, 0);
    GridLayout layout = new GridLayout();
    this.container.setLayout(layout);
    layout.numColumns = 2;
    GridData gd = new GridData(768);
    
    Label controllerNameLabel = new Label(this.container, 0);
    controllerNameLabel.setText("Controller name * : ");
    
    this.controllerName = new Text(this.container, 2052);
    this.controllerName.setText("");
    this.controllerName.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent e) {}
      
      public void keyReleased(KeyEvent e)
      {
        if (!NewControllerWizardPage.this.controllerName.getText().isEmpty())
        {
          NewControllerWizardPage.this.validatePageCompletion();
          NewControllerWizardPage.this.newControllerItem.setControllerName(NewControllerWizardPage.this.controllerName.getText());
          NewControllerWizardPage.this.newControllerItem.setControllerConstantName(NameUtil.toTitleCase(NewControllerWizardPage.this.controllerName.getText()));
        }
        else
        {
          NewControllerWizardPage.this.validatePageCompletion();
          NewControllerWizardPage.this.newControllerItem.setControllerName(null);
          NewControllerWizardPage.this.newControllerItem.setControllerConstantName(null);
        }
      }
    });
    this.controllerName.setLayoutData(gd);
    
    Label subPackageLabel = new Label(this.container, 0);
    subPackageLabel.setText("Controller sub package : ");
    
    this.controllerPackage = new Text(this.container, 2052);
    this.controllerPackage.setText("");
    this.controllerPackage.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent e) {}
      
      public void keyReleased(KeyEvent e)
      {
        NewControllerWizardPage.this.newControllerItem.setControllerPackage(NewControllerWizardPage.this.controllerPackage.getText());
      }
    });
    this.controllerPackage.setLayoutData(gd);
    
    Label priorityLabel = new Label(this.container, 0);
    priorityLabel.setText("Priority : ");
    
    this.priority = new Text(this.container, 2052);
    this.priority.setText("");
    this.priority.addVerifyListener(new VerifyListener()
    {
      public void verifyText(VerifyEvent e)
      {
        String string = e.text;
        char[] chars = new char[string.length()];
        string.getChars(0, chars.length, chars, 0);
        for (int i = 0; i < chars.length; i++) {
          if (('0' > chars[i]) || (chars[i] > '9'))
          {
            e.doit = false;
            return;
          }
        }
      }
    });
    this.priority.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent e) {}
      
      public void keyReleased(KeyEvent e)
      {
        NewControllerWizardPage.this.newControllerItem.setPriority(NewControllerWizardPage.this.priority.getText());
      }
    });
    this.priority.setLayoutData(gd);
    
    Label controllerDescriptionLabel = new Label(this.container, 0);
    controllerDescriptionLabel.setText("Controller description : ");
    
    this.controllerDesciption = new Text(this.container, 2052);
    this.controllerDesciption.setText("");
    this.controllerDesciption.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent e) {}
      
      public void keyReleased(KeyEvent e)
      {
        NewControllerWizardPage.this.newControllerItem.setControllerDescription(NewControllerWizardPage.this.controllerDesciption.getText());
      }
    });
    this.controllerDesciption.setLayoutData(gd);
    
    Label handlesHistoryLabel = new Label(this.container, 0);
    handlesHistoryLabel.setText("Add History handling : ");
    this.handlesHistory = new Button(this.container, 32);
    this.handlesHistory.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
        NewControllerWizardPage.this.newControllerItem.setHandlesHistory(NewControllerWizardPage.this.handlesHistory.getSelection());
        NewControllerWizardPage.this.validatePageCompletion();
      }
      
      public void widgetDefaultSelected(SelectionEvent e)
      {
        NewControllerWizardPage.this.newControllerItem.setHandlesHistory(NewControllerWizardPage.this.handlesHistory.getSelection());
        NewControllerWizardPage.this.validatePageCompletion();
      }
    });
    this.handlesHistory.setLayoutData(gd);
    
    setControl(this.container);
    setPageComplete(false);
  }
  
  private boolean validatePageCompletion()
  {
    boolean complete = true;
    if ((this.controllerName.getText() == null) || ("".equals(this.controllerName.getText())))
    {
      complete = false;
      setErrorMessage("Controller name connot be empty");
    }
    setPageComplete(complete);
    if (complete) {
      setErrorMessage(null);
    }
    return complete;
  }
}
