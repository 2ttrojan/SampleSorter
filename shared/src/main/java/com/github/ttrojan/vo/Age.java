package com.github.ttrojan.vo;

import com.github.ttrojan.util.ClockUtil;
import lombok.Value;

import java.time.LocalDate;
import java.time.Period;

@Value
public class Age {
    int years;
    int months;
    int days;

    private Age(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Cannot create object from null");
        }
        Period period = Period.between(
                birthDate,
                LocalDate.now(ClockUtil.systemDefaultZone)
        );
        this.years = period.getYears();
        this.months = period.getMonths();
        this.days = period.getDays();
    }

    public static Age of(LocalDate birthDate) {
        return new Age(birthDate);
    }

}
