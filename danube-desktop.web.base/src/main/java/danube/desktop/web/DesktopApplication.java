package danube.desktop.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.TaskQueueHandle;
import nextapp.echo.app.Window;
import nextapp.echo.webcontainer.WebContainerServlet;
import danube.desktop.web.events.ApplicationContextClosedEvent;
import danube.desktop.web.events.ApplicationContextOpenedEvent;
import danube.desktop.web.events.ApplicationEvent;
import danube.desktop.web.events.ApplicationListener;
import danube.desktop.web.logger.Logger;
import danube.desktop.web.resource.style.Styles;
import danube.desktop.web.ui.MainContentPane;
import danube.desktop.web.ui.MainWindow;
import danube.desktop.xdi.XdiClient;
import danube.desktop.xdi.XdiEndpoint;

/**
 * Application instance implementation.
 */
public class DesktopApplication extends ApplicationInstance {

	private static final long serialVersionUID = -8129396233829232511L;

	private DesktopServlet servlet;
	private MainWindow mainWindow;
	private TaskQueueHandle taskQueueHandle;
	private Map<String, Object> attributes;
	private XdiEndpoint openedEndpoint;

	private transient Logger logger;
	private transient XdiClient xdiClient;

	private List<ApplicationListener> applicationListeners;

	public DesktopApplication(DesktopServlet servlet) {

		this.servlet = servlet;

		this.initLogger();
		this.initXdiClient();

		this.applicationListeners = new ArrayList<ApplicationListener> ();
	}

	public static DesktopApplication getApp() {

		return (DesktopApplication) getActive();
	}

	public static DesktopApplication getAppFromSession(HttpSession session) {

		return (DesktopApplication) session.getAttribute("__echoapp");
	}

	@Override
	public Window init() {

		HttpSession session = WebContainerServlet.getActiveConnection().getRequest().getSession();
		session.setAttribute("__echoapp", this);
		this.setStyleSheet(Styles.DEFAULT_STYLE_SHEET);

		this.mainWindow = new MainWindow();
		this.mainWindow.setTitle("Personal Cloud");
		this.mainWindow.setContent(new MainContentPane());

		this.taskQueueHandle = this.createTaskQueue();

		this.attributes = new HashMap<String, Object> ();

		return this.mainWindow;
	}

	@Override
	public void dispose() {

		super.dispose();

		this.removeTaskQueue(this.taskQueueHandle);

		if (this.isEndpointOpen()) this.closeContext();
	}

	public DesktopServlet getServlet() {

		return this.servlet;
	}

	public MainWindow getMainWindow() {

		return this.mainWindow;
	}

	public TaskQueueHandle getTaskQueueHandle() {

		return this.taskQueueHandle;
	}

	public void setAttribute(String name, Object value) {

		this.attributes.put(name, value);
	}

	public Object getAttribute(String name) {

		return this.attributes.get(name);
	}

	public void openEndpoint(XdiEndpoint endpoint) throws Exception {

		try {

			if (this.openedEndpoint != null) this.closeContext();

			String remoteAddr = WebContainerServlet.getActiveConnection().getRequest().getRemoteAddr();

			this.openedEndpoint = endpoint;

			this.fireApplicationEvent(new ApplicationContextOpenedEvent(this, this.openedEndpoint));

			this.logger.info("Your Personal Cloud has been opened from " + remoteAddr + ".", null);
		} catch (Exception ex) {

			if (this.isEndpointOpen()) this.closeContext();
			throw ex;
		}
	}

	public void closeContext() {

		try {

			if (this.openedEndpoint == null) return;

			this.fireApplicationEvent(new ApplicationContextClosedEvent(this, this.openedEndpoint));
		} catch (Exception ex) {

		} finally {

			this.logger.info("Your Personal Cloud has been closed.", null);

			this.openedEndpoint = null;
		}
	}

	public XdiEndpoint getOpenEndpoint() {

		return this.openedEndpoint;
	}

	public boolean isEndpointOpen() {

		return this.openedEndpoint != null;
	}

	/*
	 * Logger and Xdi
	 */

	private void initLogger() {

		try {

			this.logger = new Logger();
		} catch (Exception ex) {

			throw new RuntimeException("Cannot initialize Logger component: " + ex.getMessage(), ex);
		}
	}

	private void initXdiClient() {

		try {

			this.xdiClient = new XdiClient(this.getServlet().getResolver());
		} catch (Exception ex) {

			throw new RuntimeException("Cannot initialize Xdi component: " + ex.getMessage(), ex);
		}
	}

	public Logger getLogger() {

		if (this.logger == null) this.initLogger();

		return this.logger;
	}

	public XdiClient getXdiClient() {

		if (this.xdiClient == null) this.initXdiClient();

		return this.xdiClient;
	}

	/*
	 * Events
	 */

	public void addApplicationListener(ApplicationListener applicationListener) {

		if (this.applicationListeners.contains(applicationListener)) return;
		this.applicationListeners.add(applicationListener);
	}

	public void removeApplicationListener(ApplicationListener applicationListener) {

		this.applicationListeners.remove(applicationListener);
	}

	public void fireApplicationEvent(ApplicationEvent applicationEvent) {

		for (Iterator<ApplicationListener> applicationListeners = this.applicationListeners.iterator(); applicationListeners.hasNext(); ) {

			ApplicationListener applicationListener = applicationListeners.next();
			applicationListener.onApplicationEvent(applicationEvent);
		}
	}
}
