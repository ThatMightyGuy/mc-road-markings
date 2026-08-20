package com.jetfly.roadmarkings;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import oshi.util.tuples.Triplet;

enum Gender {
    NONE,
    MALE,
    FEMALE
}

enum Count {
    ONE,
    MANY
}

public class RussianLocalizer {
    private static Map<String, Triplet<Gender, Count, String>> generateHints() {
        Map<String, Triplet<Gender, Count, String>> h = new HashMap<>();

        h.put("all_turns", new Triplet<>(Gender.MALE, Count.ONE, "разъезд"));

        h.put("asphalt", new Triplet<>(Gender.MALE, Count.ONE, "асфальт"));

        h.put("bands_half", new Triplet<>(Gender.NONE, Count.MANY, "широкие полосы"));
        h.put("bands_quarter", new Triplet<>(Gender.NONE, Count.MANY, "обычные полосы"));
        h.put("bands_eighth", new Triplet<>(Gender.NONE, Count.MANY, "узкие полосы"));

        h.put("double_straight", new Triplet<>(Gender.FEMALE, Count.ONE, "двойная сплошная"));
        h.put("double_corner", new Triplet<>(Gender.MALE, Count.ONE, "угол двойной сплошной"));
        h.put("double_cross", new Triplet<>(Gender.NONE, Count.ONE, "пересечение двойной сплошной"));
        h.put("double_t", new Triplet<>(Gender.NONE, Count.ONE, "Т-образное пересечение двойной сплошной"));

        h.put("left", new Triplet<>(Gender.FEMALE, Count.ONE, "стрелка налево"));
        h.put("left_right", new Triplet<>(Gender.FEMALE, Count.ONE, "стрелка налево или направо"));

        h.put("pig_path", new Triplet<>(Gender.FEMALE, Count.ONE, "свинодорожка"));

        h.put("stop", new Triplet<>(Gender.MALE, Count.ONE, "указатель остановки"));
        h.put("ru_stop", new Triplet<>(Gender.MALE, Count.ONE, "указатель остановки (Россия)"));

        h.put("bus", new Triplet<>(Gender.MALE, Count.ONE, "указатель автобусной полосы"));

        h.put("crossing_ahead", new Triplet<>(Gender.MALE, Count.ONE, "указатель пешеходного перехода впереди (Япония)"));

        h.put("kana_ni", new Triplet<>(Gender.FEMALE, Count.ONE, "катакана ни"));
        h.put("kanji_kei", new Triplet<>(Gender.NONE, Count.ONE, "кандзи кей"));
        h.put("kanji_ryo", new Triplet<>(Gender.NONE, Count.ONE, "кандзи рё"));
        h.put("kanji_sha", new Triplet<>(Gender.NONE, Count.ONE, "кандзи ша"));
        h.put("kanji_wa", new Triplet<>(Gender.NONE, Count.ONE, "кандзи ва"));

        h.put("lane", new Triplet<>(Gender.MALE, Count.ONE, "указатель полосы"));

        h.put("priority_ahead", new Triplet<>(Gender.MALE, Count.ONE, "указатель главной дороги впереди (Япония, Россия)"));

        h.put("diagonal", new Triplet<>(Gender.FEMALE, Count.ONE, "диагональ"));
        h.put("diagonal_thin", new Triplet<>(Gender.FEMALE, Count.ONE, "тонкая диагональ"));

        h.put("roundabout_lhd", new Triplet<>(Gender.MALE, Count.ONE, "указатель левосторонней кольцевой дороги"));
        h.put("roundabout_rhd", new Triplet<>(Gender.MALE, Count.ONE, "указатель правосторонней кольцевой дороги"));

        h.put("railroad_crossing", new Triplet<>(Gender.MALE, Count.ONE, "Ж/Д переезд"));

        h.put("right", new Triplet<>(Gender.FEMALE, Count.ONE, "стрелка направо"));

        h.put("shoulder_double_inner", new Triplet<>(Gender.MALE, Count.ONE, "внутренний угол двойной обочины"));
        h.put("shoulder_double_outer", new Triplet<>(Gender.MALE, Count.ONE, "внешний угол двойной обочины"));
        h.put("shoulder_double_straight", new Triplet<>(Gender.FEMALE, Count.ONE, "двойная обочина"));

        h.put("shoulder_inner", new Triplet<>(Gender.MALE, Count.ONE, "внутренний угол обочины"));
        h.put("shoulder_outer", new Triplet<>(Gender.MALE, Count.ONE, "внешний угол обочины"));
        h.put("shoulder_straight", new Triplet<>(Gender.FEMALE, Count.ONE, "прямая обочина"));
        h.put("shoulder_t_left", new Triplet<>(Gender.NONE, Count.ONE, "левое Т-образное пересечение обочины"));
        h.put("shoulder_t_right", new Triplet<>(Gender.NONE, Count.ONE, "правое Т-образное пересечение обочины"));

        h.put("solid_straight", new Triplet<>(Gender.FEMALE, Count.ONE, "сплошная"));
        h.put("solid_corner", new Triplet<>(Gender.MALE, Count.ONE, "угол сплошной"));
        h.put("solid_cross", new Triplet<>(Gender.NONE, Count.ONE, "пересечение сплошной"));
        h.put("solid_cross_shoulder", new Triplet<>(Gender.NONE, Count.ONE, "пересечение сплошной и обочины"));
        h.put("solid_t", new Triplet<>(Gender.NONE, Count.ONE, "Т-образное пересечение сплошной"));
        h.put("solid_t_shoulder", new Triplet<>(Gender.NONE, Count.ONE, "Т-образное пересечение сплошной и обочины"));
        h.put("solid_t_shoulder_left", new Triplet<>(Gender.NONE, Count.ONE, "левое Т-образное пересечение сплошной и обочины"));
        h.put("solid_t_shoulder_right", new Triplet<>(Gender.NONE, Count.ONE, "правое Т-образное пересечение сплошной и обочины"));

        h.put("through", new Triplet<>(Gender.FEMALE, Count.ONE, "стрелка прямо"));
        h.put("through_left", new Triplet<>(Gender.FEMALE, Count.ONE, "стрелка прямо и налево"));
        h.put("through_right", new Triplet<>(Gender.FEMALE, Count.ONE, "стрелка прямо и направо"));

        h.put("hatch_diagonal_regular", new Triplet<>(Gender.NONE, Count.ONE, "обычное диагональное заполнение"));
        h.put("hatch_diagonal_tight", new Triplet<>(Gender.NONE, Count.ONE, "плотное диагональное заполнение"));
        h.put("hatch_diamond_regular", new Triplet<>(Gender.NONE, Count.ONE, "обычное сеточное заполнение"));
        h.put("hatch_diamond_tight", new Triplet<>(Gender.NONE, Count.ONE, "плотное сеточное заполнение"));

        h.put("dot", new Triplet<>(Gender.MALE, Count.ONE, "интерпункт"));

        h.put("jp_no_parking_straight", new Triplet<>(Gender.FEMALE, Count.ONE, "прямая разметка зоны без парковки (Япония)"));

        h.put("ped_direction", new Triplet<>(Gender.FEMALE, Count.ONE, "стрелка направления пешеходного движения"));

        h.put("stop_line", new Triplet<>(Gender.FEMALE, Count.ONE, "линия остановки"));
        h.put("flush_line", new Triplet<>(Gender.FEMALE, Count.ONE, "линия заподлицо"));
        h.put("flush_line_thin", new Triplet<>(Gender.FEMALE, Count.ONE, "тонкая линия заподлицо"));

        h.put("line_crossing_ahead_zag", new Triplet<>(Gender.MALE, Count.ONE, "левый зигзаг (Великобритания)"));
        h.put("line_crossing_ahead_zig", new Triplet<>(Gender.MALE, Count.ONE, "правый зигзаг (Великобритания)"));
        h.put("zigzag_end_full", new Triplet<>(Gender.MALE, Count.ONE, "полный конец зигзага (Великобритания)"));
        h.put("zigzag_end_left", new Triplet<>(Gender.MALE, Count.ONE, "левый конец зигзага (Великобритания)"));
        h.put("zigzag_end_right", new Triplet<>(Gender.MALE, Count.ONE, "правый конец зигзага (Великобритания)"));

        return h;
    }

    private static Map<Triplet<Gender, Count, String>, String> generateColorHints() {
        Map<Triplet<Gender, Count, String>, String> c = new HashMap<>();

        c.put(new Triplet<>(Gender.MALE, Count.ONE, "white"), "Белый");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "orange"), "Оранжевый");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "magenta"), "Пурпурный");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "~~LightBlue"), "Голубой");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "yellow"), "Жёлтый");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "lime"), "Лаймовый");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "pink"), "Розовый");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "~~LightGray"), "Светло-серый");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "gray"), "Серый");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "cyan"), "Бирюзовый");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "purple"), "Фиолетовый");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "blue"), "Синий");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "brown"), "Коричневый");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "green"), "Зелёный");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "red"), "Красный");
        c.put(new Triplet<>(Gender.MALE, Count.ONE, "black"), "Чёрный");

        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "white"), "Белая");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "orange"), "Оранжевая");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "magenta"), "Пурпурная");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "~~LightBlue"), "Голубая");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "yellow"), "Жёлтая");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "lime"), "Лаймовая");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "pink"), "Розовая");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "~~LightGray"), "Светло-серая");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "gray"), "Серая");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "cyan"), "Бирюзовая");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "purple"), "Фиолетовая");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "blue"), "Синяя");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "brown"), "Коричневая");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "green"), "Зелёная");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "red"), "Красная");
        c.put(new Triplet<>(Gender.FEMALE, Count.ONE, "black"), "Чёрная");

        c.put(new Triplet<>(Gender.NONE, Count.ONE, "white"), "Белое");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "orange"), "Оранжевое");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "magenta"), "Пурпурное");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "~~LightBlue"), "Голубое");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "yellow"), "Жёлтое");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "lime"), "Лаймовое");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "pink"), "Розовое");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "~~LightGray"), "Светло-серое");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "gray"), "Серое");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "cyan"), "Бирюзовое");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "purple"), "Фиолетовое");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "blue"), "Синее");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "brown"), "Коричневое");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "green"), "Зелёное");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "red"), "Красное");
        c.put(new Triplet<>(Gender.NONE, Count.ONE, "black"), "Чёрное");

        c.put(new Triplet<>(Gender.NONE, Count.MANY, "white"), "Белые");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "orange"), "Оранжевые");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "magenta"), "Пурпурные");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "~~LightBlue"), "Голубые");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "yellow"), "Жёлтые");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "lime"), "Лаймовые");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "pink"), "Розовые");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "~~LightGray"), "Светло-серые");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "gray"), "Серые");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "cyan"), "Бирюзовые");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "purple"), "Фиолетовые");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "blue"), "Синие");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "brown"), "Коричневые");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "green"), "Зелёные");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "red"), "Красные");
        c.put(new Triplet<>(Gender.NONE, Count.MANY, "black"), "Чёрные");

        return c;
    }

    private static final Map<String, Triplet<Gender, Count, String>> hints = generateHints();

    private static final Map<Triplet<Gender, Count, String>, String> colorHints = generateColorHints();

    private static final String findColor(Triplet<Gender, Count, String> hint) {
        for (Map.Entry<Triplet<Gender, Count, String>, String> entry : colorHints.entrySet()) {
            // Triplets equality is kinda weird. I think it should work?
            Triplet<Gender, Count, String> key = entry.getKey();
            if (key.getA() == hint.getA() &&
                key.getB() == hint.getB() &&
                key.getC().equals(hint.getC())) {
                return entry.getValue();
            }
        }
        return "not_found";
    }

    public static String translate(String unloc) {
        String[] loc = (unloc + "==")
        .replace("light_blue==", "~~LightBlue")
        .replace("light_gray==", "~~LightGray")
        .split("_");

        String color = loc[loc.length - 1].replace("==", "");

        String name = String.join("_", Arrays.copyOf(loc, loc.length - 1));

        Triplet<Gender, Count, String> hint = hints.get(name);

        if(hint == null) {
            RoadMarkings.LOGGER.error("Localize (ru_RU) error: name not found: {}", name);
            return null;
        }

        Triplet<Gender, Count, String> colorKey = new Triplet<>(hint.getA(), hint.getB(), color);

        return findColor(colorKey) + " " + hint.getC();
    }
}
