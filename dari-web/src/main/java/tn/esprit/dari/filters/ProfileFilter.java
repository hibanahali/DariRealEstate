package tn.esprit.dari.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tn.esprit.dari.managedbeans.AuthenticationBean;

@WebFilter("/user/*")
public class ProfileFilter  implements Filter{
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		System.out.println(" url = "+ ((HttpServletRequest) request).getRequestURL());
	
	
	HttpServletRequest servletRequest = (HttpServletRequest) request;
	HttpServletResponse servletResponse = (HttpServletResponse) response;
	
	AuthenticationBean authenticationBean = (AuthenticationBean) servletRequest.getSession().getAttribute("authenticationBean");
	
	if ((authenticationBean != null && authenticationBean.getLoggedIn() == true) || servletRequest.getRequestURL().toString().contains("sign-in.jsf")) {
		chain.doFilter(servletRequest, servletResponse);
	}
	
	else
	{
		servletResponse.sendRedirect(servletRequest.getContextPath()+"/dari/sign-in.jsf");
	}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	


	

}
