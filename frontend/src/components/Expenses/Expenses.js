import React, { useState, useEffect } from 'react';
import { expenseService } from '../../services/api';
import { toast } from 'react-toastify';
import ExpenseForm from './ExpenseForm';
import ExpenseList from './ExpenseList';
import './Expenses.css';

const Expenses = () => {
  const [expenses, setExpenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingExpense, setEditingExpense] = useState(null);
  const [filter, setFilter] = useState({
    category: '',
    startDate: '',
    endDate: '',
  });

  useEffect(() => {
    fetchExpenses();
  }, []);

  const fetchExpenses = async () => {
    try {
      const response = await expenseService.getAllExpenses();
      setExpenses(response.data);
    } catch (error) {
      toast.error('Failed to fetch expenses');
    } finally {
      setLoading(false);
    }
  };

  const handleAddExpense = async (expenseData) => {
    try {
      await expenseService.createExpense(expenseData);
      toast.success('Expense added successfully!');
      fetchExpenses();
      setShowForm(false);
    } catch (error) {
      console.error('Error adding expense:', error);
      const errorMessage = error.response?.data?.message || 
                          error.response?.data || 
                          error.message || 
                          'Failed to add expense';
      toast.error(errorMessage);
    }
  };

  const handleEditExpense = async (id, expenseData) => {
    try {
      await expenseService.updateExpense(id, expenseData);
      toast.success('Expense updated successfully!');
      fetchExpenses();
      setEditingExpense(null);
      setShowForm(false);
    } catch (error) {
      console.error('Error updating expense:', error);
      const errorMessage = error.response?.data?.message || 
                          error.response?.data || 
                          error.message || 
                          'Failed to update expense';
      toast.error(errorMessage);
    }
  };

  const handleDeleteExpense = async (id) => {
    if (window.confirm('Are you sure you want to delete this expense?')) {
      try {
        await expenseService.deleteExpense(id);
        toast.success('Expense deleted successfully!');
        fetchExpenses();
      } catch (error) {
        toast.error('Failed to delete expense');
      }
    }
  };

  const handleEdit = (expense) => {
    setEditingExpense(expense);
    setShowForm(true);
  };

  const handleCancelForm = () => {
    setShowForm(false);
    setEditingExpense(null);
  };

  const handleFilterChange = (e) => {
    setFilter({
      ...filter,
      [e.target.name]: e.target.value,
    });
  };

  const applyFilter = async () => {
    try {
      setLoading(true);
      if (filter.category) {
        const response = await expenseService.getExpensesByCategory(filter.category);
        setExpenses(response.data);
      } else if (filter.startDate && filter.endDate) {
        const response = await expenseService.getExpensesByDateRange(
          filter.startDate,
          filter.endDate
        );
        setExpenses(response.data);
      } else {
        fetchExpenses();
      }
    } catch (error) {
      toast.error('Failed to apply filter');
    } finally {
      setLoading(false);
    }
  };

  const clearFilter = () => {
    setFilter({ category: '', startDate: '', endDate: '' });
    fetchExpenses();
  };

  if (loading) {
    return <div className="loading">Loading expenses...</div>;
  }

  return (
    <div className="expenses-container">
      <div className="expenses-header">
        <h2>My Expenses</h2>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : '+ Add Expense'}
        </button>
      </div>

      {showForm && (
        <ExpenseForm
          onSubmit={editingExpense ? handleEditExpense : handleAddExpense}
          initialData={editingExpense}
          onCancel={handleCancelForm}
        />
      )}

      <div className="card">
        <div className="filter-section">
          <h3>Filter Expenses</h3>
          <div className="filter-form">
            <div className="form-group">
              <label>Category</label>
              <select name="category" value={filter.category} onChange={handleFilterChange}>
                <option value="">All Categories</option>
                <option value="FOOD_AND_DINING">Food & Dining</option>
                <option value="TRANSPORTATION">Transportation</option>
                <option value="HOUSING">Housing</option>
                <option value="UTILITIES">Utilities</option>
                <option value="HEALTHCARE">Healthcare</option>
                <option value="ENTERTAINMENT">Entertainment</option>
                <option value="SHOPPING">Shopping</option>
                <option value="EDUCATION">Education</option>
                <option value="TRAVEL">Travel</option>
                <option value="INSURANCE">Insurance</option>
                <option value="SAVINGS">Savings</option>
                <option value="PERSONAL_CARE">Personal Care</option>
                <option value="GIFTS_AND_DONATIONS">Gifts & Donations</option>
                <option value="BUSINESS">Business</option>
                <option value="OTHER">Other</option>
              </select>
            </div>
            <div className="form-group">
              <label>Start Date</label>
              <input
                type="date"
                name="startDate"
                value={filter.startDate}
                onChange={handleFilterChange}
              />
            </div>
            <div className="form-group">
              <label>End Date</label>
              <input
                type="date"
                name="endDate"
                value={filter.endDate}
                onChange={handleFilterChange}
              />
            </div>
            <div className="filter-buttons">
              <button className="btn btn-primary" onClick={applyFilter}>
                Apply Filter
              </button>
              <button className="btn btn-secondary" onClick={clearFilter}>
                Clear
              </button>
            </div>
          </div>
        </div>
      </div>

      <ExpenseList
        expenses={expenses}
        onEdit={handleEdit}
        onDelete={handleDeleteExpense}
      />
    </div>
  );
};

export default Expenses;
