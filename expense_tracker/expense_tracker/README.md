# Smart Expense Tracker

A command-line Python app to log, categorize, and analyze your daily expenses.

---

## Features

- Add expenses with date, category, amount, and description
- View all recorded expenses in a clean table format
- Monthly breakdown with a simple ASCII bar chart
- Pie chart visualization using matplotlib
- Spending insights — top category, monthly average, biggest expense
- Personalized tips to reduce overspending
- Delete any logged expense
- Data stored locally in `expenses.json`

---

## Requirements

- Python 3.7+
- matplotlib (for pie chart only)

---

## Setup

```bash
# Clone or copy the project folder
cd expense_tracker

# Install dependency
pip install matplotlib

# Run the app
python tracker.py
```

---

## How to Use

When you run the app, you get a menu:

```
1. Add Expense
2. View All Expenses
3. Monthly Summary
4. Category Pie Chart
5. Insights & Tips
6. Delete an Expense
7. Exit
```

Just type the number and hit Enter.

### Adding an expense
- Enter the date (or press Enter to use today)
- Pick a category from the list
- Enter the amount in ₹
- Optionally add a short description

### Monthly Summary
- Shows a per-category breakdown with % share for any month you've logged expenses in

### Pie Chart
- Filter by month or view all-time
- Chart is displayed on screen and saved as `expense_chart.png`

### Insights
- Shows total spend, monthly average, highest/lowest category, and biggest single expense
- Gives a tailored money-saving tip based on where you spend the most

---

## File Structure

```
expense_tracker/
│
├── tracker.py       # Main application
├── expenses.json    # Data file (auto-created on first run)
└── README.md        # This file
```

---

## Categories

Food, Travel, Bills, Shopping, Health, Entertainment, Education, Other

---

## Notes

- The sample `expenses.json` includes some test data so you can explore the app immediately
- All amounts are in Indian Rupees (₹)
- Data is stored as plain JSON — easy to inspect or back up
