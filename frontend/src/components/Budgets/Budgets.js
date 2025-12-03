import React, { useState, useEffect } from 'react';
import { budgetService } from '../../services/api';
import { toast } from 'react-toastify';
import BudgetForm from './BudgetForm';
import BudgetList from './BudgetList';
import './Budgets.css';

const Budgets = () => {
  const [budgets, setBudgets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingBudget, setEditingBudget] = useState(null);

  useEffect(() => {
    fetchBudgets();
  }, []);

  const fetchBudgets = async () => {
    try {
      const response = await budgetService.getAllBudgets();
      setBudgets(response.data);
    } catch (error) {
      toast.error('Failed to fetch budgets');
    } finally {
      setLoading(false);
    }
  };

  const handleAddBudget = async (budgetData) => {
    try {
      await budgetService.createBudget(budgetData);
      toast.success('Budget created successfully!');
      fetchBudgets();
      setShowForm(false);
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to create budget');
    }
  };

  const handleEditBudget = async (id, budgetData) => {
    try {
      await budgetService.updateBudget(id, budgetData);
      toast.success('Budget updated successfully!');
      fetchBudgets();
      setEditingBudget(null);
      setShowForm(false);
    } catch (error) {
      toast.error('Failed to update budget');
    }
  };

  const handleDeleteBudget = async (id) => {
    if (window.confirm('Are you sure you want to delete this budget?')) {
      try {
        await budgetService.deleteBudget(id);
        toast.success('Budget deleted successfully!');
        fetchBudgets();
      } catch (error) {
        toast.error('Failed to delete budget');
      }
    }
  };

  const handleEdit = (budget) => {
    setEditingBudget(budget);
    setShowForm(true);
  };

  const handleCancelForm = () => {
    setShowForm(false);
    setEditingBudget(null);
  };

  if (loading) {
    return <div className="loading">Loading budgets...</div>;
  }

  return (
    <div className="budgets-container">
      <div className="budgets-header">
        <h2>Budget Management</h2>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : '+ Add Budget'}
        </button>
      </div>

      {showForm && (
        <BudgetForm
          onSubmit={editingBudget ? handleEditBudget : handleAddBudget}
          initialData={editingBudget}
          onCancel={handleCancelForm}
        />
      )}

      <BudgetList
        budgets={budgets}
        onEdit={handleEdit}
        onDelete={handleDeleteBudget}
      />
    </div>
  );
};

export default Budgets;
