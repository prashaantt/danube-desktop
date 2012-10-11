package danube.desktop.web.ui.data;

import java.util.ResourceBundle;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Button;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Panel;
import nextapp.echo.app.ResourceImageReference;
import nextapp.echo.app.Row;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.layout.RowLayoutData;
import danube.desktop.web.ui.MessageDialog;
import danube.desktop.web.ui.xdi.XdiPanel;
import danube.desktop.xdi.XdiEndpoint;
import danube.desktop.xdi.events.XdiGraphDelEvent;
import danube.desktop.xdi.events.XdiGraphEvent;
import danube.desktop.xdi.events.XdiGraphListener;
import xdi2.core.util.StatementUtil;
import xdi2.core.xri3.impl.XRI3Segment;
import xdi2.messaging.Message;
import echopoint.ImageIcon;

public class FriendPanel extends Panel implements XdiGraphListener {

	private static final long serialVersionUID = -6674403250232180782L;

	protected ResourceBundle resourceBundle;

	private XdiEndpoint endpoint;
	private XRI3Segment contextNodeXri;
	private XRI3Segment targetContextNodeXri;

	private XdiPanel xdiPanel;
	private Button friendButton;
	private Button deleteButton;

	private FriendPanelDelegate friendPanelDelegate;

	/**
	 * Creates a new <code>AccountPersonaPanel</code>.
	 */
	public FriendPanel() {
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

			String friend = this.targetContextNodeXri.toString();

			this.xdiPanel.setEndpointAndGraphListener(this.endpoint, this);
			this.friendButton.setText(friend);
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

			if (xdiGraphEvent instanceof XdiGraphDelEvent) {

				this.getParent().remove(this);
				return;
			}
		} catch (Exception ex) {

			MessageDialog.problem("Sorry, a problem occurred while retrieving your Personal Data: " + ex.getMessage(), ex);
			return;
		}
	}

	public void setEndpointAndContextNodeXriAndTargetContextNodeXri(XdiEndpoint endpoint, XRI3Segment contextNodeXri, XRI3Segment targetContextNodeXri) {

		// remove us as listener

		if (this.endpoint != null) this.endpoint.removeXdiGraphListener(this);

		// refresh

		this.endpoint = endpoint;
		this.contextNodeXri = contextNodeXri;
		this.targetContextNodeXri = targetContextNodeXri;

		this.refresh();

		// add us as listener

		this.endpoint.addXdiGraphListener(this);
	}

	public void setFriendPanelDelegate(FriendPanelDelegate friendPanelDelegate) {

		this.friendPanelDelegate = friendPanelDelegate;
	}

	public FriendPanelDelegate getFriendPanelDelegate() {

		return this.friendPanelDelegate;
	}

	private void onFriendActionPerformed(ActionEvent e) {

		if (this.friendPanelDelegate != null) {

			this.friendPanelDelegate.onFriendActionPerformed(e);
		}
	}

	private void onDeleteActionPerformed(ActionEvent e) {

		try {

			// $del

			Message message = this.endpoint.prepareMessage();
			message.createDelOperation(StatementUtil.fromRelationComponents(this.contextNodeXri, new XRI3Segment("+friend"), this.targetContextNodeXri));

			this.endpoint.send(message);
		} catch (Exception ex) {

			MessageDialog.problem("Sorry, a problem occurred while storing your Personal Data: " + ex.getMessage(), ex);
			return;
		}
	}

	public static interface FriendPanelDelegate {

		public void onFriendActionPerformed(ActionEvent e);
	}

	/**
	 * Configures initial state of component.
	 * WARNING: AUTO-GENERATED METHOD.
	 * Contents will be overwritten.
	 */
	private void initComponents() {
		Row row1 = new Row();
		row1.setCellSpacing(new Extent(10, Extent.PX));
		add(row1);
		xdiPanel = new XdiPanel();
		RowLayoutData xdiPanelLayoutData = new RowLayoutData();
		xdiPanelLayoutData.setAlignment(new Alignment(Alignment.DEFAULT,
				Alignment.CENTER));
		xdiPanel.setLayoutData(xdiPanelLayoutData);
		row1.add(xdiPanel);
		ImageIcon imageIcon1 = new ImageIcon();
		ResourceImageReference imageReference1 = new ResourceImageReference(
				"/danube/desktop/web/resource/image/friend.png");
		imageIcon1.setIcon(imageReference1);
		imageIcon1.setHeight(new Extent(48, Extent.PX));
		imageIcon1.setWidth(new Extent(48, Extent.PX));
		row1.add(imageIcon1);
		friendButton = new Button();
		friendButton.setStyleName("Plain");
		friendButton.setText("...");
		friendButton.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				onFriendActionPerformed(e);
			}
		});
		row1.add(friendButton);
		deleteButton = new Button();
		deleteButton.setStyleName("Plain");
		ResourceImageReference imageReference2 = new ResourceImageReference(
				"/danube/desktop/web/resource/image/op-cancel.png");
		deleteButton.setIcon(imageReference2);
		RowLayoutData deleteButtonLayoutData = new RowLayoutData();
		deleteButtonLayoutData.setAlignment(new Alignment(Alignment.DEFAULT,
				Alignment.CENTER));
		deleteButton.setLayoutData(deleteButtonLayoutData);
		deleteButton.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				onDeleteActionPerformed(e);
			}
		});
		row1.add(deleteButton);
	}
}
