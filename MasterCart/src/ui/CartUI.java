package ui;

import model.CartItem;
import service.CartService;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class CartUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private final CartService cartService;
    private final String username;

    private JPanel mainPanel;
    private JLabel totalLabel;
    private JTable cartTable;
    private CartTableModel tableModel;
    private Image bgImage;

    // Guard flag to prevent double checkout
    private boolean isCheckingOut = false;

    public CartUI(CartService cartService, String username) {
        this.cartService = cartService;
        this.username = username;

        setTitle("Your Cart - " + username);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loadBackgroundImage();
        setupBackgroundPanel();
        setupCartUI();
        refreshCart();
    }

    /** Load background image from resources */
    private void loadBackgroundImage() {
        try {
            URL imageUrl = getClass().getResource("/MasterCartimages/cartpage.jpg");
            if (imageUrl != null) {
                bgImage = new ImageIcon(imageUrl).getImage();
            } else {
                System.err.println("⚠ Cart background image not found in resources!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupBackgroundPanel() {
        mainPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        add(mainPanel);
    }

    private void setupCartUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        JPanel cartBox = new JPanel(new BorderLayout());
        cartBox.setOpaque(false);
        mainPanel.add(cartBox, gbc);

        // Columns
        String[] columnNames = {"S.No", "Product", "Quantity", "Unit Price", "Line Total", "Adjust", "Remove"};
        tableModel = new CartTableModel(columnNames, 0);

        // === Cart Table ===
        cartTable = new JTable(tableModel) {
            private static final long serialVersionUID = 1L;
            private int hoveredRow = -1;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                if (c instanceof JLabel) {
                    ((JLabel) c).setForeground(Color.WHITE);
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                }

                if (row == hoveredRow) {
                    c.setBackground(new Color(80, 80, 120, 200));
                } else {
                    c.setBackground(new Color(20, 20, 20, 180));
                }
                return c;
            }

            {
                addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        int row = rowAtPoint(e.getPoint());
                        if (row != hoveredRow) {
                            hoveredRow = row;
                            repaint();
                        }
                    }
                });
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hoveredRow = -1;
                        repaint();
                    }
                });
            }
        };

        cartTable.setRowHeight(60);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 20));
        cartTable.setFillsViewportHeight(true);
        cartTable.setOpaque(false);

        // Header
        JTableHeader header = cartTable.getTableHeader();
        header.setOpaque(true);
        header.setBackground(new Color(255, 255, 255, 200));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        // Center align text columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setVerticalAlignment(SwingConstants.CENTER);
        centerRenderer.setOpaque(false);
        centerRenderer.setForeground(Color.WHITE);
        for (int i = 0; i <= 4; i++) {
            cartTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Button columns
        cartTable.getColumnModel().getColumn(5).setCellRenderer(new AdjustCellRenderer());
        cartTable.getColumnModel().getColumn(5).setCellEditor(new AdjustCellEditor());
        cartTable.getColumnModel().getColumn(6).setCellRenderer(new RemoveCellRenderer());
        cartTable.getColumnModel().getColumn(6).setCellEditor(new RemoveCellEditor());

        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setColumnHeaderView(cartTable.getTableHeader());
        cartBox.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        bottomPanel.setOpaque(false);

        totalLabel = new JLabel("Total: 0 INR");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 24));
        totalLabel.setForeground(Color.WHITE);
        bottomPanel.add(totalLabel);

        JButton checkoutBtn = new JButton("Checkout & Generate Invoice");
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 22));
        checkoutBtn.addActionListener(e -> checkoutAction());
        bottomPanel.add(checkoutBtn);

        JButton returnBtn = new JButton("Return to Categories");
        returnBtn.setFont(new Font("Arial", Font.BOLD, 22));
        returnBtn.addActionListener(e -> dispose());
        bottomPanel.add(returnBtn);

        cartBox.add(bottomPanel, BorderLayout.SOUTH);
    }

    /** Refresh cart & total */
    public void refreshCart() {
        tableModel.setCartItems(cartService.getCartItems());
        double total = cartService.getCartItems().stream()
                .mapToDouble(CartItem::getLineTotal)
                .sum();
        totalLabel.setText("Total: " + total + " INR");
    }

    /** Checkout action */
    private void checkoutAction() {
        if (isCheckingOut) return;   //  Prevent double trigger
        isCheckingOut = true;

        if (cartService.getCartItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            isCheckingOut = false;
            return;
        }

        double total = cartService.getCartItems().stream()
                .mapToDouble(CartItem::getLineTotal)
                .sum();

        // Generate invoice (only once)
        PDFGenerator.generateInvoice(username, cartService.getCartItems(), total);

        cartService.checkout(username); // clear cart

        JOptionPane.showMessageDialog(this,
                "Checkout complete. Invoice has been generated!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        refreshCart();
        isCheckingOut = false; // ✅ Reset for next checkout
    }

    // --- Table Model ---
    class CartTableModel extends DefaultTableModel {
        private static final long serialVersionUID = 1L;
        private java.util.List<CartItem> cartItems;

        public CartTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        public void setCartItems(java.util.List<CartItem> cartItems) {
            this.cartItems = cartItems;
            setRowCount(0);
            int sno = 1;
            for (CartItem item : cartItems) {
                addRow(new Object[]{
                        sno++,
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPriceInr(),
                        item.getLineTotal(),
                        "Adjust",
                        "Remove"
                });
            }
        }

        public CartItem getCartItem(int row) {
            return cartItems.get(row);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 5 || column == 6;
        }
    }

    // --- Adjust Button Renderer ---
    class AdjustCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JPanel wrapper = new JPanel(new GridBagLayout());
            wrapper.setOpaque(false);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            btnPanel.setOpaque(false);
            btnPanel.add(createHoverButton("-"));
            btnPanel.add(createHoverButton("+"));

            wrapper.add(btnPanel);
            return wrapper;
        }
    }

    class AdjustCellEditor extends AbstractCellEditor implements TableCellEditor {
        private static final long serialVersionUID = 1L;
        private final JPanel wrapper;
        private final JButton decBtn, incBtn;

        public AdjustCellEditor() {
            wrapper = new JPanel(new GridBagLayout());
            wrapper.setOpaque(false);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            btnPanel.setOpaque(false);

            decBtn = createHoverButton("-");
            incBtn = createHoverButton("+");

            btnPanel.add(decBtn);
            btnPanel.add(incBtn);

            wrapper.add(btnPanel);

            decBtn.addActionListener(e -> adjustQuantity(-1));
            incBtn.addActionListener(e -> adjustQuantity(1));
        }

        private void adjustQuantity(int delta) {
            int row = cartTable.getEditingRow();
            if (row < 0) return;

            CartItem item = tableModel.getCartItem(row);
            int newQty = item.getQuantity() + delta;
            if (newQty >= 1) {
                item.setQuantity(newQty);
            }
            fireEditingStopped();
            refreshCart();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return wrapper;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    // --- Remove Button ---
    class RemoveCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JPanel wrapper = new JPanel(new GridBagLayout());
            wrapper.setOpaque(false);
            wrapper.add(createHoverButton("Remove"));
            return wrapper;
        }
    }

    class RemoveCellEditor extends AbstractCellEditor implements TableCellEditor {
        private static final long serialVersionUID = 1L;
        private final JButton removeBtn;
        private final JPanel wrapper;

        public RemoveCellEditor() {
            wrapper = new JPanel(new GridBagLayout());
            wrapper.setOpaque(false);

            removeBtn = createHoverButton("Remove");
            wrapper.add(removeBtn);

            removeBtn.addActionListener(e -> {
                int row = cartTable.getEditingRow();
                if (row < 0) return;
                CartItem item = tableModel.getCartItem(row);
                cartService.removeProduct(item.getProduct());
                fireEditingStopped();
                refreshCart();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return wrapper;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    // --- Transparent Hover Button ---
    private JButton createHoverButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        btn.setBackground(new Color(60, 60, 60, 200));
        btn.setForeground(Color.WHITE);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(100, 100, 160, 200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(60, 60, 60, 200));
            }
        });

        return btn;
    }
}
