# Expense Tracker Frontend

React.js frontend for the Expense Tracker application.

## Features

- 🔐 User Authentication (Login/Register)
- 💰 Expense Management (CRUD operations)
- 🎯 Budget Management with visual progress indicators
- 📊 Interactive Reports with Pie Charts
- 📥 PDF Report Generation
- 📱 Responsive Design
- 🎨 Modern UI with clean aesthetics

## Tech Stack

- **React 18.2.0** - UI Library
- **React Router 6.16.0** - Navigation
- **Axios 1.5.0** - HTTP Client
- **Recharts 2.8.0** - Data Visualization
- **React Toastify 9.1.3** - Notifications
- **date-fns 2.30.0** - Date Utilities

## Prerequisites

- Node.js 16+ and npm
- Backend API running on http://localhost:8080

## Installation

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The application will open at http://localhost:3000

## Available Scripts

- `npm start` - Run development server
- `npm build` - Build for production
- `npm test` - Run tests
- `npm eject` - Eject from Create React App (one-way operation)

## Project Structure

```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── Auth/
│   │   │   ├── Login.js
│   │   │   ├── Register.js
│   │   │   └── Auth.css
│   │   ├── Dashboard/
│   │   │   ├── Dashboard.js
│   │   │   └── Dashboard.css
│   │   ├── Expenses/
│   │   │   ├── Expenses.js
│   │   │   ├── ExpenseForm.js
│   │   │   ├── ExpenseList.js
│   │   │   └── Expenses.css
│   │   ├── Budgets/
│   │   │   ├── Budgets.js
│   │   │   ├── BudgetForm.js
│   │   │   ├── BudgetList.js
│   │   │   └── Budgets.css
│   │   ├── Reports/
│   │   │   ├── Reports.js
│   │   │   └── Reports.css
│   │   ├── Navbar.js
│   │   ├── Navbar.css
│   │   └── PrivateRoute.js
│   ├── context/
│   │   └── AuthContext.js
│   ├── services/
│   │   └── api.js
│   ├── App.js
│   ├── index.js
│   └── index.css
├── package.json
└── README.md
```

## API Configuration

The frontend is configured to proxy API requests to `http://localhost:8080`. This is set in `package.json`:

```json
"proxy": "http://localhost:8080"
```

If your backend runs on a different port, update this value.

## Features Overview

### Authentication
- User registration with validation
- Secure login with JWT tokens
- Protected routes
- Automatic token refresh

### Dashboard
- Overview of total expenses
- Current month statistics
- Top spending category
- Recent transactions
- Quick action buttons

### Expense Management
- Add, edit, and delete expenses
- Filter by category and date range
- 15 expense categories
- Notes for each transaction
- Real-time expense totals

### Budget Management
- Create monthly budgets per category
- Visual progress bars
- Alert thresholds
- Automatic budget notifications
- Spent vs. limit tracking

### Reports & Analytics
- Monthly and yearly reports
- Interactive pie charts
- Category breakdown
- PDF report download
- Expense distribution analysis

## Environment Variables

Create a `.env` file in the frontend directory:

```
REACT_APP_API_URL=http://localhost:8080/api
```

## Building for Production

1. Create production build:
```bash
npm run build
```

2. The build folder will contain optimized static files ready for deployment.

3. Deploy to any static hosting service:
   - Netlify
   - Vercel
   - AWS S3 + CloudFront
   - GitHub Pages

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

MIT License
