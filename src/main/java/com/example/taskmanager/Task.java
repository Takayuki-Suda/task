package com.example.taskmanager; // パッケージ名は実際のものに合わせてください

import java.util.Date;

public class Task {
    private int id; // タスクID
    private String description; // タスクの説明
    private boolean completed; // タスクが完了したかどうか
    private Date dueDate; // タスクの期限

 // デフォルトコンストラクタ
    public Task() {
    }

    // コンストラクタ
    public Task(int id, String description, boolean completed, Date dueDate) {
        this.id = id;
        this.description = description;
        this.completed = completed;
        this.dueDate = dueDate;
    }

    // ゲッターとセッター
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
