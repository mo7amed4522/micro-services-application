import React, { useState, useEffect } from 'react';
import './Dashboard.scss';

const Dashboard = ({ onLogout }) => {
  const [activeSection, setActiveSection] = useState('overview');
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [language, setLanguage] = useState('en');
  const [darkMode, setDarkMode] = useState(false);
  const [user, setUser] = useState({
    name: 'Admin User',
    avatar: null,
    role: 'Administrator'
  });

  // Language translations
  const translations = {
    en: {
      overview: 'Overview',
      reals: 'Reals',
      videos: 'Videos',
      posts: 'Posts',
      users: 'Users',
      visitors: 'Visitors',
      tourism: 'Tourism',
      tourGuides: 'Tour Guides',
      appVersion: 'App Version',
      police: 'Police',
      privacy: 'Privacy',
      payment: 'Payment',
      trips: 'Trips',
      dashboard: 'Luxor Dashboard',
      welcome: 'Welcome back',
      logout: 'Logout',
      settings: 'Settings',
      notifications: 'Notifications'
    },
    ar: {
      overview: 'نظرة عامة',
      reals: 'الحقائق',
      videos: 'الفيديوهات',
      posts: 'المنشورات',
      users: 'المستخدمين',
      visitors: 'الزوار',
      tourism: 'السياحة',
      tourGuides: 'المرشدين السياحيين',
      appVersion: 'إصدار التطبيق',
      police: 'الشرطة',
      privacy: 'الخصوصية',
      payment: 'الدفع',
      trips: 'الرحلات',
      dashboard: 'لوحة تحكم الأقصر',
      welcome: 'مرحباً بعودتك',
      logout: 'تسجيل الخروج',
      settings: 'الإعدادات',
      notifications: 'الإشعارات'
    },
    fr: {
      overview: 'Aperçu',
      reals: 'Réels',
      videos: 'Vidéos',
      posts: 'Publications',
      users: 'Utilisateurs',
      visitors: 'Visiteurs',
      tourism: 'Tourisme',
      tourGuides: 'Guides Touristiques',
      appVersion: 'Version de l\'App',
      police: 'Police',
      privacy: 'Confidentialité',
      payment: 'Paiement',
      trips: 'Voyages',
      dashboard: 'Tableau de Bord Luxor',
      welcome: 'Bon retour',
      logout: 'Déconnexion',
      settings: 'Paramètres',
      notifications: 'Notifications'
    },
    de: {
      overview: 'Übersicht',
      reals: 'Reals',
      videos: 'Videos',
      posts: 'Beiträge',
      users: 'Benutzer',
      visitors: 'Besucher',
      tourism: 'Tourismus',
      tourGuides: 'Reiseführer',
      appVersion: 'App-Version',
      police: 'Polizei',
      privacy: 'Datenschutz',
      payment: 'Zahlung',
      trips: 'Reisen',
      dashboard: 'Luxor Dashboard',
      welcome: 'Willkommen zurück',
      logout: 'Abmelden',
      settings: 'Einstellungen',
      notifications: 'Benachrichtigungen'
    }
  };

  const t = translations[language];

  // Navigation items
  const navigationItems = [
    { id: 'overview', icon: '📊', label: t.overview },
    { id: 'reals', icon: '🎬', label: t.reals },
    { id: 'videos', icon: '📹', label: t.videos },
    { id: 'posts', icon: '📝', label: t.posts },
    { id: 'users', icon: '👥', label: t.users },
    { id: 'visitors', icon: '🧳', label: t.visitors },
    { id: 'tourism', icon: '🏛️', label: t.tourism },
    { id: 'tourGuides', icon: '🗺️', label: t.tourGuides },
    { id: 'appVersion', icon: '📱', label: t.appVersion },
    { id: 'police', icon: '👮', label: t.police },
    { id: 'privacy', icon: '🔒', label: t.privacy },
    { id: 'payment', icon: '💳', label: t.payment },
    { id: 'trips', icon: '✈️', label: t.trips }
  ];

  // Toggle sidebar
  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  // Toggle theme
  const toggleTheme = () => {
    setDarkMode(!darkMode);
  };

  // Change language
  const changeLanguage = (lang) => {
    setLanguage(lang);
  };

  // Handle section change
  const handleSectionChange = (sectionId) => {
    setActiveSection(sectionId);
  };

  // Render content based on active section
  const renderContent = () => {
    switch (activeSection) {
      case 'overview':
        return (
          <div className="content-section overview-section">
            <h2>{t.overview}</h2>
            <div className="stats-grid">
              <div className="stat-card">
                <div className="stat-icon">👥</div>
                <div className="stat-info">
                  <h3>12,543</h3>
                  <p>{t.users}</p>
                  <span className="stat-change positive">+12%</span>
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-icon">🧳</div>
                <div className="stat-info">
                  <h3>8,921</h3>
                  <p>{t.visitors}</p>
                  <span className="stat-change positive">+8%</span>
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-icon">✈️</div>
                <div className="stat-info">
                  <h3>1,234</h3>
                  <p>{t.trips}</p>
                  <span className="stat-change positive">+15%</span>
                </div>
              </div>
              <div className="stat-card">
                <div className="stat-icon">🎬</div>
                <div className="stat-info">
                  <h3>567</h3>
                  <p>{t.reals}</p>
                  <span className="stat-change positive">+23%</span>
                </div>
              </div>
            </div>
            
            <div className="charts-section">
              <div className="chart-card">
                <h3>Monthly Visitors</h3>
                <div className="chart-placeholder">
                  <div className="chart-bars">
                    <div className="bar" style={{height: '60%'}}></div>
                    <div className="bar" style={{height: '80%'}}></div>
                    <div className="bar" style={{height: '45%'}}></div>
                    <div className="bar" style={{height: '90%'}}></div>
                    <div className="bar" style={{height: '70%'}}></div>
                    <div className="bar" style={{height: '95%'}}></div>
                  </div>
                </div>
              </div>
              
              <div className="chart-card">
                <h3>Revenue Overview</h3>
                <div className="chart-placeholder">
                  <div className="pie-chart">
                    <div className="pie-segment" style={{background: 'conic-gradient(#FFD700 0deg 120deg, #D4A017 120deg 240deg, #1E3A8A 240deg 360deg)'}}></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        );
        
      case 'reals':
        return (
          <div className="content-section reals-section">
            <h2>{t.reals}</h2>
            <div className="section-header">
              <button className="action-btn primary">+ Add New Real</button>
              <div className="search-filter">
                <input type="text" placeholder="Search reals..." />
                <select>
                  <option>All Categories</option>
                  <option>Historical</option>
                  <option>Cultural</option>
                  <option>Adventure</option>
                </select>
              </div>
            </div>
            
            <div className="content-grid">
              {[1,2,3,4,5,6].map(item => (
                <div key={item} className="content-card">
                  <div className="content-thumbnail">
                    <div className="play-button">▶️</div>
                    <span className="duration">2:30</span>
                  </div>
                  <div className="content-info">
                    <h4>Luxor Temple Tour {item}</h4>
                    <p>Amazing historical journey through ancient Egypt</p>
                    <div className="content-stats">
                      <span>👁️ 1.2K views</span>
                      <span>❤️ 89 likes</span>
                      <span>💬 12 comments</span>
                    </div>
                  </div>
                  <div className="content-actions">
                    <button className="edit-btn">✏️</button>
                    <button className="delete-btn">🗑️</button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        );
        
      case 'videos':
        return (
          <div className="content-section videos-section">
            <h2>{t.videos}</h2>
            <div className="section-header">
              <button className="action-btn primary">+ Upload Video</button>
              <div className="search-filter">
                <input type="text" placeholder="Search videos..." />
                <select>
                  <option>All Types</option>
                  <option>Promotional</option>
                  <option>Educational</option>
                  <option>Documentary</option>
                </select>
              </div>
            </div>
            
            <div className="content-grid">
              {[1,2,3,4,5,6].map(item => (
                <div key={item} className="content-card">
                  <div className="content-thumbnail">
                    <div className="play-button">▶️</div>
                    <span className="duration">5:45</span>
                  </div>
                  <div className="content-info">
                    <h4>Discover Luxor {item}</h4>
                    <p>Professional video showcasing Luxor's beauty</p>
                    <div className="content-stats">
                      <span>👁️ 5.2K views</span>
                      <span>❤️ 234 likes</span>
                      <span>💬 45 comments</span>
                    </div>
                  </div>
                  <div className="content-actions">
                    <button className="edit-btn">✏️</button>
                    <button className="delete-btn">🗑️</button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        );
        
      case 'posts':
        return (
          <div className="content-section posts-section">
            <h2>{t.posts}</h2>
            <div className="section-header">
              <button className="action-btn primary">+ Create Post</button>
              <div className="search-filter">
                <input type="text" placeholder="Search posts..." />
                <select>
                  <option>All Status</option>
                  <option>Published</option>
                  <option>Draft</option>
                  <option>Scheduled</option>
                </select>
              </div>
            </div>
            
            <div className="posts-list">
              {[1,2,3,4,5].map(item => (
                <div key={item} className="post-item">
                  <div className="post-thumbnail"></div>
                  <div className="post-content">
                    <h4>Exploring Ancient Luxor: A Journey Through Time {item}</h4>
                    <p>Discover the magnificent temples and tombs that make Luxor one of the world's greatest archaeological sites...</p>
                    <div className="post-meta">
                      <span>📅 2 days ago</span>
                      <span>👤 Admin</span>
                      <span className="status published">Published</span>
                    </div>
                  </div>
                  <div className="post-stats">
                    <div>👁️ 1.5K</div>
                    <div>❤️ 89</div>
                    <div>💬 23</div>
                  </div>
                  <div className="post-actions">
                    <button className="edit-btn">✏️</button>
                    <button className="delete-btn">🗑️</button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        );
        
      case 'users':
        return (
          <div className="content-section users-section">
            <h2>{t.users}</h2>
            <div className="section-header">
              <button className="action-btn primary">+ Add User</button>
              <div className="search-filter">
                <input type="text" placeholder="Search users..." />
                <select>
                  <option>All Roles</option>
                  <option>Admin</option>
                  <option>Guide</option>
                  <option>Tourist</option>
                </select>
              </div>
            </div>
            
            <div className="users-table">
              <div className="table-header">
                <div>User</div>
                <div>Role</div>
                <div>Status</div>
                <div>Joined</div>
                <div>Actions</div>
              </div>
              {[1,2,3,4,5,6].map(item => (
                <div key={item} className="table-row">
                  <div className="user-info">
                    <div className="user-avatar">👤</div>
                    <div>
                      <div className="user-name">User {item}</div>
                      <div className="user-email">user{item}@luxor.com</div>
                    </div>
                  </div>
                  <div className="user-role">Tourist</div>
                  <div className="user-status active">Active</div>
                  <div className="user-joined">Jan {item}, 2024</div>
                  <div className="user-actions">
                    <button className="edit-btn">✏️</button>
                    <button className="delete-btn">🗑️</button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        );
        
      case 'visitors':
        return (
          <div className="content-section visitors-section">
            <h2>{t.visitors}</h2>
            <div className="visitors-stats">
              <div className="stat-card">
                <h3>Today's Visitors</h3>
                <div className="big-number">1,234</div>
                <span className="change positive">+15% from yesterday</span>
              </div>
              <div className="stat-card">
                <h3>This Month</h3>
                <div className="big-number">45,678</div>
                <span className="change positive">+8% from last month</span>
              </div>
              <div className="stat-card">
                <h3>Peak Hours</h3>
                <div className="big-number">2-4 PM</div>
                <span className="change neutral">Most active time</span>
              </div>
            </div>
            
            <div className="visitors-chart">
              <h3>Visitor Trends (Last 7 Days)</h3>
              <div className="chart-placeholder">
                <div className="line-chart">
                  {[40, 65, 45, 80, 70, 90, 85].map((height, index) => (
                    <div key={index} className="chart-point" style={{height: `${height}%`, left: `${index * 14.28}%`}}></div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        );
        
      case 'tourism':
        return (
          <div className="content-section tourism-section">
            <h2>{t.tourism}</h2>
            <div className="tourism-overview">
              <div className="attraction-grid">
                {[
                  { name: 'Karnak Temple', visitors: '2.1K', rating: '4.8' },
                  { name: 'Valley of Kings', visitors: '1.8K', rating: '4.9' },
                  { name: 'Luxor Temple', visitors: '1.5K', rating: '4.7' },
                  { name: 'Hatshepsut Temple', visitors: '1.2K', rating: '4.6' }
                ].map((attraction, index) => (
                  <div key={index} className="attraction-card">
                    <div className="attraction-image"></div>
                    <div className="attraction-info">
                      <h4>{attraction.name}</h4>
                      <div className="attraction-stats">
                        <span>👥 {attraction.visitors} today</span>
                        <span>⭐ {attraction.rating}</span>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        );
        
      default:
        return (
          <div className="content-section">
            <h2>{navigationItems.find(item => item.id === activeSection)?.label}</h2>
            <div className="coming-soon">
              <div className="coming-soon-icon">🚧</div>
              <h3>Coming Soon</h3>
              <p>This section is under development and will be available soon.</p>
            </div>
          </div>
        );
    }
  };

  useEffect(() => {
    document.body.className = darkMode ? 'dark-mode' : 'light-mode';
  }, [darkMode]);

  return (
    <div className={`dashboard ${language === 'ar' ? 'rtl' : 'ltr'} ${darkMode ? 'dark' : 'light'}`}>
      {/* Sidebar */}
      <div className={`sidebar ${sidebarOpen ? 'open' : 'closed'}`}>
        <div className="sidebar-header">
          <div className="logo">
            <span className="logo-icon">🏛️</span>
            {sidebarOpen && <span className="logo-text">{t.dashboard}</span>}
          </div>
        </div>
        
        <nav className="sidebar-nav">
          {navigationItems.map((item) => (
            <button
              key={item.id}
              className={`nav-item ${activeSection === item.id ? 'active' : ''}`}
              onClick={() => handleSectionChange(item.id)}
            >
              <span className="nav-icon">{item.icon}</span>
              {sidebarOpen && <span className="nav-label">{item.label}</span>}
            </button>
          ))}
        </nav>
        
        {/* Sidebar Footer */}
        <div className="sidebar-footer">
          {sidebarOpen && (
            <>
              <div className="app-version">
                <span className="version-label">Version</span>
                <span className="version-number">1.0.0</span>
              </div>
              <div className="footer-links">
                <button className="footer-link" onClick={() => window.open('/privacy', '_blank')}>
                  <span className="link-icon">🔒</span>
                  <span className="link-text">Privacy Policy</span>
                </button>
                <button className="footer-link" onClick={() => window.open('/terms', '_blank')}>
                  <span className="link-icon">📋</span>
                  <span className="link-text">Terms of Service</span>
                </button>
              </div>
            </>
          )}
        </div>
      </div>

      {/* Main Content */}
      <div className={`main-content ${!sidebarOpen ? 'sidebar-closed' : ''}`}>
        {/* Header */}
        <header className="dashboard-header">
          <div className="header-left">
            <button className="sidebar-toggle" onClick={toggleSidebar}>
              <span className={`hamburger ${!sidebarOpen ? 'active' : ''}`}></span>
              <span className={`hamburger ${!sidebarOpen ? 'active' : ''}`}></span>
              <span className={`hamburger ${!sidebarOpen ? 'active' : ''}`}></span>
            </button>
            <h1 className="page-title">{navigationItems.find(item => item.id === activeSection)?.label}</h1>
          </div>
          
          <div className="header-right">
            {/* Language Selector */}
            <div className="language-selector">
              <select value={language} onChange={(e) => changeLanguage(e.target.value)}>
                <option value="en">🇺🇸 EN</option>
                <option value="ar">🇪🇬 AR</option>
                <option value="fr">🇫🇷 FR</option>
                <option value="de">🇩🇪 DE</option>
              </select>
            </div>
            
            {/* Theme Toggle */}
            <button className="theme-toggle" onClick={toggleTheme}>
              {darkMode ? '☀️' : '🌙'}
            </button>
            
            {/* Notifications */}
            <button className="notifications">
              <span className="notification-icon">🔔</span>
              <span className="notification-badge">3</span>
            </button>
            
            {/* User Menu */}
            <div className="user-menu" onClick={() => onLogout && onLogout()}>
              <div className="user-avatar">
                {user.avatar ? (
                  <img src={user.avatar} alt={user.name} />
                ) : (
                  <span className="avatar-placeholder">👤</span>
                )}
              </div>
              <div className="user-info">
                <span className="user-name">{user.name}</span>
                <span className="user-role">{user.role}</span>
              </div>
              <div className="logout-icon">🚪</div>
            </div>
          </div>
        </header>

        {/* Content Area */}
        <main className="content-area">
          {renderContent()}
        </main>
      </div>
    </div>
  );
};

export default Dashboard;