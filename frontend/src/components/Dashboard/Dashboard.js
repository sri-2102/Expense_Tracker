import React, { useState, useEffect } from 'react';
import { expenseService } from '../../services/api';
import { format, startOfMonth, endOfMonth } from 'date-fns';
import { toast } from 'react-toastify';
import './Dashboard.css';

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalExpenses: 0,
    monthlyExpenses: 0,
    transactionCount: 0,
    topCategory: 'N/A',
  });
  const [recentExpenses, setRecentExpenses] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      // Fetch all expenses
      const allExpensesResponse = await expenseService.getAllExpenses();
      const allExpenses = allExpensesResponse.data;

      // Fetch current month expenses
      const now = new Date();
      const startDate = format(startOfMonth(now), 'yyyy-MM-dd');
      const endDate = format(endOfMonth(now), 'yyyy-MM-dd');
      const monthlyResponse = await expenseService.getExpensesByDateRange(startDate, endDate);
      const monthlyExpenses = monthlyResponse.data;

      // Calculate stats
      const totalExpenses = allExpenses.reduce((sum, exp) => sum + exp.amount, 0);
      const monthlyTotal = monthlyExpenses.reduce((sum, exp) => sum + exp.amount, 0);

      // Find top category
      const categoryTotals = {};
      allExpenses.forEach((expense) => {
        const category = expense.category;
        categoryTotals[category] = (categoryTotals[category] || 0) + expense.amount;
      });
      const topCategory = Object.keys(categoryTotals).length > 0
        ? Object.entries(categoryTotals).reduce((a, b) => (a[1] > b[1] ? a : b))[0]
        : 'N/A';

      setStats({
        totalExpenses,
        monthlyExpenses: monthlyTotal,
        transactionCount: allExpenses.length,
        topCategory: formatCategory(topCategory),
      });

      // Get recent 5 expenses
      const recent = allExpenses
        .sort((a, b) => new Date(b.date) - new Date(a.date))
        .slice(0, 5);
      setRecentExpenses(recent);
    } catch (error) {
      toast.error('Failed to fetch dashboard data');
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(amount);
  };

  const formatCategory = (category) => {
    if (category === 'N/A') return category;
    return category.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, (l) => l.toUpperCase());
  };

  if (loading) {
    return <div className="loading">Loading dashboard...</div>;
  }

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h2>Dashboard</h2>
        <p>Welcome back! Here's your expense overview.</p>
      </div>

      <div className="stats-grid">
        <div className="stat-card card">
          <div className="stat-icon" style={{ backgroundColor: '#dbeafe' }}>
            💰
          </div>
          <div className="stat-content">
            <span className="stat-label">Total Expenses</span>
            <span className="stat-value">{formatCurrency(stats.totalExpenses)}</span>
          </div>
        </div>

        <div className="stat-card card">
          <div className="stat-icon" style={{ backgroundColor: '#dcfce7' }}>
            📅
          </div>
          <div className="stat-content">
            <span className="stat-label">This Month</span>
            <span className="stat-value">{formatCurrency(stats.monthlyExpenses)}</span>
          </div>
        </div>

        <div className="stat-card card">
          <div className="stat-icon" style={{ backgroundColor: '#fef3c7' }}>
            📊
          </div>
          <div className="stat-content">
            <span className="stat-label">Total Transactions</span>
            <span className="stat-value">{stats.transactionCount}</span>
          </div>
        </div>

        <div className="stat-card card">
          <div className="stat-icon" style={{ backgroundColor: '#fce7f3' }}>
            🏆
          </div>
          <div className="stat-content">
            <span className="stat-label">Top Category</span>
            <span className="stat-value" style={{ fontSize: '18px' }}>
              {stats.topCategory}
            </span>
          </div>
        </div>
      </div>

      <div className="card">
        <h3>Recent Expenses</h3>
        {recentExpenses.length === 0 ? (
          <p className="no-data">No expenses yet. Start tracking your spending!</p>
        ) : (
          <div className="recent-expenses-list">
            {recentExpenses.map((expense) => (
              <div key={expense.id} className="recent-expense-item">
                <div className="expense-info">
                  <span className="expense-description">{expense.description}</span>
                  <span className="expense-date">
                    {format(new Date(expense.date), 'MMM dd, yyyy')}
                  </span>
                </div>
                <div className="expense-details">
                  <span className={`category-badge category-${expense.category.toLowerCase()}`}>
                    {formatCategory(expense.category)}
                  </span>
                  <span className="expense-amount">{formatCurrency(expense.amount)}</span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      <div className="quick-actions card">
        <h3>Quick Actions</h3>
        <div className="action-buttons-grid">
          <a href="/expenses" className="action-button">
            <span className="action-icon">📝</span>
            <span className="action-label">Add Expense</span>
          </a>
          <a href="/budgets" className="action-button">
            <span className="action-icon">🎯</span>
            <span className="action-label">Set Budget</span>
          </a>
          <a href="/reports" className="action-button">
            <span className="action-icon">📊</span>
            <span className="action-label">View Reports</span>
          </a>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
