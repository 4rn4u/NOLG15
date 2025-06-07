<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="com.google.gson.reflect.TypeToken" %>
<%@ page import="java.lang.reflect.Type" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    // Verificar sesión activa y rol de profesor
    if (session == null || session.getAttribute("key") == null || !"profesor".equals(session.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/login.html");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alumnos de la Asignatura - NOL_G15</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="./css/asignaturas.css">
</head>
<body>
    <div class="container mt-4">
        <!-- Header -->
        <div class="header mb-4">
            <div class="d-flex justify-content-between align-items-center">
                <h1 class="text-primary">
                    Alumnos de: 
                    <span class="text-secondary">
                        ${asignaturaNombre} (${asignaturaAcronimo})
                    </span>
                </h1>
                <div>
                    <a href="asignaturasProfesor.jsp" class="btn btn-outline-primary me-2">
                        <i class="bi bi-arrow-left"></i> Volver
                    </a>
                    <a href="logout" class="btn btn-outline-danger">
                        <i class="bi bi-box-arrow-right"></i> Cerrar Sesión
                    </a>
                </div>
            </div>
        </div>

        <!-- Lista de alumnos -->
        <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0">
                    <i class="bi bi-people-fill"></i> Lista de Alumnos
                </h5>
            </div>
            <div class="card-body">
                <%
                    String alumnosJson = (String) request.getAttribute("alumnos");
                    if (alumnosJson != null && !alumnosJson.isEmpty()) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Map<String, String>>>(){}.getType();
                        List<Map<String, String>> alumnos = gson.fromJson(alumnosJson, listType);
                        
                        if (alumnos != null && !alumnos.isEmpty()) {
                %>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>DNI</th>
                                            <th>Nombre Completo</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            for (Map<String, String> alumno : alumnos) {
                                        %>
                                                <tr>
                                                    <td><%= alumno.get("dni") %></td>
                                                    <td><%= alumno.get("nombreCompleto") %></td>
                                                    <td>
                                                        <a href="alumno-detalle?dni=<%= alumno.get("dni") %>&asignatura=${asignaturaAcronimo}"  class="btn btn-sm btn-primary">
                                                            <i class="bi bi-eye"></i> Ver Detalles
                                                        </a>
                                                    </td>
                                                </tr>
                                        <%
                                            }
                                        %>
                                    </tbody>
                                </table>
                            </div>
                <%
                        } else {
                %>
                            <div class="alert alert-warning text-center">
                                <i class="bi bi-exclamation-triangle"></i>
                                <h4>No hay alumnos matriculados</h4>
                                <p>No se encontraron alumnos en esta asignatura.</p>
                            </div>
                <%
                        }
                    } else {
                %>
                        <div class="alert alert-danger text-center">
                            <i class="bi bi-x-circle"></i>
                            <h4>Error al cargar los alumnos</h4>
                            <p>No se pudieron obtener los datos de los alumnos.</p>
                            <a href="listaAlumnosAsignatura?acronimo=${asignaturaAcronimo}" class="btn btn-primary">
                                <i class="bi bi-arrow-clockwise"></i> Reintentar
                            </a>
                        </div>
                <%
                    }
                %>
            </div>
        </div>

        <!-- Footer -->
        <footer class="bg-light text-center py-3 mt-5 border-top">
            <div class="container">
                <p class="mb-0 text-muted">
                    <strong>NOL_G15:</strong> Arnau Vila, Ferran Belloch, Álvaro Gómez, Mario Pérez
                </p>
                <small class="text-muted">Desarrollo de Aplicaciones Web - 2024/2025</small>
            </div>
        </footer>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>