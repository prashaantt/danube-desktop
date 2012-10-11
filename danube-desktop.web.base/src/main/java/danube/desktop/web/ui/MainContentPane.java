package danube.desktop.web.ui;



import java.util.EventObject;
import java.util.ResourceBundle;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Button;
import nextapp.echo.app.CheckBox;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.Extent;
import nextapp.echo.app.FillImage;
import nextapp.echo.app.Grid;
import nextapp.echo.app.IllegalChildException;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Label;
import nextapp.echo.app.Panel;
import nextapp.echo.app.ResourceImageReference;
import nextapp.echo.app.Row;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.WindowPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.layout.RowLayoutData;
import nextapp.echo.app.layout.SplitPaneLayoutData;
import nextapp.echo.extras.app.ToolTipContainer;
import danube.desktop.web.DesktopApplication;
import danube.desktop.web.events.ApplicationContextClosedEvent;
import danube.desktop.web.events.ApplicationContextOpenedEvent;
import danube.desktop.web.events.ApplicationEvent;
import danube.desktop.web.events.ApplicationListener;
import danube.desktop.web.ui.accountroot.AccountRootWindowPane;
import danube.desktop.web.ui.app.DesktopWebApp;
import danube.desktop.web.ui.connectors.FacebookConnectorPanel;
import danube.desktop.web.ui.connectors.PersonalConnectorPanel;
import danube.desktop.web.ui.dataexport.DataExportWindowPane;
import danube.desktop.web.ui.dataimport.DataImportWindowPane;
import danube.desktop.web.ui.endpoint.EndpointWindowPane;
import danube.desktop.web.ui.log.LogWindowPane;
import danube.desktop.xdi.XdiEndpoint;
import xdi2.connector.facebook.mapping.FacebookMapping;
import xdi2.connector.personal.mapping.PersonalMapping;
import xdi2.core.constants.XDIConstants;
import xdi2.core.xri3.impl.XRI3Segment;
import xdi2.messaging.Message;
import xdi2.messaging.constants.XDIMessagingConstants;
import echopoint.ImageIcon;
import danube.desktop.web.ui.connectors.AllfiledConnectorPanel;
import danube.desktop.web.ui.AccountRootGrid;

public class MainContentPane extends ContentPane implements ApplicationListener {

	private static final long serialVersionUID = 3164240822381021756L;

	protected ResourceBundle resourceBundle;

	private XdiEndpoint endpoint;

	private Column pdsColumn;
	private AccountRootGrid accountRootGrid;
	private FacebookConnectorPanel facebookConnectorPanel;
	private PersonalConnectorPanel personalConnectorPanel;
	private CheckBox logWindowCheckBox;
	private CheckBox developerModeCheckBox;
	private Grid pdsWebAppGrid;

	private AllfiledConnectorPanel allfiledConnectorPanel;

	/**
	 * Creates a new <code>MainContentPane</code>.
	 */
	public MainContentPane() {
		super();

		// Add design-time configured components.
		initComponents();
	}

	@Override
	public void init() {

		super.init();

		// add PdsWebApps

		for (final DesktopWebApp pdsWebApp : DesktopApplication.getApp().getServlet().getDesktopWebApps()) {

			Button pdsWebAppButton = new Button();
			pdsWebAppButton.setStyleName("PlainWhite");
			ResourceImageReference imageReference = pdsWebApp.getResourceImageReference();
			pdsWebAppButton.setIcon(imageReference);
			pdsWebAppButton.setText(pdsWebApp.getName());
			pdsWebAppButton.setInsets(new Insets(new Extent(10, Extent.PX)));

			pdsWebAppButton.addActionListener(new ActionListener() {

				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {

					pdsWebApp.onActionPerformed(MainContentPane.this, MainContentPane.this.getEndpoint());
				}
			});
			MainContentPane.this.pdsWebAppGrid.add(pdsWebAppButton);
		}

		// add us as listener

		DesktopApplication.getApp().addApplicationListener(this);
	}

	@Override
	public void dispose() {

		super.dispose();

		// remove us as listener

		DesktopApplication.getApp().removeApplicationListener(this);
	}

	@Override
	public void add(Component component, int n) throws IllegalChildException {

		super.add(component, n);

		if (component instanceof MessageDialog) {

			((MessageDialog) component).setZIndex(Integer.MAX_VALUE);
		} if (component instanceof WindowPane) {

			((WindowPane) component).setZIndex(Integer.MAX_VALUE - 1);
		}
	}

	private void refresh(EventObject event) {

		XRI3Segment facebookXdiAttributeXri = new XRI3Segment("" + FacebookMapping.XRI_S_FACEBOOK_CONTEXT + this.endpoint.getCanonical() + XDIMessagingConstants.XRI_S_OAUTH_TOKEN);
		XRI3Segment personalXdiAttributeXri = new XRI3Segment("" + PersonalMapping.XRI_S_PERSONAL_CONTEXT + this.endpoint.getCanonical() + XDIMessagingConstants.XRI_S_OAUTH_TOKEN);

		this.facebookConnectorPanel.setEndpointAndXdiAttribute(this.endpoint, null, facebookXdiAttributeXri);
		this.personalConnectorPanel.setEndpointAndXdiAttribute(this.endpoint, null, personalXdiAttributeXri);
	}

	public XdiEndpoint getEndpoint() {

		return this.endpoint;
	}

	public boolean isDeveloperModeSelected() {

		return this.developerModeCheckBox.isSelected();
	}

	public void onApplicationEvent(ApplicationEvent applicationEvent) {

		if (applicationEvent instanceof ApplicationContextOpenedEvent) {

			this.pdsColumn.setVisible(true);

			this.endpoint = ((ApplicationContextOpenedEvent) applicationEvent).getEndpoint();

			this.refresh(applicationEvent);
		}

		if (applicationEvent instanceof ApplicationContextClosedEvent) {

			this.pdsColumn.setVisible(false);

			for (Component component : MainWindow.findChildComponentsByClass(this, WindowPane.class)) {

				if (component instanceof LogWindowPane) continue;
				if (component instanceof EndpointWindowPane) continue;

				this.remove(component);
			}
		}
	}

	private void onAccountRootActionPerformed(ActionEvent e) {

		XRI3Segment contextNodeXri = this.endpoint.getCanonical();
		AccountRootWindowPane accountRootWindowPane = new AccountRootWindowPane();
		accountRootWindowPane.setEndpointAndContextNodeXri(this.endpoint, contextNodeXri);

		this.add(accountRootWindowPane);
	}

	private void onLinkContractsActionPerformed(ActionEvent e) {

		MessageDialog.notImplemented();
	}

	private void onLogWindowActionPerformed(ActionEvent e) {

		if (this.logWindowCheckBox.isSelected()) {

			MainWindow.findChildComponentByClass(this, LogWindowPane.class).setVisible(true);
		} else {

			MainWindow.findChildComponentByClass(this, LogWindowPane.class).setVisible(false);
		}
	}

	private void onDeveloperModeActionPerformed(ActionEvent e) {

		if (this.developerModeCheckBox.isSelected()) {

			MainWindow.findChildComponentById(this, "transactionEventPanelsContentPane").setVisible(true);
			for (Component component : MainWindow.findChildComponentsByClass(this, DeveloperModeComponent.class)) component.setVisible(true);
		} else {

			MainWindow.findChildComponentById(this, "transactionEventPanelsContentPane").setVisible(false);
			for (Component component : MainWindow.findChildComponentsByClass(this, DeveloperModeComponent.class)) component.setVisible(false);
		}
	}

	private void onResetDataActionPerformed(ActionEvent e) {

		MessageDialog.yesNo("Really delete all your Personal Data? This operation can not be reversed!", new ActionListener() {

			private static final long serialVersionUID = -4208410059076895667L;

			@Override
			public void actionPerformed(ActionEvent ee) {

				if (ee.getActionCommand().equals(MessageDialog.COMMAND_OK)) {

					// do a $del on everything

					try {

						Message message = MainContentPane.this.endpoint.prepareOperation(XDIMessagingConstants.XRI_S_DEL, XDIConstants.XRI_S_ROOT);
						MainContentPane.this.endpoint.send(message);
					} catch (Exception ex) {

						MessageDialog.problem("Sorry, a problem occurred while deleting your Personal Data: " + ex.getMessage(), ex);
						return;
					}

					MessageDialog.info("All your Personal Data has been deleted!");
				}
			}
		});
	}

	private void onDataExportActionPerformed(ActionEvent e) {

		DataExportWindowPane dataExportWindowPane = new DataExportWindowPane();
		dataExportWindowPane.setEndpoint(this.endpoint);

		this.add(dataExportWindowPane);
	}

	private void onDataImportActionPerformed(ActionEvent e) {

		DataImportWindowPane dataImportWindowPane = new DataImportWindowPane();
		dataImportWindowPane.setEndpoint(this.endpoint);

		this.add(dataImportWindowPane);
	}

	/**
	 * Configures initial state of component.
	 * WARNING: AUTO-GENERATED METHOD.
	 * Contents will be overwritten.
	 */
	private void initComponents() {
		ResourceImageReference imageReference1 = new ResourceImageReference(
				"/danube/desktop/web/resource/image/mainback-gray.jpg");
		this.setBackgroundImage(new FillImage(imageReference1));
		SplitPane splitPane1 = new SplitPane();
		splitPane1.setStyleName("Default");
		splitPane1.setSeparatorVisible(false);
		add(splitPane1);
		Column column1 = new Column();
		splitPane1.add(column1);
		pdsColumn = new Column();
		pdsColumn.setVisible(false);
		pdsColumn.setInsets(new Insets(new Extent(30, Extent.PX)));
		pdsColumn.setCellSpacing(new Extent(20, Extent.PX));
		RowLayoutData pdsColumnLayoutData = new RowLayoutData();
		pdsColumnLayoutData.setAlignment(new Alignment(Alignment.LEFT,
				Alignment.DEFAULT));
		pdsColumn.setLayoutData(pdsColumnLayoutData);
		column1.add(pdsColumn);
		Row row4 = new Row();
		row4.setCellSpacing(new Extent(10, Extent.PX));
		pdsColumn.add(row4);
		Button button1 = new Button();
		button1.setStyleName("PlainWhite");
		ResourceImageReference imageReference2 = new ResourceImageReference(
				"/danube/desktop/web/resource/image/accountroot.png");
		button1.setIcon(imageReference2);
		button1.setText("Account Root");
		button1.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 1L;
	
			public void actionPerformed(ActionEvent e) {
				onAccountRootActionPerformed(e);
			}
		});
		row4.add(button1);
		Button button10 = new Button();
		button10.setStyleName("PlainWhite");
		ResourceImageReference imageReference3 = new ResourceImageReference(
				"/danube/desktop/web/resource/image/linkcontracts.png");
		button10.setIcon(imageReference3);
		button10.setText("Link Contracts");
		button10.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 1L;
	
			public void actionPerformed(ActionEvent e) {
				onLinkContractsActionPerformed(e);
			}
		});
		row4.add(button10);
		facebookConnectorPanel = new FacebookConnectorPanel();
		facebookConnectorPanel.setId("facebookConnectorPanel");
		row4.add(facebookConnectorPanel);
		personalConnectorPanel = new PersonalConnectorPanel();
		personalConnectorPanel.setId("personalConnectorPanel");
		row4.add(personalConnectorPanel);
		allfiledConnectorPanel = new AllfiledConnectorPanel();
		allfiledConnectorPanel.setId("allfiledConnectorPanel");
		row4.add(allfiledConnectorPanel);
		Label label1 = new Label();
		label1.setStyleName("Header");
		label1.setText("Account Personas");
		pdsColumn.add(label1);
		Row row7 = new Row();
		pdsColumn.add(row7);
		accountRootGrid = new AccountRootGrid();
		row7.add(accountRootGrid);
		Label label2 = new Label();
		label2.setStyleName("Header");
		label2.setText("Applications");
		pdsColumn.add(label2);
		pdsWebAppGrid = new Grid();
		pdsWebAppGrid.setOrientation(Grid.ORIENTATION_HORIZONTAL);
		pdsWebAppGrid.setSize(5);
		pdsColumn.add(pdsWebAppGrid);
		Label label3 = new Label();
		label3.setStyleName("Header");
		label3.setText("Data Housekeeping");
		pdsColumn.add(label3);
		Row row5 = new Row();
		row5.setCellSpacing(new Extent(20, Extent.PX));
		pdsColumn.add(row5);
		ToolTipContainer toolTipContainer1 = new ToolTipContainer();
		row5.add(toolTipContainer1);
		Button button2 = new Button();
		button2.setStyleName("PlainWhite");
		ResourceImageReference imageReference4 = new ResourceImageReference(
				"/danube/desktop/web/resource/image/data-clean.png");
		button2.setIcon(imageReference4);
		button2.setText("Reset data");
		button2.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 1L;
	
			public void actionPerformed(ActionEvent e) {
				onResetDataActionPerformed(e);
			}
		});
		toolTipContainer1.add(button2);
		Panel panel1 = new Panel();
		panel1.setStyleName("Tooltip");
		toolTipContainer1.add(panel1);
		Label label4 = new Label();
		label4.setStyleName("Default");
		label4.setText("This will clear all data from your Personal Cloud. Make sure you have a backup!");
		panel1.add(label4);
		ToolTipContainer toolTipContainer2 = new ToolTipContainer();
		row5.add(toolTipContainer2);
		Button button3 = new Button();
		button3.setStyleName("PlainWhite");
		ResourceImageReference imageReference5 = new ResourceImageReference(
				"/danube/desktop/web/resource/image/data-export.png");
		button3.setIcon(imageReference5);
		button3.setText("Data Export");
		button3.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 1L;
	
			public void actionPerformed(ActionEvent e) {
				onDataExportActionPerformed(e);
			}
		});
		toolTipContainer2.add(button3);
		Panel panel2 = new Panel();
		panel2.setStyleName("Tooltip");
		toolTipContainer2.add(panel2);
		Label label5 = new Label();
		label5.setStyleName("Default");
		label5.setText("This allows you to download all the contents of your Personal Cloud as an XDI file.");
		panel2.add(label5);
		ToolTipContainer toolTipContainer3 = new ToolTipContainer();
		row5.add(toolTipContainer3);
		Button button11 = new Button();
		button11.setStyleName("PlainWhite");
		ResourceImageReference imageReference6 = new ResourceImageReference(
				"/danube/desktop/web/resource/image/data-import.png");
		button11.setIcon(imageReference6);
		button11.setText("Data Import");
		button11.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 1L;
	
			public void actionPerformed(ActionEvent e) {
				onDataImportActionPerformed(e);
			}
		});
		toolTipContainer3.add(button11);
		Panel panel3 = new Panel();
		panel3.setStyleName("Tooltip");
		toolTipContainer3.add(panel3);
		Label label6 = new Label();
		label6.setStyleName("Default");
		label6.setText("This allows you to import data from an XDI file input your Personal Cloud.");
		panel3.add(label6);
		Column column2 = new Column();
		column2.setInsets(new Insets(new Extent(10, Extent.PX)));
		SplitPaneLayoutData column2LayoutData = new SplitPaneLayoutData();
		column2LayoutData.setAlignment(new Alignment(Alignment.RIGHT,
				Alignment.DEFAULT));
		column2LayoutData.setMinimumSize(new Extent(400, Extent.PX));
		column2LayoutData.setMaximumSize(new Extent(400, Extent.PX));
		column2.setLayoutData(column2LayoutData);
		splitPane1.add(column2);
		ImageIcon imageIcon1 = new ImageIcon();
		ResourceImageReference imageReference7 = new ResourceImageReference(
				"/danube/desktop/web/resource/image/pds-logo.png");
		imageIcon1.setIcon(imageReference7);
		imageIcon1.setHeight(new Extent(45, Extent.PX));
		imageIcon1.setWidth(new Extent(337, Extent.PX));
		imageIcon1.setInsets(new Insets(new Extent(0, Extent.PX), new Extent(
				10, Extent.PX), new Extent(0, Extent.PX), new Extent(0,
				Extent.PX)));
		column2.add(imageIcon1);
		Row row2 = new Row();
		row2.setAlignment(new Alignment(Alignment.RIGHT, Alignment.DEFAULT));
		row2.setInsets(new Insets(new Extent(0, Extent.PX), new Extent(0,
				Extent.PX), new Extent(0, Extent.PX), new Extent(10, Extent.PX)));
		row2.setCellSpacing(new Extent(10, Extent.PX));
		column2.add(row2);
		ImageIcon imageIcon2 = new ImageIcon();
		ResourceImageReference imageReference8 = new ResourceImageReference(
				"/danube/desktop/web/resource/image/projectdanube.png");
		imageIcon2.setIcon(imageReference8);
		imageIcon2.setHeight(new Extent(68, Extent.PX));
		imageIcon2.setWidth(new Extent(68, Extent.PX));
		row2.add(imageIcon2);
		Column column3 = new Column();
		column3.setCellSpacing(new Extent(10, Extent.PX));
		row2.add(column3);
		logWindowCheckBox = new CheckBox();
		logWindowCheckBox.setSelected(false);
		logWindowCheckBox.setText("Show Log Window");
		logWindowCheckBox.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 1L;
	
			public void actionPerformed(ActionEvent e) {
				onLogWindowActionPerformed(e);
			}
		});
		column3.add(logWindowCheckBox);
		developerModeCheckBox = new CheckBox();
		developerModeCheckBox.setSelected(false);
		developerModeCheckBox.setText("Enable Developer Mode");
		developerModeCheckBox.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 1L;
	
			public void actionPerformed(ActionEvent e) {
				onDeveloperModeActionPerformed(e);
			}
		});
		column3.add(developerModeCheckBox);
		LogWindowPane logWindowPane1 = new LogWindowPane();
		logWindowPane1.setVisible(false);
		add(logWindowPane1);
		EndpointWindowPane accountWindowPane1 = new EndpointWindowPane();
		add(accountWindowPane1);
	}
}
