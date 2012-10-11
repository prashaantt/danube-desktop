package danube.desktop.web.ui.app.directxdi;

import nextapp.echo.app.ResourceImageReference;
import danube.desktop.web.ui.MainContentPane;
import danube.desktop.web.ui.app.DesktopWebApp;
import danube.desktop.xdi.XdiEndpoint;

public class DirectXdiDesktopWebApp implements DesktopWebApp {

	@Override
	public String getName() {

		return "Direct XDI";
	}

	@Override
	public ResourceImageReference getResourceImageReference() {

		return new ResourceImageReference("/danube/desktop/web/ui/app/directxdi/app.png");
	}

	@Override
	public void onActionPerformed(MainContentPane mainContentPane, XdiEndpoint endpoint) {

		DirectXdiWindowPane directXdiWindowPane = new DirectXdiWindowPane();
		directXdiWindowPane.setEndpoint(endpoint);

		mainContentPane.add(directXdiWindowPane);
	}
}
