import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class Task {
    private final String description;
    private final LocalTime startTime;
    private final int durationMinutes;
    private boolean completed;
    private final int priority; // 1 (highest) to 5 (lowest)

    public Task(String description, LocalTime startTime, int durationMinutes, int priority) {
        this.description = description;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.completed = false;
        this.priority = priority;
    }

    // Getters and setters
    public String getDescription() { return description; }
    public LocalTime getStartTime() { return startTime; }
    public int getDurationMinutes() { return durationMinutes; }
    public boolean isCompleted() { return completed; }
    public int getPriority() { return priority; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return String.format("[%s] %s - Duration: %d mins (Priority: %d) [%s]",
                startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                description,
                durationMinutes,
                priority,
                completed ? "DONE" : "NOT DONE");
    }
}

class DailyPlanner {
    private final LocalDate currentDate;
    private final List<Task> tasks;
    private final Scanner scanner;

    public DailyPlanner() {
        this.currentDate = LocalDate.now();
        this.tasks = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            displayMenu();
            int choice = getMenuChoice();

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    viewTasks();
                    break;
                case 3:
                    markTaskCompleted();
                    break;
                case 4:
                    removeTask();
                    break;
                case 5:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=== Daily Planner (" +
                currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ") ===");
        System.out.println("1. Add Task");
        System.out.println("2. View Tasks");
        System.out.println("3. Mark Task as Completed");
        System.out.println("4. Remove Task");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private int getMenuChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void addTask() {
        try {
            System.out.print("Enter task description: ");
            String description = scanner.nextLine();

            System.out.print("Enter start time (HH:mm): ");
            LocalTime startTime = LocalTime.parse(scanner.nextLine(),
                    DateTimeFormatter.ofPattern("HH:mm"));

            System.out.print("Enter duration (in minutes): ");
            int duration = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter priority (1-5, 1 being highest): ");
            int priority = Integer.parseInt(scanner.nextLine());

            if (priority < 1 || priority > 5) {
                System.out.println("Invalid priority. Must be between 1 and 5.");
                return;
            }

            tasks.add(new Task(description, startTime, duration, priority));
            System.out.println("Task added successfully!");

            // Sort tasks by start time
            tasks.sort(Comparator.comparing(Task::getStartTime));

        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format. Please use HH:mm format.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        }
    }

    private void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled for today.");
            return;
        }

        System.out.println("\nTasks for today:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, tasks.get(i));
        }
    }

    private void markTaskCompleted() {
        viewTasks();
        if (tasks.isEmpty()) return;

        System.out.print("Enter task number to mark as completed: ");
        try {
            int taskNum = Integer.parseInt(scanner.nextLine()) - 1;
            if (taskNum >= 0 && taskNum < tasks.size()) {
                Task task = tasks.get(taskNum);
                task.setCompleted(!task.isCompleted());
                System.out.println("Task status updated!");
                System.out.println("Updated task: " + task);
            } else {
                System.out.println("Invalid task number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void removeTask() {
        viewTasks();
        if (tasks.isEmpty()) return;

        System.out.print("Enter task number to remove: ");
        try {
            int taskNum = Integer.parseInt(scanner.nextLine()) - 1;
            if (taskNum >= 0 && taskNum < tasks.size()) {
                tasks.remove(taskNum);
                System.out.println("Task removed successfully!");
            } else {
                System.out.println("Invalid task number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        DailyPlanner planner = new DailyPlanner();
        planner.run();
    }
}