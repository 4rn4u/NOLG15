# ACTA DE REUNIÓN - PROYECTO NOL_G15

**Fecha:** Lunes, 2 de junio de 2025  
**Lugar:** Reunión telemática por Discord  
**Asistentes:** Arnau Vila, Nacho Pinazo, Álvaro Gómez, Ferran Belloch, Mario Pérez

---

## Orden del día:

- Evaluación del progreso del desarrollo conjunto  
- Identificación de cuellos de botella al trabajar todos a la vez  
- Reorganización del trabajo mediante reparto individual de tareas  
- Establecimiento de fecha límite interna de finalización (viernes 7 de junio)

---

## Desarrollo de la reunión:

Durante esta décima reunión, celebrada en modalidad online por Discord, el grupo detectó que **el desarrollo conjunto estaba generando bloqueos y falta de eficiencia**. Por consenso, se decidió realizar un **reparto de tareas claro**, asignando a cada integrante responsabilidades específicas para avanzar más rápido hacia la entrega final.

El reparto de tareas quedó establecido de la siguiente forma:

- **Arnau**: se encargará de la **configuración completa del login para el rol de profesor**, así como de **reparar y validar el filtro de logs**. 
- **Nacho**: asumirá la tarea de **reconstruir el script de carga de datos** (poblado de CentroEducativo), incluyendo:
  - Asignación de alumnos a asignaturas
  - Inserción de calificaciones válidas (solución de errores 405 y 406)
  - Inclusión de nuevos objetos en la base de datos simulada
  - Además, desarrollará un nuevo **JSP asignaturas** para ver el detalle de las asignaturas que imparte cada profesor
- **Mario, Ferran y Álvaro**: trabajarán conjuntamente en:
  - **Conectar todos los JSPs entre sí** y con los servlets correspondientes
  - Crear el **servlet central para profesores**, capaz de manejar las peticiones REST y actualizar notas
  - **Resolver de forma definitiva los problemas de dependencias** en todas las máquinas del grupo

Asimismo, se estableció como objetivo interno tener **todas las funcionalidades listas el sábado 7 de junio**, de modo que se pueda dedicar el domingo exclusivamente a **revisar errores, limpiar código y preparar la entrega final**.

---

## Acuerdos:

1. Se abandona el trabajo conjunto sin división de tareas por ser poco eficiente en esta etapa.  
2. Se realiza un **reparto individual de responsabilidades**, con entregables claros para cada miembro.  
3. Todo el sistema debe estar **completo y funcional el sábado 7 de junio como fecha límite interna**.  
4. Se utilizarán sesiones de revisión grupal durante el fin de semana para pulir detalles y documentar.  
5. El script de carga y la validación de roles deben estar operativos para el entorno de evaluación del profesorado.

---

## Observaciones adicionales:

- Se recomienda que cada integrante documente brevemente su avance en el repositorio o por Discord.  
- Los errores relacionados con dependencias deberán resolverse de forma definitiva y reflejarse en una guía de instalación del proyecto.  
- Se recordó que el JSP del profesor debe cumplir con los requisitos de uso de AJAX y edición de notas sin recarga de página.

---
