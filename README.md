## Архитектура системы ProcureMatch

```mermaid
flowchart TD
    %% Стилизация узлов
    classDef ui fill:#e0e7ff,stroke:#4f46e5,stroke-width:2px;
    classDef server fill:#fef3c7,stroke:#d97706,stroke-width:2px;
    classDef ai fill:#dcfce7,stroke:#16a34a,stroke-width:2px;
    classDef storage fill:#ffedd5,stroke:#ea580c,stroke-width:2px;

    subgraph Frontend ["ВЕБ-ИНТЕРФЕЙС / CRM (Клиент)"]
        UI_Train["Ввод гиперпараметров (Батч, LR, Эпохи)"]:::ui
        UI_Telemetry["Вывод телеметрии (Loss, Точность)"]:::ui
        UI_InputTables["Ввод таблиц (Предприятие + Поставщик)"]:::ui
        UI_OutputTable["Сводная таблица (Совпадение + Цена)"]:::ui
    end

    subgraph Trainer_VDS ["ОБУЧАЮЩИЙ КОНТУР (VDS)"]
        Trainer["tAires-Trainer (Пайплайн обучения)"]:::server
        Evaluator["Блок автотестов (Расчет метрик)"]:::ai
    end

    subgraph Storage ["ХРАНИЛИЩЕ S3"]
        S3[("S3: Нейросети (Чекпоинты, Веса)")]:::storage
    end

    subgraph Inference_Server ["ИНФЕРЕНС-КОНТУР (Сервер ИИ)"]
        AI_Model["ИИ-Модель (Embedding + Cross-Encoder)"]:::ai
        Matcher["Векторный поиск и скоринг (Estimate)"]:::ai
    end

    subgraph Main_Server ["БИЗНЕС-КОНТУР (Основной сервер)"]
        API["API шлюз / Валидация Токена"]:::server
        Merger["Сведение сопоставлений с ценами поставщика"]:::server
    end

    %% Логика обучения (Training Flow)
    UI_Train -->|Отправка параметров| Trainer
    Trainer -->|Обучение модели| Evaluator
    Evaluator -->|Отправка телеметрии| UI_Telemetry
    Evaluator -->|Загрузка готовых весов| S3

    %% Логика инференса (Inference Flow)
    UI_InputTables -->|Две таблицы| API
    API -->|Таблицы и Токен| Matcher
    S3 -->|Выгрузка актуальных весов| AI_Model
    Matcher <-->|Запросы к модели| AI_Model
    Matcher -->|Возврат пар и Estimate| Merger
    API -->|Оригинальные цены| Merger
    Merger -->|Итоговый отчет| UI_OutputTable
