package br.com.fiap.pedidos.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Gera um requestId para cada requisição e coloca no MDC,
 * assim todos os logs daquela requisição saem com o mesmo id.
 */
@Component
public class RequestIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestIdFilter.class);
    public static final String HEADER = "X-Request-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestId = request.getHeader(HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        MDC.put("requestId", requestId);
        response.setHeader(HEADER, requestId);
        long inicio = System.currentTimeMillis();

        try {
            MDC.put("operacao", "HTTP_REQUEST");
            log.info("Requisição recebida: {} {}", request.getMethod(), request.getRequestURI());

            chain.doFilter(request, response);
        } finally {
            long tempo = System.currentTimeMillis() - inicio;
            MDC.put("operacao", "HTTP_RESPONSE");
            log.info("Requisição finalizada: {} {} - status {} ({} ms)",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), tempo);
            MDC.clear();
        }
    }
}
