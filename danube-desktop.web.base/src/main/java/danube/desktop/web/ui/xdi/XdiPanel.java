package danube.desktop.web.ui.xdi;

import java.util.ResourceBundle;

import nextapp.echo.app.Button;
import nextapp.echo.app.Panel;
import nextapp.echo.app.ResourceImageReference;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import danube.desktop.web.ui.DeveloperModeComponent;
import danube.desktop.web.ui.MainWindow;
import danube.desktop.xdi.XdiEndpoint;
import danube.desktop.xdi.events.XdiGraphListener;

public class XdiPanel extends Panel implements DeveloperModeComponent {

	private static final long serialVersionUID = -5082464847478633075L;

	protected ResourceBundle resourceBundle;

	private XdiEndpoint endpoint;
	private XdiGraphListener graphListener;

	/**
	 * Creates a new <code>ClaimPanel</code>.
	 */
	public XdiPanel() {
		super();

		// Add design-time configured components.
		initComponents();
	}

	@Override
	public void init() {

		super.init();
		
		this.setVisible(MainWindow.findMainContentPane(this).isDeveloperModeSelected());
	}

	@Override
	public void dispose() {

		super.dispose();
	}

	public void setEndpointAndGraphListener(XdiEndpoint endpoint, XdiGraphListener graphListener) {

		this.endpoint = endpoint;
		this.graphListener = graphListener;
	}

	public XdiEndpoint getEndpoint() {

		return this.endpoint;
	}

	public XdiGraphListener getGraphListener() {
		
		return this.graphListener;
	}

	private void onButtonActionPerformed(ActionEvent e) {

		XdiWindowPane xdiWindowPane = new XdiWindowPane();
		xdiWindowPane.setEndpointAndGraphListener(this.endpoint, this.graphListener);

		MainWindow.findMainContentPane(this).add(xdiWindowPane);
	}

	/**
	 * Configures initial state of component.
	 * WARNING: AUTO-GENERATED METHOD.
	 * Contents will be overwritten.
	 */
	private void initComponents() {
		this.setVisible(false);
		Button button1 = new Button();
		button1.setStyleName("Plain");
		ResourceImageReference imageReference1 = new ResourceImageReference(
				"/danube/desktop/web/resource/image/xdi.png");
		button1.setIcon(imageReference1);
		button1.addActionListener(new ActionListener() {
			private static final long serialVersionUID = 1L;
	
			public void actionPerformed(ActionEvent e) {
				onButtonActionPerformed(e);
			}
		});
		add(button1);
	}
}
