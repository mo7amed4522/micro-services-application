import './App.scss';
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/Login';
import Dashboard from './components/Dashboard';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('luxor_auth_token');
    if (token) {
      setIsAuthenticated(true);
    }
    setIsLoading(false);
  }, []);
  const handleLogin = (userData) => {
    localStorage.setItem('luxor_auth_token', userData.token || 'demo_token');
    localStorage.setItem('luxor_user_data', JSON.stringify(userData));
    setIsAuthenticated(true);
  };
  const handleLogout = () => {
    localStorage.removeItem('luxor_auth_token');
    localStorage.removeItem('luxor_user_data');
    setIsAuthenticated(false);
  };

  if (isLoading) {
    return (
      <div className="App loading">
        <div className="loading-spinner">
          <div className="spinner"></div>
          <p>Loading Luxor Dashboard...</p>
        </div>
      </div>
    );
  }

  return (
    <Router>
      <div className="App">
        <Routes>
          <Route 
            path="/login" 
            element={
              isAuthenticated ? 
              <Navigate to="/dashboard" replace /> : 
              <Login onLogin={handleLogin} />
            } 
          />
          <Route 
            path="/dashboard" 
            element={
              isAuthenticated ? 
              <Dashboard onLogout={handleLogout} /> : 
              <Navigate to="/login" replace />
            } 
          />

          <Route 
            path="/" 
            element={
              <Navigate to={isAuthenticated ? "/dashboard" : "/login"} replace />
            } 
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
