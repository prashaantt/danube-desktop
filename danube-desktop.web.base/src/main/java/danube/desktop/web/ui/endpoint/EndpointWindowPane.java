package danube.desktop.web.ui.endpoint;

import java.util.ResourceBundle;

import nextapp.echo.app.Extent;
import nextapp.echo.app.WindowPane;
import danube.desktop.web.DesktopApplication;
import danube.desktop.web.events.ApplicationContextClosedEvent;
import danube.desktop.web.events.ApplicationContextOpenedEvent;
import danube.desktop.web.events.ApplicationEvent;
import danube.desktop.web.events.ApplicationListener;
import danube.desktop.web.ui.endpoint.ClosedContentPane;

public class EndpointWindowPane extends WindowPane implements ApplicationListener {

	private static final long serialVersionUID = -2629138014392846780L;

	protected ResourceBundle resourceBundle;

	/**
	 * Creates a new <code>AccountWindowPane</code>.
	 */
	public EndpointWindowPane() {
		super();

		// Add design-time configured components.
		initComponents();
	}

	@Override
	public void init() {

		// put us into the center

		this.setPositionX(null);
		this.setPositionY(null);

		// add us as listener

		DesktopApplication.getApp().addApplicationListener(this);
	}

	@Override
	public void dispose() {

		// remove us as listener

		DesktopApplication.getApp().removeApplicationListener(this);
	}

	public void onApplicationEvent(ApplicationEvent applicationEvent) {

		if (applicationEvent instanceof ApplicationContextClosedEvent) {

			this.removeAll();
			ClosedContentPane closedContentPane = new ClosedContentPane();
			this.add(closedContentPane);

			// put us into the center

			this.setPositionX(null);
			this.setPositionY(null);
			this.setWidth(new Extent(600, Extent.PX));
			this.setHeight(new Extent(400, Extent.PX));
		}

		if (applicationEvent instanceof ApplicationContextOpenedEvent) {

			this.removeAll();
			OpenContentPane openContentPane = new OpenContentPane();
			openContentPane.setEndpoint(((ApplicationContextOpenedEvent) applicationEvent).getEndpoint());
			this.add(openContentPane);

			// put us into the bottom right corner

			this.setPositionX(new Extent(999999));
			this.setPositionY(new Extent(999999));
			this.setWidth(new Extent(450, Extent.PX));
			this.setHeight(new Extent(240, Extent.PX));
		}
	}

	/**
	 * Configures initial state of component.
	 * WARNING: AUTO-GENERATED METHOD.
	 * Contents will be overwritten.
	 */
	private void initComponents() {
		this.setStyleName("Glass");
		this.setZIndex(10);
		this.setTitle("Personal Cloud");
		this.setHeight(new Extent(400, Extent.PX));
		this.setMaximizeEnabled(false);
		this.setMinimizeEnabled(false);
		this.setClosable(false);
		this.setWidth(new Extent(600, Extent.PX));
		ClosedContentPane loginContentPane1 = new ClosedContentPane();
		add(loginContentPane1);
	}
}
