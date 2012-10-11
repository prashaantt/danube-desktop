package danube.desktop.web.servlet.external;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import danube.desktop.web.DesktopApplication;

public interface ExternalCallReceiver {

	public void onExternalCall(DesktopApplication pdsApplication, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
