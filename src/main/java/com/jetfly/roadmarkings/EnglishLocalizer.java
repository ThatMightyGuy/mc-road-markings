package com.jetfly.roadmarkings;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class EnglishLocalizer {
    private static Map<String, String> generateHints() {
        Map<String, String> h = new HashMap<>();

        h.put("all_turns", "Three Way");

        h.put("asphalt", "Asphalt");

        h.put("bands_half", "Wide Bands");
        h.put("bands_quarter", "Regular Bands");
        h.put("bands_eighth", "Thin Bands");

        h.put("double_straight", "Double Solid Straight");
        h.put("double_corner", "Double Solid Corner");
        h.put("double_cross", "Double Solid Cross");
        h.put("double_t", "Double Solid T-Junction");

        h.put("left", "Left Turn Arrow");
        h.put("left_right", "Left-Right Turn Arrow");

        h.put("pig_path", "Pig Path");

        h.put("stop", "Stop Marker");

        h.put("railroad_crossing", "Railroad Crossing");

        h.put("right", "Right Turn Arrow");

        h.put("shoulder_double_inner", "Double Solid Shoulder Inner Corner");
        h.put("shoulder_double_outer", "Double Solid Shoulder Outer Corner");
        h.put("shoulder_double_straight", "Double Solid Shoulder");

        h.put("shoulder_inner", "Shoulder Inner Corner");
        h.put("shoulder_outer", "Shoulder Outer Corner");
        h.put("shoulder_straight", "Shoulder Straight");
        h.put("shoulder_t_left", "Shoulder T-Junction Left");
        h.put("shoulder_t_right", "Shoulder T-Junction Right");

        h.put("solid_straight", "Solid Straight");
        h.put("solid_corner", "Solid Corner");
        h.put("solid_cross", "Solid Cross");
        h.put("solid_cross_shoulder", "Solid and Shoulder Cross");
        h.put("solid_t", "Solid T-Junction");
        h.put("solid_t_shoulder", "Solid and Shoulder T-Junction");
        h.put("solid_t_shoulder_left", "Solid and Shoulder T-Junction Left");
        h.put("solid_t_shoulder_right", "Solid and Shoulder T-Junction Right");

        h.put("through", "Straight Arrow");
        h.put("through_left", "Through-Left Arrow");
        h.put("through_right", "Through-Right Arrow");

        return h;
    }

    private static Map<String, String> generateColorHints() {
        Map<String, String> c = new HashMap<>();

        c.put("white", "White");
        c.put("orange", "Orange");
        c.put("magenta", "Purple");
        c.put("~~LightBlue", "Light Blue");
        c.put("yellow", "Yellow");
        c.put("lime", "Lime");
        c.put("pink", "Pink");
        c.put("~~LightGray", "Light Gray");
        c.put("gray", "Gray");
        c.put("cyan", "Cyan");
        c.put("purple", "Purple");
        c.put("blue", "Blue");
        c.put("brown", "Brown");
        c.put("green", "Green");
        c.put("red", "Red");
        c.put("black", "Black");

        return c;
    }

    private static final Map<String, String> hints = generateHints();

    private static final Map<String, String> colorHints = generateColorHints();

    public static String translate(String unloc) {
        String[] loc = (unloc + "==")
        .replace("light_blue==", "~~LightBlue")
        .replace("light_gray==", "~~LightGray")
        .split("_");

        String color = loc[loc.length - 1].replace("==", "");

        String name = String.join("_", Arrays.copyOf(loc, loc.length - 1));

        String hint = hints.get(name);

        if(hint == null) {
            RoadMarkings.LOGGER.error("Localize (en_US) error: name not found: {}", name);
            return null;
        }

        return colorHints.get(color) + " " + hint;
    }
}
