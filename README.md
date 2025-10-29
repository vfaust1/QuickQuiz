# QuickQuiz - Application JavaFX

## 🎮 Description

Application de quiz interactive développée en Java avec JavaFX. Le jeu propose plusieurs thèmes de questions avec deux modes de jeu, un système de classement avancé et une interface graphique moderne avec design immersif.

## ✨ Fonctionnalités

### 🎯 Modes de jeu
- **Mode Libre** : Choisissez le nombre de questions (de 1 à 50) et prenez votre temps pour y répondre
- **Mode Chrono** : 60 secondes pour répondre au maximum de questions
  - +2 secondes par bonne réponse
  - -2 secondes par mauvaise réponse

### 🎨 Thèmes disponibles
- Cinéma
- Football
- Jeux Vidéo
- Manga
- Musique
- Animation
- Series TV
- Bande Dessinée
- Aléatoire (toutes catégories mélangées)

### 🏆 Système de classement
- **Podium visuel** avec top 3 (médailles or/argent/bronze)
- **Classements séparés** : Mode Libre et Mode Chrono
- **Filtres** : Par mode de jeu et par thème
- **Affichage des 10 meilleurs scores**

### 🎨 Interface moderne
- Design avec fond d'écran personnalisé
- Boutons thématiques avec couleurs distinctives
- Animations et effets visuels (ombres, dégradés)
- Titres dynamiques selon la performance
- Effet "WOW" multicolore pour les scores parfaits
- Interface responsive et adaptative

## 🚀 Installation et utilisation

### Prérequis
- Java 17 ou supérieur
- Maven 3.6 ou supérieur

### Compilation et lancement
```bash
# Compilation
mvn clean compile

# Lancement
mvn javafx:run

# Ou utilisez les scripts fournis
.\launch-app.bat    # Lancement rapide
.\test-app.bat      # Test complet
```

## 📁 Structure du projet

```
Perso/
├── pom.xml                     # Configuration Maven
├── src/
│   └── main/
│       ├── java/
│       │   └── quizgame/
│       │       ├── QuizApplication.java          # Application principale
│       │       ├── controllers/                  # Contrôleurs JavaFX
│       │       │   ├── WelcomeController.java    # Page d'accueil
│       │       │   ├── ProfileSelectionController.java  # Sélection profil
│       │       │   ├── MenuController.java       # Menu principal
│       │       │   ├── ModeSelectionController.java     # Choix du mode
│       │       │   ├── ThemeController.java      # Choix du thème
│       │       │   ├── NbQuestionsController.java       # Choix nb questions
│       │       │   ├── JeuController.java        # Partie en cours
│       │       │   ├── ResultatsController.java  # Résultats
│       │       │   └── ClassementController.java # Classements
│       │       ├── Question.java                 # Modèle Question
│       │       ├── Joueur.java                   # Modèle Joueur
│       │       ├── Classement.java               # Gestion du classement
│       │       ├── QuestionCsvLoader.java        # Chargement des questions
│       │       ├── Theme.java                    # Énumération des thèmes
│       │       ├── AffichageTxt.java             # Affichage texte
│       │       └── Main.java                     # Version console
│       └── resources/
│           ├── fxml/                             # Interfaces FXML
│           │   ├── WelcomeController.fxml
│           │   ├── ProfileSelection.fxml
│           │   ├── MenuController.fxml
│           │   ├── ModeSelectionController.fxml
│           │   ├── ThemeController.fxml
│           │   ├── NbQuestionsController.fxml
│           │   ├── JeuController.fxml
│           │   ├── ResultatsController.fxml
│           │   └── ClassementController.fxml
│           ├── css/
│           │   └── style.css                     # Styles CSS
│           └── img/                              # Images et ressources
│               ├── fond.jpg                      # Fond d'écran
│               ├── podium.png                    # Image podium
│               ├── Classement.png                # Titre classement
│               ├── ChoixProfil.png               # Titre choix profil
│               └── [themes].png                  # Bannières thématiques
├── csv/                                          # Questions par thème
│   ├── cinema.csv
│   ├── football.csv
│   ├── jeuxvideo.csv
│   ├── manga.csv
│   ├── musique.csv
│   └── litterature.csv
├── txt/                                          # Données et classements
│   ├── classement_libre.txt                     # Scores mode libre
│   ├── classement_chrono.txt                    # Scores mode chrono
│   ├── profils.txt                              # Liste des profils
│   ├── menu.txt
│   ├── theme.txt
│   └── auRevoir.txt
├── launch-app.bat                            # Script de lancement rapide
├── test-app.bat                              # Script de test complet
└── README.md                                 # Ce fichier
```

## 🎯 Utilisation

1. **Lancez l'application** avec `.\launch-app.bat` (rapide) ou `.\test-app.bat` (complet)
2. **Créez ou sélectionnez votre profil** sur l'écran d'accueil
3. **Choisissez votre mode de jeu** :
   - Mode Libre : Partie classique avec choix du nombre de questions
   - Mode Chrono : Course contre la montre de 60 secondes
4. **Sélectionnez un thème** parmi les 7 disponibles
5. **Répondez aux questions** :
   - Cliquez sur la réponse de votre choix
   - Double-clic pour valider directement
   - Mode Libre : Visualisez la bonne réponse avant de continuer
   - Mode Chrono : Passage instantané à la question suivante
6. **Consultez vos résultats** avec titre personnalisé selon votre score
7. **Explorez le classement** avec filtres par mode et par thème

## 🛠️ Développement

### Compilation
```bash
mvn clean compile
```

### Tests
```bash
mvn test
```

### Package
```bash
mvn package
```

## 📝 Notes techniques

### Technologies utilisées
- **JavaFX 17.0.2** : Framework d'interface graphique moderne
- **Maven 3.11.0** : Gestion des dépendances et compilation
- **FXML** : Définition déclarative des interfaces utilisateur
- **CSS personnalisé** : Styling avancé avec effets visuels (dropshadow, gradients)
- **Java 17** : Langage de programmation

### Fonctionnalités techniques
- **Système de profils** : Gestion multi-utilisateurs avec persistance
- **Double classement** : Séparation des scores par mode de jeu
- **Mélange des réponses** : Ordre aléatoire pour éviter les patterns
- **Chronomètre dynamique** : Bonus/malus de temps en mode chrono
- **Sauvegarde automatique** : Persistance des scores et profils
- **Filtres avancés** : Tri et filtrage par mode et thème
- **Interface responsive** : Préservation de l'état d'affichage (plein écran, maximisé)
- **Gestion d'erreurs** : Validation des entrées et gestion des cas limites

### Architecture
- **Pattern MVC** : Séparation des contrôleurs, modèles et vues
- **Programmation orientée objet** : Classes Question, Joueur, Classement
- **Streams Java** : Filtrage et tri fonctionnel des données
- **Gestion d'événements** : Listeners JavaFX pour l'interactivité

## 🎨 Personnalisation

### Ajouter des questions
1. Éditez les fichiers CSV dans le dossier `csv/`
2. Format : `Question;Réponse1;Réponse2;Réponse3;Réponse4;NuméroBonneRéponse`

### Modifier les thèmes
- Ajoutez une bannière PNG dans `src/main/resources/img/`
- Créez un fichier CSV correspondant
- Ajoutez le thème dans `ThemeController.java`

### Personnaliser l'apparence
- Modifiez `src/main/resources/css/style.css`
- Remplacez `fond.jpg` pour changer le fond d'écran

## 🎉 Crédits

**QuickQuiz** - Application de quiz développée avec ❤️ en Java et JavaFX

Développé dans le cadre d'un projet académique BUT Informatique
