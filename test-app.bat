@echo off
echo ========================================
echo    TEST COMPLET - Quiz Game JavaFX
echo ========================================
echo.

REM Définir les variables d'environnement
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.15.6-hotspot
set MAVEN_HOME=C:\Program Files\apache-maven-3.9.9
set PATH=%MAVEN_HOME%\bin;%JAVA_HOME%\bin;%PATH%

echo Configuration de l'environnement...
echo JAVA_HOME=%JAVA_HOME%
echo MAVEN_HOME=%MAVEN_HOME%
echo.

echo 🔨 Compilation de l'application...
call mvn clean compile

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ Compilation réussie !
    echo.
    echo 🚀 Lancement de l'application JavaFX...
    echo L'application va s'ouvrir dans une nouvelle fenêtre...
    echo.
    call mvn javafx:run
    echo.
    echo ✅ Test terminé - Application fermée.
) else (
    echo.
    echo ❌ Erreur de compilation !
    echo Vérifiez que Maven et Java sont correctement installés.
    pause
)

pause
