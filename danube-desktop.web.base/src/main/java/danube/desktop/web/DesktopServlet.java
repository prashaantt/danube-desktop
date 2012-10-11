package danube.desktop.web;


import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;
import nextapp.echo.webcontainer.service.StaticTextService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xdi2.resolution.XDIResolver;
import danube.desktop.web.ui.app.DesktopWebApp;
import danube.desktop.web.ui.endpoint.SignInMethod;

public class DesktopServlet extends WebContainerServlet {

	private static final long serialVersionUID = -7856586634363745175L;

	private static final Logger log = LoggerFactory.getLogger(DesktopServlet.class);

	private List<DesktopWebApp> desktopWebApps;
	private List<SignInMethod> signInMethods;

	private transient XDIResolver resolver;

	@Override
	public ApplicationInstance newApplicationInstance() {

		return new DesktopApplication(this);
	}

	public void init(ServletConfig servletConfig) throws ServletException {

		super.init(servletConfig);

		log.info("Initializing...");

		this.addInitScript(JavaScriptService.forResource("CustomWaitIndicator", "danube/desktop/web/resource/js/CustomWaitIndicator.js"));
		this.addInitStyleSheet(StaticTextService.forResource("desktop.web.css", "text/css", "danube/desktop/web/resource/style/desktop.web.css"));

		this.initResolver();

		log.info("Initializing complete.");
	}

	private void initResolver() {

		try {

			this.resolver = new XDIResolver();
		} catch (Exception ex) {

			throw new RuntimeException("Cannot initialize resolver: " + ex.getMessage(), ex);
		}
	}

	public XDIResolver getResolver() {

		if (this.resolver == null) this.initResolver();

		return this.resolver;
	}

	public List<DesktopWebApp> getDesktopWebApps() {

		return this.desktopWebApps;
	}

	public void setDesktopWebApps(List<DesktopWebApp> desktopWebApps) {

		this.desktopWebApps = desktopWebApps;
	}

	public List<SignInMethod> getSignInMethods() {

		return this.signInMethods;
	}

	public void setSignInMethods(List<SignInMethod> signInMethods) {

		this.signInMethods = signInMethods;
	}
}
