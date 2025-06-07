package nol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@WebServlet("/listaAlumnosAsignatura")
public class ListaAlumnosAsignaturaServlet extends HttpServlet {
    private Client client;
    private Gson gson;
    private static final String API_BASE_URL = "http://localhost:9090/CentroEducativo";

    @Override
    public void init() {
        client = ClientBuilder.newClient();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. Validar sesión y token (solo profesores)
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("key") == null
                || !"profesor".equals(session.getAttribute("role"))) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        String key = (String) session.getAttribute("key");
        String acronimo = req.getParameter("acronimo");

        if (acronimo == null || acronimo.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/asignaturasProfesor.jsp");
            return;
        }

        try {
            // 2. Obtener detalles de la asignatura
            WebTarget asignaturaTarget = client.target(API_BASE_URL)
                    .path("/asignaturas/" + acronimo);
            Invocation.Builder asignaturaInvocation = asignaturaTarget.request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + key);
            Response asignaturaResponse = asignaturaInvocation.get();

            if (asignaturaResponse.getStatus() != 200) {
                asignaturaResponse.close();
                throw new ServletException(
                        "Error al obtener detalles de la asignatura (código " + asignaturaResponse.getStatus() + ")");
            }

            JsonObject asignaturaData = gson.fromJson(
                    asignaturaResponse.readEntity(String.class),
                    JsonObject.class);
            asignaturaResponse.close();

            // 3. Obtener lista de alumnos de la asignatura
            WebTarget alumnosTarget = client.target(API_BASE_URL)
                    .path("/asignaturas/" + acronimo + "/alumnos");
            Invocation.Builder alumnosInvocation = alumnosTarget.request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + key);
            Response alumnosResponse = alumnosInvocation.get();

            if (alumnosResponse.getStatus() != 200) {
                alumnosResponse.close();
                throw new ServletException(
                        "Error al obtener alumnos de la asignatura (código " + alumnosResponse.getStatus() + ")");
            }

            JsonArray alumnosArray = gson.fromJson(
                    alumnosResponse.readEntity(String.class),
                    JsonArray.class);
            alumnosResponse.close();

            // 4. Preparar datos para la JSP
            List<Map<String, String>> alumnosList = new ArrayList<>();
            for (JsonElement alumnoElement : alumnosArray) {
                JsonObject alumno = alumnoElement.getAsJsonObject();
                Map<String, String> alumnoMap = new HashMap<>();
                alumnoMap.put("dni", alumno.get("dni").getAsString());
                alumnoMap.put("nombreCompleto",
                        alumno.get("nombre").getAsString() + " " + alumno.get("apellidos").getAsString());
                alumnosList.add(alumnoMap);
            }

            req.setAttribute("asignaturaNombre", asignaturaData.get("nombre").getAsString());
            req.setAttribute("asignaturaAcronimo", acronimo);
            req.setAttribute("alumnos", gson.toJson(alumnosList));

            // 5. Forward a la JSP
            req.getRequestDispatcher("/listaAlumnosAsignatura.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al procesar la solicitud: " + e.getMessage());
        }
    }

    @Override
    public void destroy() {
        if (client != null) {
            client.close();
        }
    }
}