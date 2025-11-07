import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { motion, AnimatePresence } from 'framer-motion';
import {
  FiHome,
  FiBookOpen,
  FiBookmark,
  FiUsers,
  FiSettings,
  FiUser,
  FiTrendingUp,
  FiAward,
  FiMessageCircle,
  FiCalendar,
  FiFileText,
  FiBarChart2,
  FiShield,
  FiSettings as FiCog,
  FiChevronDown,
  FiChevronRight,
  FiPlay,
  FiEdit,
  FiPlus,
  FiDownload,
  FiStar,
  FiClock,
  FiTarget,
  FiZap
} from 'react-icons/fi';

const Sidebar = () => {
  const { user, isAdmin, isInstructor, isStudent } = useAuth();
  const location = useLocation();
  const [expandedSections, setExpandedSections] = useState({});
  const [isCollapsed, setIsCollapsed] = useState(false);

  // Check if path is active
  const isActive = (path) => {
    return location.pathname === path || location.pathname.startsWith(path + '/');
  };

  // Toggle section expansion
  const toggleSection = (section) => {
    setExpandedSections(prev => ({
      ...prev,
      [section]: !prev[section]
    }));
  };

  // Navigation items based on user role
  const getNavigationItems = () => {
    const baseItems = [
      {
        id: 'dashboard',
        label: 'Dashboard',
        icon: FiHome,
        path: '/dashboard',
        description: 'Overview and analytics'
      },
      {
        id: 'courses',
        label: 'Browse Courses',
        icon: FiBookOpen,
        path: '/courses',
        description: 'Explore all courses'
      }
    ];

    if (isStudent || isInstructor) {
      baseItems.push({
        id: 'my-courses',
        label: 'My Courses',
        icon: FiBookmark,
        path: '/my-courses',
        description: 'Your enrolled courses'
      });
    }

    if (isStudent) {
      baseItems.push(
        {
          id: 'assignments',
          label: 'Assignments',
          icon: FiFileText,
          path: '/assignments',
          description: 'View and submit assignments'
        },
        {
          id: 'quizzes',
          label: 'Quizzes',
          icon: FiTarget,
          path: '/quizzes',
          description: 'Take practice quizzes'
        },
        {
          id: 'achievements',
          label: 'Achievements',
          icon: FiAward,
          path: '/achievements',
          description: 'Your badges and certificates'
        },
        {
          id: 'schedule',
          label: 'Schedule',
          icon: FiCalendar,
          path: '/schedule',
          description: 'Upcoming classes and deadlines'
        }
      );
    }

    if (isInstructor) {
      baseItems.push(
        {
          id: 'instructor',
          label: 'Instructor Panel',
          icon: FiUsers,
          path: '/instructor',
          description: 'Manage your courses'
        },
        {
          id: 'create-course',
          label: 'Create Course',
          icon: FiPlus,
          path: '/instructor/create-course',
          description: 'Build new courses'
        },
        {
          id: 'analytics',
          label: 'Analytics',
          icon: FiBarChart2,
          path: '/instructor/analytics',
          description: 'Student performance insights'
        },
        {
          id: 'live-classes',
          label: 'Live Classes',
          icon: FiPlay,
          path: '/instructor/live-classes',
          description: 'Conduct live sessions'
        }
      );
    }

    if (isAdmin) {
      baseItems.push(
        {
          id: 'admin',
          label: 'Admin Panel',
          icon: FiShield,
          path: '/admin',
          description: 'System administration'
        },
        // {
        //   id: 'user-management',
        //   label: 'User Management',
        //   icon: FiUsers,
        //   path: '/admin/users',
        //   description: 'Manage users and roles'
        // },
        // {
        //   id: 'system-analytics',
        //   label: 'System Analytics',
        //   icon: FiTrendingUp,
        //   path: '/admin/analytics',
        //   description: 'Platform-wide insights'
        // },
        // {
        //   id: 'system-settings',
        //   label: 'System Settings',
        //   icon: FiCog,
        //   path: '/admin/settings',
        //   description: 'Configure platform settings'
        // }
      );
    }

    // Add common items
    baseItems.push(
      {
        id: 'profile',
        label: 'Profile',
        icon: FiUser,
        path: '/profile',
        description: 'Manage your account'
      },
      {
        id: 'settings',
        label: 'Settings',
        icon: FiSettings,
        path: '/settings',
        description: 'Preferences and configuration'
      }
    );

    return baseItems;
  };

  const navigationItems = getNavigationItems();

  // Get link classes
  const getLinkClasses = (path) => {
    const baseClasses = 'flex items-center px-4 py-3 text-sm font-medium rounded-lg transition-all duration-200 group';
    const activeClasses = 'bg-blue-100 text-blue-700 shadow-sm';
    const inactiveClasses = 'text-slate-700 hover:bg-slate-100 hover:text-slate-900';
    
    return `${baseClasses} ${isActive(path) ? activeClasses : inactiveClasses}`;
  };

  // Get icon classes
  const getIconClasses = (path) => {
    const baseClasses = 'mr-3 h-5 w-5 transition-colors duration-200';
    const activeClasses = 'text-blue-600';
    const inactiveClasses = 'text-slate-400 group-hover:text-slate-600';
    
    return `${baseClasses} ${isActive(path) ? activeClasses : inactiveClasses}`;
  };

  // Animation variants
  const sidebarVariants = {
    expanded: {
      width: '16rem',
      transition: { duration: 0.3, ease: 'easeInOut' }
    },
    collapsed: {
      width: '4rem',
      transition: { duration: 0.3, ease: 'easeInOut' }
    }
  };

  const itemVariants = {
    hidden: { opacity: 0, x: -20 },
    visible: { opacity: 1, x: 0 },
    exit: { opacity: 0, x: -20 }
  };

  return (
    <motion.div
      className="fixed left-0 top-16 h-full bg-white shadow-xl border-r border-slate-200 z-40 overflow-hidden"
      variants={sidebarVariants}
      animate={isCollapsed ? 'collapsed' : 'expanded'}
      initial="expanded"
    >
      <div className="h-full flex flex-col">
        {/* Sidebar Header */}
        <div className="p-4 border-b border-slate-200">
          <div className="flex items-center justify-between">
            {!isCollapsed && (
              <motion.div
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                className="flex items-center space-x-2"
              >
                <div className="w-8 h-8 bg-gradient-primary rounded-lg flex items-center justify-center">
                  <FiBookOpen className="w-5 h-5 text-white" />
                </div>
                <span className="text-lg font-semibold text-slate-900">EduLMS</span>
              </motion.div>
            )}
            <button
              onClick={() => setIsCollapsed(!isCollapsed)}
              className="p-2 rounded-lg hover:bg-slate-100 transition-colors duration-200"
            >
              {isCollapsed ? (
                <FiChevronRight className="w-4 h-4 text-slate-600" />
              ) : (
                <FiChevronDown className="w-4 h-4 text-slate-600" />
              )}
            </button>
          </div>
        </div>

        {/* Navigation */}
        <nav className="flex-1 p-4 space-y-2 overflow-y-auto scrollbar-thin">
          <AnimatePresence>
            {navigationItems.map((item, index) => (
              <motion.div
                key={item.id}
                variants={itemVariants}
                initial="hidden"
                animate="visible"
                exit="exit"
                transition={{ delay: index * 0.1 }}
              >
                <Link
                  to={item.path}
                  className={getLinkClasses(item.path)}
                  title={isCollapsed ? item.label : ''}
                >
                  <item.icon className={getIconClasses(item.path)} />
                  {!isCollapsed && (
                    <motion.div
                      initial={{ opacity: 0 }}
                      animate={{ opacity: 1 }}
                      exit={{ opacity: 0 }}
                      className="flex-1"
                    >
                      <div className="flex items-center justify-between">
                        <span>{item.label}</span>
                        {item.badge && (
                          <span className="bg-red-500 text-white text-xs rounded-full px-2 py-0.5">
                            {item.badge}
                          </span>
                        )}
                      </div>
                      {!isCollapsed && item.description && (
                        <p className="text-xs text-slate-500 mt-1">
                          {item.description}
                        </p>
                      )}
                    </motion.div>
                  )}
          </Link>
              </motion.div>
            ))}
          </AnimatePresence>
        </nav>

        {/* Sidebar Footer */}
        {!isCollapsed && (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: 20 }}
            className="p-4 border-t border-slate-200"
          >
            <div className="flex items-center space-x-3">
              <div className="w-8 h-8 bg-gradient-primary rounded-full flex items-center justify-center">
                <span className="text-white text-sm font-medium">
                  {user?.firstName?.charAt(0)}{user?.lastName?.charAt(0)}
                </span>
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium text-slate-900 truncate">
                  {user?.firstName} {user?.lastName}
                </p>
                <p className="text-xs text-slate-500 capitalize">
                  {user?.role?.toLowerCase()}
                </p>
      </div>
    </div>
          </motion.div>
        )}
      </div>
    </motion.div>
  );
};

export default Sidebar;

