package catholicon.filter.archive;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import catholicon.archive.RestArchive;
import catholicon.filter.archive.stream.CharResponseWrapper;

public class RestCachingFilter extends GenericFilterBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestCachingFilter.class);
	
	private RestArchive restArchive;
	
	private ScheduledExecutorService exec;
	

	public RestCachingFilter(RestArchive restArchive) {
		super();
		this.restArchive = restArchive;
	}

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		exec = Executors.newSingleThreadScheduledExecutor();
	}

	@Override
	public void destroy() {
		super.destroy();
		exec.shutdown();
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		CharResponseWrapper wrappedResponse = new CharResponseWrapper((HttpServletResponse)resp);
		HttpServletRequest hReq = (HttpServletRequest) req;
		String fullUrl = hReq.getRequestURL().toString();
		String key = fullUrl.substring(fullUrl.indexOf(hReq.getContextPath()));
	    try {
	    	chain.doFilter(req, wrappedResponse);
	    	
	    	if(((HttpServletResponse) resp).getStatus() == HttpServletResponse.SC_OK) {
	    		byte[] bytes = wrappedResponse.getByteArray();
	    		cache(key, bytes);	// TODO Do this in another thread
	    		resp.getOutputStream().write(bytes);
		    	return;
		    }
	    	
	    }
	    catch(ServletException e) {
	    	LOGGER.error("Could not fetch data ("+fullUrl+") - using archive", e);
	    }
	    
	    resp.getWriter().write(getCache(key));
	}
	
	private void cache(final String url, final byte[] data) {
		Runnable r = new Runnable(){
			@Override
			public void run() {
				restArchive.save(url, new String(data));
				LOGGER.debug("Updated cache for "+url);
			}};
		exec.execute(r);
	}
	
	private String getCache(String url) {
		String json = restArchive.find(url);
		if(null == json) {
			throw new CacheMissException(url);
		}
		
		return json;
	}
}
