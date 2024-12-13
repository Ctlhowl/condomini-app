@echo off
set "SPRING_BOOT_DIR=C:\Users\Fabrizio Ciotola\Desktop\GIT\condomini-app\backend\target"
set "ANGULAR_DIR=C:\Users\Fabrizio Ciotola\Desktop\GIT\condomini-app\frontend"

echo Avvio dell'app Spring Boot...
cd "%SPRING_BOOT_DIR%"
if errorlevel 1 (
    echo Directory Spring Boot non trovata: %SPRING_BOOT_DIR%
    exit /b 1
)
start cmd /k "java -jar condomini-0.0.1-SNAPSHOT.jar"
timeout /t 1 >nul

echo Avvio dell'app Angular...
cd "%ANGULAR_DIR%"
if errorlevel 1 (
    echo Directory Angular non trovata: %ANGULAR_DIR%
    exit /b 1
)
call npm install
start cmd /k "ng serve --open"
