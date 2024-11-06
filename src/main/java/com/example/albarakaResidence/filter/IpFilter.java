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
/*

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

    // Liste des adresses IP autorisées
    private static final List<String> ALLOWED_IPS = Arrays.asList(
            "192.168.11.112", // Ajoutez l'adresse IP de votre machine ici
          //  "192.168.1.101",  // Adresse IP de la deuxième machine autorisée, si nécessaire
            "127.0.0.1"       // Adresse IPv4 de boucle locale pour les tests
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Récupérer l'adresse IP à partir de l'en-tête X-Forwarded-For si elle existe
        String clientIp = httpRequest.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = httpRequest.getRemoteAddr();
        }

        // Convertir l'adresse IPv6 de boucle locale en IPv4
        if ("0:0:0:0:0:0:0:1".equals(clientIp)) {
            clientIp = "127.0.0.1";
        }

        System.out.println("Adresse IP de la requête : " + clientIp); // Ligne de débogage

        if (ALLOWED_IPS.contains(clientIp.trim())) {
            chain.doFilter(request, response); // Continue la chaîne de filtres
        } else {
            response.getWriter().write("Accès refusé : votre adresse IP n'est pas autorisée.");
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            ((jakarta.servlet.http.HttpServletResponse) response).setStatus(403);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation si nécessaire
    }

    @Override
    public void destroy() {
        // Nettoyage si nécessaire
    }
}

*
*
*
* */