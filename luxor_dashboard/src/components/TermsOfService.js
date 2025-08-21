import React from 'react';
import './PolicyPages.scss';

const TermsOfService = () => {
  return (
    <div className="policy-page">
      <div className="policy-container">
        <header className="policy-header">
          <h1>📋 Terms of Service</h1>
          <p className="last-updated">Last updated: January 2024</p>
        </header>
        
        <div className="policy-content">
          <section className="policy-section">
            <h2>1. Acceptance of Terms</h2>
            <p>
              By accessing and using Luxor Dashboard, you accept and agree to be bound by the terms 
              and provision of this agreement. If you do not agree to abide by the above, please do 
              not use this service.
            </p>
          </section>
          
          <section className="policy-section">
            <h2>2. Use License</h2>
            <p>
              Permission is granted to temporarily download one copy of Luxor Dashboard per device 
              for personal, non-commercial transitory viewing only. This is the grant of a license, 
              not a transfer of title, and under this license you may not:
            </p>
            <ul>
              <li>Modify or copy the materials</li>
              <li>Use the materials for any commercial purpose or for any public display</li>
              <li>Attempt to reverse engineer any software contained in the service</li>
              <li>Remove any copyright or other proprietary notations</li>
            </ul>
          </section>
          
          <section className="policy-section">
            <h2>3. User Accounts</h2>
            <p>When you create an account with us, you must provide information that is accurate, complete, and current at all times. You are responsible for:</p>
            <ul>
              <li>Safeguarding the password and all activities under your account</li>
              <li>Notifying us immediately of any unauthorized use</li>
              <li>Ensuring your account information remains accurate</li>
              <li>Complying with all applicable laws and regulations</li>
            </ul>
          </section>
          
          <section className="policy-section">
            <h2>4. Prohibited Uses</h2>
            <p>You may not use our service:</p>
            <ul>
              <li>For any unlawful purpose or to solicit others to unlawful acts</li>
              <li>To violate any international, federal, provincial, or state regulations, rules, laws, or local ordinances</li>
              <li>To infringe upon or violate our intellectual property rights or the intellectual property rights of others</li>
              <li>To harass, abuse, insult, harm, defame, slander, disparage, intimidate, or discriminate</li>
              <li>To submit false or misleading information</li>
            </ul>
          </section>
          
          <section className="policy-section">
            <h2>5. Service Availability</h2>
            <p>
              We strive to provide continuous service availability, but we do not guarantee that our 
              service will be uninterrupted or error-free. We reserve the right to modify, suspend, 
              or discontinue the service at any time without notice.
            </p>
          </section>
          
          <section className="policy-section">
            <h2>6. Limitation of Liability</h2>
            <p>
              In no event shall Luxor Dashboard or its suppliers be liable for any damages 
              (including, without limitation, damages for loss of data or profit, or due to business 
              interruption) arising out of the use or inability to use the service.
            </p>
          </section>
          
          <section className="policy-section">
            <h2>7. Governing Law</h2>
            <p>
              These terms and conditions are governed by and construed in accordance with the laws 
              and you irrevocably submit to the exclusive jurisdiction of the courts in that state 
              or location.
            </p>
          </section>
          
          <section className="policy-section">
            <h2>8. Contact Information</h2>
            <p>
              If you have any questions about these Terms of Service, please contact us at:
            </p>
            <div className="contact-info">
              <p>📧 Email: mo7amed455@gmail.com</p>
              <p>📞 Phone: +971 (52) 537-4187</p>
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

export default TermsOfService;