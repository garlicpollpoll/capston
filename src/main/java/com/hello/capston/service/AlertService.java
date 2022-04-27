package com.hello.capston.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class AlertService {

    public static void init(HttpServletResponse response) {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
    }

    public static void alert(HttpServletResponse response, String alertMessage) throws IOException {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + alertMessage + "');</script>");
        out.flush();
    }

    public static void alertAndRedirect(HttpServletResponse response, String alertMessage, String url) throws IOException {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + alertMessage + "');</script>");
        out.println("<script>location.href = '" + url + "'</script>");
        out.flush();
    }
}
