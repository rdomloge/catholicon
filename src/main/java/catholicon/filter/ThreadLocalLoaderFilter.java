package catholicon.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Value;

import catholicon.dao.Loader;

public class ThreadLocalLoaderFilter implements Filter {
	
	private static ThreadLocal<Loader> threadLocal = new ThreadLocal<>();
	
	@Value("${BASE_URL:http://192.168.0.14}")
	private String BASE;
	
	

	@Override
	public void doFilter(ServletRequest sreq, ServletResponse sresp, FilterChain chain)
			throws IOException, ServletException {
		

        threadLocal.set(new Loader(BASE));
        
        chain.doFilter(sreq, sresp);
        
        threadLocal.remove();
	}
	
	public static void set(Loader loader) {
		threadLocal.set(loader);
	}
	
	public static Loader getLoader() {
		if(null == threadLocal) throw new IllegalStateException("Bork");
		if(null == threadLocal.get()) throw new IllegalStateException("No loader was set");
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