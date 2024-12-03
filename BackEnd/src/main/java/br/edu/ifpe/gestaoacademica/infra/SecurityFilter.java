package br.edu.ifpe.gestaoacademica.infra;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.edu.ifpe.gestaoacademica.repository.UtilizadorRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{

	private TokenService tokenService;
	
	private UtilizadorRepository utilizadorRepository;
	
	public SecurityFilter(TokenService tokenService,UtilizadorRepository utilizadorRepository) {
		this.tokenService = tokenService;
		this.utilizadorRepository = utilizadorRepository;
	}
	
	
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = getToken(request);
        if(token != null){
            String username = tokenService.validateToken(token);

            if(username != null) {
                UserDetails user = utilizadorRepository.findByLogin(username);

                var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request,response);
    }
	
	public String getToken(HttpServletRequest request){
        String auth = request.getHeader("Authorization");
        if (auth != null)
            auth = auth.replace("Bearer ","");

        return auth;
    }
}
