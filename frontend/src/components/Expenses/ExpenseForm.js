import React, { useState, useEffect } from 'react';
import './Expenses.css';

const ExpenseForm = ({ onSubmit, initialData, onCancel }) => {
  const [formData, setFormData] = useState({
    description: '',
    amount: '',
    date: new Date().toISOString().split('T')[0],
    category: 'FOOD_AND_DINING',
    notes: '',
  });

  useEffect(() => {
    if (initialData) {
      setFormData({
        description: initialData.description || '',
        amount: initialData.amount || '',
        date: initialData.date || new Date().toISOString().split('T')[0],
        category: initialData.category || 'FOOD_AND_DINING',
        notes: initialData.notes || '',
      });
    }
  }, [initialData]);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    // Validate amount
    const amount = parseFloat(formData.amount);
    if (isNaN(amount) || amount <= 0) {
      alert('Please enter a valid amount greater than 0');
      return;
    }
    
    // Convert amount to number before sending
    const submissionData = {
      ...formData,
      amount: amount
    };
    
    console.log('Submitting expense data:', submissionData);
    
    if (initialData) {
      onSubmit(initialData.id, submissionData);
    } else {
      onSubmit(submissionData);
    }
  };

  return (
    <div className="card">
      <h3>{initialData ? 'Edit Expense' : 'Add New Expense'}</h3>
      <form onSubmit={handleSubmit}>
        <div className="form-row">
          <div className="form-group">
            <label>Description *</label>
            <input
              type="text"
              name="description"
              value={formData.description}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label>Amount *</label>
            <input
              type="number"
              name="amount"
              value={formData.amount}
              onChange={handleChange}
              step="0.01"
              min="0"
              required
            />
          </div>
        </div>
        <div className="form-row">
          <div className="form-group">
            <label>Date *</label>
            <input
              type="date"
              name="date"
              value={formData.date}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label>Category *</label>
            <select name="category" value={formData.category} onChange={handleChange} required>
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
        </div>
        <div className="form-group">
          <label>Notes</label>
          <textarea
            name="notes"
            value={formData.notes}
            onChange={handleChange}
            rows="3"
          />
        </div>
        <div className="form-actions">
          <button type="submit" className="btn btn-primary">
            {initialData ? 'Update' : 'Add'} Expense
          </button>
          <button type="button" className="btn btn-secondary" onClick={onCancel}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default ExpenseForm;
