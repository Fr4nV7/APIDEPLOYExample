# Script de Despliegue Rápido - Mutant Detector API
# Autor: Francisco Velasco (Legajo 51141)

Write-Host "🚀 Iniciando despliegue de Mutant Detector API..." -ForegroundColor Cyan
Write-Host ""

# Verificar prerequisitos
Write-Host "📋 Verificando prerequisitos..." -ForegroundColor Yellow

# Verificar Java
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "✅ Java encontrado: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Java no encontrado. Por favor instala JDK 17+" -ForegroundColor Red
    exit 1
}

# Verificar Docker
try {
    $dockerVersion = docker --version
    Write-Host "✅ Docker encontrado: $dockerVersion" -ForegroundColor Green
} catch {
    Write-Host "⚠️  Docker no encontrado. El despliegue local funcionará, pero Docker no estará disponible." -ForegroundColor Yellow
}

Write-Host ""

# Paso 1: Limpiar y compilar
Write-Host "🔨 Paso 1: Compilando proyecto..." -ForegroundColor Yellow
.\gradlew.bat clean build
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Error en la compilación. Revisa los errores arriba." -ForegroundColor Red
    exit 1
}
Write-Host "✅ Compilación exitosa" -ForegroundColor Green
Write-Host ""

# Paso 2: Ejecutar tests
Write-Host "🧪 Paso 2: Ejecutando tests..." -ForegroundColor Yellow
.\gradlew.bat test
if ($LASTEXITCODE -ne 0) {
    Write-Host "⚠️  Algunos tests fallaron. Revisa los reportes en build/reports/tests/" -ForegroundColor Yellow
} else {
    Write-Host "✅ Todos los tests pasaron" -ForegroundColor Green
}
Write-Host ""

# Paso 3: Generar JAR
Write-Host "📦 Paso 3: Generando JAR ejecutable..." -ForegroundColor Yellow
.\gradlew.bat bootJar
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Error generando el JAR." -ForegroundColor Red
    exit 1
}
Write-Host "✅ JAR generado en build/libs/APIDeployExample-1.0-SNAPSHOT.jar" -ForegroundColor Green
Write-Host ""

# Paso 4: Construir imagen Docker (si Docker está disponible)
if (Get-Command docker -ErrorAction SilentlyContinue) {
    Write-Host "🐳 Paso 4: Construyendo imagen Docker..." -ForegroundColor Yellow
    docker build -t mutant-detector-api:1.0.0 .
    if ($LASTEXITCODE -ne 0) {
        Write-Host "⚠️  Error construyendo la imagen Docker." -ForegroundColor Yellow
    } else {
        Write-Host "✅ Imagen Docker construida: mutant-detector-api:1.0.0" -ForegroundColor Green
        Write-Host ""
        Write-Host "💡 Para ejecutar el contenedor, usa:" -ForegroundColor Cyan
        Write-Host "   docker run -d -p 8080:8080 --name mutant-api mutant-detector-api:1.0.0" -ForegroundColor White
    }
} else {
    Write-Host "⏭️  Paso 4: Docker no disponible, saltando construcción de imagen." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "🎉 ¡Despliegue completado!" -ForegroundColor Green
Write-Host ""
Write-Host "📝 Próximos pasos:" -ForegroundColor Cyan
Write-Host "   1. Ejecutar localmente: .\gradlew.bat bootRun" -ForegroundColor White
Write-Host "   2. O ejecutar el JAR: java -jar build\libs\APIDeployExample-1.0-SNAPSHOT.jar" -ForegroundColor White
Write-Host "   3. Probar la API: http://localhost:8080/api/stats" -ForegroundColor White
Write-Host ""





