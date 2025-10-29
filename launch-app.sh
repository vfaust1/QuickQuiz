#!/bin/bash

echo "========================================"
echo "   QUIZ GAME - Application JavaFX"
echo "========================================"
echo

# Définir les variables d'environnement
export JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64"
export MAVEN_HOME="/opt/apache-maven-3.9.9"
export PATH="$MAVEN_HOME/bin:$PATH"

echo "Configuration de l'environnement..."
echo

echo "Lancement de l'application..."
echo "Une fenêtre JavaFX va s'ouvrir..."
echo

mvn javafx:run

echo
read -p "Appuyez sur Entrée pour fermer l'application..."
