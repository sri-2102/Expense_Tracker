import React from 'react';
import './Budgets.css';

const BudgetList = ({ budgets, onEdit, onDelete }) => {
  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(amount);
  };

  const formatCategory = (category) => {
    return category.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, (l) => l.toUpperCase());
  };

  const getMonthName = (month) => {
    const months = [
      'January', 'February', 'March', 'April', 'May', 'June',
      'July', 'August', 'September', 'October', 'November', 'December'
    ];
    return months[month - 1];
  };

  const calculateProgress = (spent, limit) => {
    return Math.min((spent / limit) * 100, 100);
  };

  const getProgressColor = (percentage, threshold) => {
    if (percentage >= 100) return '#ef4444';
    if (percentage >= threshold) return '#f59e0b';
    return '#10b981';
  };

  if (budgets.length === 0) {
    return (
      <div className="card">
        <p className="no-data">No budgets found. Create your first budget to track spending!</p>
      </div>
    );
  }

  return (
    <div className="budgets-grid">
      {budgets.map((budget) => {
        const progress = calculateProgress(budget.spentAmount, budget.limitAmount);
        const progressColor = getProgressColor(progress, budget.alertThreshold);
        const remaining = budget.limitAmount - budget.spentAmount;

        return (
          <div key={budget.id} className="budget-card card">
            <div className="budget-card-header">
              <h3>{formatCategory(budget.category)}</h3>
              <div className="budget-actions">
                <button
                  className="btn-icon btn-edit"
                  onClick={() => onEdit(budget)}
                  title="Edit"
                >
                  ✏️
                </button>
                <button
                  className="btn-icon btn-delete"
                  onClick={() => onDelete(budget.id)}
                  title="Delete"
                >
                  🗑️
                </button>
              </div>
            </div>

            <div className="budget-period">
              {getMonthName(budget.month)} {budget.year}
            </div>

            <div className="budget-amounts">
              <div className="budget-spent">
                <span className="label">Spent</span>
                <span className="value">{formatCurrency(budget.spentAmount)}</span>
              </div>
              <div className="budget-limit">
                <span className="label">Limit</span>
                <span className="value">{formatCurrency(budget.limitAmount)}</span>
              </div>
            </div>

            <div className="progress-bar">
              <div
                className="progress-fill"
                style={{
                  width: `${progress}%`,
                  backgroundColor: progressColor,
                }}
              />
            </div>

            <div className="budget-stats">
              <div className="stat">
                <span className="stat-label">Progress</span>
                <span className="stat-value" style={{ color: progressColor }}>
                  {progress.toFixed(1)}%
                </span>
              </div>
              <div className="stat">
                <span className="stat-label">Remaining</span>
                <span className={`stat-value ${remaining < 0 ? 'negative' : 'positive'}`}>
                  {formatCurrency(Math.abs(remaining))}
                  {remaining < 0 && ' over'}
                </span>
              </div>
            </div>

            {progress >= budget.alertThreshold && (
              <div className={`budget-alert ${progress >= 100 ? 'danger' : 'warning'}`}>
                {progress >= 100
                  ? '⚠️ Budget exceeded!'
                  : `⚠️ ${budget.alertThreshold}% threshold reached`}
              </div>
            )}
          </div>
        );
      })}
    </div>
  );
};

export default BudgetList;
