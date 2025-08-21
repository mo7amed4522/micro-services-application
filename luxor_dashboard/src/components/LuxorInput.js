import React, { useState } from 'react';
import './LuxorInput.scss';

const LuxorInput = ({ 
  type = 'text', 
  value, 
  onChange, 
  placeholder, 
  icon, 
  isDarkMode = false 
}) => {
  const [isEditing, setIsEditing] = useState(false);
  const [isValid, setIsValid] = useState(true);
  const [showPassword, setShowPassword] = useState(false);

  const validateInput = (inputValue) => {
    if (type === 'email') {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailRegex.test(inputValue) || inputValue === '';
    }
    if (type === 'password') {
      return inputValue.length >= 8 || inputValue === '';
    }
    return true;
  };

  const handleInputChange = (e) => {
    const newValue = e.target.value;
    onChange(newValue);
    setIsValid(validateInput(newValue));
  };

  const handleFocus = () => {
    setIsEditing(true);
  };

  const handleBlur = () => {
    setIsEditing(false);
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const getIconSymbol = () => {
    switch (icon) {
      case 'envelope':
        return isEditing ? '📧' : '✉️';
      case 'lock':
        return showPassword ? '🔓' : '🔒';
      default:
        return '📝';
    }
  };

  const getValidationIcon = () => {
    if (value === '') return null;
    return isValid ? '✅' : '❌';
  };

  return (
    <div className="luxor-input-container">
      <div className={`luxor-input-wrapper ${
        isEditing ? 'editing' : ''
      } ${
        !isValid ? 'invalid' : ''
      } ${
        isDarkMode ? 'dark' : 'light'
      }`}>
        {/* Input Field */}
        <div className="input-field">
          {/* Icon */}
          <div className={`input-icon ${isEditing ? 'active' : ''}`}>
            <span className="icon-symbol">{getIconSymbol()}</span>
          </div>

          {/* Input Content */}
          <div className="input-content">
            {/* Floating Label */}
            <label className={`floating-label ${
              isEditing || value ? 'active' : ''
            }`}>
              {placeholder}
            </label>

            {/* Input Element */}
            <input
              type={type === 'password' && showPassword ? 'text' : type}
              value={value}
              onChange={handleInputChange}
              onFocus={handleFocus}
              onBlur={handleBlur}
              className="input-element"
              autoComplete={type === 'password' ? 'current-password' : 'email'}
            />
          </div>

          {/* Right Side Actions */}
          <div className="input-actions">
            {/* Password Toggle */}
            {type === 'password' && (
              <button
                type="button"
                className="password-toggle"
                onClick={togglePasswordVisibility}
                aria-label={showPassword ? 'Hide password' : 'Show password'}
              >
                <span className="toggle-icon">
                  {showPassword ? '👁️' : '👁️‍🗨️'}
                </span>
              </button>
            )}

            {/* Validation Icon */}
            {getValidationIcon() && (
              <div className={`validation-icon ${isValid ? 'valid' : 'invalid'}`}>
                <span>{getValidationIcon()}</span>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Validation Message */}
      {!isValid && value && (
        <div className="validation-message">
          <span className="error-icon">⚠️</span>
          <span className="error-text">
            {type === 'email' 
              ? 'Please enter a valid email address'
              : 'Password must be at least 8 characters'
            }
          </span>
        </div>
      )}

      {/* Password Requirements */}
      {type === 'password' && value && (
        <div className="password-requirements">
          <div className="requirements-title">Password Requirements</div>
          <div className="requirements-list">
            <div className={`requirement ${value.length >= 8 ? 'met' : ''}`}>
              <span className="req-icon">{value.length >= 8 ? '✅' : '⭕'}</span>
              <span>At least 8 characters</span>
            </div>
            <div className={`requirement ${/[A-Z]/.test(value) ? 'met' : ''}`}>
              <span className="req-icon">{/[A-Z]/.test(value) ? '✅' : '⭕'}</span>
              <span>At least one capital letter</span>
            </div>
            <div className={`requirement ${/[!@#$%^&*(),.?":{}|<>]/.test(value) ? 'met' : ''}`}>
              <span className="req-icon">{/[!@#$%^&*(),.?":{}|<>]/.test(value) ? '✅' : '⭕'}</span>
              <span>At least one special character</span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default LuxorInput;