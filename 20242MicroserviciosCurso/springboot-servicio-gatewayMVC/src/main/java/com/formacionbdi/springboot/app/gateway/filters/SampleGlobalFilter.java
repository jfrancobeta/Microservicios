package com.formacionbdi.springboot.app.gateway.filters;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@Component
public class SampleGlobalFilter  implements Filter, Ordered{

    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
            throws IOException, ServletException {
        // Convertir ServletRequest a HttpServletRequest para obtener información específica
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Registrar la información de la solicitud
        System.out.println("Método: " + httpRequest.getMethod());
        System.out.println("URI: " + httpRequest.getRequestURI());

        // Verificar si el método de la solicitud es GET
        if ("GET".equalsIgnoreCase(httpRequest.getMethod())) {
            // Responder con un mensaje específico si es una solicitud GET
            httpResponse.getWriter().write("Esta es una respuesta para las solicitudes GET.");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return; // Terminar aquí, no llamar a chain.doFilter()
        }

        // Continuar con la cadena de filtros o la solicitud
        filter.doFilter(request, response);
    }
    
}
