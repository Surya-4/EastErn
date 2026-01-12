package jwtcommon.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
    	String path = request.getRequestURI();

        if (path.startsWith("/users/login") ||
            path.startsWith("/users/register") ||
            path.startsWith("/users/logout")) {

            filterChain.doFilter(request, response);
            return;
        }

    		String token=null;
    		Cookie[] cookies=request.getCookies();
    		if(cookies!=null) {
    			for(Cookie cookie:cookies) {
    				if(cookie.getName().equals("jwt")) {
    					token=cookie.getValue();
    				}
    			}
    		}
    	    if (token == null) {
    	        filterChain.doFilter(request, response);
    	        return;
    	    }
    	    if(!jwtUtil.isValidToken(token)) {
    	    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    	    	return;
    	    }

                String username = jwtUtil.getUserName(token);
                String userId = jwtUtil.getUserId(token);    
                List<SimpleGrantedAuthority> authorities = jwtUtil.getRoles(token).stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                CustomPrincipal principal = new CustomPrincipal(userId, username);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(principal, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
