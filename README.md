# PDMS — Personal Diary Management System

> **A University Semester Project by Saad Khan and Zahid Ullah**

A modern Java Swing desktop application for organizing your daily life. PDMS combines five essential modules — **Diary**, **Grocery**, **Tasks**, **Mood**, and **Memories** — into a single, polished dark-themed interface with a flat design aesthetic.

<p align="center">
  <img src="src/images/logo.png" alt="PDMS Logo" width="300"/>
</p>

---

## Table of Contents

- [Features](#features)
- [Screenshots](#screenshots)
- [Installation](#installation)
  - [Option 1 — Run the .exe (Recommended)](#option-1--run-the-exe-recommended)
  - [Option 2 — Run the JAR File](#option-2--run-the-jar-file)
  - [Option 3 — Compile and Run from Source](#option-3--compile-and-run-from-source)
- [Building the .exe Yourself](#building-the-exe-yourself)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [How Data is Stored](#how-data-is-stored)
- [Technologies Used](#technologies-used)
- [Authors](#authors)

---

## Features

### Diary Entry
- Write and manage personal diary entries with a **title**, **date**, and **content**.
- **Mood Linking** — Attach a mood (Happy, Sad, Relaxed, Stressed, Angry) to each entry.
- Search and filter entries by title or date.
- Full CRUD support (Create, Read, Update, Delete).

### 🛒 Grocery List
- Add grocery items and mark them as purchased.
- Purchased items are highlighted in **green** for quick visual scanning.
- Full CRUD support.

### 📋 Task Manager
- Create tasks with a title, deadline, and priority level (High / Medium / Low).
- **Color-Coded Priorities:**
  - 🔴 **Red** — High priority
  - 🟠 **Orange** — Medium priority
  - 🟢 **Green** — Low priority
- Mark tasks as completed; completed tasks appear dimmed.
- Full CRUD support.

### 😊 Mood Tracker
- Log your daily mood with an optional note.
- **Mood Summary** — A summary card at the top shows your most frequently logged mood with an emoji.
- Visual emoji indicators for each mood in the history list.

### 💫 Memories
- Record personal memories with a title, date, location, and description.
- Search and filter memories by title, date, or location.
- Location pins displayed alongside each memory entry.

---

## Screenshots

| Dashboard | Diary Entry | Task Manager |
|-----------|-------------|--------------|
| Deep navy themed landing page with the PDMS logo and navigation buttons | Two-panel layout with mood dropdown and search | Color-coded priority bars (Red/Orange/Green) |

<img width="1039" height="611" alt="image" src="https://github.com/user-attachments/assets/1d3bc86c-ad54-4710-8f5a-a029156aafda" />

<img width="904" height="571" alt="image" src="https://github.com/user-attachments/assets/44411a6e-d23e-4497-bc25-eb51397ce553" />

<img width="465" height="515" alt="image" src="https://github.com/user-attachments/assets/be3c5ed6-c515-4ed0-9fce-6afeac2025b3" />

<img width="569" height="552" alt="image" src="https://github.com/user-attachments/assets/dfe160a8-8dfa-4e65-bf1f-f5532a26656c" />

<img width="487" height="596" alt="image" src="https://github.com/user-attachments/assets/78f73de6-d0e4-4c22-9973-b1bfa4830e99" />

<img width="947" height="580" alt="image" src="https://github.com/user-attachments/assets/d3a91c17-e762-4b36-b792-5da67bf4a09a" />

---

## Installation

### Prerequisites

| Requirement | Minimum Version | Check Command |
|---|---|---|
| **Java JDK** | 8 or higher | `java -version` |

> **No external libraries, Maven, or Gradle required.** The entire application uses only built-in Java libraries.

---

### Option 1 — Run the `.exe` (Recommended)

This is the easiest way to run PDMS on any Windows PC. No Java installation needed on the target machine.

1. Download or locate the `installer/PDMS/` folder.
2. Copy the **entire `PDMS` folder** to any location on your PC (e.g., `C:\Program Files\PDMS\` or your Desktop).
3. Double-click **`PDMS.exe`** to launch the application.

> **Note:** The `PDMS` folder must stay intact — it contains the runtime and app files needed to run.

---

### Option 2 — Run the JAR File

If you have Java installed, you can run the portable JAR file:

1. Open a terminal (Command Prompt / PowerShell).
2. Navigate to the project root folder:
   ```bash
   cd path\to\PersonalDiaryApp
   ```
3. Build the JAR (first time only):
   ```bash
   build.bat
   ```
4. Run the application:
   ```bash
   java -jar dist\PDMS.jar
   ```

---

### Option 3 — Compile and Run from Source

For developers or when modifying the source code:

1. Open a terminal and navigate to the project root:
   ```bash
   cd path\to\PersonalDiaryApp
   ```

2. Compile all source files:
   ```bash
   javac -encoding UTF-8 -d out src\util\FileUtil.java src\model\*.java src\manager\*.java src\gui\*.java src\main\DiaryApplication.java
   ```

3. Copy the images to the output folder (so icons load properly):
   ```bash
   xcopy /s /i src\images out\images
   ```

4. Run the application:
   ```bash
   java -cp out main.DiaryApplication
   ```

> **Important:** Always run from the **project root directory** so relative paths to images and data files resolve correctly.

---

### Using an IDE (IntelliJ / Eclipse / VS Code)

1. Open the `PersonalDiaryApp` folder as a project.
2. Mark `src/` as the **Source Root**.
3. Run `main.DiaryApplication` as the main class.

---

## Building the `.exe` Yourself

To create a native Windows `.exe` from source:

### Prerequisites

| Requirement | Minimum Version | Why |
|---|---|---|
| **Java JDK** | 14 or higher | `jpackage` was introduced in JDK 14 |

### Steps

1. Open a terminal in the project root.
2. Run the build script:
   ```bash
   build-exe.bat
   ```
3. The executable will be created at:
   ```
   installer\PDMS\PDMS.exe
   ```
4. To distribute, copy the **entire `installer\PDMS\` folder** to the target PC.

### What the Script Does

| Step | Description |
|---|---|
| `build.bat` | Compiles sources → copies images → creates `dist\PDMS.jar` |
| `jpackage` | Bundles the JAR + a Java runtime into a standalone `.exe` |

> **The resulting `.exe` includes its own Java runtime**, so the target PC does **not** need Java installed.

---

## Project Structure

```
PersonalDiaryApp/
│
├── src/                              # Source code
│   ├── main/
│   │   └── DiaryApplication.java     # Application entry point
│   ├── model/
│   │   ├── Mood.java                 # Mood enum (HAPPY, SAD, RELAXED, STRESSED, ANGRY)
│   │   ├── Priority.java             # Priority enum (HIGH, MEDIUM, LOW)
│   │   ├── DiaryEntry.java           # Diary entry data model
│   │   ├── GroceryItem.java          # Grocery item data model
│   │   ├── Memory.java               # Memory data model
│   │   ├── MoodEntry.java            # Mood entry data model
│   │   └── Task.java                 # Task data model
│   ├── manager/
│   │   ├── DiaryManager.java         # Diary file read/write operations
│   │   ├── GroceryManager.java       # Grocery file read/write operations
│   │   ├── MemoryManager.java        # Memory file read/write operations
│   │   ├── MoodManager.java          # Mood file read/write + frequency analysis
│   │   └── TaskManager.java          # Task file read/write operations
│   ├── gui/
│   │   ├── UITheme.java              # Centralized theme (colors, fonts, components)
│   │   ├── DashboardFrame.java       # Main dashboard / landing screen
│   │   ├── DiaryFrame.java           # Diary module UI
│   │   ├── GroceryFrame.java         # Grocery module UI
│   │   ├── TaskFrame.java            # Task module UI
│   │   ├── MoodFrame.java            # Mood module UI
│   │   └── MemoryFrame.java          # Memory module UI
│   ├── util/
│   │   └── FileUtil.java             # File I/O, Base64 encoding, date validation
│   └── images/
│       ├── logo.png                   # Application logo (dashboard + window icon)
│       ├── logo.ico                   # Windows ICO format (for .exe icon)
│       ├── diary.png                  # Banner logo (by Saad-Zahid)
│       ├── icon_diary.png             # Diary module icon
│       ├── icon_grocery.png           # Grocery module icon
│       ├── icon_task.png              # Task module icon
│       ├── icon_mood.png              # Mood module icon
│       └── icon_memories.png          # Memories module icon
│
├── data/                              # Runtime data (auto-created on first run)
│   ├── diary.txt                     #   Diary entries
│   ├── grocery.txt                   #   Grocery items
│   ├── tasks.txt                     #   Tasks
│   ├── mood.txt                      #   Mood entries
│   └── memories.txt                  #   Memories
│
├── out/                               # Compiled .class files
├── dist/                              # Packaged JAR file
│   └── PDMS.jar
├── installer/                         # Native .exe application
│   └── PDMS/
│       └── PDMS.exe
│
├── build.bat                          # Build script (compile + JAR)
├── build-exe.bat                      # Build script (compile + JAR + .exe)
├── MANIFEST.MF                        # JAR manifest (main class)
├── README.md                          # This file
└── .gitignore
```

---

## Architecture

The application follows a clean **three-layer architecture**:

```
┌────────────────────────────────────────────┐
│              GUI Layer (gui/)              │
│  DashboardFrame, DiaryFrame, TaskFrame,   │
│  GroceryFrame, MoodFrame, MemoryFrame     │
│  UITheme (centralized styling)            │
├────────────────────────────────────────────┤
│         Manager Layer (manager/)           │
│  DiaryManager, GroceryManager,            │
│  TaskManager, MoodManager, MemoryManager  │
├────────────────────────────────────────────┤
│          Model Layer (model/)             │
│  DiaryEntry, GroceryItem, Task,           │
│  MoodEntry, Memory, Mood, Priority        │
├────────────────────────────────────────────┤
│          Utility Layer (util/)            │
│  FileUtil (I/O, Base64, validation)       │
└────────────────────────────────────────────┘
```

| Layer | Package | Responsibility |
|---|---|---|
| **View** | `gui` | Swing frames, user interaction, theming |
| **Controller** | `manager` | Business logic, data parsing, persistence |
| **Model** | `model` | Data classes (POJOs) and enums |
| **Utility** | `util` | File I/O, encoding, date validation |

---

## How Data is Stored

All data is persisted in **plain text files** inside the `data/` directory (auto-created on first launch). Each line represents one record, with fields separated by `|`.

### Safe Encoding

To prevent data corruption when users type special characters (like `|`, newlines, etc.), every field is **Base64-encoded** before being written. This means:
- A pipe `|` inside user input will never break the delimiter.
- Newlines and special characters are safely handled.
- Legacy data (without encoding) is gracefully handled on read.

### Date Format

All dates are validated and stored in **YYYY-MM-DD** (ISO 8601) format:
- ✅ Sortable
- ✅ Internationally unambiguous
- ✅ Human-readable

---

## Design Highlights

| Aspect | Details |
|---|---|
| **Color Palette** | Deep navy (#0F172A, #1E293B) with soft white (#F1F5F9) text |
| **Accent Colors** | Blue, Green, Orange, Teal, Purple — one per module |
| **Fonts** | Segoe UI for a clean, modern look |
| **Buttons** | Custom-painted rounded buttons with hover effects |
| **Cards** | Rounded card panels with subtle depth |
| **Style** | Flat design aesthetic — no 3D effects |

---

## Technologies Used

| Technology | Purpose |
|---|---|
| **Java 8+** | Core programming language |
| **Java Swing** | GUI framework (no external UI libraries) |
| **Text Files** | Simple file-based data persistence |
| **Base64** | Safe field encoding for storage |
| **jpackage** | Native `.exe` packaging (JDK 14+) |

---

## Authors

**Created by Saad Khan and Zahid Ullah** as a university semester project.

---

*PDMS v1.0*
