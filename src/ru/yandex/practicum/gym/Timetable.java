package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();
        TreeMap<TimeOfDay, List<TrainingSession>> sessionsForDay = timetable.get(day);
        List<TrainingSession> sessionsAtTime = sessionsForDay.get(time);

        if (sessionsAtTime == null) {
            sessionsAtTime = new ArrayList<>();
            sessionsForDay.put(time, sessionsAtTime);
        }
        sessionsAtTime.add(trainingSession);
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        System.out.println(dayOfWeek + ": ");
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> sessionsForDay = timetable.get(dayOfWeek);
        System.out.println(dayOfWeek + " " + timeOfDay + " :");
        if (sessionsForDay == null) {
            return new ArrayList<>();
        }
        List<TrainingSession> sessions = sessionsForDay.get(timeOfDay);

        return sessions;
    }
}
