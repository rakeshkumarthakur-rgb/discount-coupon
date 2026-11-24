# Coupon & Discount Manager

## Project Description

The Coupon & Discount Manager is a Java-based application designed to manage discount coupons efficiently. It allows administrators to add coupons with unique codes, discount types, validity dates and usage limits. Customers can apply coupons to their bills or find the best available discount.

The application uses fundamental Data Structures and Algorithms concepts to organize coupon records, perform quick lookups and select the highest valid discount.

## Features

- **Add Coupon:** Create coupons with unique codes, discount types, values, validity dates and usage limits.
- **Apply Coupon:** Apply a valid coupon to a customer's bill.
- **Find Best Discount:** Automatically select the coupon offering the highest valid discount.
- **Coupon Validation:** Check coupon validity dates, usage limits and discount values.
- **Coupon Report:** Display all coupon records in sorted order.
- **File Storage:** Save and load coupon records using a text file.
- **Input Validation:** Handle invalid entries and duplicate coupon codes.

## Technologies Used

- Java
- Java Collections Framework
- File Handling
- Object-Oriented Programming
- Data Structures and Algorithms

## Data Structures and Algorithms

| Concept | Purpose |
|---|---|
| Binary Search Tree (BST) | Stores and organizes coupons by coupon code |
| HashMap | Provides quick coupon code lookup |
| Greedy Approach | Selects the coupon with the highest valid discount |
| Inorder Traversal | Displays coupons in sorted order |
| File Handling | Saves and retrieves coupon records |

## Project Structure

```text
CouponDiscountManager/
│
├── Main.java
├── coupons.txt
└── README.md
```

**Note:** The `coupons.txt` file is automatically created when coupon data is saved.

## How to Run

### 1. Clone the Repository

```bash
git clone <your-repository-url>
```

### 2. Open the Project Folder

```bash
cd CouponDiscountManager
```

### 3. Compile the Program

```bash
javac Main.java
```

### 4. Run the Program

```bash
java Main
```

## Main Menu

```text
==================================
       COUPON & DISCOUNT MANAGER
==================================
1. Add Coupon
2. Apply Coupon
3. Find Best Discount
4. Coupon Report
5. Exit
==================================
```

## File Storage

The application stores coupon records in `coupons.txt` using the following comma-separated format:

```text
CouponCode,Type,DiscountValue,StartDate,EndDate,UsageLimit,UsedCount
```

Previously stored coupon records are automatically loaded when the application starts, ensuring data persistence. Updated coupon usage information is saved when the application exits.

## Sample Output

```text
========== CUSTOMER BILL ==========
Customer Name : Rahul
Coupon Code   : SAVE10
Total Bill    : Rs. 100
Discount      : Rs. 10
Final Bill    : Rs. 90
===================================
```

## Future Enhancements

- Graphical User Interface using Java Swing or JavaFX.
- Database integration using MySQL.
- Customer management and usage history.
- Coupon update and deletion functionality.
- Advanced coupon search and filtering.
- Detailed discount and usage analytics.

## Conclusion

The Coupon & Discount Manager demonstrates the practical implementation of Java programming and fundamental Data Structures and Algorithms concepts. It provides an organized approach to coupon management, discount calculation and file-based data persistence while strengthening programming and problem-solving skills.

## Author

**Project:** Coupon & Discount Manager  
**PS No.:** 70  
**Language:** Java  
**Academic Session:** 2026–2027
