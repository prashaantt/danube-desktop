package danube.desktop.web.ui.xdi;

import java.util.ResourceBundle;

import nextapp.echo.app.Border;
import nextapp.echo.app.Color;
import nextapp.echo.app.Column;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.layout.GridLayoutData;
import nextapp.echo.app.layout.RowLayoutData;
import nextapp.echo.app.layout.SplitPaneLayoutData;
import danube.desktop.web.DesktopApplication;
import danube.desktop.web.ui.MessageDialog;
import danube.desktop.xdi.XdiEndpoint;
import danube.desktop.xdi.events.XdiGraphListener;
import xdi2.messaging.Message;
import xdi2.messaging.MessageResult;
import xdi2.messaging.constants.XDIMessagingConstants;

public class XdiContentPane extends ContentPane {

	private static final long serialVersionUID = -6760462770679963055L;

	protected ResourceBundle resourceBundle;

	private XdiEndpoint endpoint;
	private XdiGraphListener graphListener;

	private Label xdiAddressLabel;
	private Label httpAddressLabel;

	private GraphContentPane graphContentPane;

	/**
	 * Creates a new <code>XdiContentPane</code>.
	 */
	public XdiContentPane() {
		super();

		// Add design-time configured components.
		initComponents();
	}

	/**
	 * Returns the user's application instance, cast to its specific type.
	 *
	 * @return The user's application instance.
	 */
	protected DesktopApplication getApplication() {
		return (DesktopApplication) getApplicationInstance();
	}

	private void refresh() {

		if (this.graphListener.xdiGetAddresses()  == null) return;

		try {

			Message message = this.endpoint.prepareOperations(XDIMessagingConstants.XRI_S_GET, this.graphListener.xdiGetAddresses());
			MessageResult messageResult = this.endpoint.send(message);

			String httpEndpoint = this.endpoint.getEndpoint();
			if (! httpEndpoint.endsWith("/")) httpEndpoint += "/";

			this.xdiAddressLabel.setText(this.graphListener.xdiMainAddress() == null ? "" : this.graphListener.xdiMainAddress().toString());
			this.httpAddressLabel.setText(httpEndpoint + (this.graphListener.xdiMainAddress() == null ? "" : this.graphListener.xdiMainAddress().toString()));
			this.graphContentPane.setGraph(messageResult.getGraph());
		} catch (Exception ex) {

			MessageDialog.problem("Sorry, a problem occurred while retrieving your Personal Data: " + ex.getMessage(), ex);
			return;
		}
	}

	public void setEndpointAndGraphListener(XdiEndpoint endpoint, XdiGraphListener graphListener) {

		this.endpoint = endpoint;
		this.graphListener = graphListener;

		this.refresh();
	}

	public XdiEndpoint getEndpoint() {

		return this.endpoint;
	}

	public XdiGraphListener getGraphListener() {

		return this.graphListener;
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
		splitPane1.setSeparatorHeight(new Extent(10, Extent.PX));
		splitPane1.setSeparatorVisible(false);
		add(splitPane1);
		Column column1 = new Column();
		column1.setCellSpacing(new Extent(10, Extent.PX));
		SplitPaneLayoutData column1LayoutData = new SplitPaneLayoutData();
		column1LayoutData.setMinimumSize(new Extent(110, Extent.PX));
		column1LayoutData.setMaximumSize(new Extent(110, Extent.PX));
		column1LayoutData.setOverflow(SplitPaneLayoutData.OVERFLOW_HIDDEN);
		column1.setLayoutData(column1LayoutData);
		splitPane1.add(column1);
		Row row1 = new Row();
		row1.setCellSpacing(new Extent(10, Extent.PX));
		row1.setBorder(new Border(new Extent(3, Extent.PX), Color.BLACK,
				Border.STYLE_SOLID));
		column1.add(row1);
		Label label1 = new Label();
		label1.setStyleName("Default");
		label1.setText("This window displays the raw XDI data of an object in your Personal Cloud.");
		RowLayoutData label1LayoutData = new RowLayoutData();
		label1LayoutData.setInsets(new Insets(new Extent(10, Extent.PX)));
		label1.setLayoutData(label1LayoutData);
		row1.add(label1);
		Column column2 = new Column();
		column2.setCellSpacing(new Extent(5, Extent.PX));
		column1.add(column2);
		Grid grid1 = new Grid();
		grid1.setOrientation(Grid.ORIENTATION_HORIZONTAL);
		grid1.setColumnWidth(0, new Extent(120, Extent.PX));
		grid1.setSize(2);
		column2.add(grid1);
		Label label2 = new Label();
		label2.setStyleName("Default");
		label2.setText("XDI Address:");
		GridLayoutData label2LayoutData = new GridLayoutData();
		label2LayoutData.setInsets(new Insets(new Extent(0, Extent.PX),
				new Extent(0, Extent.PX), new Extent(0, Extent.PX), new Extent(
						10, Extent.PX)));
		label2.setLayoutData(label2LayoutData);
		grid1.add(label2);
		xdiAddressLabel = new Label();
		xdiAddressLabel.setStyleName("Bold");
		xdiAddressLabel.setText("...");
		GridLayoutData xdiAddressLabelLayoutData = new GridLayoutData();
		xdiAddressLabelLayoutData.setInsets(new Insets(
				new Extent(0, Extent.PX), new Extent(0, Extent.PX), new Extent(
						0, Extent.PX), new Extent(5, Extent.PX)));
		xdiAddressLabel.setLayoutData(xdiAddressLabelLayoutData);
		grid1.add(xdiAddressLabel);
		Label label3 = new Label();
		label3.setStyleName("Default");
		label3.setText("HTTP Address:");
		grid1.add(label3);
		httpAddressLabel = new Label();
		httpAddressLabel.setStyleName("Bold");
		httpAddressLabel.setText("...");
		grid1.add(httpAddressLabel);
		graphContentPane = new GraphContentPane();
		splitPane1.add(graphContentPane);
	}
}
