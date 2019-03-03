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

@WebFilter(urlPatterns= {"/*"})
public class LogFilter implements Filter{
	public LogFilter() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		try {
			String remoteAddr = req.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				  remoteAddr = request.getRemoteAddr();
			}
			
			// TODO try catch+save to db
			if (remoteAddr != null && req.getRequestURL().indexOf("tickPrice.jsp") == -1) {	// /tickprice sürekli çağırılıyor. Loglama.
				  System.out.println(req.getMethod() + " " + remoteAddr +  " " +  req.getRequestURL().substring(req.getRequestURL().indexOf(req.getContextPath()) + req.getContextPath().length()) + " " + req.getQueryString());
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		}

		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
		// 		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// 
	}	
}
