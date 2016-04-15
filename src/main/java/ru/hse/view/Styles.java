package ru.hse.view;

/**
 * Created by Ivan on 21.03.2016.
 */
public class Styles {
    public enum Icon {
        //NEW(null),
        LOWSPEED("http://icons.iconarchive.com/icons/icons-land/vista-map-markers/24/Map-Marker-Marker-Outside-Azure-icon.png"),
        MEDIUMSPEED("http://individual.icons-land.com/IconsPreview/MapMarkers/PNG/Centered/24x24/MapMarker_Marker_Outside_Green.png"),
        HIGHSPEED("http://icons.iconarchive.com/icons/icons-land/vista-map-markers/24/Map-Marker-Marker-Outside-Pink-icon.png"),
        HIDDEN("http://wiki.plarium.com/images/5/59/Empty.png");

        Icon(String iconURL) {
            this.iconURL = iconURL;
        }

        private String iconURL;

        public String value() {
            return iconURL;
        }

        public static Styles.Icon getNecessaryIcon(int windSpeed) {
            if (windSpeed < 0) {
                return Icon.values()[3];
            }
            else if (windSpeed == 0) {
                return Icon.values()[0];
            }
            else if (windSpeed <= 30) {
                return Icon.values()[1];
            }
            else {
                return Icon.values()[2];
            }
        }
    }

    public enum TrackColor {
        BLUE("#0000FF"),
        RED("#FF0000"),
        GREEN("#059000"),
        ORANGE("#FF7722"),
        PURPLE("7700FF"),
        AZURE("#0080FF"),
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
            current = current % 7;
            current++;
            return TrackColor.values()[current - 1];
        }
    }
}
