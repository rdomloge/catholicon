package catholicon.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import catholicon.archive.SeasonArchive;

public class ArchiveFilter extends GenericFilterBean {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArchiveFilter.class);
	
	private SeasonArchive seasonArchive;
	

	public ArchiveFilter(SeasonArchive seasonArchive) {
		this.seasonArchive = seasonArchive;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		CharResponseWrapper wrappedResponse = new CharResponseWrapper(
                (HttpServletResponse)resp);
		
	    try {
	    	chain.doFilter(req, wrappedResponse);
	    	
	    	if(((HttpServletResponse) resp).getStatus() == HttpServletResponse.SC_OK) {
	    		byte[] bytes = wrappedResponse.getByteArray();
	    		seasonArchive.saveAll(new String(bytes)); 	
	    		resp.getOutputStream().write(bytes);		// TODO Do this in another thread
		    	return;
		    }
	    	
	    }
	    catch(ServletException e) {
	    	LOGGER.error("Could not fetch data - using archive", e);
	    }
	    
	    resp.getWriter().write(seasonArchive.getAll());
	    
	}
}
