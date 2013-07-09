package au.gov.amsa.wsf;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.auth10.federation.Claim;
import com.auth10.federation.FederatedPrincipal;

public class ClaimServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(ClaimServlet.class);

	private static final long serialVersionUID = -1267968245396758348L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("handling post");
		process(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("handling get");
		process(req, resp);

	}

	private void process(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// gets the user name
		String name = req.getRemoteUser();

		// gets the user claims
		FederatedPrincipal principal = (FederatedPrincipal) req
				.getUserPrincipal();

		List<Claim> claims;
		if (principal == null)
			claims = new ArrayList<Claim>();
		else
			claims = principal.getClaims();

		setNoCache(resp);

		PrintWriter out = resp.getWriter();
		out.println("hi " + name + " " + claims
				+ (principal == null ? " null principal" : ""));

		Enumeration<String> en = req.getParameterNames();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			out.println(key + "=" + req.getParameter(key));
		}
	}

	private static void setNoCache(HttpServletResponse response) {
		// Set to expire far in the past.
		response.setHeader("Expires", "-1");
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
	}

}
