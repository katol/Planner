import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Planner {
    private Map<Integer, Task> tasks = new HashMap<>();

    static class Task {
        int parentsCount;
        List<Integer> children = new LinkedList<>();
        int stepNumber = -1;

        Task() {

        }

        Task(int parentsCount) {
            this.parentsCount = parentsCount;
        }

        void addChild(Integer childNumber) {
            this.children.add(childNumber);
        }
    }

    public List<List<Integer>> planTasks(String filename) {
        readFileToTasks(filename);
        List<List<Integer>> result = new LinkedList<>();
        int tasksCount = tasks.size();
        int checkedTasksCount = 0;
        int actualStepNumber = 0;
        while (tasksCount != checkedTasksCount) {
            List<Integer> actualList = new LinkedList<>();
            for (Iterator<Map.Entry<Integer, Task>> iterator = tasks.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry<Integer, Task> entry = iterator.next();
                Integer key = entry.getKey();
                Task value = entry.getValue();
                if (value.parentsCount == 0 && value.stepNumber < actualStepNumber) {
                    actualList.add(key);
                    checkedTasksCount++;
                    for (Integer childNumber : value.children) {
                        Task task = tasks.get(childNumber);
                        task.parentsCount -= 1;
                        task.stepNumber = actualStepNumber;
                    }
                    iterator.remove();
                }
            }
            result.add(actualList);
            actualStepNumber++;
        }
        return result;
    }

    private void readFileToTasks(String filename) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line != null) {
                String[] tasksNumbers = line.split(" ");

                Integer parentNumber = Integer.parseInt(tasksNumbers[0]);
                Integer childNumber = Integer.parseInt(tasksNumbers[1]);

                if (tasks.containsKey(childNumber))
                    tasks.get(childNumber).parentsCount++;
                else
                    tasks.put(childNumber, new Task(1));

                tasks.putIfAbsent(parentNumber, new Task());
                tasks.get(parentNumber).addChild(childNumber);

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
