package danube.desktop.web.ui.app;

import nextapp.echo.app.ResourceImageReference;
import danube.desktop.web.ui.MainContentPane;
import danube.desktop.xdi.XdiEndpoint;

public interface DesktopWebApp {

	public String getName();
	public ResourceImageReference getResourceImageReference();
	public void onActionPerformed(MainContentPane mainContentPane, XdiEndpoint endpoint);
}
