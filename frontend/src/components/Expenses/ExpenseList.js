import React from 'react';
import { format } from 'date-fns';
import './Expenses.css';

const ExpenseList = ({ expenses, onEdit, onDelete }) => {
  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(amount);
  };

  const formatCategory = (category) => {
    return category.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, (l) => l.toUpperCase());
  };

  const getTotalExpenses = () => {
    return expenses.reduce((total, expense) => total + expense.amount, 0);
  };

  if (expenses.length === 0) {
    return (
      <div className="card">
        <p className="no-data">No expenses found. Add your first expense to get started!</p>
      </div>
    );
  }

  return (
    <div className="card">
      <div className="expenses-summary">
        <h3>Total: {formatCurrency(getTotalExpenses())}</h3>
        <p>{expenses.length} expense(s)</p>
      </div>
      <div className="table-responsive">
        <table>
          <thead>
            <tr>
              <th>Date</th>
              <th>Description</th>
              <th>Category</th>
              <th>Amount</th>
              <th>Notes</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {expenses.map((expense) => (
              <tr key={expense.id}>
                <td>{format(new Date(expense.date), 'MMM dd, yyyy')}</td>
                <td>{expense.description}</td>
                <td>
                  <span className={`category-badge category-${expense.category.toLowerCase()}`}>
                    {formatCategory(expense.category)}
                  </span>
                </td>
                <td className="amount">{formatCurrency(expense.amount)}</td>
                <td>{expense.notes || '-'}</td>
                <td>
                  <div className="action-buttons">
                    <button
                      className="btn-icon btn-edit"
                      onClick={() => onEdit(expense)}
                      title="Edit"
                    >
                      ✏️
                    </button>
                    <button
                      className="btn-icon btn-delete"
                      onClick={() => onDelete(expense.id)}
                      title="Delete"
                    >
                      🗑️
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ExpenseList;
