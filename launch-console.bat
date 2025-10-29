@echo off

echo.
echo.
echo ========================================
echo    QUIZ GAME - Version Console
echo ========================================
echo.

REM Définir les variables d'environnement
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.15.6-hotspot

echo Configuration de l'environnement...
echo.

echo Compilation de la version console...
"%JAVA_HOME%\bin\javac.exe" -d target\classes src\main\java\quizgame\Main.java src\main\java\quizgame\Question.java src\main\java\quizgame\Joueur.java src\main\java\quizgame\Classement.java src\main\java\quizgame\QuestionCsvLoader.java src\main\java\quizgame\Theme.java src\main\java\quizgame\QuestionUtils.java src\main\java\quizgame\AffichageTxt.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Compilation reussie !
    echo.
    echo Lancement de la version console...
    echo.
    "%JAVA_HOME%\bin\java.exe" -cp target\classes quizgame.Main
    echo.
    echo Fin du jeu !
) else (
    echo.
    echo Erreur de compilation !
    pause
)

pause
