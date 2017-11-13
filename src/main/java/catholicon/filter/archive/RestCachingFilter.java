package catholicon.filter.archive;

import java.io.IOException;
import java.nio.charset.Charset;
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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import catholicon.archive.RestArchive;
import catholicon.filter.archive.stream.CharResponseWrapper;
import catholicon.parser.ParserUtil;

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
	    		cache(key, bytes);
	    		resp.getOutputStream().write(bytes);
		    	return;
		    }
	    	
	    }
	    catch(ServletException e) {
	    	LOGGER.error("Could not fetch data ("+fullUrl+") - using archive");
	    }
	    
	    resp.getWriter().write(getCache(key));
	}
	
	private void cache(final String url, final byte[] data) {
		Runnable r = new Runnable(){
			@Override
			public void run() {
				String json = new String(data, Charset.forName("ISO8859_1"));
				if(StringUtils.isEmpty(ParserUtil.trim(json))) {
					LOGGER.debug("Cache value for "+url+" is empty - not updating cache");
					return;
				}
				
				restArchive.save(url, json);
				LOGGER.debug("Updated cache for "+url);
			}};
		exec.execute(r);
	}
	
	private String getCache(String url) {
		String json = restArchive.find(url);
		if(StringUtils.isEmpty(json)) {
			throw new CacheMissException(url);
		}
		else {
			LOGGER.debug("Cache value: "+json);
		}
		
		return json;
	}
}
