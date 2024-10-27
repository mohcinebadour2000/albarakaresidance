package com.example.albarakaResidence.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class IpFilter implements Filter {

    // Liste des IPs strictement autorisées
    private static final List<String> ALLOWED_IPS = Arrays.asList(
            "192.168.11.112",  // Votre IP IPv4 locale
            "127.0.0.1",       // Localhost IPv4
            "::1",             // Localhost IPv6
           "0:0:0:0:0:0:0:1", // Représentation IPv6 de localhost
            "fe80::7262:47de:c1a6:24f0%14"  // Votre IP IPv6 link-local
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Obtenez l'adresse IP du client
        String remoteIp = httpRequest.getRemoteAddr();
        System.out.println("Detected IP: " + remoteIp); // Affiche l'IP détectée

        // Vérifiez si l'IP est dans la liste des IP autorisées
        boolean authorized = ALLOWED_IPS.contains(remoteIp);

        if (authorized) {
            chain.doFilter(request, response); // Autoriser l'accès
        } else {
            response.getWriter().write("Access Denied: Unauthorized IP"); // Refuser l'accès
            response.getWriter().flush();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void destroy() { }
}
