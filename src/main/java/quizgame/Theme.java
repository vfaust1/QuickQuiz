package quizgame;

public enum Theme {
    CINEMA,
    FOOTBALL,
    JEUX_VIDEO,
    MANGA,
    MUSIQUE,
    BANDEDESSINEE,
    ANIMATION,
    SERIESTV;


    public static Theme fromString(String theme) {
        return Theme.valueOf(theme.toUpperCase());
    }

    public static String equivalent(String number) {
        switch (number) {
            case "1":
                return "Cinema";
            case "2":
                return "Football";
            case "3":
                return "JeuxVideo";
            case "4":
                return "Manga";
            case "5":
                return "Musique";
            case "6":
                return "Bandedessinee";
            case "7":
                return "Animation";
            case "8":
                return "SeriesTV";
            default:
                return "Invalid theme";
        }
    }

    public String toString() {
        return this.name();
    }

    public Theme[] getAllThemes() {
        return Theme.values();
    }
}
