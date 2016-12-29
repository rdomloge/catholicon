package catholicon.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import catholicon.dao.Loader;

public class ThreadLocalLoaderFilter implements Filter {
	
	private static final String LOADER_KEY = "LOADER";
	
	private static ThreadLocal<Loader> threadLocal = new ThreadLocal<>();
	
	

	@Override
	public void doFilter(ServletRequest sreq, ServletResponse sresp, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) sreq;
		
		HttpSession session = req.getSession();

        if(null == session.getAttribute(LOADER_KEY)) {
            session.setAttribute(LOADER_KEY, new Loader());
        }

        threadLocal.set((Loader) session.getAttribute(LOADER_KEY));
        
        chain.doFilter(sreq, sresp);
        
        threadLocal.remove();
	}
	
	public static Loader getLoader() {
		return threadLocal.get();
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	
	public ThreadLocalLoaderFilter() {
	}

	@Override
	public void destroy() {
	}
}