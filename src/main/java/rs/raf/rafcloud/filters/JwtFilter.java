package rs.raf.rafcloud.filters;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rs.raf.rafcloud.services.UserService;
import rs.raf.rafcloud.utils.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public JwtFilter(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = httpServletRequest.getHeader("Authorization");
        String jwt = null;
        String username = null;
        Long userId = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
            userId = jwtUtil.extractUserId(jwt);
        }

        if (jwt == null && httpServletRequest.getParameter("jwt") != null) {
            jwt = httpServletRequest.getParameter("jwt");
            username = jwtUtil.extractUsername(jwt);
            userId = jwtUtil.extractUserId(jwt);
        }

        if (username != null) {

            UserDetails userDetails = this.userService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        httpServletRequest.setAttribute("userId", userId);

//        if(httpServletRequest.getRequestURI().contains("/api/machines/createdByUser/")) {
//            String[] pathVars = httpServletRequest.getRequestURI().split("/");
//            Long userIdFromPath = Long.valueOf(pathVars[pathVars.length - 1]);
//            if(userId != userIdFromPath) throw new RuntimeException("Korisnik ne moze videti masine drugog korinsika");
//
//        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
