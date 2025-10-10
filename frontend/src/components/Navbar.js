import React, { useState, useEffect, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import NotificationSystem from './NotificationSystem';
import { 
  FiSearch, 
  FiUser, 
  FiSettings, 
  FiLogOut, 
  FiMenu, 
  FiX, 
  FiSun, 
  FiMoon,
  FiChevronDown,
  FiBookmark,
  FiAward,
  FiTrendingUp
} from 'react-icons/fi';

const Navbar = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  
  // State management
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isProfileOpen, setIsProfileOpen] = useState(false);
  const [isDarkMode, setIsDarkMode] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');

  // Refs for click outside detection
  const profileRef = useRef(null);

  // Dark mode toggle
  const toggleDarkMode = () => {
    const newTheme = isDarkMode ? 'light' : 'dark';
    setIsDarkMode(!isDarkMode);
    document.documentElement.setAttribute('data-theme', newTheme);
    localStorage.setItem('theme', newTheme);
  };

  // Load theme from localStorage
  useEffect(() => {
    const savedTheme = localStorage.getItem('theme');
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    const theme = savedTheme || (prefersDark ? 'dark' : 'light');
    
    setIsDarkMode(theme === 'dark');
    document.documentElement.setAttribute('data-theme', theme);
  }, []);

  // Handle logout
  const handleLogout = () => {
    logout();
    navigate('/login');
    setIsProfileOpen(false);
  };

  // Handle search
  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/search?q=${encodeURIComponent(searchQuery)}`);
      setSearchQuery('');
    }
  };

  // Click outside handlers
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (profileRef.current && !profileRef.current.contains(event.target)) {
        setIsProfileOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  return (
    <nav className="bg-white shadow-lg border-b border-slate-200 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          {/* Logo and Brand */}
          <div className="flex items-center">
            <Link to="/" className="flex-shrink-0 flex items-center space-x-2">
              <div className="w-8 h-8 bg-gradient-primary rounded-lg flex items-center justify-center">
                <FiBookmark className="w-5 h-5 text-white" />
              </div>
              <h1 className="text-2xl font-bold text-gradient">EduLMS</h1>
            </Link>
          </div>

          {/* Search Bar - Desktop */}
          {isAuthenticated && (
            <div className="hidden md:flex flex-1 max-w-lg mx-8">
              <form onSubmit={handleSearch} className="w-full relative">
                <div className="relative">
                  <FiSearch className="absolute left-3 top-1/2 transform -translate-y-1/2 text-slate-400 w-4 h-4" />
                  <input
                    type="text"
                    placeholder="Search courses, lessons, assignments..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="search-input pl-10 pr-4 py-2 w-full"
                  />
                </div>
              </form>
            </div>
          )}

          {/* Right side items */}
          <div className="flex items-center space-x-4">
            {isAuthenticated ? (
              <>
                {/* Dark Mode Toggle */}
                <button
                  onClick={toggleDarkMode}
                  className="p-2 rounded-lg hover:bg-slate-100 transition-colors duration-200"
                  title={isDarkMode ? 'Switch to light mode' : 'Switch to dark mode'}
                >
                  {isDarkMode ? (
                    <FiSun className="w-5 h-5 text-yellow-500" />
                  ) : (
                    <FiMoon className="w-5 h-5 text-slate-600" />
                  )}
                </button>

                {/* Notifications */}
                <NotificationSystem />

                {/* Profile Dropdown */}
                <div className="relative" ref={profileRef}>
                  <button
                    onClick={() => setIsProfileOpen(!isProfileOpen)}
                    className="flex items-center space-x-2 p-2 rounded-lg hover:bg-slate-100 transition-colors duration-200"
                  >
                    <div className="w-8 h-8 bg-gradient-primary rounded-full flex items-center justify-center">
                      <span className="text-white text-sm font-medium">
                        {user?.firstName?.charAt(0)}{user?.lastName?.charAt(0)}
                      </span>
                    </div>
                    <div className="hidden md:block text-left">
                      <p className="text-sm font-medium text-slate-900">
                        {user?.firstName} {user?.lastName}
                      </p>
                      <p className="text-xs text-slate-500 capitalize">
                        {user?.role?.toLowerCase()}
                      </p>
                    </div>
                    <FiChevronDown className="w-4 h-4 text-slate-500" />
                  </button>

                  {/* Profile Dropdown Menu */}
                  {isProfileOpen && (
                    <div className="absolute right-0 mt-2 w-64 bg-white rounded-xl shadow-lg border border-slate-200 z-50">
                      <div className="p-4 border-b border-slate-200">
                        <div className="flex items-center space-x-3">
                          <div className="w-12 h-12 bg-gradient-primary rounded-full flex items-center justify-center">
                            <span className="text-white text-lg font-medium">
                              {user?.firstName?.charAt(0)}{user?.lastName?.charAt(0)}
                            </span>
                          </div>
                          <div>
                            <p className="font-medium text-slate-900">
                              {user?.firstName} {user?.lastName}
                            </p>
                            <p className="text-sm text-slate-500 capitalize">
                              {user?.role?.toLowerCase()}
                            </p>
                          </div>
                        </div>
                      </div>
                      
                      <div className="py-2">
                        <Link
                          to="/profile"
                          className="flex items-center px-4 py-2 text-sm text-slate-700 hover:bg-slate-100 transition-colors duration-200"
                          onClick={() => setIsProfileOpen(false)}
                        >
                          <FiUser className="w-4 h-4 mr-3" />
                          Profile
                        </Link>
                        <Link
                          to="/settings"
                          className="flex items-center px-4 py-2 text-sm text-slate-700 hover:bg-slate-100 transition-colors duration-200"
                          onClick={() => setIsProfileOpen(false)}
                        >
                          <FiSettings className="w-4 h-4 mr-3" />
                          Settings
                        </Link>
                        <Link
                          to="/my-courses"
                          className="flex items-center px-4 py-2 text-sm text-slate-700 hover:bg-slate-100 transition-colors duration-200"
                          onClick={() => setIsProfileOpen(false)}
                        >
                          <FiBookmark className="w-4 h-4 mr-3" />
                          My Courses
                        </Link>
                        <Link
                          to="/achievements"
                          className="flex items-center px-4 py-2 text-sm text-slate-700 hover:bg-slate-100 transition-colors duration-200"
                          onClick={() => setIsProfileOpen(false)}
                        >
                          <FiAward className="w-4 h-4 mr-3" />
                          Achievements
                        </Link>
                        <Link
                          to="/analytics"
                          className="flex items-center px-4 py-2 text-sm text-slate-700 hover:bg-slate-100 transition-colors duration-200"
                          onClick={() => setIsProfileOpen(false)}
                        >
                          <FiTrendingUp className="w-4 h-4 mr-3" />
                          Analytics
                        </Link>
                      </div>
                      
                      <div className="border-t border-slate-200 py-2">
                        <button
                          onClick={handleLogout}
                          className="flex items-center w-full px-4 py-2 text-sm text-red-600 hover:bg-red-50 transition-colors duration-200"
                        >
                          <FiLogOut className="w-4 h-4 mr-3" />
                          Sign Out
                        </button>
                      </div>
                    </div>
                  )}
                </div>
              </>
            ) : (
              <>
                <Link to="/login" className="btn btn-outline">
                  Login
                </Link>
                <Link to="/signup" className="btn btn-primary">
                  Sign Up
                </Link>
              </>
            )}

            {/* Mobile menu button */}
            <div className="md:hidden">
              <button
                onClick={() => setIsMenuOpen(!isMenuOpen)}
                className="p-2 rounded-lg hover:bg-slate-100 transition-colors duration-200"
              >
                {isMenuOpen ? (
                  <FiX className="w-6 h-6 text-slate-600" />
                ) : (
                  <FiMenu className="w-6 h-6 text-slate-600" />
                )}
              </button>
            </div>
          </div>
        </div>

        {/* Mobile menu */}
        {isMenuOpen && (
          <div className="md:hidden border-t border-slate-200">
            <div className="px-2 pt-2 pb-3 space-y-1">
              {isAuthenticated ? (
                <>
                  {/* Mobile Search */}
                  <div className="px-3 py-2">
                    <form onSubmit={handleSearch}>
                      <div className="relative">
                        <FiSearch className="absolute left-3 top-1/2 transform -translate-y-1/2 text-slate-400 w-4 h-4" />
                        <input
                          type="text"
                          placeholder="Search..."
                          value={searchQuery}
                          onChange={(e) => setSearchQuery(e.target.value)}
                          className="search-input pl-10 pr-4 py-2 w-full"
                        />
                      </div>
                    </form>
                  </div>
                  
                  {/* Mobile Profile Info */}
                  <div className="px-3 py-2 border-b border-slate-200">
                    <div className="flex items-center space-x-3">
                      <div className="w-10 h-10 bg-gradient-primary rounded-full flex items-center justify-center">
                        <span className="text-white font-medium">
                          {user?.firstName?.charAt(0)}{user?.lastName?.charAt(0)}
                        </span>
                      </div>
                      <div>
                        <p className="font-medium text-slate-900">
                          {user?.firstName} {user?.lastName}
                        </p>
                        <p className="text-sm text-slate-500 capitalize">
                          {user?.role?.toLowerCase()}
                        </p>
                      </div>
                    </div>
                  </div>
                  
                  {/* Mobile Navigation Links */}
                  <Link
                    to="/profile"
                    className="block px-3 py-2 text-sm text-slate-700 hover:bg-slate-100 rounded-lg"
                    onClick={() => setIsMenuOpen(false)}
                  >
                    Profile
                  </Link>
                  <Link
                    to="/my-courses"
                    className="block px-3 py-2 text-sm text-slate-700 hover:bg-slate-100 rounded-lg"
                    onClick={() => setIsMenuOpen(false)}
                  >
                    My Courses
                  </Link>
                  <Link
                    to="/notifications"
                    className="block px-3 py-2 text-sm text-slate-700 hover:bg-slate-100 rounded-lg"
                    onClick={() => setIsMenuOpen(false)}
                  >
                    Notifications
                  </Link>
                  <button
                    onClick={handleLogout}
                    className="block w-full text-left px-3 py-2 text-sm text-red-600 hover:bg-red-50 rounded-lg"
                  >
                    Sign Out
                  </button>
                </>
              ) : (
                <>
                  <Link
                    to="/login"
                    className="block px-3 py-2 text-sm text-slate-700 hover:bg-slate-100 rounded-lg"
                    onClick={() => setIsMenuOpen(false)}
                  >
                    Login
                  </Link>
                  <Link
                    to="/signup"
                    className="block px-3 py-2 text-sm text-slate-700 hover:bg-slate-100 rounded-lg"
                    onClick={() => setIsMenuOpen(false)}
                  >
                    Sign Up
                  </Link>
                </>
              )}
            </div>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;

