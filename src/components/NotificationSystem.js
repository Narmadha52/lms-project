import React, { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import {
  FiBell,
  FiCheck,
  FiX,
  FiAlertCircle,
  FiInfo,
  FiCheckCircle,
  FiStar,
  FiAward,
  FiMessageCircle,
  FiCalendar,
  FiFileText,
  FiTarget,
  FiTrendingUp,
  FiUsers,
  FiSettings,
  FiTrash2,
  FiMarkAsRead,
  FiFilter
} from 'react-icons/fi';

const NotificationSystem = () => {
  const [notifications, setNotifications] = useState([
    {
      id: 1,
      type: 'assignment',
      title: 'New Assignment Posted',
      message: 'Complete the React Hooks assignment by Friday',
      timestamp: new Date(Date.now() - 2 * 60 * 60 * 1000), // 2 hours ago
      read: false,
      priority: 'high',
      course: 'JavaScript Fundamentals',
      actionUrl: '/assignments/1'
    },
    {
      id: 2,
      type: 'course',
      title: 'Course Update',
      message: 'New lesson added to JavaScript Fundamentals',
      timestamp: new Date(Date.now() - 5 * 60 * 60 * 1000), // 5 hours ago
      read: false,
      priority: 'medium',
      course: 'JavaScript Fundamentals',
      actionUrl: '/courses/1'
    },
    {
      id: 3,
      type: 'quiz',
      title: 'Quiz Results',
      message: 'You scored 95% on the CSS Quiz',
      timestamp: new Date(Date.now() - 24 * 60 * 60 * 1000), // 1 day ago
      read: true,
      priority: 'low',
      course: 'Advanced CSS',
      actionUrl: '/quizzes/1'
    },
    {
      id: 4,
      type: 'achievement',
      title: 'Achievement Unlocked!',
      message: 'You earned the "First Course" badge',
      timestamp: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000), // 2 days ago
      read: true,
      priority: 'high',
      course: 'Complete React Development',
      actionUrl: '/achievements'
    },
    {
      id: 5,
      type: 'message',
      title: 'New Message',
      message: 'Instructor replied to your question',
      timestamp: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000), // 3 days ago
      read: false,
      priority: 'medium',
      course: 'JavaScript Fundamentals',
      actionUrl: '/messages'
    },
    {
      id: 6,
      type: 'reminder',
      title: 'Upcoming Deadline',
      message: 'Assignment due in 2 days',
      timestamp: new Date(Date.now() - 4 * 24 * 60 * 60 * 1000), // 4 days ago
      read: true,
      priority: 'high',
      course: 'Full Stack Development',
      actionUrl: '/assignments/2'
    }
  ]);

  const [isOpen, setIsOpen] = useState(false);
  const [filter, setFilter] = useState('all');
  const [sortBy, setSortBy] = useState('newest');
  const notificationRef = useRef(null);

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (notificationRef.current && !notificationRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const getNotificationIcon = (type) => {
    switch (type) {
      case 'assignment':
        return <FiFileText className="w-5 h-5" />;
      case 'course':
        return <FiCheckCircle className="w-5 h-5" />;
      case 'quiz':
        return <FiTarget className="w-5 h-5" />;
      case 'achievement':
        return <FiAward className="w-5 h-5" />;
      case 'message':
        return <FiMessageCircle className="w-5 h-5" />;
      case 'reminder':
        return <FiCalendar className="w-5 h-5" />;
      default:
        return <FiInfo className="w-5 h-5" />;
    }
  };

  const getNotificationColor = (type, priority) => {
    if (priority === 'high') {
      return 'text-red-600 bg-red-50 border-red-200';
    }
    if (priority === 'medium') {
      return 'text-yellow-600 bg-yellow-50 border-yellow-200';
    }
    
    switch (type) {
      case 'assignment':
        return 'text-blue-600 bg-blue-50 border-blue-200';
      case 'course':
        return 'text-green-600 bg-green-50 border-green-200';
      case 'quiz':
        return 'text-purple-600 bg-purple-50 border-purple-200';
      case 'achievement':
        return 'text-orange-600 bg-orange-50 border-orange-200';
      case 'message':
        return 'text-indigo-600 bg-indigo-50 border-indigo-200';
      case 'reminder':
        return 'text-pink-600 bg-pink-50 border-pink-200';
      default:
        return 'text-slate-600 bg-slate-50 border-slate-200';
    }
  };

  const formatTimestamp = (timestamp) => {
    const now = new Date();
    const diff = now - timestamp;
    const minutes = Math.floor(diff / (1000 * 60));
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const days = Math.floor(diff / (1000 * 60 * 60 * 24));

    if (minutes < 60) {
      return `${minutes} minutes ago`;
    } else if (hours < 24) {
      return `${hours} hours ago`;
    } else {
      return `${days} days ago`;
    }
  };

  const markAsRead = (id) => {
    setNotifications(prev => 
      prev.map(notification => 
        notification.id === id 
          ? { ...notification, read: true }
          : notification
      )
    );
  };

  const markAllAsRead = () => {
    setNotifications(prev => 
      prev.map(notification => ({ ...notification, read: true }))
    );
  };

  const deleteNotification = (id) => {
    setNotifications(prev => prev.filter(notification => notification.id !== id));
  };

  const filteredNotifications = notifications.filter(notification => {
    if (filter === 'all') return true;
    if (filter === 'unread') return !notification.read;
    if (filter === 'high') return notification.priority === 'high';
    return notification.type === filter;
  });

  const sortedNotifications = [...filteredNotifications].sort((a, b) => {
    if (sortBy === 'newest') {
      return b.timestamp - a.timestamp;
    } else if (sortBy === 'oldest') {
      return a.timestamp - b.timestamp;
    } else if (sortBy === 'priority') {
      const priorityOrder = { high: 3, medium: 2, low: 1 };
      return priorityOrder[b.priority] - priorityOrder[a.priority];
    }
    return 0;
  });

  const unreadCount = notifications.filter(n => !n.read).length;

  return (
    <div className="relative" ref={notificationRef}>
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="relative p-2 rounded-lg hover:bg-slate-100 transition-colors duration-200"
        title="Notifications"
      >
        <FiBell className="w-5 h-5 text-slate-600" />
        {unreadCount > 0 && (
          <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center animate-pulse">
            {unreadCount}
          </span>
        )}
      </button>

      <AnimatePresence>
        {isOpen && (
          <motion.div
            initial={{ opacity: 0, y: -10, scale: 0.95 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: -10, scale: 0.95 }}
            transition={{ duration: 0.2 }}
            className="absolute right-0 mt-2 w-96 bg-white rounded-xl shadow-lg border border-slate-200 z-50"
          >
            {/* Header */}
            <div className="p-4 border-b border-slate-200">
              <div className="flex items-center justify-between mb-3">
                <h3 className="text-lg font-semibold text-slate-900">Notifications</h3>
                <div className="flex items-center space-x-2">
                  <button
                    onClick={markAllAsRead}
                    className="text-sm text-blue-600 hover:text-blue-700 font-medium"
                    disabled={unreadCount === 0}
                  >
                    Mark all as read
                  </button>
                  <button
                    onClick={() => setIsOpen(false)}
                    className="p-1 text-slate-400 hover:text-slate-600 rounded"
                  >
                    <FiX className="w-4 h-4" />
                  </button>
                </div>
              </div>
              
              {/* Filters */}
              <div className="flex items-center space-x-2">
                <select
                  value={filter}
                  onChange={(e) => setFilter(e.target.value)}
                  className="text-sm border border-slate-300 rounded-lg px-2 py-1 focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="all">All</option>
                  <option value="unread">Unread</option>
                  <option value="high">High Priority</option>
                  <option value="assignment">Assignments</option>
                  <option value="course">Courses</option>
                  <option value="quiz">Quizzes</option>
                  <option value="achievement">Achievements</option>
                  <option value="message">Messages</option>
                </select>
                
                <select
                  value={sortBy}
                  onChange={(e) => setSortBy(e.target.value)}
                  className="text-sm border border-slate-300 rounded-lg px-2 py-1 focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="newest">Newest</option>
                  <option value="oldest">Oldest</option>
                  <option value="priority">Priority</option>
                </select>
              </div>
            </div>

            {/* Notifications List */}
            <div className="max-h-96 overflow-y-auto">
              {sortedNotifications.length === 0 ? (
                <div className="p-8 text-center">
                  <FiBell className="w-12 h-12 text-slate-400 mx-auto mb-3" />
                  <p className="text-slate-600">No notifications found</p>
                </div>
              ) : (
                sortedNotifications.map((notification, index) => (
                  <motion.div
                    key={notification.id}
                    initial={{ opacity: 0, x: -20 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ delay: index * 0.05 }}
                    className={`p-4 border-b border-slate-100 hover:bg-slate-50 cursor-pointer transition-colors duration-200 ${
                      !notification.read ? 'bg-blue-50' : ''
                    }`}
                    onClick={() => {
                      markAsRead(notification.id);
                      if (notification.actionUrl) {
                        window.location.href = notification.actionUrl;
                      }
                    }}
                  >
                    <div className="flex items-start space-x-3">
                      <div className={`w-8 h-8 rounded-full flex items-center justify-center ${getNotificationColor(notification.type, notification.priority)}`}>
                        {getNotificationIcon(notification.type)}
                      </div>
                      
                      <div className="flex-1 min-w-0">
                        <div className="flex items-start justify-between">
                          <div className="flex-1">
                            <h4 className="text-sm font-medium text-slate-900 mb-1">
                              {notification.title}
                            </h4>
                            <p className="text-sm text-slate-600 mb-2">
                              {notification.message}
                            </p>
                            <div className="flex items-center space-x-2 text-xs text-slate-500">
                              <span>{notification.course}</span>
                              <span>•</span>
                              <span>{formatTimestamp(notification.timestamp)}</span>
                              {notification.priority === 'high' && (
                                <>
                                  <span>•</span>
                                  <span className="text-red-600 font-medium">High Priority</span>
                                </>
                              )}
                            </div>
                          </div>
                          
                          <div className="flex items-center space-x-1 ml-2">
                            {!notification.read && (
                              <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
                            )}
                            <button
                              onClick={(e) => {
                                e.stopPropagation();
                                deleteNotification(notification.id);
                              }}
                              className="p-1 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded transition-colors duration-200"
                            >
                              <FiTrash2 className="w-3 h-3" />
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </motion.div>
                ))
              )}
            </div>

            {/* Footer */}
            <div className="p-4 border-t border-slate-200 bg-slate-50">
              <div className="flex items-center justify-between">
                <span className="text-sm text-slate-600">
                  {unreadCount} unread notifications
                </span>
                <button className="text-sm text-blue-600 hover:text-blue-700 font-medium">
                  View all notifications
                </button>
              </div>
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default NotificationSystem;
