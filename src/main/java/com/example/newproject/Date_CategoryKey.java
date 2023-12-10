package com.example.newproject;

import java.time.LocalDate;
import java.util.Objects;


public class Date_CategoryKey {
    public  LocalDate date;
    public  String category;
    public Date_CategoryKey(LocalDate date, String category){
        this.date = date;
        this.category = category;
    }
    @Override
    public int hashCode() {
        return Objects.hash(date, category);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Date_CategoryKey other = (Date_CategoryKey) obj;
        return Objects.equals(date, other.date) && Objects.equals(category, other.category);
    }
}


