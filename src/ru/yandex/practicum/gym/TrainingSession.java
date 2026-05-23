package ru.yandex.practicum.gym;

import java.util.Objects;

public class TrainingSession implements Comparable<TrainingSession> {

    //группа
    private final Group group;
    //тренер
    private final Coach coach;
    //день недели
    private final DayOfWeek dayOfWeek;
    //время начала занятия
    private final TimeOfDay timeOfDay;

    public TrainingSession(Group group, Coach coach, DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        this.group = group;
        this.coach = coach;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
    }

    public Group getGroup() {
        return group;
    }

    public Coach getCoach() {
        return coach;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    @Override
    public String toString() {
        return "TrainingSession{" +
                "group=" + group +
                ", coach=" + coach +
                ", dayOfWeek=" + dayOfWeek +
                ", timeOfDay=" + timeOfDay +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TrainingSession that = (TrainingSession) o;
        return Objects.equals(getGroup(), that.getGroup()) && Objects.equals(getCoach(), that.getCoach())
                && getDayOfWeek() == that.getDayOfWeek() && Objects.equals(getTimeOfDay(), that.getTimeOfDay());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroup(), getCoach(), getDayOfWeek(), getTimeOfDay());
    }

    @Override
    public int compareTo(TrainingSession other) {
        int result;

        // сравниваем по группе
        result = this.group.getTitle().compareTo(other.group.getTitle());
        if (result != 0) {
            return result;
        }

        result = this.group.getAge().compareTo(other.group.getAge());
        if (result != 0) {
            return result;
        }

        result = Integer.compare(this.group.getDuration(), other.group.getDuration());
        if (result != 0) {
            return result;
        }

        // сравниваем по дню неделе и времени
        result = this.dayOfWeek.compareTo(other.dayOfWeek);
        if (result != 0) {
            return result;
        }

        result = this.timeOfDay.compareTo(other.timeOfDay);
        if (result != 0) {
            return result;
        }

        // сравниваем по тренеру
        result = this.coach.getSurname().compareTo(other.coach.getSurname());
        if (result != 0) {
            return result;
        }

        result = this.coach.getName().compareTo(other.coach.getName());
        if (result != 0) {
            return result;
        }

        return this.coach.getMiddleName().compareTo(other.coach.getMiddleName());

    }
}
