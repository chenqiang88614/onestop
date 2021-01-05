package com.onestop.ecosystem.constant;

public enum Grade {
    PERFECT(75, "优"),
    BEST(55, "良"),
    NORMAL(35, "一般"),
    WORSE(20, "较差"),
    BAD(0, "差")
    ;

    private Integer grade;

    private String name;

    Grade(Integer grade, String name) {
        this.grade = grade;
        this.name = name;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
