import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isActive = (path) => {
    return location.pathname === path ? 'active' : '';
  };

  if (!user) {
    return null;
  }

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/dashboard" className="navbar-brand">
          💰 Expense Tracker
        </Link>

        <div className="navbar-menu">
          <Link to="/dashboard" className={`nav-link ${isActive('/dashboard')}`}>
            Dashboard
          </Link>
          <Link to="/expenses" className={`nav-link ${isActive('/expenses')}`}>
            Expenses
          </Link>
          <Link to="/budgets" className={`nav-link ${isActive('/budgets')}`}>
            Budgets
          </Link>
          <Link to="/reports" className={`nav-link ${isActive('/reports')}`}>
            Reports
          </Link>
        </div>

        <div className="navbar-user">
          <span className="user-name">👤 {user.username}</span>
          <button className="btn btn-secondary btn-sm" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
