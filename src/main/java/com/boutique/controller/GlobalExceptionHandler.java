package com.boutique.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.boutique.exception.ReferencedEntityException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ReferencedEntityException.class, DataIntegrityViolationException.class, IllegalArgumentException.class})
    public void handleReferenceError(Exception ex, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String referer = req.getHeader("referer");
        String msg = ex.getMessage() == null ? "Operación inválida" : ex.getMessage();
        String encoded = URLEncoder.encode(msg, StandardCharsets.UTF_8);
        if (referer != null && !referer.isBlank()) {
            String sep = referer.contains("?") ? "&" : "?";
            resp.sendRedirect(referer + sep + "errorMsg=" + encoded);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        }
    }
}
