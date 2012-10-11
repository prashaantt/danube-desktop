package danube.desktop.web.signin.xri;

import nextapp.echo.app.Panel;
import danube.desktop.web.ui.endpoint.SignInMethod;

public class XriSignInMethod implements SignInMethod {

	@Override
	public String getMethodName() {

		return "I-Name Sign-In";
	}

	@Override
	public Panel newPanel() {

		XriSignInPanel manualSignInPanel = new XriSignInPanel();

		return manualSignInPanel;
	}
}
