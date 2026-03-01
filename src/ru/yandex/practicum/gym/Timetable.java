package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();
        TreeMap<TimeOfDay, List<TrainingSession>> timetableOfDay = timetable.get(dayOfWeek);

        if (timetableOfDay == null) {
            timetableOfDay = new TreeMap<>();
            timetable.put(dayOfWeek, timetableOfDay);
        }

        List<TrainingSession> listAtTime = timetableOfDay.get(timeOfDay);

        if (listAtTime == null) {
            listAtTime = new ArrayList<>();
            timetableOfDay.put(timeOfDay, listAtTime);
        }

        listAtTime.add(trainingSession);

    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.getOrDefault(dayOfWeek, new TreeMap<>());
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> trainingSessionsForDay =
                timetable.getOrDefault(dayOfWeek, new TreeMap<>());

        return trainingSessionsForDay.getOrDefault(timeOfDay, new ArrayList<>());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        HashMap<Coach, Integer> countsMap = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> dayMap : timetable.values()) {
            for (List<TrainingSession> sessionsList : dayMap.values()) {
                for (TrainingSession session : sessionsList) {
                    Coach coach = session.getCoach();
                    countsMap.put(coach, countsMap.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : countsMap.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        Collections.sort(result);
        return result;
    }
}
