package com.revature.teamManager.web.util.security;


import com.revature.teamManager.util.exceptions.AuthenticationException;
import com.revature.teamManager.util.exceptions.AuthorizationException;
import com.revature.teamManager.web.dtos.Principal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Aspect
@Component
public class SecurityAspect {

    private JwtConfig jwtConfig;

    public SecurityAspect(JwtConfig jwtConfig){
        this.jwtConfig = jwtConfig;
    }

    @Around("@annotation(com.revature.teamManager.web.util.security.Secured)")
    public Object secureEndpoint(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Secured securedAnno = method.getAnnotation(Secured.class);
        List<String> allowedRoles = Arrays.asList(securedAnno.allowedRoles());

        HttpServletRequest req = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();


        Principal principal = parseToken(req).orElseThrow(() -> new AuthenticationException("Request originate from an unauthenticated source."));

        if(!allowedRoles.contains(principal.getRole())){
            throw new AuthorizationException("A forbidden request was made by: "+ principal.getUsername());
        }

        return pjp.proceed();

    }

    public Optional<Principal> parseToken(HttpServletRequest req) {

        try {

            String header = req.getHeader(jwtConfig.getHeader());

            System.out.println("Header value: " + header);

            if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
                //logger.warn("Request originates from an unauthenticated source.");
                return Optional.empty();
            }

            String token = header.replaceAll(jwtConfig.getPrefix(), "");

            Claims jwtClaims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSigningKey())
                    .parseClaimsJws(token)
                    .getBody();

            return Optional.of(new Principal(jwtClaims));

        } catch (Exception e) {
            //logger.error(e.getMessage(), e);
            return Optional.empty();
        }

    }

}
