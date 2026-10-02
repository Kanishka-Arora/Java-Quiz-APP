# 🧠 Java Quiz Application (Multi-Topic Edition)

A lightweight, interactive desktop Quiz Application built with **Java** and **Java Swing**. The application tests knowledge across multiple academic subjects with dynamic question selection, randomized questions and answer choices, real-time score tracking, and instant feedback.

---

## ✨ Features

- 🎯 **Multi-Subject Support**: Choose from individual topics or test across all subjects:
  - 🔢 **Math**
  - 🔬 **Science**
  - 🏛️ **History**
  - 🌍 **Geography**
  - 📚 **Literature**
  - 💻 **Computer Science**
- ⚙️ **Customizable Quiz Setup**:
  - Select your preferred subject (or **All Subjects**).
  - Customize the number of questions per attempt (up to 25 questions).
  - Dynamic display showing how many questions are available for each subject.
- 🔀 **Smart Randomization**:
  - Questions are randomly selected and shuffled for every quiz attempt.
  - Option choices are shuffled each round to prevent pattern memorization.
- 📊 **Real-time Quiz Experience**:
  - Live progress bar visualizer.
  - Question and topic counters.
  - Instant answer validation and feedback (displays the correct answer if wrong).
  - Radio options lock after submission to ensure integrity.
- 🏆 **Score Summary & Replayability**:
  - Final score report with percentage calculation.
  - Easy navigation: **Play Again** (re-shuffled), **Change Subject / Count**, or **Exit**.

---

## 🛠️ Tech Stack & Concepts Demonstrated

- **Language**: Java (JDK 8 or higher)
- **GUI Framework**: Java Swing (`JFrame`, `CardLayout`, `JRadioButton`, `JProgressBar`, `JSpinner`, `JOptionPane`) & AWT
- **Core Concepts**:
  - **Object-Oriented Programming (OOP)**: Encapsulated `Question` model class.
  - **Java Collections Framework**: `ArrayList`, `List`, `LinkedHashSet`, `Set`.
  - **Randomization Algorithms**: `Collections.shuffle()` for question order and choice variations.
  - **Event-Driven Architecture**: Action listeners for interactive UI state transitions.

---

## 📁 Project Structure

```text
javaquizApp/
├── QuizApp.java     # Main application source code (UI, Question Bank, Game Logic)
└── README.md        # Project documentation
```

---

## 🚀 Getting Started

### Prerequisites

Make sure you have **Java Development Kit (JDK 8 or higher)** installed on your machine.

To verify your Java installation:
```bash
java -version
javac -version
```

### 📥 Clone the Repository

```bash
https://github.com/Kanishka-Arora/Java-Quiz-APP.git
cd Java-Quiz-APP
```

### 🔨 Compile and Run

1. **Compile the Java source code:**
   ```bash
   javac QuizApp.java
   ```

2. **Run the application:**
   ```bash
   java QuizApp
   ```

---

## 🎮 How to Play

1. **Setup Screen**:
   - Select your desired subject or choose *All Subjects*.
   - Adjust the number of questions using the spinner.
   - Click **Start Quiz**.
2. **Quiz Screen**:
   - Read the question and choose one of the 4 options.
   - Click **Submit Answer** to see immediate feedback.
   - Click **Next Question** to proceed.
3. **Score Screen**:
   - Review your final score and percentage.
   - Choose to replay, pick another topic, or quit.

---

## 💡 Future Improvements

- [ ] Add a countdown timer per question.
- [ ] Load questions dynamically from external JSON/CSV or database (SQLite/MySQL).
- [ ] Implement user profiles and local leaderboard / high scores.
- [ ] Add sound effects and custom UI themes (Dark/Light mode).

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to fork the repository and submit a pull request.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
