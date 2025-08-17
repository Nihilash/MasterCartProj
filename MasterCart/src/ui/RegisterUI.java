package ui;

import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;

public class RegisterUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private final UserService userService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField mobileField;
    private JComboBox<String> genderBox;
    private JTextField ageField;
    private JButton registerBtn;

    private boolean registered = false; // registration status

    public RegisterUI(UserService userService) {
        this.userService = userService;
        setTitle("MasterCart - Register");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Background panel
        JPanel backgroundPanel = new JPanel() {
            private final Image bgImage = new ImageIcon(
                    "D:\\MaterCartWorkSpace\\MasterCart\\MasterCartimages\\registrationpage.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // --- Welcome Label ---
        JLabel welcomeLabel = new JLabel("Welcome to MasterCart Registration", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        welcomeLabel.setForeground(Color.BLACK);

        // Overlay panel wrapping fields and label
        JPanel overlayPanel = new JPanel(new GridBagLayout());
        overlayPanel.setBackground(new Color(255, 255, 255, 120)); // semi-transparent
        overlayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        overlayPanel.setOpaque(true);

        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(10, 10, 10, 10);
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;

        // Add welcome label
        overlayPanel.add(welcomeLabel, innerGbc);

        // Input fields panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints inGbc = new GridBagConstraints();
        inGbc.insets = new Insets(8, 8, 8, 8);
        inGbc.gridx = 0;
        inGbc.gridy = 0;
        inGbc.anchor = GridBagConstraints.WEST;

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(usernameLabel, inGbc);
        inGbc.gridx = 1;
        inputPanel.add(usernameField, inGbc);

        // Password
        inGbc.gridx = 0; inGbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(passwordLabel, inGbc);
        inGbc.gridx = 1;
        inputPanel.add(passwordField, inGbc);

        // Confirm Password
        inGbc.gridx = 0; inGbc.gridy++;
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 18));
        confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(confirmLabel, inGbc);
        inGbc.gridx = 1;
        inputPanel.add(confirmPasswordField, inGbc);

        // Mobile
        inGbc.gridx = 0; inGbc.gridy++;
        JLabel mobileLabel = new JLabel("Mobile:");
        mobileLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mobileField = new JTextField(15);
        mobileField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(mobileLabel, inGbc);
        inGbc.gridx = 1;
        inputPanel.add(mobileField, inGbc);

        // Gender (Dropdown instead of free text)
        inGbc.gridx = 0; inGbc.gridy++;
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("Arial", Font.BOLD, 18));
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderBox.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(genderLabel, inGbc);
        inGbc.gridx = 1;
        inputPanel.add(genderBox, inGbc);

        // Age
        inGbc.gridx = 0; inGbc.gridy++;
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        ageField = new JTextField(15);
        ageField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(ageLabel, inGbc);
        inGbc.gridx = 1;
        inputPanel.add(ageField, inGbc);

        // Register Button
        inGbc.gridx = 0; inGbc.gridy++;
        inGbc.gridwidth = 2;
        inGbc.anchor = GridBagConstraints.CENTER;
        registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Arial", Font.BOLD, 18));
        registerBtn.setBackground(new Color(100, 200, 255));
        registerBtn.setForeground(Color.BLACK);
        registerBtn.setFocusPainted(false);
        inputPanel.add(registerBtn, inGbc);

        // Add input panel below welcome
        innerGbc.gridy = 1;
        overlayPanel.add(inputPanel, innerGbc);

        // Add overlay to background
        backgroundPanel.add(overlayPanel, gbc);

        // Register action
        registerBtn.addActionListener(e -> handleRegister());
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());
        String mobile = mobileField.getText().trim();
        String gender = (String) genderBox.getSelectedItem();
        String ageText = ageField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()
                || mobile.isEmpty() || gender.isEmpty() || ageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!mobile.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Enter a valid 10-digit mobile number.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid age.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User newUser = new User(0, username, mobile, password);
        boolean ok = userService.register(newUser);

        if (ok) {
            registered = true;
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isRegistered() {
        return registered;
    }
}
