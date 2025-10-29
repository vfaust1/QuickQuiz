@echo off
echo ========================================
echo    QUIZ GAME - Application JavaFX
echo ========================================
echo.

REM Définir les variables d'environnement
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.15.6-hotspot
set MAVEN_HOME=C:\Program Files\apache-maven-3.9.9
set PATH=%MAVEN_HOME%\bin;%PATH%

echo Configuration de l'environnement...
echo.

echo Lancement de l'application...
echo Une fenêtre JavaFX va s'ouvrir...
echo.

mvn javafx:run

echo.
echo Application fermée.
pause
