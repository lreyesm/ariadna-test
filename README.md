# Ariadna Events

A simple Java application designed for handling and querying event data from sources.  
This technical test simulates the core logic of processing electrical distribution events in-memory, as required by Ariadna Grid.

---

## 📚 Project Description

The application manages:
- **Sources**: origin points of events (with ID and name).
- **Events**: records with timestamp, source ID, and a value.

It loads data from CSV files, stores it in memory, and exposes search operations such as:
- 🔍 Search by source name.
- ⏱️ Search by timestamp range.
- 📈 Search by value range.

---

## 📁 Project Structure

```
ariadna-events/
├── pom.xml
├── README.md
├── resources/
│   ├── sources.csv
│   ├── events_1.csv
│   ├── events_2.csv
│   └── ... events_6.csv
├── src/
│   ├── main/java/com/ariadna/
│   │   ├── Main.java
│   │   ├── model/
│   │   │   ├── Event.java
│   │   │   └── Source.java
│   │   ├── repository/
│   │   │   └── DataStore.java
│   │   ├── service/
│   │   │   └── EventService.java
│   │   └── util/
│   │       └── FileLoader.java
│   └── test/java/com/ariadna/service/
│       └── EventServiceTest.java
```

---

## 🚀 How to Run

### 1. ✅ Requirements

- Java 17 or higher
- Maven 3.x

### 2. 🛠️ Build the project

```bash
mvn clean compile
```

### 3. ▶️ Run the application

```bash
mvn exec:java
```

The application will:
- Load sources and events from `/resources/`
- Link sources to their events
- Perform example queries printed to the console

### 4. 🧪 Run Unit Tests

```bash
mvn test
```

Tests cover:
- ✅ Source name filtering
- ✅ Date range queries
- ✅ Value range queries

---

## 🧠 Notes

- Data is stored **entirely in memory**, no external database is used.
- Event loading is done in **parallel** using a thread pool.
- Timestamp-based search is **efficient** thanks to `TreeMap` indexing.

---

## 👨‍💻 Author

Luis Alejandro – Technical Test for **Ariadna Grid**  
Feel free to explore, test and improve! 💡
