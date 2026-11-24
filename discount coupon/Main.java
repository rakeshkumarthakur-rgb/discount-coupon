
import java.util.*;
import java.io.*;
import java.time.*;

public class Main {

    // Stores coupon details
    static class Coupon {
        String code, type;
        double value;
        LocalDate start, end;
        int limit, used;

        Coupon(String code, String type, double value,
               LocalDate start, LocalDate end, int limit, int used) {
            this.code = code;
            this.type = type;
            this.value = value;
            this.start = start;
            this.end = end;
            this.limit = limit;
            this.used = used;
        }

        // Calculates the discount
        double discount(double bill) {
            double amount;

            if (type.equals("PERCENT")) {
                amount = bill * value / 100;
            } else {
                amount = value;
            }

            return Math.min(bill, amount);
        }

        // Checks whether the coupon can be used
        boolean valid(double bill) {
            LocalDate today = LocalDate.now();

            return bill > 0
                    && !today.isBefore(start)
                    && !today.isAfter(end)
                    && used < limit;
        }
    }

    // Node of the Binary Search Tree
    static class Node {
        Coupon coupon;
        Node left, right;

        Node(Coupon coupon) {
            this.coupon = coupon;
        }
    }

    static Node root;
    static HashMap<String, Coupon> map = new HashMap<>();
    static Scanner sc = new Scanner(System.in);
    static final String FILE = "coupons.txt";

    // Inserts a coupon into the BST
    static boolean insert(Node node, Coupon coupon) {
        int comparison = coupon.code.compareTo(node.coupon.code);

        if (comparison == 0) {
            return false;
        }

        if (comparison < 0) {
            if (node.left == null) {
                node.left = new Node(coupon);
            } else {
                return insert(node.left, coupon);
            }
        } else {
            if (node.right == null) {
                node.right = new Node(coupon);
            } else {
                return insert(node.right, coupon);
            }
        }

        return true;
    }

    // Adds a coupon to both BST and HashMap
    static boolean addCoupon(Coupon coupon) {
        if (map.containsKey(coupon.code)) {
            return false;
        }

        if (root == null) {
            root = new Node(coupon);
        } else if (!insert(root, coupon)) {
            return false;
        }

        map.put(coupon.code, coupon);
        return true;
    }

    // Loads saved coupons when the program starts
    static void loadCoupons() {
        File file = new File(FILE);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length != 7) {
                    continue;
                }

                Coupon coupon = new Coupon(
                        data[0],
                        data[1],
                        Double.parseDouble(data[2]),
                        LocalDate.parse(data[3]),
                        LocalDate.parse(data[4]),
                        Integer.parseInt(data[5]),
                        Integer.parseInt(data[6])
                );

                addCoupon(coupon);
            }

        } catch (Exception e) {
            System.out.println("Error loading coupon data!");
        }
    }

    // Saves all coupons into the file
    static void saveCoupons() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {

            for (Coupon coupon : map.values()) {
                pw.println(
                        coupon.code + ","
                        + coupon.type + ","
                        + coupon.value + ","
                        + coupon.start + ","
                        + coupon.end + ","
                        + coupon.limit + ","
                        + coupon.used
                );
            }

        } catch (IOException e) {
            System.out.println("Error saving coupon data!");
        }
    }

    // Adds a new coupon
    static void addNewCoupon() {
        System.out.println("\n===== ADD NEW COUPON =====");

        System.out.print("Enter Coupon Code: ");
        String code = sc.nextLine().trim().toUpperCase();

        if (code.isEmpty() || map.containsKey(code)) {
            System.out.println("Invalid or duplicate coupon code!");
            return;
        }

        System.out.print("Enter Type (PERCENT/FLAT): ");
        String type = sc.nextLine().trim().toUpperCase();

        if (!type.equals("PERCENT") && !type.equals("FLAT")) {
            System.out.println("Invalid coupon type!");
            return;
        }

        System.out.print("Enter Discount Value: ");
        double value = Double.parseDouble(sc.nextLine());

        if (value <= 0 || (type.equals("PERCENT") && value > 100)) {
            System.out.println("Invalid discount value!");
            return;
        }

        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        LocalDate start = LocalDate.parse(sc.nextLine());

        System.out.print("Enter End Date (YYYY-MM-DD): ");
        LocalDate end = LocalDate.parse(sc.nextLine());

        if (end.isBefore(start)) {
            System.out.println("Invalid date range!");
            return;
        }

        System.out.print("Enter Usage Limit: ");
        int limit = Integer.parseInt(sc.nextLine());

        if (limit <= 0) {
            System.out.println("Invalid usage limit!");
            return;
        }

        Coupon coupon = new Coupon(
                code, type, value, start, end, limit, 0
        );

        if (addCoupon(coupon)) {
            System.out.println("\nCoupon added successfully!");
            System.out.println("Coupon Code: " + code);
            System.out.println("Discount: " + formatAmount(value)
                    + (type.equals("PERCENT") ? "%" : " Rs."));
            saveCoupons();
        } else {
            System.out.println("Coupon could not be added!");
        }
    }

    // Applies a coupon entered by the customer
    static void applyCoupon() {
        System.out.println("\n===== APPLY COUPON =====");

        System.out.print("Enter Customer Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Bill Amount: ");
        double bill = Double.parseDouble(sc.nextLine());

        if (bill <= 0) {
            System.out.println("Invalid bill amount!");
            return;
        }

        System.out.print("Enter Coupon Code: ");
        String code = sc.nextLine().trim().toUpperCase();

        Coupon coupon = map.get(code);

        if (coupon == null) {
            System.out.println("Coupon " + code + " not found.");
            return;
        }

        if (!coupon.valid(bill)) {
            System.out.println("Coupon is expired, exhausted or invalid.");
            return;
        }

        double discount = coupon.discount(bill);
        coupon.used++;

        showBill(name, bill, code, discount);
    }

    // Finds and applies the coupon with the highest discount
    static void applyBestCoupon() {
        System.out.println("\n===== FIND BEST DISCOUNT =====");

        System.out.print("Enter Customer Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Bill Amount: ");
        double bill = Double.parseDouble(sc.nextLine());

        if (bill <= 0) {
            System.out.println("Invalid bill amount!");
            return;
        }

        Coupon bestCoupon = null;
        double highestDiscount = 0;

        // Greedy approach: select the highest valid discount
        for (Coupon coupon : map.values()) {
            if (coupon.valid(bill)) {
                double currentDiscount = coupon.discount(bill);

                if (currentDiscount > highestDiscount) {
                    highestDiscount = currentDiscount;
                    bestCoupon = coupon;
                }
            }
        }

        if (bestCoupon == null) {
            System.out.println("No valid coupon available!");
            return;
        }

        bestCoupon.used++;

        System.out.println("\nBest Coupon Selected: " + bestCoupon.code);

        showBill(name, bill, bestCoupon.code, highestDiscount);
    }

    // Displays the customer's bill
    static void showBill(String name, double bill,
                         String code, double discount) {

        System.out.println("\n========== CUSTOMER BILL ==========");
        System.out.println("Customer Name : " + name);
        System.out.println("Coupon Code   : " + code);
        System.out.println("Total Bill    : Rs. " + formatAmount(bill));
        System.out.println("Discount      : Rs. " + formatAmount(discount));
        System.out.println("Final Bill    : Rs. " + formatAmount(bill - discount));
        System.out.println("===================================");
    }

    // Displays coupons in sorted order using inorder traversal
    static void report(Node node) {
        if (node == null) {
            return;
        }

        report(node.left);

        Coupon coupon = node.coupon;

        System.out.println(
                coupon.code
                + " | " + coupon.type
                + " | " + formatAmount(coupon.value)
                + " | " + coupon.start + " to " + coupon.end
                + " | Used: " + coupon.used + "/" + coupon.limit
        );

        report(node.right);
    }

    // Displays whole coupon report
    static void showReport() {
        System.out.println("\n========== COUPON REPORT ==========");

        if (root == null) {
            System.out.println("No coupons available.");
        } else {
            System.out.println("Code | Type | Value | Validity | Usage");
            System.out.println("-------------------------------------");
            report(root);
        }

        System.out.println("===================================");
    }

    // Removes unnecessary decimal zeros from amounts
    static String formatAmount(double amount) {
        return String.format(Locale.US, "%.2f", amount)
                .replaceAll("0+$", "")
                .replaceAll("\\.$", "");
    }

    public static void main(String[] args) {
        loadCoupons();

        System.out.println("Coupon data loaded successfully!");

        while (true) {
            System.out.println("\n==================================");
            System.out.println("       COUPON & DISCOUNT MANAGER");
            System.out.println("==================================");
            System.out.println("1. Add Coupon");
            System.out.println("2. Apply Coupon");
            System.out.println("3. Find Best Discount");
            System.out.println("4. Coupon Report");
            System.out.println("5. Exit");
            System.out.println("==================================");

            System.out.print("Enter Your Choice: ");
            String choice = sc.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addNewCoupon();
                        break;

                    case "2":
                        applyCoupon();
                        break;

                    case "3":
                        applyBestCoupon();
                        break;

                    case "4":
                        showReport();
                        break;

                    case "5":
                        saveCoupons();
                        System.out.println("\nCoupon data saved successfully!");
                        System.out.println("Thank you for using the system!");
                        return;

                    default:
                        System.out.println("Invalid choice! Please select 1-5.");
                }

            } catch (Exception e) {
                System.out.println("Invalid input! Please try again.");
            }
        }
    }
}
