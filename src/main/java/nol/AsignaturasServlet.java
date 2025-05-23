package nol;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/asignaturas")
public class AsignaturasServlet extends HttpServlet {
    private Client client;
    
    @Override
    public void init() {
        client = ClientBuilder.newClient();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("key") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }
        
        String key = (String) session.getAttribute("key");
        String dni = (String) session.getAttribute("dni");
        
        try {
            Response response = client.target("http://localhost:9090/CentroEducativo/alumnosyasignaturas")
                                    .queryParam("key", key)
                                    .request(MediaType.APPLICATION_JSON)
                                    .get();
            
            if (response.getStatus() == 200) {
                String jsonResponse = response.readEntity(String.class);
                
                List<Map<String, Object>> asignaturasAlumno = procesarAsignaturasAlumno(jsonResponse, dni);
                String nombreAlumno = obtenerNombreAlumno(jsonResponse, dni);
                
                req.setAttribute("asignaturasData", asignaturasAlumno);
                req.setAttribute("nombreAlumno", nombreAlumno);
                req.setAttribute("dniAlumno", dni);
                
                req.getRequestDispatcher("asignaturas.jsp").forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error al obtener las asignaturas");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servidor");
        }
    }
    
    private List<Map<String, Object>> procesarAsignaturasAlumno(String jsonResponse, String dniAlumno) {
        List<Map<String, Object>> asignaturasAlumno = new ArrayList<>();
        
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonObject root = reader.readObject();
            JsonArray alumnos = root.getJsonArray("alumnos");
            JsonArray asignaturas = root.getJsonArray("asignaturas");
            
            // Buscar el alumno por DNI
            for (JsonObject alumno : alumnos.getValuesAs(JsonObject.class)) {
                if (dniAlumno.equals(alumno.getString("dni"))) {
                    JsonArray asignaturasArray = alumno.getJsonArray("asignaturas");
                    
                    // Para cada asignatura del alumno
                    for (String acronimoAsignatura : asignaturasArray.getValuesAs(String.class)) {
                        // Buscar detalles de la asignatura
                        for (JsonObject asigDetalle : asignaturas.getValuesAs(JsonObject.class)) {
                            if (acronimoAsignatura.equals(asigDetalle.getString("acronimo"))) {
                                Map<String, Object> asignatura = new HashMap<>();
                                asignatura.put("codigo", asigDetalle.getString("acronimo"));
                                asignatura.put("nombre", asigDetalle.getString("nombre"));
                                asignatura.put("curso", asigDetalle.getInt("curso"));
                                asignatura.put("cuatrimestre", asigDetalle.getString("cuatrimestre"));
                                asignatura.put("creditos", asigDetalle.getJsonNumber("creditos").doubleValue());
                                asignatura.put("grupoNombre", "Grupo " + generarGrupo(acronimoAsignatura));
                                asignatura.put("miembros", generarMiembros(acronimoAsignatura));
                                
                                asignaturasAlumno.add(asignatura);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return asignaturasAlumno;
    }
    
    private String obtenerNombreAlumno(String jsonResponse, String dniAlumno) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonObject root = reader.readObject();
            JsonArray alumnos = root.getJsonArray("alumnos");
            
            for (JsonObject alumno : alumnos.getValuesAs(JsonObject.class)) {
                if (dniAlumno.equals(alumno.getString("dni"))) {
                    return alumno.getString("nombre") + " " + alumno.getString("apellidos");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Usuario";
    }
    
    private String generarGrupo(String acronimo) {
        switch (acronimo) {
            case "DEW": return "A";
            case "DCU": return "B";
            case "IAP": return "C";
            default: return "A";
        }
    }
    
    private List<String> generarMiembros(String acronimo) {
        List<String> miembros = new ArrayList<>();
        switch (acronimo) {
            case "DEW":
                miembros.add("Ana Martínez López");
                miembros.add("Carlos Rodríguez García");
                miembros.add("María Sánchez Pérez");
                miembros.add("Juan Pérez Martín");
                break;
            case "DCU":
                miembros.add("Pedro González Ruiz");
                miembros.add("Laura Fernández Torres");
                miembros.add("Diego Martín Castro");
                miembros.add("Carmen López Vega");
                break;
            case "IAP":
                miembros.add("Isabel Ruiz Díaz");
                miembros.add("Roberto Díaz Moreno");
                miembros.add("Elena Castro Jiménez");
                miembros.add("Miguel Ángel Vega Ruiz");
                break;
            default:
                miembros.add("Estudiante 1");
                miembros.add("Estudiante 2");
                miembros.add("Estudiante 3");
                break;
        }
        return miembros;
    }
    
    @Override
    public void destroy() {
        if (client != null) {
            client.close();
        }
    }
}
