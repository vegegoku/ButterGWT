package org.akab.buttergwt.wizard.request;

import org.akab.buttergwt.wizard.ClassUtil;
import org.akab.buttergwt.wizard.PluginUtil;
import org.akab.engine.core.client.api.mvp.presenter.ViewPresenter;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewRequestPage
  extends WizardPage
{
  private DataBindingContext m_bindingContext;
  private Text requestName;
  private Text requestPresenter;
  private Text servletPath;
  private Button browsePresenterButton;
  private NewRequestItem newRequestItem;
  private Button emptyArguments;
  private Button emptyResponse;
  private Button executeOnServer;
  private Button downloadable;
  private Button uploadable;
  private Label servletPathLabel;
  
  public NewRequestPage(NewRequestItem newRequestItem)
  {
    super("wizardPage");
    this.newRequestItem = newRequestItem;
    setTitle("New request");
    setDescription("Create a new request classes");
  }
  
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, 0);
    
    setControl(container);
    container.setLayout(new GridLayout(4, false));
    
    Label lblNewLabel = new Label(container, 0);
    GridData gd_lblNewLabel = new GridData(131072, 16777216, false, false, 1, 1);
    gd_lblNewLabel.widthHint = 114;
    lblNewLabel.setLayoutData(gd_lblNewLabel);
    lblNewLabel.setText("Name");
    
    this.requestName = new Text(container, 2048);
    this.requestName.setLayoutData(new GridData(4, 16777216, true, false, 3, 1));
    
    Label lblNewLabel_1 = new Label(container, 0);
    lblNewLabel_1.setText("Presenter");
    
    this.requestPresenter = new Text(container, 2048);
    this.requestPresenter.setLayoutData(new GridData(4, 16777216, true, false, 2, 1));
    
    this.browsePresenterButton = new Button(container, 0);
    GridData gd_browsePresenterButton = new GridData(16384, 16777216, false, false, 1, 1);
    gd_browsePresenterButton.widthHint = 95;
    this.browsePresenterButton.setLayoutData(gd_browsePresenterButton);
    this.browsePresenterButton.setText("Browse");
    new Label(container, 0);
    
    this.emptyArguments = new Button(container, 32);
    GridData gd_emptyArguments = new GridData(16384, 16777216, false, false, 1, 1);
    gd_emptyArguments.widthHint = 158;
    this.emptyArguments.setLayoutData(gd_emptyArguments);
    this.emptyArguments.setText("Empty arguments");
    
    this.emptyResponse = new Button(container, 32);
    this.emptyResponse.setText("Empty rsponse");
    new Label(container, 0);
    new Label(container, 0);
    
    this.executeOnServer = new Button(container, 32);
    this.executeOnServer.setText("Execute on server");
    new Label(container, 0);
    new Label(container, 0);
    new Label(container, 0);
    
    this.downloadable = new Button(container, 32);
    this.downloadable.setText("Downloadable");
    
    this.uploadable = new Button(container, 32);
    this.uploadable.setText("Uploadable");
    new Label(container, 0);
    
    this.servletPathLabel = new Label(container, 0);
    this.servletPathLabel.setText("Servlet path");
    
    this.servletPath = new Text(container, 2048);
    this.servletPath.setLayoutData(new GridData(4, 16777216, true, false, 3, 1));
    new Label(container, 0);
    new Label(container, 0);
    new Label(container, 0);
    new Label(container, 0);
    this.m_bindingContext = initDataBindings();
    
    initControls();
  }
  
  private void initControls()
  {
    this.requestName.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent arg0)
      {
        NewRequestPage.this.validatePage();
      }
    });
    this.requestPresenter.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent arg0)
      {
        NewRequestPage.this.validatePage();
      }
    });
    this.browsePresenterButton.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
        try
        {
          IType selectedType = ClassUtil.browseClass(NewRequestPage.this.getShell(), NewRequestPage.this.getWizard(), 
            ViewPresenter.class.getName(), "*ViewPresenter", 
            "Select presenter");
          if ((NewRequestPage.this.requestPresenter != null) && (selectedType != null))
          {
            NewRequestPage.this.requestPresenter.setText(selectedType.getFullyQualifiedName());
            NewRequestPage.this.newRequestItem.setPresenterImport(NewRequestPage.this.requestPresenter.getText());
          }
        }
        catch (JavaModelException e1)
        {
          e1.printStackTrace();
          PluginUtil.showErrorMessage(e1);
        }
        NewRequestPage.this.validatePage();
      }
      
      public void widgetDefaultSelected(SelectionEvent e)
      {
        try
        {
          IType selectedType = ClassUtil.browseClass(NewRequestPage.this.getShell(), NewRequestPage.this.getWizard(), 
            ViewPresenter.class.getName(), "*ViewPresenter", 
            "Select presenter");
          if ((NewRequestPage.this.requestPresenter != null) && (selectedType != null))
          {
            NewRequestPage.this.requestPresenter.setText(selectedType.getFullyQualifiedName());
            NewRequestPage.this.newRequestItem.setPresenterImport(NewRequestPage.this.requestPresenter.getText());
          }
        }
        catch (JavaModelException e1)
        {
          e1.printStackTrace();
          PluginUtil.showErrorMessage(e1);
        }
        NewRequestPage.this.validatePage();
      }
    });
    this.downloadable.setEnabled(false);
    this.downloadable.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
        NewRequestPage.this.updatePathState();
        NewRequestPage.this.newRequestItem.setHasDownloadController(NewRequestPage.this.downloadable.getSelection());
        if ((NewRequestPage.this.downloadable.getSelection()) && (NewRequestPage.this.uploadable.getSelection())) {
          NewRequestPage.this.uploadable.setSelection(false);
        }
        NewRequestPage.this.validatePage();
      }
      
      public void widgetDefaultSelected(SelectionEvent e)
      {
        NewRequestPage.this.updatePathState();
        NewRequestPage.this.newRequestItem.setHasDownloadController(NewRequestPage.this.downloadable.getSelection());
        if ((NewRequestPage.this.downloadable.getSelection()) && (NewRequestPage.this.uploadable.getSelection())) {
          NewRequestPage.this.uploadable.setSelection(false);
        }
        NewRequestPage.this.validatePage();
      }
    });
    this.uploadable.setEnabled(false);
    this.uploadable.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
        NewRequestPage.this.updatePathState();
        NewRequestPage.this.newRequestItem.setHasUploadController(NewRequestPage.this.uploadable.getSelection());
        if ((NewRequestPage.this.downloadable.getSelection()) && (NewRequestPage.this.uploadable.getSelection())) {
          NewRequestPage.this.downloadable.setSelection(false);
        }
        NewRequestPage.this.validatePage();
      }
      
      public void widgetDefaultSelected(SelectionEvent e)
      {
        NewRequestPage.this.updatePathState();
        NewRequestPage.this.newRequestItem.setHasUploadController(NewRequestPage.this.uploadable.getSelection());
        if ((NewRequestPage.this.downloadable.getSelection()) && (NewRequestPage.this.uploadable.getSelection())) {
          NewRequestPage.this.downloadable.setSelection(false);
        }
        NewRequestPage.this.validatePage();
      }
    });
    this.executeOnServer.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
        NewRequestPage.this.newRequestItem.setServerRequest(NewRequestPage.this.executeOnServer.getSelection());
        NewRequestPage.this.downloadable.setEnabled(NewRequestPage.this.executeOnServer.getSelection());
        NewRequestPage.this.uploadable.setEnabled(NewRequestPage.this.executeOnServer.getSelection());
        NewRequestPage.this.validatePage();
      }
      
      public void widgetDefaultSelected(SelectionEvent e)
      {
        NewRequestPage.this.newRequestItem.setServerRequest(NewRequestPage.this.executeOnServer.getSelection());
        NewRequestPage.this.downloadable.setEnabled(NewRequestPage.this.executeOnServer.getSelection());
        NewRequestPage.this.uploadable.setEnabled(NewRequestPage.this.executeOnServer.getSelection());
        NewRequestPage.this.validatePage();
      }
    });
    this.servletPath.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent arg0)
      {
        NewRequestPage.this.validatePage();
      }
    });
  }
  
  private void updatePathState()
  {
    this.servletPath.setEnabled((this.downloadable.getSelection()) || (this.uploadable.getSelection()));
    if (this.servletPath.isEnabled()) {
      this.servletPathLabel.setText("Servlet path *: ");
    } else {
      this.servletPathLabel.setText("Servlet path : ");
    }
    validatePage();
  }
  
  protected DataBindingContext initDataBindings()
  {
    DataBindingContext bindingContext = new DataBindingContext();
    
    IObservableValue observeTextRequestNameObserveWidget = WidgetProperties.text(24).observe(this.requestName);
    IObservableValue requestNameNewRequestItemObserveValue = PojoProperties.value("requestName").observe(this.newRequestItem);
    bindingContext.bindValue(observeTextRequestNameObserveWidget, requestNameNewRequestItemObserveValue, null, null);
    
    IObservableValue observeTextRequestPresenterObserveWidget = WidgetProperties.text(24).observe(this.requestPresenter);
    IObservableValue presenterImportNewRequestItemObserveValue = PojoProperties.value("presenterImport").observe(this.newRequestItem);
    bindingContext.bindValue(observeTextRequestPresenterObserveWidget, presenterImportNewRequestItemObserveValue, null, null);
    
    IObservableValue observeSelectionEmptyArgumentsObserveWidget = WidgetProperties.selection().observe(this.emptyArguments);
    IObservableValue emptyRequestNewRequestItemObserveValue = PojoProperties.value("emptyRequest").observe(this.newRequestItem);
    bindingContext.bindValue(observeSelectionEmptyArgumentsObserveWidget, emptyRequestNewRequestItemObserveValue, null, null);
    
    IObservableValue observeSelectionEmptyResponseObserveWidget = WidgetProperties.selection().observe(this.emptyResponse);
    IObservableValue emptyResponseNewRequestItemObserveValue = PojoProperties.value("emptyResponse").observe(this.newRequestItem);
    bindingContext.bindValue(observeSelectionEmptyResponseObserveWidget, emptyResponseNewRequestItemObserveValue, null, null);
    
    IObservableValue observeSelectionExecuteOnServerObserveWidget = WidgetProperties.selection().observe(this.executeOnServer);
    IObservableValue serverRequestNewRequestItemObserveValue = PojoProperties.value("serverRequest").observe(this.newRequestItem);
    bindingContext.bindValue(observeSelectionExecuteOnServerObserveWidget, serverRequestNewRequestItemObserveValue, null, null);
    
    IObservableValue observeSelectionDownloadableObserveWidget = WidgetProperties.selection().observe(this.downloadable);
    IObservableValue hasDownloadControllerNewRequestItemObserveValue = PojoProperties.value("hasDownloadController").observe(this.newRequestItem);
    bindingContext.bindValue(observeSelectionDownloadableObserveWidget, hasDownloadControllerNewRequestItemObserveValue, null, null);
    
    IObservableValue observeSelectionUploadableObserveWidget = WidgetProperties.selection().observe(this.uploadable);
    IObservableValue hasUploadControllerNewRequestItemObserveValue = PojoProperties.value("hasUploadController").observe(this.newRequestItem);
    bindingContext.bindValue(observeSelectionUploadableObserveWidget, hasUploadControllerNewRequestItemObserveValue, null, null);
    
    return bindingContext;
  }
  
  protected Button getBrowsePresenterButton()
  {
    return this.browsePresenterButton;
  }
  
  protected Label getLblNewLabel_2()
  {
    return this.servletPathLabel;
  }
  
  private void validatePage()
  {
    boolean complete = true;
    if (StringUtils.isEmpty(this.requestName.getText()))
    {
      complete = false;
      setErrorMessage("Request name connot be empty!.");
    }
    if (StringUtils.isEmpty(this.requestPresenter.getText()))
    {
      complete = false;
      setErrorMessage("Request presenter connaot be empty!.");
    }
    if (((this.downloadable.getSelection()) || (this.uploadable.getSelection())) && (StringUtils.isEmpty(this.servletPath.getText())))
    {
      complete = false;
      setErrorMessage("Servlet path connot be empty when request is downloadable/uploadable.!");
    }
    setPageComplete(complete);
    if (complete) {
      setErrorMessage(null);
    }
  }
}
