# 📋 Instrucciones - Pasos Recomendados Completados

## ✅ Paso 1: Ejecutar Tests

**Comando ejecutado:**
```bash
gradlew.bat test
```

**Resultado:** ✅ BUILD SUCCESSFUL
- Todos los tests pasaron correctamente
- Se ejecutaron tests unitarios y de integración
- JaCoCo generó el reporte automáticamente

**Tests ejecutados:**
- `DnaAnalyzerServiceTest` - Tests unitarios del servicio de análisis
- `DnaMetricsServiceTest` - Tests unitarios del servicio de estadísticas
- `MutantControllerTest` - Tests de integración del controlador

---

## ✅ Paso 2: Verificar Cobertura de Código

**Comando ejecutado:**
```bash
gradlew.bat jacocoTestReport
```

**Resultado:** ✅ Reporte generado exitosamente

**Ubicación del reporte:**
```
build/reports/jacoco/test/html/index.html
```

**Para ver el reporte:**
1. Abre el archivo `build/reports/jacoco/test/html/index.html` en tu navegador
2. Verás un resumen con:
   - **Instructions**: Porcentaje de instrucciones ejecutadas
   - **Branches**: Porcentaje de ramas cubiertas
   - **Lines**: Porcentaje de líneas cubiertas
   - **Methods**: Porcentaje de métodos ejecutados
   - **Classes**: Porcentaje de clases testeadas

**Verificación de cobertura mínima:**
- El build fallará si la cobertura es menor al 80%
- Configurado en `build.gradle` con `jacocoTestCoverageVerification`

**Para ver el reporte detallado:**
```bash
# Windows
start build\reports\jacoco\test\html\index.html

# Mac/Linux
open build/reports/jacoco/test/html/index.html
```

---

## ✅ Paso 3: Verificar Swagger/OpenAPI

**Configuración:**
- ✅ Dependencia agregada: `springdoc-openapi-starter-webmvc-ui:2.3.0`
- ✅ Clase de configuración creada: `SwaggerConfig.java`
- ✅ Propiedades agregadas en `application.properties`

**Para verificar que funciona:**

1. **Inicia la aplicación:**
   ```bash
   gradlew.bat bootRun
   ```

2. **Abre en tu navegador:**
   - **Swagger UI**: http://localhost:8080/swagger-ui.html
   - **API Docs (JSON)**: http://localhost:8080/api-docs

3. **Deberías ver:**
   - Interfaz de Swagger con todos los endpoints
   - Documentación de `POST /mutant/`
   - Documentación de `GET /stats`
   - Posibilidad de probar los endpoints directamente desde el navegador

**Endpoints disponibles en Swagger:**
- `POST /mutant/` - Verificar si un ADN es mutante
- `GET /stats` - Obtener estadísticas

---

## 📄 Paso 4: Convertir Diagrama de Secuencia a PDF

**Archivo creado:** `DIAGRAMA_SECUENCIA.md`

### Opción 1: Usando Pandoc (Recomendado)

**Instalación de Pandoc:**
- Windows: Descargar desde https://pandoc.org/installing.html
- O usar Chocolatey: `choco install pandoc`

**Comando:**
```bash
pandoc DIAGRAMA_SECUENCIA.md -o DIAGRAMA_SECUENCIA.pdf --pdf-engine=wkhtmltopdf
```

**O con LaTeX:**
```bash
pandoc DIAGRAMA_SECUENCIA.md -o DIAGRAMA_SECUENCIA.pdf
```

### Opción 2: Usando herramientas online

1. **Markdown to PDF:**
   - https://www.markdowntopdf.com/
   - Sube el archivo `DIAGRAMA_SECUENCIA.md`
   - Descarga el PDF generado

2. **Dillinger:**
   - https://dillinger.io/
   - Abre el archivo y exporta a PDF

### Opción 3: Usando VS Code

1. Instala la extensión "Markdown PDF"
2. Abre `DIAGRAMA_SECUENCIA.md`
3. Click derecho → "Markdown PDF: Export (pdf)"

### Opción 4: Usando herramientas de diagramas

Si prefieres crear un diagrama visual:

1. **PlantUML** (recomendado para diagramas de secuencia):
   ```plantuml
   @startuml
   Client -> Controller: POST /mutant/ {dna:[]}
   Controller -> DnaAnalyzerService: inspectGenome(dna)
   DnaAnalyzerService -> MutantRegistry: findByGenomeHash(hash)
   MutantRegistry -> H2: SELECT * FROM...
   H2 --> MutantRegistry: Optional<Entity>
   MutantRegistry --> DnaAnalyzerService: Optional.empty()
   DnaAnalyzerService -> DnaAnalyzerService: detectMutations()
   DnaAnalyzerService -> MutantRegistry: save(entity)
   MutantRegistry -> H2: INSERT INTO...
   H2 --> MutantRegistry: Entity saved
   MutantRegistry --> DnaAnalyzerService: true
   DnaAnalyzerService --> Controller: true
   Controller --> Client: 200 OK
   @enduml
   ```

2. **Draw.io / diagrams.net:**
   - Abre https://app.diagrams.net/
   - Crea un nuevo diagrama de secuencia
   - Usa el contenido de `DIAGRAMA_SECUENCIA.md` como referencia

---

## 🌐 Paso 5: Actualizar URL de Render

**Estado actual:** URL placeholder agregada en README.md

**Cuando despliegues en Render:**

1. **Crea una cuenta en Render:**
   - Ve a https://render.com/
   - Regístrate con GitHub

2. **Crea un nuevo Web Service:**
   - Conecta tu repositorio de GitHub
   - Selecciona el proyecto
   - Render detectará automáticamente que es un proyecto Spring Boot

3. **Configuración recomendada:**
   - **Build Command:** `./gradlew bootJar`
   - **Start Command:** `java -jar build/libs/APIDeployExample-1.0-SNAPSHOT.jar`
   - **Environment:** Java 17

4. **Obtén la URL:**
   - Render te dará una URL como: `https://tu-app.onrender.com`
   - Actualiza la URL en `README.md` línea 16

5. **Verifica que funciona:**
   ```bash
   curl -X POST https://tu-app.onrender.com/mutant/ \
     -H "Content-Type: application/json" \
     -d '{"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}'
   ```

---

## 📊 Resumen de Verificaciones

| Paso | Estado | Detalles |
|------|--------|----------|
| 1. Tests | ✅ Completado | Todos los tests pasaron |
| 2. Cobertura | ✅ Completado | Reporte generado en `build/reports/jacoco/test/html/` |
| 3. Swagger | ✅ Configurado | Listo para usar cuando inicies la app |
| 4. Diagrama PDF | 📝 Instrucciones | Ver sección anterior |
| 5. URL Render | 📝 Pendiente | Actualizar cuando despliegues |

---

## 🚀 Próximos Pasos

1. **Ejecutar la aplicación localmente:**
   ```bash
   gradlew.bat bootRun
   ```

2. **Probar Swagger:**
   - Abre http://localhost:8080/swagger-ui.html
   - Prueba los endpoints

3. **Convertir diagrama a PDF:**
   - Usa una de las opciones mencionadas arriba

4. **Desplegar en Render:**
   - Sigue las instrucciones del Paso 5

5. **Verificar cobertura:**
   - Abre `build/reports/jacoco/test/html/index.html`
   - Verifica que esté por encima del 80%

---

## ✅ Checklist Final

- [x] Tests ejecutados y pasando
- [x] Reporte de cobertura generado
- [x] Swagger configurado
- [ ] Diagrama convertido a PDF
- [ ] Aplicación desplegada en Render
- [ ] URL actualizada en README

---

**Nota:** Todos los pasos críticos están completos. Solo falta desplegar en Render y convertir el diagrama a PDF cuando estés listo.

