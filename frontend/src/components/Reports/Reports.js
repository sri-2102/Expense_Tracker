import React, { useState, useEffect } from 'react';
import { expenseService, reportService } from '../../services/api';
import { PieChart, Pie, Cell, ResponsiveContainer, Legend, Tooltip } from 'recharts';
import { format, startOfMonth, endOfMonth, startOfYear, endOfYear } from 'date-fns';
import { toast } from 'react-toastify';
import './Reports.css';

const Reports = () => {
  const [reportType, setReportType] = useState('monthly');
  const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth() + 1);
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());
  const [expenses, setExpenses] = useState([]);
  const [chartData, setChartData] = useState([]);
  const [totalAmount, setTotalAmount] = useState(0);
  const [loading, setLoading] = useState(false);

  const COLORS = [
    '#4f46e5', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6',
    '#ec4899', '#06b6d4', '#84cc16', '#f97316', '#6366f1',
    '#14b8a6', '#a855f7', '#22c55e', '#eab308', '#64748b'
  ];

  useEffect(() => {
    fetchExpenses();
  }, [reportType, selectedMonth, selectedYear]);

  const fetchExpenses = async () => {
    setLoading(true);
    try {
      let startDate, endDate;

      if (reportType === 'monthly') {
        const date = new Date(selectedYear, selectedMonth - 1, 1);
        startDate = format(startOfMonth(date), 'yyyy-MM-dd');
        endDate = format(endOfMonth(date), 'yyyy-MM-dd');
      } else {
        const date = new Date(selectedYear, 0, 1);
        startDate = format(startOfYear(date), 'yyyy-MM-dd');
        endDate = format(endOfYear(date), 'yyyy-MM-dd');
      }

      const response = await expenseService.getExpensesByDateRange(startDate, endDate);
      const expensesData = response.data;
      setExpenses(expensesData);

      // Calculate category totals
      const categoryTotals = {};
      let total = 0;

      expensesData.forEach((expense) => {
        const category = expense.category;
        if (!categoryTotals[category]) {
          categoryTotals[category] = 0;
        }
        categoryTotals[category] += expense.amount;
        total += expense.amount;
      });

      // Convert to chart data format
      const chartData = Object.entries(categoryTotals).map(([category, amount]) => ({
        name: category.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, (l) => l.toUpperCase()),
        value: amount,
        percentage: ((amount / total) * 100).toFixed(1),
      }));

      setChartData(chartData);
      setTotalAmount(total);
    } catch (error) {
      toast.error('Failed to fetch expenses');
    } finally {
      setLoading(false);
    }
  };

  const downloadPDFReport = async () => {
    try {
      setLoading(true);
      let response;

      console.log('Downloading report:', reportType, selectedYear, selectedMonth);

      if (reportType === 'monthly') {
        response = await reportService.generateMonthlyReport(selectedYear, selectedMonth);
      } else {
        response = await reportService.generateYearlyReport(selectedYear);
      }

      console.log('Report response received:', response);

      // Create blob and download
      const blob = new Blob([response.data], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `expense-report-${reportType}-${selectedYear}${
        reportType === 'monthly' ? `-${selectedMonth}` : ''
      }.pdf`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);

      toast.success('Report downloaded successfully!');
    } catch (error) {
      console.error('Error downloading report:', error);
      const errorMessage = error.response?.data?.message || 
                          error.message || 
                          'Failed to download report';
      toast.error(errorMessage);
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

  const months = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];

  const currentYear = new Date().getFullYear();
  const years = Array.from({ length: 5 }, (_, i) => currentYear - 2 + i);

  const renderCustomLabel = ({ cx, cy, midAngle, innerRadius, outerRadius, percentage }) => {
    const RADIAN = Math.PI / 180;
    const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
    const x = cx + radius * Math.cos(-midAngle * RADIAN);
    const y = cy + radius * Math.sin(-midAngle * RADIAN);

    return (
      <text
        x={x}
        y={y}
        fill="white"
        textAnchor={x > cx ? 'start' : 'end'}
        dominantBaseline="central"
        fontSize="14"
        fontWeight="600"
      >
        {`${percentage}%`}
      </text>
    );
  };

  return (
    <div className="reports-container">
      <div className="reports-header">
        <h2>Expense Reports & Analytics</h2>
        <button
          className="btn btn-success"
          onClick={downloadPDFReport}
          disabled={loading || expenses.length === 0}
        >
          📥 Download PDF Report
        </button>
      </div>

      <div className="card">
        <div className="report-controls">
          <div className="form-group">
            <label>Report Type</label>
            <select value={reportType} onChange={(e) => setReportType(e.target.value)}>
              <option value="monthly">Monthly Report</option>
              <option value="yearly">Yearly Report</option>
            </select>
          </div>

          {reportType === 'monthly' && (
            <div className="form-group">
              <label>Month</label>
              <select
                value={selectedMonth}
                onChange={(e) => setSelectedMonth(parseInt(e.target.value))}
              >
                {months.map((month, index) => (
                  <option key={index} value={index + 1}>
                    {month}
                  </option>
                ))}
              </select>
            </div>
          )}

          <div className="form-group">
            <label>Year</label>
            <select
              value={selectedYear}
              onChange={(e) => setSelectedYear(parseInt(e.target.value))}
            >
              {years.map((year) => (
                <option key={year} value={year}>
                  {year}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {loading ? (
        <div className="loading">Loading report data...</div>
      ) : expenses.length === 0 ? (
        <div className="card">
          <p className="no-data">No expenses found for the selected period.</p>
        </div>
      ) : (
        <>
          <div className="report-summary card">
            <h3>Summary</h3>
            <div className="summary-grid">
              <div className="summary-item">
                <span className="summary-label">Total Expenses</span>
                <span className="summary-value total">{formatCurrency(totalAmount)}</span>
              </div>
              <div className="summary-item">
                <span className="summary-label">Number of Transactions</span>
                <span className="summary-value">{expenses.length}</span>
              </div>
              <div className="summary-item">
                <span className="summary-label">Average per Transaction</span>
                <span className="summary-value">
                  {formatCurrency(totalAmount / expenses.length)}
                </span>
              </div>
              <div className="summary-item">
                <span className="summary-label">Categories Used</span>
                <span className="summary-value">{chartData.length}</span>
              </div>
            </div>
          </div>

          <div className="report-visualization card">
            <h3>Expense Distribution by Category</h3>
            <div className="chart-container">
              <ResponsiveContainer width="100%" height={400}>
                <PieChart>
                  <Pie
                    data={chartData}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    label={renderCustomLabel}
                    outerRadius={150}
                    fill="#8884d8"
                    dataKey="value"
                  >
                    {chartData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip
                    formatter={(value) => formatCurrency(value)}
                    contentStyle={{
                      backgroundColor: 'white',
                      border: '1px solid #e5e7eb',
                      borderRadius: '8px',
                    }}
                  />
                  <Legend
                    verticalAlign="bottom"
                    height={36}
                    formatter={(value, entry) => `${value}: ${formatCurrency(entry.payload.value)}`}
                  />
                </PieChart>
              </ResponsiveContainer>
            </div>

            <div className="category-breakdown">
              <h4>Category Breakdown</h4>
              <table>
                <thead>
                  <tr>
                    <th>Category</th>
                    <th>Amount</th>
                    <th>Percentage</th>
                  </tr>
                </thead>
                <tbody>
                  {chartData
                    .sort((a, b) => b.value - a.value)
                    .map((category, index) => (
                      <tr key={index}>
                        <td>
                          <span
                            className="category-color"
                            style={{ backgroundColor: COLORS[index % COLORS.length] }}
                          />
                          {category.name}
                        </td>
                        <td className="amount">{formatCurrency(category.value)}</td>
                        <td>{category.percentage}%</td>
                      </tr>
                    ))}
                </tbody>
              </table>
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default Reports;
