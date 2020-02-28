import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Основной класс приложения со свойством tasks
 *      (задачи, хранящие необходимую информацию для их планирования методом planTask)
 * @author Анатолий Берелехис
 */
public class Planner {
    private Map<Integer, Task> tasks = new HashMap<>();

    /**
     * Основная функция приложения
     * @param args - аргументы командной строки
     *      (первый аргумент - имя файла с исходными задачами, второй аргумент - имя файла, куда сохранится результат)
     */
    public static void main(String[] args) {
        System.out.println("Start planTask method...");
        long startTime = System.nanoTime();
        List<List<Integer>> result = new Planner().planTasks(args[0]);
        long endTime = System.nanoTime();
        long durationInNano = (endTime - startTime);
        long durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);
        System.out.println("planTask has finished with elapsed time = " + durationInMillis + " milliseconds");
        if (args.length > 1) {
            System.out.println("Start output...");
            Planner.saveResult(result, args[1]);
            System.out.println("Planner has saved result in " + args[1]);
        }
    }

    /**
     * Планирует задачи
     * @param filename - имя файла с исходными задачами
     */
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

    /**
     * Считывает файл с исходными задачами и сохраняет в переменную tasks
     * @param filename - имя файла с исходными задачами
     */
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

    /**
     * Класс задачи
     * Каждая задача содержит количество задач, от которых она зависит, список задач, зависящих от нее, и номер шага
     * @author Анатолий Берелехис
     */
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

    /**
     * Записывает результатт планирования в файл
     * @param result - результат планирования
     * @param fileName - имя файла, куда выводится результат
     */
    static void saveResult(List<List<Integer>> result, String fileName) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            for (List<Integer> list : result) {
                for (Integer element : list)
                    writer.write(element + " ");
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
