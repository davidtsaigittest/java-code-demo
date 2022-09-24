package juc.countdownlatch;

import lombok.Getter;

public enum SeasonEnum {

    SPRING(1, "春天", "spring"),
    SUMMER(2, "夏天", "summer"),
    FALL(3, "秋天", "fall"),
    WINTER(4, "冬天", "winter");

    SeasonEnum(Integer sn, String chtName, String engName) {
        this.sn = sn;
        this.chtName = chtName;
        this.engName = engName;
    }

    @Getter
    private Integer sn;

    @Getter
    private String chtName;

    @Getter
    private String engName;

    public static SeasonEnum forEachSeasonEnum(int index) {
        for (SeasonEnum element : SeasonEnum.values()) {
            if (index == element.getSn()) {
                return element;
            }
        }
        return null;
    }
}
