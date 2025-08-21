import React, { useState } from 'react';
import './Login.scss';
import LuxorLogo from './LuxorLogo';

const Login = ({ onLogin }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isDarkMode, setIsDarkMode] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [rememberMe, setRememberMe] = useState(false);
  const [language, setLanguage] = useState('en'); // 'en' or 'ar'

  const isFormValid = email.includes('@') && password.length >= 8;

  const handleLogin = async (e) => {
    e.preventDefault();
    if (!isFormValid) return;
    
    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
      
      // Simulate successful login with user data
      const userData = {
        token: 'demo_token_' + Date.now(),
        user: {
          id: 1,
          name: 'Admin User',
          email: email,
          role: 'Administrator',
          avatar: null
        }
      };
      
      // Call the onLogin function passed from App component
      if (onLogin) {
        onLogin(userData);
      }
    }, 2000);
  };

  const handleForgotPassword = () => {
    console.log('Forgot password clicked');
  };

  const handleCreateAccount = () => {
    console.log('Create account clicked');
  };

  const handleSocialLogin = (provider) => {
    console.log(`${provider} login clicked`);
  };

  return (
    <div className={`login-container ${isDarkMode ? 'dark-mode' : 'light-mode'} ${language === 'ar' ? 'rtl' : 'ltr'}`}>
      {/* Animated Background Elements */}
      <div className="background-elements">
        <div className="floating-element element-1"></div>
        <div className="floating-element element-2"></div>
        <div className="floating-element element-3"></div>
      </div>

      {/* Mobile Animated Circles */}
      <div className="mobile-circles">
        <div className="circle circle-1"></div>
        <div className="circle circle-2"></div>
        <div className="circle circle-3"></div>
      </div>

      {/* Desktop Animated Circles */}
      <div className="desktop-circles">
        <div className="desktop-circle desktop-circle-1"></div>
        <div className="desktop-circle desktop-circle-2"></div>
        <div className="desktop-circle desktop-circle-3"></div>
      </div>

      {/* Controls */}
      <div className="top-controls">
        {/* Language Toggle */}
        <button 
          className="language-toggle"
          onClick={() => setLanguage(language === 'en' ? 'ar' : 'en')}
          aria-label="Toggle language"
        >
          <span className="lang-text">{language === 'en' ? 'العربية' : 'EN'}</span>
        </button>
        
        {/* Theme Toggle */}
        <button 
          className="theme-toggle"
          onClick={() => setIsDarkMode(!isDarkMode)}
          aria-label="Toggle theme"
        >
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            {isDarkMode ? (
              <path d="M12 3V4M12 20V21M4 12H3M6.31412 6.31412L5.5 5.5M17.6859 6.31412L18.5 5.5M6.31412 17.69L5.5 18.5M17.6859 17.69L18.5 18.5M21 12H20M16 12C16 14.2091 14.2091 16 12 16C9.79086 16 8 14.2091 8 12C8 9.79086 9.79086 8 12 8C14.2091 8 16 9.79086 16 12Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
            ) : (
              <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
            )}
          </svg>
        </button>
      </div>

      <div className={`login-card ${isDarkMode ? 'dark' : 'light'}`}>
        {/* Header Section */}
        <div className="login-header">
          <div className="logo-container">
            <LuxorLogo size={80} isDarkMode={isDarkMode} />
          </div>
          <div className="welcome-text">
            <h1 className="welcome-title">
              {language === 'en' ? 'Welcome to Luxor' : 'مرحباً بك في الأقصر'}
            </h1>
            <p className="welcome-subtitle">
              {language === 'en' 
                ? 'Enter your credentials to access your account' 
                : 'أدخل بياناتك للوصول إلى حسابك'
              }
            </p>
          </div>
        </div>

        {/* Login Form */}
        <form className="login-form" onSubmit={handleLogin}>
          <div className="form-group">
            <label className="form-label">
              {language === 'en' ? 'Email Address' : 'عنوان البريد الإلكتروني'}
            </label>
            <div className="input-container">
              <svg className="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none">
                <path d="M4 4H20C21.1 4 22 4.9 22 6V18C22 19.1 21.1 20 20 20H4C2.9 20 2 19.1 2 18V6C2 4.9 2.9 4 4 4Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                <polyline points="22,6 12,13 2,6" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder={language === 'en' ? 'Enter your email' : 'أدخل بريدك الإلكتروني'}
                className="form-input"
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">
              {language === 'en' ? 'Password' : 'كلمة المرور'}
            </label>
            <div className="input-container">
              <svg className="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2" stroke="currentColor" strokeWidth="2"/>
                <circle cx="12" cy="16" r="1" fill="currentColor"/>
                <path d="M7 11V7C7 5.67392 7.52678 4.40215 8.46447 3.46447C9.40215 2.52678 10.6739 2 12 2C13.3261 2 14.5979 2.52678 15.5355 3.46447C16.4732 4.40215 17 5.67392 17 7V11" stroke="currentColor" strokeWidth="2"/>
              </svg>
              <input
                type={showPassword ? 'text' : 'password'}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder={language === 'en' ? 'Enter your password' : 'أدخل كلمة المرور'}
                className="form-input"
                required
              />
              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword(!showPassword)}
              >
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  {showPassword ? (
                    <path d="M17.94 17.94C16.2306 19.243 14.1491 19.9649 12 20C5 20 1 12 1 12C2.24389 9.68192 4.231 7.81663 6.62 6.68M9.9 4.24C10.5883 4.0789 11.2931 3.99836 12 4C19 4 23 12 23 12C22.393 13.1356 21.6691 14.2048 20.84 15.19M14.12 14.12C13.8454 14.4148 13.5141 14.6512 13.1462 14.8151C12.7782 14.9791 12.3809 15.0673 11.9781 15.0744C11.5753 15.0815 11.1752 15.0074 10.8016 14.8565C10.4281 14.7056 10.0887 14.4811 9.80385 14.1962C9.51897 13.9113 9.29439 13.5719 9.14351 13.1984C8.99262 12.8248 8.91853 12.4247 8.92563 12.0219C8.93274 11.6191 9.02091 11.2218 9.18488 10.8538C9.34884 10.4858 9.58525 10.1546 9.88 9.88M1 1L23 23" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                  ) : (
                    <path d="M1 12C1 12 5 4 12 4C19 4 23 12 23 12C23 12 19 20 12 20C5 20 1 12 1 12Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                  )}
                  <circle cx="12" cy="12" r="3" stroke="currentColor" strokeWidth="2"/>
                </svg>
              </button>
            </div>
          </div>

          <div className="form-options">
            <label className="checkbox-container">
              <input
                type="checkbox"
                checked={rememberMe}
                onChange={(e) => setRememberMe(e.target.checked)}
              />
              <span className="checkmark"></span>
              <span className="checkbox-label">
                {language === 'en' ? 'Remember me' : 'تذكرني'}
              </span>
            </label>
            <button 
              type="button" 
              className="forgot-link"
              onClick={handleForgotPassword}
            >
              {language === 'en' ? 'Forgot Password?' : 'نسيت كلمة المرور؟'}
            </button>
          </div>

          {/* Login Button */}
          <button 
            type="submit" 
            className={`login-button ${isFormValid ? 'enabled' : 'disabled'}`}
            disabled={!isFormValid || isLoading}
          >
            {isLoading ? (
              <div className="loading-spinner">
                <div className="spinner"></div>
                <span>{language === 'en' ? 'Signing in...' : 'جاري تسجيل الدخول...'}</span>
              </div>
            ) : (
              <span>{language === 'en' ? 'Sign In' : 'تسجيل الدخول'}</span>
            )}
          </button>

          {/* Divider */}
          <div className="divider">
            <span className="divider-line"></span>
            <span className="divider-text">
              {language === 'en' ? 'or continue with' : 'أو تابع مع'}
            </span>
            <span className="divider-line"></span>
          </div>

          {/* Social Login */}
          <div className="social-login">
            <button 
              type="button" 
              className="social-button google"
              onClick={() => handleSocialLogin('Google')}
            >
              <svg className="social-icon" width="20" height="20" viewBox="0 0 24 24">
                <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/>
                <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/>
                <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#FBBC05"/>
                <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/>
              </svg>
              <span>Google</span>
            </button>
            
            <button 
              type="button" 
              className="social-button apple"
              onClick={() => handleSocialLogin('Apple')}
            >
              <svg className="social-icon" width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                <path d="M18.71 19.5c-.83 1.24-1.71 2.45-3.05 2.47-1.34.03-1.77-.79-3.29-.79-1.53 0-2 .77-3.27.82-1.31.05-2.3-1.32-3.14-2.53C4.25 17 2.94 12.45 4.7 9.39c.87-1.52 2.43-2.48 4.12-2.51 1.28-.02 2.5.87 3.29.87.78 0 2.26-1.07 3.81-.91.65.03 2.47.26 3.64 1.98-.09.06-2.17 1.28-2.15 3.81.03 3.02 2.65 4.03 2.68 4.04-.03.07-.42 1.44-1.38 2.83M13 3.5c.73-.83 1.94-1.46 2.94-1.5.13 1.17-.34 2.35-1.04 3.19-.69.85-1.83 1.51-2.95 1.42-.15-1.15.41-2.35 1.05-3.11z"/>
              </svg>
              <span>Apple</span>
            </button>
          </div>

          {/* Create Account Section */}
          <div className="create-account">
            <p className="account-text">
              {language === 'en' ? "Don't have an account?" : 'ليس لديك حساب؟'}
            </p>
            <button 
              type="button" 
              className="create-link"
              onClick={handleCreateAccount}
            >
              {language === 'en' ? 'Create Account' : 'إنشاء حساب'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;