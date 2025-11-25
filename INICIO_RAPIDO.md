# ⚡ Inicio Rápido - Mutant Detector API

**Autor:** Francisco Velasco (Legajo 51141)

---

## 🚀 Despliegue en 3 Pasos

### Opción 1: Script Automatizado (Recomendado)

```powershell
.\desplegar.ps1
```

Este script:
- ✅ Verifica prerequisitos
- ✅ Compila el proyecto
- ✅ Ejecuta tests
- ✅ Genera el JAR
- ✅ Construye la imagen Docker (si está disponible)

### Opción 2: Manual

```powershell
# 1. Compilar y testear
.\gradlew.bat clean build

# 2. Generar JAR
.\gradlew.bat bootJar

# 3. Ejecutar
.\gradlew.bat bootRun
```

### Opción 3: Con Docker

```powershell
# 1. Construir imagen
docker build -t mutant-detector-api:1.0.0 .

# 2. Ejecutar contenedor
docker run -d -p 8080:8080 --name mutant-api mutant-detector-api:1.0.0

# 3. Ver logs
docker logs -f mutant-api
```

---

## ✅ Verificar que Funciona

Abre tu navegador:
- **API Stats**: http://localhost:8080/api/stats
- **H2 Console**: http://localhost:8080/h2-console

---

## 📚 Documentación Completa

Para más detalles, consulta: **[GUIA_DESPLIEGUE.md](GUIA_DESPLIEGUE.md)**

---

## 🆘 Problemas Comunes

| Problema | Solución |
|----------|----------|
| `gradlew.bat no se reconoce` | Asegúrate de estar en el directorio raíz del proyecto |
| `JAVA_HOME is not set` | Instala JDK 17 y configura la variable de entorno |
| `Puerto 8080 en uso` | Cambia el puerto en `application.properties` o detén el proceso |

---

**¡Listo para usar! 🎉**

