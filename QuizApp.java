import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;

/**
 * Simple Quiz Application
 * ------------------------
 * Demonstrates:
 *  - OOP (Question class encapsulates data)
 *  - Collections (ArrayList/List to store questions, Collections.shuffle for randomization)
 *  - Swing GUI for user interaction (subject + question-count selection screen, then the quiz)
 *  - Score calculation and final display
 *
 * Questions cover multiple education topics: Math, Science, History,
 * Geography, Literature, and Computer Science. The user first picks a
 * subject (or "All Subjects") and how many questions to be asked
 * (capped at 25 per subject, or per attempt for "All Subjects").
 * Questions and their answer options are then randomized.
 */
public class QuizApp extends JFrame {

    // ---------- Question model (OOP) ----------
    static class Question {
        private final String topic;
        private final String questionText;
        private final List<String> options;      // mutable so it can be shuffled
        private final String correctAnswerText;   // track by text, not index, since options get shuffled

        public Question(String topic, String questionText, String[] options, int correctOptionIndex) {
            this.topic = topic;
            this.questionText = questionText;
            this.options = new ArrayList<>(Arrays.asList(options));
            this.correctAnswerText = options[correctOptionIndex];
        }

        public String getTopic() {
            return topic;
        }

        public String getQuestionText() {
            return questionText;
        }

        public List<String> getOptions() {
            return options;
        }

        public void shuffleOptions() {
            Collections.shuffle(options);
        }

        public boolean isCorrect(String selectedText) {
            return correctAnswerText.equals(selectedText);
        }

        public String getCorrectAnswerText() {
            return correctAnswerText;
        }
    }

    private static final String ALL_SUBJECTS = "All Subjects";

    // Hard limit: no test can ever contain more than this many questions per subject
    private static final int MAX_QUESTIONS_PER_SUBJECT = 25;
    private static final int DEFAULT_QUESTION_COUNT = 10;

    // ---------- Quiz data & state ----------
    private final List<Question> allQuestions = new ArrayList<>();   // full question bank
    private List<Question> quizQuestions = new ArrayList<>();        // randomized subset/order used per attempt
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String selectedSubject = ALL_SUBJECTS;

    // ---------- Layout / card switching ----------
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardContainer = new JPanel();
    private static final String CARD_SELECT = "select";
    private static final String CARD_QUIZ = "quiz";

    // ---------- Selection screen components ----------
    private ButtonGroup subjectGroup;
    private JSpinner questionCountSpinner;
    private JLabel availabilityLabel;

    // ---------- Quiz screen components ----------
    private JLabel questionNumberLabel;
    private JLabel topicLabel;
    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionsGroup;
    private JButton nextButton;
    private JLabel feedbackLabel;
    private JProgressBar progressBar;

    public QuizApp() {
        loadQuestionBank();
        initUI();
        setContentPane(cardContainer);
    }

    // ---------- Populate the question bank (multiple education topics) ----------
    private void loadQuestionBank() {
        // ----- Math -----
        allQuestions.add(new Question("Math",
                "What is the value of 12 x 8?",
                new String[]{"96", "108", "86", "112"}, 0));
        allQuestions.add(new Question("Math",
                "What is the square root of 144?",
                new String[]{"11", "12", "13", "14"}, 1));
        allQuestions.add(new Question("Math",
                "What is the value of Pi (rounded to 2 decimal places)?",
                new String[]{"3.14", "3.41", "3.12", "3.16"}, 0));
        allQuestions.add(new Question("Math",
                "Solve: 15 + 27 x 2",
                new String[]{"84", "69", "42", "57"}, 1));

        // ----- Science -----
        allQuestions.add(new Question("Science",
                "What is the chemical symbol for water?",
                new String[]{"H2O", "O2", "CO2", "HO2"}, 0));
        allQuestions.add(new Question("Science",
                "Which planet is known as the Red Planet?",
                new String[]{"Venus", "Jupiter", "Mars", "Saturn"}, 2));
        allQuestions.add(new Question("Science",
                "What gas do plants absorb from the atmosphere for photosynthesis?",
                new String[]{"Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"}, 2));
        allQuestions.add(new Question("Science",
                "What is the powerhouse of the cell?",
                new String[]{"Nucleus", "Ribosome", "Mitochondria", "Golgi Body"}, 2));
        allQuestions.add(new Question("Science",
                "What force pulls objects toward the Earth?",
                new String[]{"Magnetism", "Gravity", "Friction", "Tension"}, 1));

        // ----- History -----
        allQuestions.add(new Question("History",
                "In which year did World War II end?",
                new String[]{"1943", "1945", "1947", "1950"}, 1));
        allQuestions.add(new Question("History",
                "Who was the first President of the United States?",
                new String[]{"Thomas Jefferson", "Abraham Lincoln", "George Washington", "John Adams"}, 2));
        allQuestions.add(new Question("History",
                "The ancient pyramids of Giza are located in which country?",
                new String[]{"Mexico", "Egypt", "India", "Greece"}, 1));
        allQuestions.add(new Question("History",
                "Which empire built the Colosseum?",
                new String[]{"Greek Empire", "Ottoman Empire", "Roman Empire", "British Empire"}, 2));

        // ----- Geography -----
        allQuestions.add(new Question("Geography",
                "What is the capital of France?",
                new String[]{"Berlin", "Madrid", "Paris", "Rome"}, 2));
        allQuestions.add(new Question("Geography",
                "Which is the largest ocean on Earth?",
                new String[]{"Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Pacific Ocean"}, 3));
        allQuestions.add(new Question("Geography",
                "Which continent is the Sahara Desert located on?",
                new String[]{"Asia", "Africa", "Australia", "South America"}, 1));
        allQuestions.add(new Question("Geography",
                "What is the longest river in the world?",
                new String[]{"Amazon River", "Nile River", "Yangtze River", "Mississippi River"}, 1));

        // ----- Literature -----
        allQuestions.add(new Question("Literature",
                "Who wrote the play 'Romeo and Juliet'?",
                new String[]{"Charles Dickens", "William Shakespeare", "Mark Twain", "Jane Austen"}, 1));
        allQuestions.add(new Question("Literature",
                "Which novel begins with 'Call me Ishmael'?",
                new String[]{"Moby Dick", "Great Expectations", "War and Peace", "Don Quixote"}, 0));
        allQuestions.add(new Question("Literature",
                "Who wrote 'Pride and Prejudice'?",
                new String[]{"Emily Bronte", "Jane Austen", "Virginia Woolf", "Mary Shelley"}, 1));

        // ----- Computer Science -----
        allQuestions.add(new Question("Computer Science",
                "Which language is primarily used for Android app development?",
                new String[]{"Swift", "Kotlin/Java", "Python", "C#"}, 1));
        allQuestions.add(new Question("Computer Science",
                "Which Java collection allows key-value pairs?",
                new String[]{"ArrayList", "LinkedList", "HashMap", "HashSet"}, 2));
        allQuestions.add(new Question("Computer Science",
                "What does OOP stand for?",
                new String[]{"Object Oriented Programming", "Order Of Precedence",
                        "Open Object Protocol", "Output Oriented Process"}, 0));
        allQuestions.add(new Question("Computer Science",
                "Which keyword is used to inherit a class in Java?",
                new String[]{"implements", "extends", "inherits", "instanceof"}, 1));
        allQuestions.add(new Question("Computer Science",
                "What does CPU stand for?",
                new String[]{"Central Process Unit", "Central Processing Unit",
                        "Computer Personal Unit", "Central Processor Utility"}, 1));
    }

    // ---------- Derive the list of distinct subjects from the question bank ----------
    private List<String> getSubjects() {
        Set<String> subjects = new LinkedHashSet<>(); // preserves insertion order, no duplicates
        for (Question q : allQuestions) {
            subjects.add(q.getTopic());
        }
        List<String> result = new ArrayList<>();
        result.add(ALL_SUBJECTS);
        result.addAll(subjects);
        return result;
    }

    // ---------- How many questions exist for a given subject (capped at the hard limit) ----------
    private int countAvailableForSubject(String subject) {
        int count = 0;
        for (Question q : allQuestions) {
            if (subject.equals(ALL_SUBJECTS) || q.getTopic().equals(subject)) {
                count++;
            }
        }
        return Math.min(count, MAX_QUESTIONS_PER_SUBJECT);
    }

    // ---------- Build both screens ----------
    private void initUI() {
        setTitle("Java Quiz Application - Multi-Topic Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(580, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        cardContainer.setLayout(cardLayout);
        cardContainer.add(buildSelectionPanel(), CARD_SELECT);
        cardContainer.add(buildQuizPanel(), CARD_QUIZ);
    }

    // ---------- Subject + question-count selection screen ----------
    private JPanel buildSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("Set Up Your Quiz");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel("Pick a subject and how many questions you want (max 25 per subject).");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(90, 90, 90));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(title);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        headerPanel.add(subtitle);

        // ----- Subject radio buttons -----
        JPanel subjectPanel = new JPanel();
        subjectPanel.setLayout(new BoxLayout(subjectPanel, BoxLayout.Y_AXIS));
        subjectPanel.setBorder(BorderFactory.createEmptyBorder(15, 60, 10, 60));

        subjectGroup = new ButtonGroup();
        List<String> subjects = getSubjects();
        for (int i = 0; i < subjects.size(); i++) {
            String subject = subjects.get(i);
            JRadioButton radio = new JRadioButton(subject);
            radio.setFont(new Font("SansSerif", Font.PLAIN, 15));
            radio.setActionCommand(subject);
            radio.setAlignmentX(Component.LEFT_ALIGNMENT);
            if (i == 0) {
                radio.setSelected(true); // default to "All Subjects"
            }
            radio.addActionListener(e -> onSubjectChanged());
            subjectGroup.add(radio);
            subjectPanel.add(radio);
            subjectPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        }

        // ----- Question count control -----
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel countLabel = new JLabel("Number of questions:");
        countLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        int initialMax = countAvailableForSubject(ALL_SUBJECTS);
        int initialValue = Math.min(DEFAULT_QUESTION_COUNT, initialMax);
        questionCountSpinner = new JSpinner(new SpinnerNumberModel(initialValue, 1, initialMax, 1));
        questionCountSpinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
        questionCountSpinner.setPreferredSize(new Dimension(60, 26));

        countPanel.add(countLabel);
        countPanel.add(questionCountSpinner);

        availabilityLabel = new JLabel();
        availabilityLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        availabilityLabel.setForeground(new Color(110, 110, 110));
        availabilityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateAvailabilityLabel(initialMax);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        subjectPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        countPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        availabilityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subjectPanel);
        centerPanel.add(countPanel);
        centerPanel.add(availabilityLabel);

        JButton startButton = new JButton("Start Quiz");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        startButton.addActionListener(this::onStartClicked);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(startButton);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ---------- Keep the spinner's range in sync with the chosen subject's question count ----------
    private void onSubjectChanged() {
        String chosen = subjectGroup.getSelection() != null
                ? subjectGroup.getSelection().getActionCommand()
                : ALL_SUBJECTS;

        int max = countAvailableForSubject(chosen);
        int currentValue = (Integer) questionCountSpinner.getValue();
        int newValue = Math.min(currentValue, max);

        questionCountSpinner.setModel(new SpinnerNumberModel(newValue, 1, max, 1));
        updateAvailabilityLabel(max);
    }

    private void updateAvailabilityLabel(int max) {
        availabilityLabel.setText("(" + max + " question" + (max == 1 ? "" : "s") + " available for this subject, capped at "
                + MAX_QUESTIONS_PER_SUBJECT + ")");
    }

    private void onStartClicked(ActionEvent e) {
        String chosen = subjectGroup.getSelection() != null
                ? subjectGroup.getSelection().getActionCommand()
                : ALL_SUBJECTS;
        selectedSubject = chosen;

        int requestedCount = (Integer) questionCountSpinner.getValue();
        // Hard enforcement of the 25-per-subject limit regardless of spinner state
        requestedCount = Math.min(requestedCount, MAX_QUESTIONS_PER_SUBJECT);

        startNewQuiz(requestedCount);
        cardLayout.show(cardContainer, CARD_QUIZ);
    }

    // ---------- Build a randomized quiz from the (filtered) question bank ----------
    private void startNewQuiz(int questionCount) {
        List<Question> filteredBank = new ArrayList<>();
        for (Question q : allQuestions) {
            if (selectedSubject.equals(ALL_SUBJECTS) || q.getTopic().equals(selectedSubject)) {
                filteredBank.add(q);
            }
        }

        Collections.shuffle(filteredBank); // randomize question order

        int cappedCount = Math.min(questionCount, MAX_QUESTIONS_PER_SUBJECT);
        int count = Math.min(cappedCount, filteredBank.size());
        quizQuestions = new ArrayList<>(filteredBank.subList(0, count));

        // Randomize option order within each selected question too
        for (Question q : quizQuestions) {
            q.shuffleOptions();
        }

        progressBar.setMaximum(quizQuestions.size());
        currentQuestionIndex = 0;
        score = 0;
        showQuestion();
    }

    // Re-run the quiz with the same subject and the same number of questions as last time
    private void startNewQuiz() {
        startNewQuiz(quizQuestions.isEmpty() ? DEFAULT_QUESTION_COUNT : quizQuestions.size());
    }

    // ---------- Quiz screen ----------
    private JPanel buildQuizPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top: progress bar + question number + topic
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel topLabels = new JPanel(new GridLayout(2, 1));
        questionNumberLabel = new JLabel();
        questionNumberLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        topicLabel = new JLabel();
        topicLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        topicLabel.setForeground(new Color(80, 80, 80));
        topLabels.add(questionNumberLabel);
        topLabels.add(topicLabel);

        progressBar = new JProgressBar(0, DEFAULT_QUESTION_COUNT);
        progressBar.setStringPainted(true);

        topPanel.add(topLabels, BorderLayout.NORTH);
        topPanel.add(progressBar, BorderLayout.SOUTH);

        // Center: question + options
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(questionLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        optionButtons = new JRadioButton[4];
        optionsGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("SansSerif", Font.PLAIN, 14));
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            optionsGroup.add(optionButtons[i]);
            centerPanel.add(optionButtons[i]);
            centerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(feedbackLabel);

        // Bottom: change subject + next button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton changeSubjectButton = new JButton("Change Subject / Count");
        changeSubjectButton.addActionListener(e -> cardLayout.show(cardContainer, CARD_SELECT));

        JPanel leftBottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftBottom.add(changeSubjectButton);

        nextButton = new JButton("Submit Answer");
        nextButton.addActionListener(this::onNextClicked);
        JPanel rightBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightBottom.add(nextButton);

        bottomPanel.add(leftBottom, BorderLayout.WEST);
        bottomPanel.add(rightBottom, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    // ---------- Display current question ----------
    private void showQuestion() {
        Question q = quizQuestions.get(currentQuestionIndex);

        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + quizQuestions.size());
        topicLabel.setText("Subject: " + selectedSubject + "   |   Topic: " + q.getTopic());
        progressBar.setValue(currentQuestionIndex);
        questionLabel.setText("<html><body style='width: 490px'>" + q.getQuestionText() + "</body></html>");

        List<String> options = q.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options.get(i));
            optionButtons[i].setSelected(false);
            optionButtons[i].setEnabled(true);
        }
        optionsGroup.clearSelection();

        feedbackLabel.setText(" ");
        nextButton.setText("Submit Answer");
    }

    // ---------- Handle button click (Submit vs Next vs Finish) ----------
    private void onNextClicked(ActionEvent e) {
        // Phase 1: user needs to submit an answer first
        if (nextButton.getText().equals("Submit Answer")) {
            String selectedText = getSelectedOptionText();
            if (selectedText == null) {
                JOptionPane.showMessageDialog(this,
                        "Please select an answer before continuing.",
                        "No Answer Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Question q = quizQuestions.get(currentQuestionIndex);
            boolean correct = q.isCorrect(selectedText);
            if (correct) {
                score++;
                feedbackLabel.setForeground(new Color(0, 128, 0));
                feedbackLabel.setText("Correct!");
            } else {
                feedbackLabel.setForeground(Color.RED);
                feedbackLabel.setText("Incorrect. Correct answer: " + q.getCorrectAnswerText());
            }

            // lock in the answer
            for (JRadioButton btn : optionButtons) {
                btn.setEnabled(false);
            }

            boolean isLastQuestion = (currentQuestionIndex == quizQuestions.size() - 1);
            nextButton.setText(isLastQuestion ? "Finish Quiz" : "Next Question");
            return;
        }

        // Phase 2: move to next question or finish
        currentQuestionIndex++;
        if (currentQuestionIndex < quizQuestions.size()) {
            showQuestion();
        } else {
            progressBar.setValue(quizQuestions.size());
            showFinalScore();
        }
    }

    private String getSelectedOptionText() {
        for (JRadioButton btn : optionButtons) {
            if (btn.isSelected()) {
                return btn.getText();
            }
        }
        return null;
    }

    // ---------- Final score screen ----------
    private void showFinalScore() {
        double percentage = quizQuestions.isEmpty() ? 0 : (score * 100.0) / quizQuestions.size();
        String message = String.format(
                "Quiz Completed! (%s - %d questions)\n\nYour Score: %d / %d (%.1f%%)",
                selectedSubject, quizQuestions.size(), score, quizQuestions.size(), percentage);

        Object[] optionsArray = {"Play Again", "Change Subject / Count", "Exit"};
        int choice = JOptionPane.showOptionDialog(
                this,
                message,
                "Final Score",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                optionsArray,
                optionsArray[0]);

        if (choice == 0) {
            startNewQuiz(); // same subject and question count, re-randomized
        } else if (choice == 1) {
            cardLayout.show(cardContainer, CARD_SELECT); // back to setup screen
        } else {
            System.exit(0);
        }
    }

    // ---------- Entry point ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuizApp app = new QuizApp();
            app.setVisible(true);
        });
    }
}