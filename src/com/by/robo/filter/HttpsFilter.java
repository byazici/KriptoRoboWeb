package com.by.robo.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.utils.SysUtils;

//@ WebF ilter(urlPatterns= {"/login.jsp"})
public class HttpsFilter implements Filter {
	final static Logger logger = LoggerFactory.getLogger(HttpsFilter.class);

	public HttpsFilter() {
		
	}
	 
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		if (!SysUtils.isDevMode()) {
			 
			String uri = req.getRequestURI();
			String getProtocol = req.getScheme();
			String getDomain = req.getServerName();
			String getPort = Integer.toString(req.getServerPort());
			
			
			logger.error("uri : " + uri);
			logger.error("getProtocol : " + getProtocol);
			logger.error("getDomain : " + getDomain);
			logger.error("getPort : " + getPort);
			
			if (getProtocol.toLowerCase().equals("http")) {
			//if (!req.isSecure()) {
				// Set response content type
				response.setContentType("text/html");
				 
				// New location to be redirected
				String httpsPath = "https" + "://" + getDomain  + ":" + getPort  + uri;
				 
				String site = new String(httpsPath);
				
				logger.debug("site : " + site);

				res.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
				res.setHeader("Location", site);
			}
		}

		// Pass request back down the filter chain
		chain.doFilter(req, res);

	}
	 
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.debug("Https filter initialied.");
	}
	 
	@Override
	public void destroy() {
		logger.debug("Https filter destroyed.");
	}
}
