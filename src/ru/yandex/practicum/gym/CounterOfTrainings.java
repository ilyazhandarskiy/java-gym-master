package ru.yandex.practicum.gym;

import java.util.Objects;

public class CounterOfTrainings implements Comparable<CounterOfTrainings> {
    private final Coach coach;
    private final int numberOfTrainings;

    public CounterOfTrainings(Coach coach, int numberOfTrainings) {
        this.coach = coach;
        this.numberOfTrainings = numberOfTrainings;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getNumberOfTrainings() {
        return numberOfTrainings;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CounterOfTrainings that = (CounterOfTrainings) o;
        return numberOfTrainings == that.numberOfTrainings && Objects.equals(coach, that.coach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coach, numberOfTrainings);
    }

    @Override
    public int compareTo(CounterOfTrainings o) {
        return this.numberOfTrainings -  o.numberOfTrainings;
    }

    @Override
    public String toString() {
        return "CounterOfTrainings{" +
                "coach=" + coach +
                ", numberOfTrainings=" + numberOfTrainings +
                '}';
    }
}
