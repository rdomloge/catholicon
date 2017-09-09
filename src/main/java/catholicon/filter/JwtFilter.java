package catholicon.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        	response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        	response.getWriter().write("Not authorised");
        	return;
        }

        final String token = authHeader.substring(7); // The part after "Bearer "

        try {
        	JwtParser signingKey = Jwts.parser().setSigningKey("secretkey");
            final Claims claims = signingKey.parseClaimsJws(token).getBody();
            request.setAttribute("claims", claims);
        }
        catch (final SignatureException e) {
            throw new ServletException("Invalid token.");
        }

        chain.doFilter(req, res);
    }

}
