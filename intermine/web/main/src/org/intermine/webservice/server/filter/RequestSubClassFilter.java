package org.intermine.webservice.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.intermine.webservice.server.core.Request;

/**
 * Filter to wrap all web service requests in a custom delegating wrapper that
 * prevents sessions being created.
 * 
 * @author Alex Kalderimis
 */
public class RequestSubClassFilter implements Filter
{
    @Override
    public void destroy() {
        // Nothing to do...
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        // It only makes sense to use this filter on HTTP requests.
        Request wrapped = new Request((HttpServletRequest) request);
        chain.doFilter(wrapped, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // Nothing to do...
    }

}
