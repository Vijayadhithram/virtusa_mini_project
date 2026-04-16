import json
import os
import datetime
from collections import defaultdict

DATA_FILE = "expenses.json"

CATEGORIES = [
    "Food", "Travel", "Bills", "Shopping",
    "Health", "Entertainment", "Education", "Other"
]

TIPS = {
    "Food": "Try meal prepping at home — cooking in bulk cuts food costs significantly.",
    "Travel": "Use public transport or carpool when possible to save on fuel/cab fare.",
    "Bills": "Review subscriptions monthly and cancel ones you barely use.",
    "Shopping": "Wait 24 hours before any non-essential purchase to avoid impulse buys.",
    "Health": "Preventive care is cheaper — regular checkups beat emergency visits.",
    "Entertainment": "Look for free local events, libraries, and community activities.",
    "Education": "YouTube, free MOOCs, and public libraries are goldmines.",
    "Other": "Log 'Other' expenses more specifically to spot hidden spending patterns.",
}


def load_expenses():
    if not os.path.exists(DATA_FILE):
        return []
    with open(DATA_FILE, "r") as f:
        try:
            return json.load(f)
        except json.JSONDecodeError:
            return []


def save_expenses(expenses):
    with open(DATA_FILE, "w") as f:
        json.dump(expenses, f, indent=2)


def clear():
    os.system("cls" if os.name == "nt" else "clear")


def divider(char="-", width=55):
    print(char * width)


def banner():
    clear()
    divider("=")
    print("        SMART EXPENSE TRACKER")
    print("        Track. Analyze. Save.")
    divider("=")


def add_expense():
    banner()
    print("\n  [ ADD NEW EXPENSE ]\n")

    # Date
    date_str = input("  Date (YYYY-MM-DD) or press Enter for today: ").strip()
    if not date_str:
        date_str = datetime.date.today().isoformat()
    else:
        try:
            datetime.date.fromisoformat(date_str)
        except ValueError:
            print("\n  Invalid date format. Use YYYY-MM-DD.")
            input("\n  Press Enter to go back...")
            return

    # Category
    print("\n  Categories:")
    for i, cat in enumerate(CATEGORIES, 1):
        print(f"    {i}. {cat}")
    cat_input = input("\n  Pick a category (1-8): ").strip()
    if not cat_input.isdigit() or not (1 <= int(cat_input) <= len(CATEGORIES)):
        print("\n  Invalid choice.")
        input("\n  Press Enter to go back...")
        return
    category = CATEGORIES[int(cat_input) - 1]

    # Amount
    amount_str = input("\n  Amount (₹): ").strip()
    try:
        amount = float(amount_str)
        if amount <= 0:
            raise ValueError
    except ValueError:
        print("\n  Invalid amount.")
        input("\n  Press Enter to go back...")
        return

    # Description
    description = input("\n  Description (optional): ").strip()
    if not description:
        description = category

    expense = {
        "date": date_str,
        "category": category,
        "amount": round(amount, 2),
        "description": description
    }

    expenses = load_expenses()
    expenses.append(expense)
    save_expenses(expenses)

    print(f"\n  Expense of ₹{amount:.2f} added under '{category}'.")
    input("\n  Press Enter to continue...")


def view_all():
    banner()
    print("\n  [ ALL EXPENSES ]\n")
    expenses = load_expenses()

    if not expenses:
        print("  No expenses found.")
        input("\n  Press Enter to continue...")
        return

    sorted_exp = sorted(expenses, key=lambda x: x["date"], reverse=True)
    divider()
    print(f"  {'DATE':<13} {'CATEGORY':<14} {'AMOUNT':>9}  DESCRIPTION")
    divider()
    for e in sorted_exp:
        print(f"  {e['date']:<13} {e['category']:<14} ₹{e['amount']:>8.2f}  {e['description']}")
    divider()
    total = sum(e["amount"] for e in expenses)
    print(f"  {'TOTAL':<13} {'':<14} ₹{total:>8.2f}")
    divider()
    input("\n  Press Enter to continue...")


def monthly_summary():
    banner()
    print("\n  [ MONTHLY SUMMARY ]\n")
    expenses = load_expenses()

    if not expenses:
        print("  No expenses to summarize.")
        input("\n  Press Enter to continue...")
        return

    # Group by YYYY-MM
    months = defaultdict(list)
    for e in expenses:
        key = e["date"][:7]
        months[key].append(e)

    sorted_months = sorted(months.keys(), reverse=True)

    print("  Available months:")
    for i, m in enumerate(sorted_months, 1):
        print(f"    {i}. {m}")

    choice = input("\n  Select month (number) or Enter for latest: ").strip()
    if not choice:
        selected = sorted_months[0]
    elif choice.isdigit() and 1 <= int(choice) <= len(sorted_months):
        selected = sorted_months[int(choice) - 1]
    else:
        print("\n  Invalid choice.")
        input("\n  Press Enter to go back...")
        return

    month_expenses = months[selected]
    cat_totals = defaultdict(float)
    for e in month_expenses:
        cat_totals[e["category"]] += e["amount"]

    total = sum(cat_totals.values())
    top_cat = max(cat_totals, key=cat_totals.get)

    print(f"\n  Summary for {selected}")
    divider()
    print(f"  {'CATEGORY':<16} {'AMOUNT':>10}  {'SHARE':>7}")
    divider()
    for cat, amt in sorted(cat_totals.items(), key=lambda x: x[1], reverse=True):
        pct = (amt / total) * 100
        bar = "#" * int(pct / 5)
        print(f"  {cat:<16} ₹{amt:>9.2f}  {pct:>5.1f}%  {bar}")
    divider()
    print(f"  {'TOTAL':<16} ₹{total:>9.2f}")
    divider()

    print(f"\n  Highest spending: {top_cat} (₹{cat_totals[top_cat]:.2f})")
    print(f"\n  Tip: {TIPS.get(top_cat, 'Track consistently for better insights.')}")

    input("\n  Press Enter to continue...")


def generate_chart():
    banner()
    print("\n  [ PIE CHART — CATEGORY BREAKDOWN ]\n")

    try:
        import matplotlib.pyplot as plt
        import matplotlib.patches as mpatches
    except ImportError:
        print("  matplotlib not installed.")
        print("  Run: pip install matplotlib")
        input("\n  Press Enter to continue...")
        return

    expenses = load_expenses()
    if not expenses:
        print("  No expense data to chart.")
        input("\n  Press Enter to continue...")
        return

    # Optional: filter by month
    months = sorted(set(e["date"][:7] for e in expenses), reverse=True)
    print("  Available months (or Enter for all time):")
    for i, m in enumerate(months, 1):
        print(f"    {i}. {m}")
    choice = input("\n  Select (number) or Enter for all: ").strip()

    if choice and choice.isdigit() and 1 <= int(choice) <= len(months):
        selected_month = months[int(choice) - 1]
        filtered = [e for e in expenses if e["date"][:7] == selected_month]
        title = f"Spending by Category — {selected_month}"
    else:
        filtered = expenses
        title = "Spending by Category — All Time"

    cat_totals = defaultdict(float)
    for e in filtered:
        cat_totals[e["category"]] += e["amount"]

    if not cat_totals:
        print("  No data for that selection.")
        input("\n  Press Enter to continue...")
        return

    labels = list(cat_totals.keys())
    sizes = list(cat_totals.values())

    colors = [
        "#e05c5c", "#f0a500", "#4caf8a", "#5b8de0",
        "#b57bee", "#e07a3a", "#3ab8c4", "#a0a0a0"
    ]

    fig, ax = plt.subplots(figsize=(8, 6), facecolor="#1a1a2e")
    ax.set_facecolor("#1a1a2e")

    wedges, texts, autotexts = ax.pie(
        sizes,
        labels=None,
        autopct="%1.1f%%",
        startangle=140,
        colors=colors[:len(labels)],
        pctdistance=0.75,
        wedgeprops=dict(linewidth=1.5, edgecolor="#1a1a2e"),
    )

    for at in autotexts:
        at.set_color("white")
        at.set_fontsize(9)
        at.set_fontweight("bold")

    legend_patches = [
        mpatches.Patch(color=colors[i % len(colors)], label=f"{labels[i]}  ₹{sizes[i]:.0f}")
        for i in range(len(labels))
    ]
    ax.legend(
        handles=legend_patches,
        loc="lower center",
        bbox_to_anchor=(0.5, -0.12),
        ncol=3,
        framealpha=0,
        labelcolor="white",
        fontsize=9,
    )

    ax.set_title(title, color="white", fontsize=13, fontweight="bold", pad=20)
    total = sum(sizes)
    ax.text(0, 0, f"₹{total:.0f}\ntotal", ha="center", va="center",
            fontsize=10, color="white", fontweight="bold")

    plt.tight_layout()
    chart_path = "expense_chart.png"
    plt.savefig(chart_path, dpi=150, bbox_inches="tight", facecolor="#1a1a2e")
    plt.show()
    print(f"\n  Chart saved as '{chart_path}'.")
    input("\n  Press Enter to continue...")


def delete_expense():
    banner()
    print("\n  [ DELETE AN EXPENSE ]\n")
    expenses = load_expenses()
    if not expenses:
        print("  Nothing to delete.")
        input("\n  Press Enter to continue...")
        return

    sorted_exp = sorted(expenses, key=lambda x: x["date"], reverse=True)
    for i, e in enumerate(sorted_exp, 1):
        print(f"  {i:>3}. {e['date']}  {e['category']:<14} ₹{e['amount']:>8.2f}  {e['description']}")

    choice = input("\n  Enter number to delete (or 0 to cancel): ").strip()
    if not choice.isdigit():
        return
    idx = int(choice)
    if idx == 0:
        return
    if 1 <= idx <= len(sorted_exp):
        to_remove = sorted_exp[idx - 1]
        expenses.remove(to_remove)
        save_expenses(expenses)
        print(f"\n  Deleted: {to_remove['description']} (₹{to_remove['amount']})")
    else:
        print("\n  Invalid number.")

    input("\n  Press Enter to continue...")


def insights():
    banner()
    print("\n  [ SPENDING INSIGHTS ]\n")
    expenses = load_expenses()
    if not expenses:
        print("  No data yet. Add some expenses first.")
        input("\n  Press Enter to continue...")
        return

    total = sum(e["amount"] for e in expenses)
    cat_totals = defaultdict(float)
    for e in expenses:
        cat_totals[e["category"]] += e["amount"]

    top_cat = max(cat_totals, key=cat_totals.get)
    low_cat = min(cat_totals, key=cat_totals.get)

    # Monthly avg
    months_seen = set(e["date"][:7] for e in expenses)
    monthly_avg = total / len(months_seen)

    # Most expensive single expense
    biggest = max(expenses, key=lambda x: x["amount"])

    divider()
    print(f"  Total spent (all time) : ₹{total:.2f}")
    print(f"  Monthly average        : ₹{monthly_avg:.2f}")
    print(f"  Months tracked         : {len(months_seen)}")
    divider()
    print(f"  Highest category       : {top_cat} (₹{cat_totals[top_cat]:.2f})")
    print(f"  Lowest category        : {low_cat} (₹{cat_totals[low_cat]:.2f})")
    print(f"  Biggest single expense : ₹{biggest['amount']:.2f} — {biggest['description']}")
    divider()
    print(f"\n  Recommendation:")
    print(f"  You spend the most on {top_cat}.")
    print(f"  {TIPS.get(top_cat, '')}")
    divider()
    input("\n  Press Enter to continue...")


def main_menu():
    while True:
        banner()
        print("""
  1. Add Expense
  2. View All Expenses
  3. Monthly Summary
  4. Category Pie Chart
  5. Insights & Tips
  6. Delete an Expense
  7. Exit
""")
        divider()
        choice = input("  Choose an option (1-7): ").strip()

        if choice == "1":
            add_expense()
        elif choice == "2":
            view_all()
        elif choice == "3":
            monthly_summary()
        elif choice == "4":
            generate_chart()
        elif choice == "5":
            insights()
        elif choice == "6":
            delete_expense()
        elif choice == "7":
            clear()
            print("\n  Goodbye. Spend wisely!\n")
            break
        else:
            print("\n  Invalid choice. Try again.")


if __name__ == "__main__":
    main_menu()
