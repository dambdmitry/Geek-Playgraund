package edu.mitin.playground.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class HttpErrorController implements ErrorController {

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/")
    public String handleError(Locale locale, Model model, HttpServletRequest request, Exception ex) {
        var status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // 403
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "errors/noAccess";
            }

            // 404
            else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "errors/notFound";
            }

            // 405
            else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                return "errors/noAccess";
            }

            // 500
            else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "errors/serverError";
            }
        }

        return "errors/error";
    }
}
