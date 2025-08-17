package service;

import model.CartItem;
import model.Product;
import ui.PDFGenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CartService {
    private final List<CartItem> cartItems = new ArrayList<>();
    private int cartItemIdCounter = 1;

    public void addToCart(Product product, int quantity) {
        // Check if product already exists in cart, update quantity
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        CartItem item = new CartItem(cartItemIdCounter++, product, quantity, product.getPrice());
        cartItems.add(item);
        System.out.println(quantity + " x " + product.getName() + " added to cart.");
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public void clearCart() {
        cartItems.clear();
    }

    /**
     * Checkout and generate invoice (PDF/HTML). Requires username for file naming & display.
     */
    public void checkout(String username) {
        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty. Cannot checkout.");
            return;
        }

        double totalAmount = 0;
        for (CartItem item : cartItems) totalAmount += item.getLineTotal();

        PDFGenerator.generateInvoice(username, cartItems, totalAmount);

        // clear cart
        cartItems.clear();
    }

    /**
     * Remove a product entirely from the cart.
     */
    public void removeProduct(Product product) {
        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getId() == product.getId()) {
                iterator.remove();
                System.out.println(product.getName() + " removed from cart.");
                break;
            }
        }
    }
}
