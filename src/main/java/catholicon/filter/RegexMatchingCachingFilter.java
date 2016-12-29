package catholicon.filter;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.constructs.blocking.LockTimeoutException;
import net.sf.ehcache.constructs.web.AlreadyCommittedException;
import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.filter.FilterNonReentrantException;
import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;

public class RegexMatchingCachingFilter extends SimplePageCachingFilter {

	private static final Logger logger = LoggerFactory.getLogger(RegexMatchingCachingFilter.class);
	
	private Set<Pattern> patterns;
	
	
	@Override
	public void doInit(FilterConfig cfg) throws CacheException {
		super.doInit(cfg);
		patterns = new HashSet<>();
		String patternString = cfg.getInitParameter("patterns");
		String[] parts = patternString.split(",");
		for (String regex : parts) {
			patterns.add(Pattern.compile(regex.trim()));
		}
	}

	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws AlreadyGzippedException, AlreadyCommittedException, FilterNonReentrantException,
			LockTimeoutException, Exception {

		for (Pattern pattern : patterns) {
			if(pattern.matcher(request.getRequestURI()).matches()) {
				
				long start = System.currentTimeMillis();
				
				super.doFilter(request, response, chain);
				
				long taken = System.currentTimeMillis() - start;
				logger.debug(String.format("Matched URI %1$s using %2$s - %3$s ms", 
						request.getRequestURI(), 
						this.getCacheName(),
						taken));
				return;
			}			
		}
		
		chain.doFilter(request, response);
	}
}
