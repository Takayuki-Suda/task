package com.example.taskmanager;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private List<Task> tasks = new ArrayList<>();
    private final String filePath = "tasks.json";
    private int nextId; // 次に使用するID
    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapperのインスタンス

    // コンストラクタ
    public TaskController() {
        loadTasks(); // アプリケーション起動時にタスクをロード
        nextId = tasks.isEmpty() ? 1 : tasks.get(tasks.size() - 1).getId() + 1; // 次のIDを設定
    }

    // タスクを追加する
    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        task.setId(nextId++); // 自動的にIDを設定
        tasks.add(task);
        try {
            saveTasks(); // タスクをファイルに保存
            return new ResponseEntity<>(task, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // エラー時は500を返す
        }
    }

    // タスクを更新する
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody Task updatedTask) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setTitle(updatedTask.getTitle());
                task.setDescription(updatedTask.getDescription()); // 説明の更新を追加
                task.setCompleted(updatedTask.isCompleted());
                task.setDueDate(updatedTask.getDueDate()); // 日付の更新を追加
                try {
                    saveTasks(); // 更新後にファイルに保存
                    return new ResponseEntity<>(task, HttpStatus.OK);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // エラー時は500を返す
                }
            }
        }
        return ResponseEntity.notFound().build(); // タスクが見つからない場合
    }

    // すべてのタスクを取得する
    @GetMapping
    public List<Task> getTasks() {
        return tasks;
    }

    // タスクを削除する
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        boolean removed = tasks.removeIf(task -> task.getId() == id);
        try {
            saveTasks(); // タスクをファイルに保存
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // エラー時は500を返す
        }

        if (removed) {
            return ResponseEntity.noContent().build(); // 成功した場合は204 No Content
        } else {
            return ResponseEntity.notFound().build(); // タスクが見つからない場合は404 Not Found
        }
    }

    // タスクをファイルに保存するメソッド
    private void saveTasks() throws IOException {
        String json = objectMapper.writeValueAsString(tasks);
        Files.write(Paths.get(filePath), json.getBytes());
    }

    // タスクをファイルからロードするメソッド
    private void loadTasks() {
        try {
            if (Files.exists(Paths.get(filePath))) {
                System.out.println("Loading tasks from: " + filePath); // デバッグ用
                String json = new String(Files.readAllBytes(Paths.get(filePath)));
                Task[] taskArray = objectMapper.readValue(json, Task[].class);
                for (Task task : taskArray) {
                    tasks.add(task);
                    System.out.println("Loaded task: " + task); // デバッグ用
                }
                // 次のIDを設定
                nextId = tasks.isEmpty() ? 1 : tasks.get(tasks.size() - 1).getId() + 1;
            } else {
                System.out.println("File does not exist: " + filePath);
                nextId = 1; // ファイルが存在しない場合はIDを1から開始
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
