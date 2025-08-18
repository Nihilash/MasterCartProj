package main;

import dao.ProductDAO;
import dao.UserDAO;
import model.User;
import service.CartService;
import service.ProductService;
import service.UserService;
import ui.LoginUI;
import ui.ProductUI;
import ui.RegisterUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SuperCartApp {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Initialize DAO layer
        UserDAO userDAO = new UserDAO();
        ProductDAO productDAO = new ProductDAO();

        // Initialize Services
        UserService userService = new UserService(userDAO);
        ProductService productService = new ProductService(productDAO);
        CartService cartService = new CartService(); // single shared instance

        SwingUtilities.invokeLater(() -> {
            // Main frame
            JFrame mainFrame = new JFrame("MasterCart - Main Menu");
            mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Panel with background image
            JPanel backgroundPanel = new JPanel() {
                private final Image bgImage;

                {
                    // Try to load from resources inside src/Mastercartimages/
                    URL resource = getClass().getClassLoader()
                            .getResource("Mastercartimages/OpencartMainpage.jpg");
                    if (resource != null) {
                        bgImage = new ImageIcon(resource).getImage();
                    } else {
                        System.err.println("Background image not found in resources!");
                        bgImage = null;
                    }
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (bgImage != null) {
                        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        // fallback background
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                }
            };
            backgroundPanel.setLayout(new GridBagLayout());
            mainFrame.add(backgroundPanel);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(20, 0, 20, 0);
            gbc.gridx = 0;

            // --- Welcome Label ---
            JPanel welcomePanel = new JPanel();
            welcomePanel.setOpaque(false);
            welcomePanel.setLayout(new GridBagLayout());

            JLabel welcomeLabel = new JLabel("Welcome to MasterCart", SwingConstants.CENTER);
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 48));
            welcomeLabel.setForeground(Color.BLACK);

            JPanel labelOverlay = new JPanel();
            labelOverlay.setBackground(new Color(255, 255, 255, 120));
            labelOverlay.setLayout(new GridBagLayout());
            labelOverlay.add(welcomeLabel);
            labelOverlay.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

            welcomePanel.add(labelOverlay);
            gbc.gridy = 0;
            backgroundPanel.add(welcomePanel, gbc);

            // --- Buttons ---
            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(false);
            buttonPanel.setLayout(new GridLayout(1, 3, 50, 0));
            gbc.gridy = 1;

            JButton loginBtn = new JButton("Login");
            styleButton(loginBtn);
            loginBtn.addActionListener(e -> {
                LoginUI loginUI = new LoginUI(userService);
                loginUI.setExtendedState(JFrame.MAXIMIZED_BOTH);
                loginUI.setVisible(true);
                mainFrame.setVisible(false);

                loginUI.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        User loggedInUser = loginUI.getLoggedInUser();
                        if (loggedInUser != null) {
                            //  Pass shared cartService instance to ProductUI
                            ProductUI productUI = new ProductUI(
                                    productService,
                                    cartService,
                                    loggedInUser.getUsername(),
                                    mainFrame
                            );
                            productUI.setExtendedState(JFrame.MAXIMIZED_BOTH);
                            productUI.setVisible(true);
                        } else {
                            mainFrame.setVisible(true);
                        }
                    }
                });
            });

            JButton registerBtn = new JButton("Register");
            styleButton(registerBtn);
            registerBtn.addActionListener(e -> {
                RegisterUI registerUI = new RegisterUI(userService);
                registerUI.setExtendedState(JFrame.MAXIMIZED_BOTH);
                registerUI.setVisible(true);
                mainFrame.setVisible(false);

                registerUI.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        if (registerUI.isRegistered()) {
                            JOptionPane.showMessageDialog(null, "Registration successful! You can now login.");
                        }
                        mainFrame.setVisible(true);
                    }
                });
            });

            JButton exitBtn = new JButton("Exit");
            styleButton(exitBtn);
            exitBtn.addActionListener(e -> System.exit(0));

            // Wrap each button with semi-transparent panel
            buttonPanel.add(wrapWithOverlay(loginBtn));
            buttonPanel.add(wrapWithOverlay(registerBtn));
            buttonPanel.add(wrapWithOverlay(exitBtn));

            backgroundPanel.add(buttonPanel, gbc);

            mainFrame.setVisible(true);
        });
    }

    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setFocusPainted(false);
        button.setBackground(new Color(200, 200, 200));
        button.setForeground(Color.BLACK);
    }

    private static JPanel wrapWithOverlay(JComponent comp) {
        JPanel overlay = new JPanel(new GridBagLayout());
        overlay.setBackground(new Color(255, 255, 255, 120));
        overlay.add(comp);
        overlay.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return overlay;
    }
}
