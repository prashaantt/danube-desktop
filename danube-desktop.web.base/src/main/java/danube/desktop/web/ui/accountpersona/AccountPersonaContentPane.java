package danube.desktop.web.ui.accountpersona;

import java.util.ResourceBundle;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Button;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Label;
import nextapp.echo.app.ResourceImageReference;
import nextapp.echo.app.Row;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.layout.RowLayoutData;
import nextapp.echo.app.layout.SplitPaneLayoutData;
import danube.desktop.web.ui.MessageDialog;
import danube.desktop.web.ui.data.XdiEntityColumn;
import danube.desktop.web.ui.xdi.XdiPanel;
import danube.desktop.xdi.XdiEndpoint;
import danube.desktop.xdi.events.XdiGraphAddEvent;
import danube.desktop.xdi.events.XdiGraphDelEvent;
import danube.desktop.xdi.events.XdiGraphEvent;
import danube.desktop.xdi.events.XdiGraphListener;
import danube.desktop.xdi.events.XdiGraphModEvent;
import xdi2.client.exceptions.Xdi2ClientException;
import xdi2.core.xri3.impl.XRI3Segment;
import xdi2.messaging.Message;
import xdi2.messaging.MessageResult;
import xdi2.messaging.constants.XDIMessagingConstants;
import echopoint.ImageIcon;

public class AccountPersonaContentPane extends ContentPane implements XdiGraphListener {

	private static final long serialVersionUID = 5781883512857770059L;

	protected ResourceBundle resourceBundle;

	private XdiEndpoint endpoint;
	private XRI3Segment contextNodeXri;
	private XRI3Segment nameAddress;

	private Label nameLabel;
	private XdiPanel xdiPanel;
	private XdiEntityColumn xdiEntityColumn;

	/**
	 * Creates a new <code>ConfigureAPIsContentPane</code>.
	 */
	public AccountPersonaContentPane() {
		super();

		// Add design-time configured components.
		initComponents();
	}

	@Override
	public void init() {

		super.init();
	}

	@Override
	public void dispose() {

		super.dispose();

		// remove us as listener

		if (this.endpoint != null) this.endpoint.removeXdiGraphListener(this);
	}

	private void refresh() {

		try {

			this.nameLabel.setText(this.getName());
			this.xdiPanel.setEndpointAndGraphListener(this.endpoint, this);
			this.xdiEntityColumn.setEndpointAndContextNodeXri(this.endpoint, this.contextNodeXri, null);
		} catch (Exception ex) {

			MessageDialog.problem("Sorry, a problem occurred while retrieving your Personal Data: " + ex.getMessage(), ex);
			return;
		}
	}

	public XRI3Segment xdiMainAddress() {

		return this.contextNodeXri;
	}

	public XRI3Segment[] xdiGetAddresses() {

		return new XRI3Segment[] {
				this.contextNodeXri
		};
	}

	public XRI3Segment[] xdiAddAddresses() {

		return new XRI3Segment[0];
	}

	public XRI3Segment[] xdiModAddresses() {

		return new XRI3Segment[] {
				this.contextNodeXri
		};
	}

	public XRI3Segment[] xdiDelAddresses() {

		return new XRI3Segment[] {
				this.contextNodeXri
		};
	}

	public void onXdiGraphEvent(XdiGraphEvent xdiGraphEvent) {

		try {

			if (xdiGraphEvent instanceof XdiGraphAddEvent) {

				this.refresh();
				return;
			}

			if (xdiGraphEvent instanceof XdiGraphModEvent) {

				this.refresh();
				return;
			}

			if (xdiGraphEvent instanceof XdiGraphDelEvent) {

				this.getParent().getParent().remove(this.getParent());
				return;
			}
		} catch (Exception ex) {

			MessageDialog.problem("Sorry, a problem occurred while retrieving your Personal Data: " + ex.getMessage(), ex);
			return;
		}
	}

	public void setEndpointAndContextNodeXri(XdiEndpoint endpoint, XRI3Segment contextNodeXri) {

		// remove us as listener

		if (this.endpoint != null) this.endpoint.removeXdiGraphListener(this);

		// refresh

		this.endpoint = endpoint;
		this.contextNodeXri = contextNodeXri;

		this.refresh();

		// add us as listener

		this.endpoint.addXdiGraphListener(this);
	}

	private void onDeletePersona(ActionEvent e) {

		try {

			this.delete();
		} catch (Exception ex) {

			MessageDialog.problem("Sorry, a problem occurred while storing your Personal Data: " + ex.getMessage(), ex);
			return;
		}
	}

	private String getName() throws Xdi2ClientException {

		Message message = this.endpoint.prepareOperation(XDIMessagingConstants.XRI_S_GET, this.nameAddress);
		MessageResult messageResult = this.endpoint.send(message);

		return messageResult.getGraph().findLiteral(this.nameAddress).getLiteralData();
	}

	private void delete() throws Xdi2ClientException {

		Message message = this.endpoint.prepareOperation(XDIMessagingConstants.XRI_S_DEL, this.contextNodeXri);

		this.endpoint.send(message);
	}

	/**
	 * Configures initial state of component.
	 * WARNING: AUTO-GENERATED METHOD.
	 * Contents will be overwritten.
	 */
	private void initComponents() {
		this.setInsets(new Insets(new Extent(10, Extent.PX)));
		SplitPane splitPane1 = new SplitPane();
		splitPane1.setStyleName("Default");
		splitPane1.setOrientation(SplitPane.ORIENTATION_VERTICAL_TOP_BOTTOM);
		splitPane1.setResizable(false);
		splitPane1.setSeparatorVisible(false);
		add(splitPane1);
		Row row2 = new Row();
		row2.setCellSpacing(new Extent(10, Extent.PX));
		SplitPaneLayoutData row2LayoutData = new SplitPaneLayoutData();
		row2LayoutData.setMinimumSize(new Extent(70, Extent.PX));
		row2LayoutData.setMaximumSize(new Extent(70, Extent.PX));
		row2.setLayoutData(row2LayoutData);
		splitPane1.add(row2);
		Row row5 = new Row();
		RowLayoutData row5LayoutData = new RowLayoutData();
		row5LayoutData.setWidth(new Extent(50, Extent.PERCENT));
		row5.setLayoutData(row5LayoutData);
		row2.add(row5);
		ImageIcon imageIcon2 = new ImageIcon();
		ResourceImageReference imageReference1 = new ResourceImageReference(
				"/danube/desktop/web/resource/image/accountpersona.png");
		imageIcon2.setIcon(imageReference1);
		imageIcon2.setHeight(new Extent(48, Extent.PX));
		imageIcon2.setWidth(new Extent(48, Extent.PX));
		row5.add(imageIcon2);
		nameLabel = new Label();
		nameLabel.setStyleName("Header");
		nameLabel.setText("...");
		row5.add(nameLabel);
		Row row3 = new Row();
		row3.setAlignment(new Alignment(Alignment.RIGHT, Alignment.DEFAULT));
		row3.setCellSpacing(new Extent(10, Extent.PX));
		RowLayoutData row3LayoutData = new RowLayoutData();
		row3LayoutData.setWidth(new Extent(50, Extent.PERCENT));
		row3.setLayoutData(row3LayoutData);
		row2.add(row3);
		xdiPanel = new XdiPanel();
		row3.add(xdiPanel);
		Button button1 = new Button();
		button1.setStyleName("Default");
		button1.setText("Delete this Persona");
		button1.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 1L;
	
			public void actionPerformed(ActionEvent e) {
				onDeletePersona(e);
			}
		});
		row3.add(button1);
		xdiEntityColumn = new XdiEntityColumn();
		splitPane1.add(xdiEntityColumn);
	}
}
