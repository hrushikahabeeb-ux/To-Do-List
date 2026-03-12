import java.util.*;

/* ---------------- TASK CLASS ---------------- */
class Task {
    int id;
    String text;
    String dueDate;
    String priority;
    String category;
    boolean completed;

    Task(int id, String text, String dueDate, String priority, String category) {
        this.id = id;
        this.text = text;
        this.dueDate = dueDate;
        this.priority = priority;
        this.category = category;
        this.completed = false;
    }
}

/* ---------------- LINKED LIST FOR HISTORY ---------------- */
class HistoryNode {
    String action;
    HistoryNode next;

    HistoryNode(String action) {
        this.action = action;
        this.next = null;
    }
}

class HistoryLinkedList {
    HistoryNode head;

    void add(String action) {
        HistoryNode newNode = new HistoryNode(action);

        if (head == null) {
            head = newNode;
            return;
        }

        HistoryNode temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }

        temp.next = newNode;
    }

    void display() {
        HistoryNode temp = head;
        System.out.println("\nActivity History:");
        while (temp != null) {
            System.out.println("- " + temp.action);
            temp = temp.next;
        }
    }
}

/* ---------------- MAIN PROGRAM ---------------- */
public class SmartTodoConsole {

    /* ARRAY STORAGE */
    static ArrayList<Task> tasks = new ArrayList<>();

    /* STACK FOR UNDO DELETE */
    static Stack<Task> deletedTasks = new Stack<>();

    /* QUEUE FOR NOTIFICATIONS */
    static Queue<String> notifications = new LinkedList<>();

    /* HASH MAP FOR FAST LOOKUP */
    static HashMap<Integer, Task> taskMap = new HashMap<>();

    /* LINKED LIST FOR HISTORY */
    static HistoryLinkedList history = new HistoryLinkedList();

    static Scanner sc = new Scanner(System.in);

    /* ---------------- ADD TASK ---------------- */
    static void addTask() {
        System.out.print("Enter task: ");
        String text = sc.nextLine();

        System.out.print("Due date: ");
        String date = sc.nextLine();

        System.out.print("Priority (High/Medium/Low): ");
        String priority = sc.nextLine();

        System.out.print("Category: ");
        String category = sc.nextLine();

        int id = (int) (System.currentTimeMillis() % 100000);

        Task task = new Task(id, text, date, priority, category);

        tasks.add(task);
        taskMap.put(id, task);

        notifications.add("Reminder for: " + text);
        history.add("Added task: " + text);

        System.out.println("Task added!");
    }

    /* ---------------- VIEW TASKS ---------------- */
    static void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks.");
            return;
        }

        for (Task t : tasks) {
            System.out.println(
                "ID: " + t.id +
                " | " + t.text +
                " | " + t.priority +
                " | " + t.category +
                " | Completed: " + t.completed
            );
        }
    }

    /* ---------------- DELETE TASK ---------------- */
    static void deleteTask() {
        System.out.print("Enter task ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        Task task = taskMap.get(id);

        if (task == null) {
            System.out.println("Task not found");
            return;
        }

        deletedTasks.push(task);
        tasks.remove(task);
        taskMap.remove(id);

        history.add("Deleted task: " + task.text);

        System.out.println("Task deleted.");
    }

    /* ---------------- UNDO DELETE ---------------- */
    static void undoDelete() {
        if (deletedTasks.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }

        Task task = deletedTasks.pop();

        tasks.add(task);
        taskMap.put(task.id, task);

        history.add("Restored task: " + task.text);

        System.out.println("Task restored.");
    }

    /* ---------------- MARK COMPLETE ---------------- */
    static void completeTask() {
        System.out.print("Enter task ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Task task = taskMap.get(id);

        if (task == null) {
            System.out.println("Task not found");
            return;
        }

        task.completed = true;

        history.add("Completed task: " + task.text);

        System.out.println("Task marked complete.");
    }

    /* ---------------- LINEAR SEARCH ---------------- */
    static void searchTask() {
        System.out.print("Search keyword: ");
        String keyword = sc.nextLine().toLowerCase();

        for (Task t : tasks) {
            if (t.text.toLowerCase().contains(keyword)) {
                System.out.println(t.id + " - " + t.text);
            }
        }
    }

    /* ---------------- SORT BY PRIORITY (BUBBLE SORT) ---------------- */
    static void sortByPriority() {
        for (int i = 0; i < tasks.size() - 1; i++) {
            for (int j = 0; j < tasks.size() - i - 1; j++) {

                if (priorityValue(tasks.get(j).priority) >
                    priorityValue(tasks.get(j + 1).priority)) {

                    Task temp = tasks.get(j);
                    tasks.set(j, tasks.get(j + 1));
                    tasks.set(j + 1, temp);
                }
            }
        }

        System.out.println("Tasks sorted by priority.");
    }

    static int priorityValue(String p) {
        if (p.equalsIgnoreCase("High")) return 1;
        if (p.equalsIgnoreCase("Medium")) return 2;
        return 3;
    }

    /* ---------------- PROCESS NOTIFICATION ---------------- */
    static void processNotification() {
        if (notifications.isEmpty()) {
            System.out.println("No notifications.");
            return;
        }

        String note = notifications.poll();
        System.out.println("Processed: " + note);
    }

    /* ---------------- MAIN MENU ---------------- */
    public static void main(String[] args) {

        while (true) {

            System.out.println("\n=== DSA Smart To-Do List ===");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Delete Task");
            System.out.println("4. Undo Delete");
            System.out.println("5. Complete Task");
            System.out.println("6. Search Task");
            System.out.println("7. Sort by Priority");
            System.out.println("8. Process Notification");
            System.out.println("9. View History");
            System.out.println("0. Exit");

            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: addTask(); break;
                case 2: viewTasks(); break;
                case 3: deleteTask(); break;
                case 4: undoDelete(); break;
                case 5: completeTask(); break;
                case 6: searchTask(); break;
                case 7: sortByPriority(); break;
                case 8: processNotification(); break;
                case 9: history.display(); break;
                case 0: System.exit(0);
                default: System.out.println("Invalid choice");
            }
        }
    }
}