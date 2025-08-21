import React, { useState, useEffect } from 'react';
import './LuxorLogo.scss';

const LuxorLogo = ({ size = 120, isDarkMode = false }) => {
  const [isAnimating, setIsAnimating] = useState(false);

  useEffect(() => {
    setIsAnimating(true);
  }, []);

  return (
    <div className="luxor-logo" style={{ '--logo-size': `${size}px` }}>
      {/* Outer Glow Circle */}
      <div className={`glow-circle ${isAnimating ? 'animating' : ''} ${isDarkMode ? 'dark' : 'light'}`}></div>
      
      {/* Main Logo Container */}
      <div className={`logo-container ${isAnimating ? 'animating' : ''}`}>
        {/* Gradient Border Circle */}
        <div className={`gradient-border ${isDarkMode ? 'dark' : 'light'}`}>
          {/* Inner Background Circle */}
          <div className={`inner-background ${isDarkMode ? 'dark' : 'light'}`}>
            {/* Logo Content */}
            <div className="logo-content">
              {/* Radial Glow */}
              <div className="radial-glow"></div>
              
              {/* Logo Text/Symbol */}
              <div className="logo-symbol">
                <div className="luxor-text">L</div>
                <div className="pharaoh-crown">
                  <div className="crown-top"></div>
                  <div className="crown-middle"></div>
                  <div className="crown-bottom"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      {/* Light Reflection */}
      <div className={`light-reflection ${isDarkMode ? 'dark' : 'light'}`}></div>
    </div>
  );
};

export default LuxorLogo;