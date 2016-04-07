package ru.hse.model;

/**
 * Created by Ivan on 21.03.2016.
 */
public class Styles {
    public enum Icon {
        NEW(null),
        LOWSPEED("https://maps.google.com/mapfiles/ms/icons/blue-dot.png");

        Icon(String iconURL) {
            this.iconURL = iconURL;
        }

        private String iconURL;

        public String value() {
            return iconURL;
        }
    }

    public enum TrackColor {
        BLUE("#0000FF"),
        RED("#FF0000"),
        GREEN("#059000"),
        ORANGE("#FF7722"),
        PURPLE("7700FF"),
        BLACK("#000000");

        TrackColor(String value) {
            this.value = value;
        }

        private String value;
        private static int current = 1;

        public String value() {
            return value;
        }

        public static TrackColor next() {
            current = current % 6;
            current++;
            return TrackColor.values()[current - 1];
        }
    }
}
