import React from 'react';
import './PolicyPages.scss';

const PrivacyPolicy = () => {
  return (
    <div className="policy-page">
      <div className="policy-container">
        <header className="policy-header">
          <h1>🔒 Privacy Policy</h1>
          <p className="last-updated">Last updated: January 2024</p>
        </header>
        
        <div className="policy-content">
          <section className="policy-section">
            <h2>1. Information We Collect</h2>
            <p>
              We collect information you provide directly to us, such as when you create an account, 
              use our services, or contact us for support. This may include:
            </p>
            <ul>
              <li>Personal information (name, email address, phone number)</li>
              <li>Account credentials and preferences</li>
              <li>Usage data and analytics</li>
              <li>Device and browser information</li>
            </ul>
          </section>
          
          <section className="policy-section">
            <h2>2. How We Use Your Information</h2>
            <p>We use the information we collect to:</p>
            <ul>
              <li>Provide, maintain, and improve our services</li>
              <li>Process transactions and send related information</li>
              <li>Send technical notices and support messages</li>
              <li>Respond to your comments and questions</li>
              <li>Analyze usage patterns and trends</li>
            </ul>
          </section>
          
          <section className="policy-section">
            <h2>3. Information Sharing</h2>
            <p>
              We do not sell, trade, or otherwise transfer your personal information to third parties 
              without your consent, except as described in this policy. We may share information:
            </p>
            <ul>
              <li>With service providers who assist in our operations</li>
              <li>To comply with legal obligations</li>
              <li>To protect our rights and safety</li>
              <li>In connection with a business transfer</li>
            </ul>
          </section>
          
          <section className="policy-section">
            <h2>4. Data Security</h2>
            <p>
              We implement appropriate security measures to protect your personal information against 
              unauthorized access, alteration, disclosure, or destruction. However, no method of 
              transmission over the internet is 100% secure.
            </p>
          </section>
          
          <section className="policy-section">
            <h2>5. Your Rights</h2>
            <p>You have the right to:</p>
            <ul>
              <li>Access and update your personal information</li>
              <li>Request deletion of your data</li>
              <li>Opt-out of certain communications</li>
              <li>Request data portability</li>
            </ul>
          </section>
          
          <section className="policy-section">
            <h2>6. Contact Us</h2>
            <p>
              If you have any questions about this Privacy Policy, please contact us at:
            </p>
            <div className="contact-info">
              <p>📧 Email: privacy@luxordashboard.com</p>
              <p>📞 Phone: +1 (555) 123-4567</p>
              <p>📍 Address: 123 Dashboard Street, Tech City, TC 12345</p>
            </div>
          </section>
        </div>
        
        <footer className="policy-footer">
          <button className="back-button" onClick={() => window.close()}>
            ← Back to Dashboard
          </button>
        </footer>
      </div>
    </div>
  );
};

export default PrivacyPolicy;