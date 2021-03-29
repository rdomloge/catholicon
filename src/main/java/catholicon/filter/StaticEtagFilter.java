package catholicon.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class StaticEtagFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StaticEtagFilter.class);
	
	private static final String HEADER_ETAG = "ETag";

	private static final String HEADER_IF_NONE_MATCH = "If-None-Match";

//	private static final String HEADER_CACHE_CONTROL = "Cache-Control";
//
//	private static final String DIRECTIVE_NO_STORE = "no-store";
	
	@Autowired
	private BuildProperties buildProperties;
	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest sreq, ServletResponse sresp, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) sreq;
		HttpServletResponse response = (HttpServletResponse) sresp;
		
		String version = buildProperties.getVersion();
		String requestETag = request.getHeader(HEADER_IF_NONE_MATCH);
		
		if(version.equals(requestETag)) {
			LOGGER.debug("Etag matches");
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		}
		else {
			LOGGER.debug("Etag {} does not match {}", requestETag, version);
			response.setHeader(HEADER_ETAG, version);
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
