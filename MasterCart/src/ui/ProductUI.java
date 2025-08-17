package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;

import model.Product;
import service.CartService;
import service.ProductService;

public class ProductUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private final ProductService productService;
    private final CartService cartService;
    private final String username;
    private final JFrame mainMenuFrame;

    private JPanel categoryPanel;
    private JLabel taglineLabel;

    public ProductUI(ProductService productService, CartService cartService, String username, JFrame mainMenuFrame) {
        this.productService = productService;
        this.cartService = cartService;
        this.username = username != null ? username : "guest";
        this.mainMenuFrame = mainMenuFrame;

        setupFrame();
        JPanel mainPanel = setupBackgroundPanel();
        setupTaglineLabel(mainPanel);
        setupCategoryPanel(mainPanel);

        ToolTipManager.sharedInstance().setInitialDelay(200);
        ToolTipManager.sharedInstance().setDismissDelay(5000);

        showCategories();
    }

    private void setupFrame() {
        setTitle("MasterCart - Products (" + username + ")");
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private JPanel setupBackgroundPanel() {
        JPanel backgroundPanel = new JPanel() {
            private Image bgImage = new ImageIcon(
                    getClass().getResource("/Mastercartimages/productpage.jpg")
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel);
        return backgroundPanel;
    }

    private void setupTaglineLabel(JPanel panel) {
        taglineLabel = new JLabel("Hey there " + username + "! Welcome to MasterCart – Browse Categories",
                SwingConstants.CENTER);
        taglineLabel.setFont(new Font("Arial", Font.BOLD, 30));
        taglineLabel.setForeground(Color.BLACK);
        taglineLabel.setOpaque(true);
        taglineLabel.setBackground(new Color(255, 255, 255, 180));
        taglineLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        taglineLabel.setPreferredSize(new Dimension(0, 90));
        panel.add(taglineLabel, BorderLayout.NORTH);
    }

    private void setupCategoryPanel(JPanel panel) {
        categoryPanel = new JPanel();
        categoryPanel.setOpaque(false);
        categoryPanel.setLayout(new BorderLayout());
        panel.add(categoryPanel, BorderLayout.CENTER);
    }

    public void showCategories() {
        categoryPanel.removeAll();

        String[] categories = {"Smartphone", "Laptop", "Earbuds", "Books", "T-Shirt", "Jeans"};

        JPanel cardsPanel = new JPanel();
        cardsPanel.setOpaque(false);
        cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 60));

        for (String cat : categories) {
            JPanel card = createCategoryCard(cat);
            cardsPanel.add(card);
        }

        categoryPanel.add(cardsPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 20));

        JButton goToCartBtn = new JButton("Go to Cart");
        goToCartBtn.setFont(new Font("Arial", Font.BOLD, 20));
        goToCartBtn.addActionListener(e -> {
            CartUI cartUI = new CartUI(cartService, username);
            cartUI.setExtendedState(Frame.MAXIMIZED_BOTH);
            cartUI.setVisible(true);
        });

        JButton mainMenuBtn = new JButton("Go to Main Menu");
        mainMenuBtn.setFont(new Font("Arial", Font.BOLD, 20));
        mainMenuBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainMenuFrame, "You have been logged out.");
            this.dispose();
            mainMenuFrame.setVisible(true);
        });

        bottomPanel.add(goToCartBtn);
        bottomPanel.add(mainMenuBtn);

        categoryPanel.add(bottomPanel, BorderLayout.SOUTH);

        categoryPanel.revalidate();
        categoryPanel.repaint();

        taglineLabel.setText("Hey there " + username + "! Welcome to MasterCart – Browse Categories");
    }

    private JPanel createCategoryCard(String category) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(220, 270));
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(255, 255, 255, 180));
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel imgLabel = new JLabel();
        ImageIcon icon = new ImageIcon(getCategoryImage(category));
        Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
        imgLabel.setIcon(new ImageIcon(img));
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(category);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(Box.createVerticalGlue());
        inner.add(imgLabel);
        inner.add(Box.createVerticalStrut(10));
        inner.add(nameLabel);
        inner.add(Box.createVerticalGlue());

        card.add(inner, BorderLayout.CENTER);
        card.setToolTipText(getCategoryShortDescription(category));

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showProductDetail(category);
            }
        });

        return card;
    }

    private String getCategoryShortDescription(String category) {
        switch (category.toLowerCase()) {
            case "smartphone": return "High-performance smartphone";
            case "laptop": return "Lightweight laptop for work & play";
            case "earbuds": return "True wireless noise-canceling earbuds";
            case "books": return "Beginner-friendly Java programming book";
            case "t-shirt": return "Comfortable cotton T-Shirt";
            case "jeans": return "Stylish slim-fit jeans";
        }
        return "";
    }

    private String getCategoryFullDescription(String category) {
        switch (category.toLowerCase()) {
            case "smartphone": return "The Smartphone XYZ features a 6.5-inch AMOLED display, 128GB storage, 8GB RAM, dual cameras, long-lasting battery, and fast charging support.";
            case "laptop": return "Laptop ABC comes with a 15.6-inch FHD display, Intel i7 processor, 16GB RAM, 512GB SSD, and NVIDIA graphics.";
            case "earbuds": return "Earbuds Pro deliver high-quality sound with active noise cancellation, 24-hour battery life with charging case, and touch controls.";
            case "books": return "This Java Guide covers core concepts of Java programming with examples, exercises, and best practices.";
            case "t-shirt": return "Made from 100% breathable cotton, CoolFit T-Shirt is soft, durable, and available in multiple sizes and colors.";
            case "jeans": return "Classic slim-fit jeans made with stretchable denim for comfort and style. Ideal for daily wear or casual outings.";
        }
        return "";
    }

    private String getCategoryImage(String category) {
        switch (category.toLowerCase()) {
            case "smartphone": return "/Mastercartimages/smartphone.jpg";
            case "laptop": return "/Mastercartimages/laptop.jpg";
            case "earbuds": return "/Mastercartimages/earbuds.jpg";
            case "books": return "/Mastercartimages/books.jpg";
            case "t-shirt": return "/Mastercartimages/tshirt.jpg";
            case "jeans": return "/Mastercartimages/jeans.jpg";
        }
        return "/Mastercartimages/placeholder.jpg";
    }
    private void showProductDetail(String category) {
        categoryPanel.removeAll();

        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        JPanel details = new JPanel();
        details.setOpaque(true);
        details.setBackground(new Color(255, 255, 255, 220));
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        Product product = productService.getProductByCategory(category);

        JLabel nameLabel = new JLabel(category);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 28));
        details.add(nameLabel);

        JLabel descLabel = new JLabel("<html><p style='width:400px; font-size:14px;'>" + getCategoryFullDescription(category) + "</p></html>");
        details.add(Box.createVerticalStrut(10));
        details.add(descLabel);

        JLabel priceLabel = new JLabel("Price: ₹" + (product != null ? product.getPrice() : 0));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 22));
        details.add(Box.createVerticalStrut(10));
        details.add(priceLabel);

        JLabel stockLabel = new JLabel("Available Qty: " + (product != null ? product.getStockQty() : 0));
        stockLabel.setFont(new Font("Arial", Font.BOLD, 22));
        details.add(Box.createVerticalStrut(10));
        details.add(stockLabel);

        // Quantity and Add to Cart
        JPanel qtyPanel = new JPanel();
        qtyPanel.setOpaque(false);

        JButton decBtn = new JButton("-");
        JButton incBtn = new JButton("+");
        JTextField qtyField = new JTextField("1", 3);
        JButton addToCartBtn = new JButton("Add to Cart");

        Font btnFont = new Font("Arial", Font.BOLD, 28);
        decBtn.setFont(btnFont);
        incBtn.setFont(btnFont);
        addToCartBtn.setFont(btnFont);

        Dimension btnSize = new Dimension(80, 60);
        decBtn.setPreferredSize(btnSize);
        incBtn.setPreferredSize(btnSize);
        addToCartBtn.setPreferredSize(new Dimension(180, 60));

        qtyField.setFont(new Font("Arial", Font.BOLD, 28));
        qtyField.setHorizontalAlignment(SwingConstants.CENTER);
        qtyField.setPreferredSize(new Dimension(80, 60));

        decBtn.addActionListener(e -> {
            int val = Integer.parseInt(qtyField.getText());
            if (val > 1) {
				qtyField.setText(String.valueOf(val - 1));
			}
        });
        incBtn.addActionListener(e -> {
            int val = Integer.parseInt(qtyField.getText());
            qtyField.setText(String.valueOf(val + 1));
        });

        addToCartBtn.addActionListener(e -> {
            int quantity = Integer.parseInt(qtyField.getText());
            if (product != null) {
                cartService.addToCart(product, quantity);
                JOptionPane.showMessageDialog(this, quantity + " x " + product.getName() + " added to cart.");
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        qtyPanel.add(decBtn);
        qtyPanel.add(qtyField);
        qtyPanel.add(incBtn);
        qtyPanel.add(addToCartBtn);

        details.add(Box.createVerticalStrut(10));
        details.add(qtyPanel);

        JButton backBtn = new JButton("Return to Categories");
        backBtn.setFont(new Font("Arial", Font.BOLD, 24));
        backBtn.addActionListener(e -> showCategories());
        details.add(Box.createVerticalStrut(10));
        details.add(backBtn);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        detailPanel.add(details, gbc);

        JLabel imgLabel = new JLabel();
        ImageIcon icon = new ImageIcon(getCategoryImage(category));
        Image img = icon.getImage().getScaledInstance(450, 350, Image.SCALE_SMOOTH);
        imgLabel.setIcon(new ImageIcon(img));
        gbc.gridx = 1;
        gbc.gridy = 0;
        detailPanel.add(imgLabel, gbc);

        categoryPanel.add(detailPanel);
        categoryPanel.revalidate();
        categoryPanel.repaint();

        taglineLabel.setText(category + " - Product Details");
    }
}

