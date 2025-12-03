import React, { useState, useEffect } from 'react';
import './Budgets.css';

const BudgetForm = ({ onSubmit, initialData, onCancel }) => {
  const [formData, setFormData] = useState({
    category: 'FOOD_AND_DINING',
    limitAmount: '',
    month: new Date().getMonth() + 1,
    year: new Date().getFullYear(),
    alertThreshold: '80',
  });

  useEffect(() => {
    if (initialData) {
      setFormData({
        category: initialData.category || 'FOOD_AND_DINING',
        limitAmount: initialData.limitAmount || '',
        month: initialData.month || new Date().getMonth() + 1,
        year: initialData.year || new Date().getFullYear(),
        alertThreshold: initialData.alertThreshold || '80',
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
    if (initialData) {
      onSubmit(initialData.id, formData);
    } else {
      onSubmit(formData);
    }
  };

  const months = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];

  const currentYear = new Date().getFullYear();
  const years = Array.from({ length: 5 }, (_, i) => currentYear - 2 + i);

  return (
    <div className="card">
      <h3>{initialData ? 'Edit Budget' : 'Create New Budget'}</h3>
      <form onSubmit={handleSubmit}>
        <div className="form-row">
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
          <div className="form-group">
            <label>Budget Limit *</label>
            <input
              type="number"
              name="limitAmount"
              value={formData.limitAmount}
              onChange={handleChange}
              step="0.01"
              min="0"
              required
            />
          </div>
        </div>
        <div className="form-row">
          <div className="form-group">
            <label>Month *</label>
            <select name="month" value={formData.month} onChange={handleChange} required>
              {months.map((month, index) => (
                <option key={index} value={index + 1}>
                  {month}
                </option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label>Year *</label>
            <select name="year" value={formData.year} onChange={handleChange} required>
              {years.map((year) => (
                <option key={year} value={year}>
                  {year}
                </option>
              ))}
            </select>
          </div>
        </div>
        <div className="form-group">
          <label>Alert Threshold (%) *</label>
          <input
            type="number"
            name="alertThreshold"
            value={formData.alertThreshold}
            onChange={handleChange}
            min="0"
            max="100"
            required
          />
          <small>Get notified when spending reaches this percentage of the budget</small>
        </div>
        <div className="form-actions">
          <button type="submit" className="btn btn-primary">
            {initialData ? 'Update' : 'Create'} Budget
          </button>
          <button type="button" className="btn btn-secondary" onClick={onCancel}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default BudgetForm;
