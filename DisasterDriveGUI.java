import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * DisasterDriveGUI.java
 * A comprehensive Java Swing GUI application for "Disaster Drive".
 * Simulated backup, recovery, monitoring, and profile management.
 */
public class DisasterDriveGUI extends JFrame {

    // Color constants
    private static final Color PRIMARY_BLUE = new Color(52, 152, 219);
    private static final Color LIGHT_BLUE = new Color(236, 245, 255);
    private static final Color DARK_GREY = new Color(44, 62, 80);
    private static final Color SUCCESS_GREEN = new Color(46, 204, 113);
    private static final Color WARNING_ORANGE = new Color(230, 126, 34);
    private static final Color ERROR_RED = new Color(231, 76, 60);
    private static final Color SUB_TEXT = new Color(127, 140, 141);

    // Font constants
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 18);
    private static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font SMALL_FONT = new Font("SansSerif", Font.ITALIC, 12);

    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel backupPanel, recoveryPanel, monitoringPanel, profilePanel;
    private JTextArea backupLog, recoveryLog, monitoringLog;
    private JProgressBar backupProgress, cpuProgress, diskProgress, memoryProgress;
    private JButton backupButton, simulateRecoveryButton, refreshMonitoringButton, saveProfileButton, loadProfileButton;
    private JTextField usernameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    // Data structures (simulated)
    private List<String> backupHistory = new ArrayList<>();
    private Map<String, Double> systemMetrics = new HashMap<>();
    private Properties userProfile = new Properties();

    /**
     * Constructor
     */
    public DisasterDriveGUI() {
        super("Disaster Drive: A Comprehensive System for Resilient Data Protection and Recovery");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        initializeComponents();
        setupLayout();
        setupListeners();
        updateMonitoringMetrics(); // Initial metrics

        setVisible(true);
    }

    /**
     * Initialize UI components
     */
    private void initializeComponents() {
        // Logs
        backupLog = createLogTextArea();
        recoveryLog = createLogTextArea();
        monitoringLog = createLogTextArea();

        // Buttons
        backupButton = createStyledButton("Initiate Backup", PRIMARY_BLUE);
        simulateRecoveryButton = createStyledButton("Simulate Recovery", PRIMARY_BLUE);
        refreshMonitoringButton = createStyledButton("Refresh Health Check", PRIMARY_BLUE);
        saveProfileButton = createStyledButton("Save Profile", SUCCESS_GREEN);
        loadProfileButton = createStyledButton("Load Profile", WARNING_ORANGE);

        // Progress bars
        backupProgress = new JProgressBar(0, 100);
        backupProgress.setStringPainted(true);
        backupProgress.setForeground(SUCCESS_GREEN);
        backupProgress.setValue(0);
        backupProgress.setPreferredSize(new Dimension(400, 20));

        cpuProgress = createProgressBar("CPU Usage", 0);
        diskProgress = createProgressBar("Disk Space", 0);
        memoryProgress = createProgressBar("Memory Usage", 0);

        // Profile fields
        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        roleComboBox = new JComboBox<>(new String[]{"Admin", "User", "Manager"});

        // Simulated data
        systemMetrics.put("CPU", 45.0);
        systemMetrics.put("Disk", 70.0);
        systemMetrics.put("Memory", 55.0);
        userProfile.setProperty("username", "demoUser");
        userProfile.setProperty("email", "demo@example.com");
        userProfile.setProperty("role", "User");
    }

    /**
     * Setup main layout
     */
    private void setupLayout() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Disaster Drive - Resilient Data Protection", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(DARK_GREY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        contentPane.add(titleLabel, BorderLayout.NORTH);

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(HEADER_FONT);
        tabbedPane.setBackground(LIGHT_BLUE);
        tabbedPane.setForeground(DARK_GREY);

        backupPanel = createBackupTab();
        recoveryPanel = createRecoveryTab();
        monitoringPanel = createMonitoringTab();
        profilePanel = createProfileTab();

        tabbedPane.addTab("🔄 Data Backup", backupPanel);
        tabbedPane.addTab("💾 Recovery Simulation", recoveryPanel);
        tabbedPane.addTab("📊 System Health", monitoringPanel);
        tabbedPane.addTab("👤 User Profile", profilePanel);

        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Footer
        JLabel statusLabel = new JLabel("Status: System Ready | Last Backup: N/A", SwingConstants.CENTER);
        statusLabel.setFont(SMALL_FONT);
        statusLabel.setForeground(SUB_TEXT);
        statusLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, DARK_GREY));
        contentPane.add(statusLabel, BorderLayout.SOUTH);
    }

    /**
     * Setup listeners
     */
    private void setupListeners() {
        backupButton.addActionListener(e -> simulateBackup());
        simulateRecoveryButton.addActionListener(e -> simulateRecovery());
        refreshMonitoringButton.addActionListener(e -> updateMonitoringMetrics());
        saveProfileButton.addActionListener(e -> saveUserProfile());
        loadProfileButton.addActionListener(e -> loadUserProfile());

        tabbedPane.addChangeListener(e -> {
            int selected = tabbedPane.getSelectedIndex();
            String tabName = tabbedPane.getTitleAt(selected);
            appendToLog(monitoringLog, "Switched to tab: " + tabName + " at " + new Date());
        });
    }

    /**
     * Backup Tab
     */
    private JPanel createBackupTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(LIGHT_BLUE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel header = new JLabel("Initiate Secure Data Backup", SwingConstants.CENTER);
        header.setFont(HEADER_FONT);
        header.setForeground(DARK_GREY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(header, gbc);

        JButton selectFilesButton = createStyledButton("Select Files/Folders", PRIMARY_BLUE);
        selectFilesButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                appendToLog(backupLog, "Selected: " + chooser.getSelectedFile().getName());
            }
        });

        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(selectFilesButton, gbc);
        gbc.gridx = 1;
        panel.add(backupButton, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(backupProgress, gbc);

        gbc.gridy = 3; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        JScrollPane logScroll = new JScrollPane(backupLog);
        logScroll.setPreferredSize(new Dimension(800, 200));
        logScroll.setBorder(new TitledBorder("Backup Log"));
        panel.add(logScroll, gbc);

        return panel;
    }

    /**
     * Recovery Tab
     */
    private JPanel createRecoveryTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(LIGHT_BLUE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel header = new JLabel("Simulate Data Recovery Process", SwingConstants.CENTER);
        header.setFont(HEADER_FONT);
        header.setForeground(DARK_GREY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(header, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(simulateRecoveryButton, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        JScrollPane logScroll = new JScrollPane(recoveryLog);
        logScroll.setPreferredSize(new Dimension(800, 300));
        logScroll.setBorder(new TitledBorder("Recovery Simulation Log"));
        panel.add(logScroll, gbc);

        return panel;
    }

    /**
     * Monitoring Tab
     */
    private JPanel createMonitoringTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(LIGHT_BLUE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel header = new JLabel("Real-Time System Health Monitoring", SwingConstants.CENTER);
        header.setFont(HEADER_FONT);
        header.setForeground(DARK_GREY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(header, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(refreshMonitoringButton, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(cpuProgress, gbc);

        gbc.gridy = 3;
        panel.add(diskProgress, gbc);

        gbc.gridy = 4;
        panel.add(memoryProgress, gbc);

        gbc.gridy = 5; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        JScrollPane logScroll = new JScrollPane(monitoringLog);
        logScroll.setPreferredSize(new Dimension(800, 150));
        logScroll.setBorder(new TitledBorder("Health Alerts Log"));
        panel.add(logScroll, gbc);

        return panel;
    }

    /**
     * Profile Tab
     */
    private JPanel createProfileTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(LIGHT_BLUE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel header = new JLabel("Manage User Profile", SwingConstants.CENTER);
        header.setFont(HEADER_FONT);
        header.setForeground(DARK_GREY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(header, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        panel.add(roleComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(saveProfileButton, gbc);
        gbc.gridx = 1;
        panel.add(loadProfileButton, gbc);

        loadUserProfile();

        return panel;
    }

    // --- Utility Methods ---

    private JTextArea createLogTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(SMALL_FONT);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(Color.WHITE);
        return area;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(BODY_FONT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 35));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(bgColor.darker()); }
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
        });
        return button;
    }

    private JProgressBar createProgressBar(String label, int initialValue) {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(initialValue);
        bar.setStringPainted(true);
        bar.setPreferredSize(new Dimension(400, 20));
        bar.setForeground(SUCCESS_GREEN);
        bar.setBackground(LIGHT_BLUE);
        return bar;
    }

    private void appendToLog(JTextArea logArea, String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    // --- Simulations ---
    private void simulateBackup() {
        appendToLog(backupLog, "Backup started...");
        backupProgress.setValue(0);

        javax.swing.Timer timer = new javax.swing.Timer(300, new ActionListener() {
            int progress = 0;
            public void actionPerformed(ActionEvent e) {
                progress += 10;
                backupProgress.setValue(progress);
                if (progress >= 100) {
                    ((javax.swing.Timer)e.getSource()).stop();
                    appendToLog(backupLog, "Backup completed successfully!");
                }
            }
        });
        timer.start();
    }

    private void simulateRecovery() {
        appendToLog(recoveryLog, "Recovery simulation started...");
        appendToLog(recoveryLog, "Step 1: Verifying files...");
        appendToLog(recoveryLog, "Step 2: Restoring data...");
        appendToLog(recoveryLog, "Step 3: Finalizing...");
        appendToLog(recoveryLog, "Recovery completed successfully.");
    }

    private void updateMonitoringMetrics() {
        Random rand = new Random();
        cpuProgress.setValue(rand.nextInt(100));
        diskProgress.setValue(rand.nextInt(100));
        memoryProgress.setValue(rand.nextInt(100));
        appendToLog(monitoringLog, "Metrics updated at " + new Date());
    }

    private void saveUserProfile() {
        userProfile.setProperty("username", usernameField.getText());
        userProfile.setProperty("email", emailField.getText());
        userProfile.setProperty("role", roleComboBox.getSelectedItem().toString());
        appendToLog(monitoringLog, "Profile saved.");
    }

    private void loadUserProfile() {
        usernameField.setText(userProfile.getProperty("username", ""));
        emailField.setText(userProfile.getProperty("email", ""));
        roleComboBox.setSelectedItem(userProfile.getProperty("role", "User"));
        appendToLog(monitoringLog, "Profile loaded.");
    }
        /**
     * Main
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DisasterDriveGUI());
    }
}