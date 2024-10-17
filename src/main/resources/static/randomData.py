import json
import random
import string
import time

# ランダムな文字列を生成する関数
def generate_random_string(length=10):
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

# タスクデータを生成する関数
def generate_random_tasks(num_tasks=1000):#1000件のランダムデータを生成
    tasks = []
    current_time = int(time.time() * 1000)  # 現在の時刻（ミリ秒）
    for _ in range(num_tasks):
        task = {
            "id": _ + 1,
            "description": generate_random_string(random.randint(1, 10)),  # ランダムな説明
            "completed": random.choice([True, False]),  # 完了状態
            "dueDate": random.choice([None, current_time + random.randint(0, 365 * 24 * 60 * 60 * 1000)])  # 未来の期日またはNone
        }
        tasks.append(task)
    return tasks

# tasks.jsonにデータを保存する関数
def save_tasks_to_json(tasks, filename='C:\\Users\\s_t24\\OneDrive\\Desktop\\java\\rensyu\\demo\\tasks.json'):
    with open(filename, 'w', encoding='utf-8') as f:
        json.dump(tasks, f, ensure_ascii=False, indent=4)

# メイン処理
if __name__ == "__main__":
    random_tasks = generate_random_tasks()
    save_tasks_to_json(random_tasks)
    print(f"Generated {len(random_tasks)} tasks and saved to 'tasks.json'.")
