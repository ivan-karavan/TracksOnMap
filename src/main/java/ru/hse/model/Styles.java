package ru.hse.model;

/**
 * Created by Ivan on 21.03.2016.
 */
public class Styles {
    public enum Icon {
        NEW(null),
        LOWSPEED("C:\\Ivan\\Images\\markers\\blueMarker.png");

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
        GREEN("#00FF00");

        TrackColor(String value) {
            this.value = value;
        }

        private String value;
        private static int current = 1;

        public String value() {
            return value;
        }

        public static TrackColor next() {
            current = current % 3;
            current++;
            return TrackColor.values()[current - 1];
        }
    }
}
