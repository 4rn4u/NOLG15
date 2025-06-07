package nol;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logs implements Filter {
    private String logFilePath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logFilePath = filterConfig.getServletContext().getInitParameter("logFilePath");
        System.out.println(">>> Logs: Escribiendo logs en: " + logFilePath);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);

        String user = (session != null && session.getAttribute("dni") != null)
                      ? (String) session.getAttribute("dni")
                      : "anonimo";

        String ip = request.getRemoteAddr();
        String uri = req.getRequestURI();
        String method = req.getMethod();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String logLine = String.format("%s %s %s %s %s%n", timestamp, user, ip, uri, method);

        // Crear el directorio si no existe
        File logFile = new File(logFilePath);
        File parentDir = logFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write(logLine);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}