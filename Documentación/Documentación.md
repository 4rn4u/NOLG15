
#DOCUMENTACION NOLG15 


## Script de Poblado de Datos – Centro Educativo

### Objetivo

Este script automatiza el proceso de carga inicial de datos en la aplicación web Centro Educativo. Permite crear asignaturas, profesores y alumnos, así como matricular alumnos en asignaturas y asignarles calificaciones mediante peticiones a la API REST del sistema.

---

### Requisitos previos

- Servidor Tomcat corriendo la aplicación en `http://localhost:9090/CentroEducativo`
- Usuarios administradores, profesores y alumnos declarados en `tomcat-users.xml`
- Comando `curl` instalado en el sistema
- Herramienta `jq` instalada (para manipulación de JSON desde bash)
- Script con permisos de ejecución (`chmod +x script_poblar.sh`)
- API backend funcional con los endpoints definidos

---

### Descripción general del funcionamiento

#### 1. Login de administrador

El script se autentica con el endpoint `/login` utilizando las credenciales de un usuario administrador. Se obtiene una clave de sesión (`key`) que se utiliza para autenticar todas las peticiones posteriores.

#### 2. Creación de asignaturas

Si no existen previamente, se crean las siguientes asignaturas:

- DEW – Desarrollo Web
- IAP – Integración de Aplicaciones
- DCU – Desarrollo Centrado en el Usuario

#### 3. Creación de profesores

Se insertan profesores con sus DNI, nombre y apellidos. Antes de cada inserción, se comprueba si el profesor ya está registrado mediante una petición `GET`.

#### 4. Creación de alumnos

Se registran diez alumnos, cada uno con su `dni`, nombre, apellidos, contraseña y una lista de asignaturas. Si un alumno ya existe, su creación se omite.

#### 5. Matrícula de alumnos

Cada alumno se matricula en las asignaturas indicadas en su perfil mediante peticiones `POST` al endpoint:

```
/alumnos/{dni}/asignaturas
```

Con el cuerpo de la solicitud en formato JSON:

```json
{"acronimo": "DEW"}
```

#### 6. Asignación de calificaciones

A cada alumno matriculado se le asigna una calificación entera aleatoria (de 0 a 10) por asignatura mediante una petición `PUT` al endpoint:

```
/alumnos/{dni}/asignaturas/{acronimo}
```

El cuerpo de la petición contiene directamente un número entero, sin encapsular en JSON:

```
7
```

---

### Ejecución del script

```bash
chmod +x script_poblar.sh
./script_poblar.sh
```

---

### Endpoints utilizados

| Recurso     | Ruta                                             | Método |
|-------------|--------------------------------------------------|--------|
| Login       | `/login`                                        | POST   |
| Asignaturas | `/asignaturas`                                 | POST   |
| Asignatura  | `/asignaturas/{acronimo}`                     | GET    |
| Profesores  | `/profesores`                                   | POST   |
| Profesor    | `/profesores/{dni}`                            | GET    |
| Alumnos     | `/alumnos`                                      | POST   |
| Alumno      | `/alumnos/{dni}`                               | GET    |
| Matrícula   | `/alumnos/{dni}/asignaturas`                  | POST   |
| Nota        | `/alumnos/{dni}/asignaturas/{acronimo}`      | PUT    |

---

### Usuarios necesarios

Los usuarios deben estar definidos en el archivo `tomcat-users.xml` con los siguientes roles:

- `roladmin` para el usuario administrador
- `rolpro` para los profesores
- `rolalu` para los alumnos

Los `username` deben coincidir con el `dni` de cada usuario y usar la contraseña `123456` (por defecto).

---


### Estado del script

El script ha sido verificado como funcional. Es idóneo para su uso en entornos de desarrollo o pruebas en los que se requiera preparar un conjunto de datos representativo del sistema de gestión educativa.