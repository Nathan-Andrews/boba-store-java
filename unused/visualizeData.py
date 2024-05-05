import argparse
import pandas as pd
import matplotlib.pyplot as plt
from datetime import datetime

# Define the custom color palette with distinct colors
custom_colors = ['#DF00FF', '#0D9494', '#FFDDAA', '#801818', '#E48400', '#000100', '#0014A8', '#9CC2BF', '#AD4D8C', '#E49B0F']

# Define command-line arguments
parser = argparse.ArgumentParser(description='Visualize orders per ItemId.')
parser.add_argument('-m', '--month', type=int, help='Month (1-12) to visualize with 4 weeks of data')
args = parser.parse_args()

# Load the CSV file into a DataFrame
data = pd.read_csv('data.csv')

# Convert Unix timestamps to datetime objects
data['Timestamp'] = pd.to_datetime(data['Timestamp'], unit='s')

if args.month:
    # Filter data for the specified month
    start_date = datetime(year=datetime.now().year, month=args.month, day=1)
    end_date = datetime(year=datetime.now().year, month=args.month, day=1) + pd.DateOffset(months=1) - pd.DateOffset(days=1)
    data_for_month = data[(data['Timestamp'] >= start_date) & (data['Timestamp'] <= end_date)]
    
    # Group data by week and count the orders per ItemId
    weekly_data = data_for_month.groupby([data_for_month['Timestamp'].dt.to_period('W'), 'ItemId'])['ItemId'].count().unstack(fill_value=0)
    
    # Extract week labels and order counts
    week_labels = weekly_data.index.strftime('%Y-%U')
    item_ids = weekly_data.columns
    weekly_order_counts = weekly_data.values.T
    
    # Create a grouped bar plot for the specified month
    plt.figure(figsize=(12, 6))
    bar_width = 0.35
    index = range(len(week_labels))
    
    for i, item_id in enumerate(item_ids):
        plt.bar(index, weekly_order_counts[i], bar_width, label=f'Item {item_id}', alpha=0.7, color=custom_colors[i % len(custom_colors)])
    
    plt.xlabel('Week')
    plt.ylabel('Number of Orders')
    plt.title(f'Orders per ItemId for Month {args.month} (4 weeks)')
    plt.xticks(index, week_labels, rotation=45)
    plt.legend()
    plt.grid(axis='y', linestyle='--', alpha=0.7)
    plt.tight_layout()
    plt.show()
else:
    # Group data by month and count the orders per ItemId
    monthly_data = data.groupby([data['Timestamp'].dt.to_period('M'), 'ItemId'])['ItemId'].count().unstack(fill_value=0)
    
    # Extract month-year labels and order counts
    month_labels = monthly_data.index.strftime('%Y-%m')
    item_ids = monthly_data.columns
    monthly_order_counts = monthly_data.values.T
    
    # Create a grouped bar plot for all months
    plt.figure(figsize=(12, 6))
    bar_width = 0.35
    index = range(len(month_labels))
    
    for i, item_id in enumerate(item_ids):
        plt.bar(index, monthly_order_counts[i], bar_width, label=f'Item {item_id}', alpha=0.7, color=custom_colors[i % len(custom_colors)])
    
    plt.xlabel('Month')
    plt.ylabel('Number of Orders')
    plt.title('Orders per ItemId for All Months')
    plt.xticks(index, month_labels, rotation=45)
    plt.legend()
    plt.grid(axis='y', linestyle='--', alpha=0.7)
    plt.tight_layout()
    plt.show()
