package com.github.geirolz.exercises.introducingToFP.ex.model;



import com.github.geirolz.exercises.introducingToFP.ex.abstractions.Option;

import java.math.BigDecimal;

public class Person {

    private final String name;
    private final Integer age;
    private final Option<BigDecimal> moneyAmount;

    public Person(String name,
                  Integer age,
                  Option<BigDecimal> moneyAmount) {
        this.name = name;
        this.age = age;
        this.moneyAmount = moneyAmount;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Option<BigDecimal> getMoneyAmount() {
        return moneyAmount;
    }
}


