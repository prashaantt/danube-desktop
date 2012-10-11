package danube.desktop.web.signin.manual;

import nextapp.echo.app.Panel;
import danube.desktop.web.ui.endpoint.SignInMethod;

public class ManualSignInMethod implements SignInMethod {

	@Override
	public String getMethodName() {
		
		return "Manual Sign-In";
	}
	
	@Override
	public Panel newPanel() {

		ManualSignInPanel manualSignInPanel = new ManualSignInPanel();

		return manualSignInPanel;
	}
}
