import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

// Create axios instance
const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Unauthorized - redirect to login
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Authentication Services
export const authService = {
  register: (userData) => api.post('/auth/register', userData),
  login: (credentials) => api.post('/auth/login', credentials),
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },
  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },
  isAuthenticated: () => {
    return !!localStorage.getItem('token');
  },
};

// Expense Services
export const expenseService = {
  getAllExpenses: () => api.get('/expenses'),
  getExpenseById: (id) => api.get(`/expenses/${id}`),
  createExpense: (expenseData) => api.post('/expenses', expenseData),
  updateExpense: (id, expenseData) => api.put(`/expenses/${id}`, expenseData),
  deleteExpense: (id) => api.delete(`/expenses/${id}`),
  getExpensesByDateRange: (startDate, endDate) =>
    api.get(`/expenses/date-range?startDate=${startDate}&endDate=${endDate}`),
  getExpensesByCategory: (category) =>
    api.get(`/expenses/category/${category}`),
  getTotalExpenses: (startDate, endDate) =>
    api.get(`/expenses/total?startDate=${startDate}&endDate=${endDate}`),
};

// Budget Services
export const budgetService = {
  getAllBudgets: () => api.get('/budgets'),
  getBudgetById: (id) => api.get(`/budgets/${id}`),
  createBudget: (budgetData) => api.post('/budgets', budgetData),
  updateBudget: (id, budgetData) => api.put(`/budgets/${id}`, budgetData),
  deleteBudget: (id) => api.delete(`/budgets/${id}`),
  getBudgetsByCategory: (category) => api.get(`/budgets/category/${category}`),
};

// Report Services
export const reportService = {
  generateMonthlyReport: (year, month) =>
    api.get(`/reports/monthly/${year}/${month}`, { responseType: 'blob' }),
  generateYearlyReport: (year) =>
    api.get(`/reports/yearly/${year}`, { responseType: 'blob' }),
  getCategoryChart: (startDate, endDate) =>
    api.get(`/reports/chart/category?startDate=${startDate}&endDate=${endDate}`, {
      responseType: 'blob',
    }),
};

export default api;
