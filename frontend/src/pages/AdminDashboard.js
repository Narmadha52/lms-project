import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { motion, AnimatePresence } from 'framer-motion';
import RevenueChart from '../components/Charts/RevenueChart';
import UserGrowthChart from '../components/Charts/UserGrowthChart';
import CourseCompletionChart from '../components/Charts/CourseCompletionChart';
import {
  FiUsers,
  FiBookOpen,
  FiDollarSign,
  FiTrendingUp,
  FiShield,
  FiSettings,
  FiBarChart2,
  FiAlertTriangle,
  FiCheckCircle,
  FiClock,
  FiEye,
  FiEdit,
  FiTrash2,
  FiMoreHorizontal,
  FiSearch,
  FiFilter,
  FiDownload,
  FiUpload,
  FiRefreshCw,
  FiDatabase,
  FiServer,
  FiGlobe,
  FiMail,
  FiBell,
  FiUserPlus,
  FiUserMinus,
  FiLock,
  FiUnlock,
  FiStar,
  FiTarget,
  FiZap,
  FiActivity
} from 'react-icons/fi';

const AdminDashboard = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    totalUsers: 0,
    totalCourses: 0,
    totalRevenue: 0,
    activeUsers: 0,
    pendingApprovals: 0,
    systemHealth: 0
  });
  const [recentUsers, setRecentUsers] = useState([]);
  const [pendingCourses, setPendingCourses] = useState([]);
  const [systemAlerts, setSystemAlerts] = useState([]);
  const [analytics, setAnalytics] = useState({});

  // Mock data for admin dashboard
  const mockUsers = [
    {
      id: 1,
      name: 'Alex Johnson',
      email: 'alex@example.com',
      role: 'STUDENT',
      status: 'active',
      joinDate: '2024-01-15',
      lastActive: '2 hours ago',
      avatar: 'AJ',
      courses: 3
    },
    {
      id: 2,
      name: 'Sarah Wilson',
      email: 'sarah@example.com',
      role: 'INSTRUCTOR',
      status: 'active',
      joinDate: '2024-01-10',
      lastActive: '1 day ago',
      avatar: 'SW',
      courses: 2
    },
    {
      id: 3,
      name: 'Mike Chen',
      email: 'mike@example.com',
      role: 'STUDENT',
      status: 'inactive',
      joinDate: '2024-01-08',
      lastActive: '1 week ago',
      avatar: 'MC',
      courses: 1
    }
  ];

  const mockPendingCourses = [
    {
      id: 1,
      title: 'Advanced Machine Learning',
      instructor: 'Dr. Jane Smith',
      submittedDate: '2024-01-12',
      status: 'pending',
      category: 'Technology',
      estimatedDuration: '8 weeks'
    },
    {
      id: 2,
      title: 'Digital Marketing Fundamentals',
      instructor: 'John Doe',
      submittedDate: '2024-01-11',
      status: 'pending',
      category: 'Business',
      estimatedDuration: '6 weeks'
    }
  ];

  const mockSystemAlerts = [
    {
      id: 1,
      type: 'warning',
      title: 'High Server Load',
      message: 'Server CPU usage is at 85%',
      timestamp: '2 hours ago',
      resolved: false
    },
    {
      id: 2,
      type: 'info',
      title: 'New User Registration',
      message: '50 new users registered today',
      timestamp: '4 hours ago',
      resolved: true
    },
    {
      id: 3,
      type: 'error',
      title: 'Database Connection Issue',
      message: 'Temporary database connectivity problems',
      timestamp: '6 hours ago',
      resolved: true
    }
  ];

  useEffect(() => {
    const fetchAdminData = async () => {
      try {
        setLoading(true);
        
        // Simulate API calls
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        setStats({
          totalUsers: 12450,
          totalCourses: 156,
          totalRevenue: 245000,
          activeUsers: 8900,
          pendingApprovals: 12,
          systemHealth: 98
        });
        
        setRecentUsers(mockUsers);
        setPendingCourses(mockPendingCourses);
        setSystemAlerts(mockSystemAlerts);
        setAnalytics({
          userGrowth: [1200, 1350, 1420, 1580, 1650, 1720],
          revenueGrowth: [15000, 18000, 22000, 25000, 28000, 32000],
          courseCompletions: [450, 520, 480, 600, 580, 650]
        });
      } catch (error) {
        console.error('Failed to fetch admin data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchAdminData();
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="loading-spinner"></div>
      </div>
    );
  }

  const StatCard = ({ title, value, icon, color = 'primary', subtitle, trend, onClick }) => (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      whileHover={{ scale: 1.02 }}
      className="card hover:shadow-lg transition-all duration-300 cursor-pointer"
      onClick={onClick}
    >
      <div className="card-body">
        <div className="flex items-center justify-between">
          <div className="flex items-center">
            <div className={`p-3 rounded-full bg-${color}-100`}>
              {icon}
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-slate-600">{title}</p>
              <p className="text-2xl font-semibold text-slate-900">{value}</p>
              {subtitle && (
                <p className="text-xs text-slate-500">{subtitle}</p>
              )}
            </div>
          </div>
          {trend && (
            <div className={`flex items-center text-sm ${trend > 0 ? 'text-green-600' : 'text-red-600'}`}>
              <FiTrendingUp className="w-4 h-4 mr-1" />
              {trend > 0 ? '+' : ''}{trend}%
            </div>
          )}
        </div>
      </div>
    </motion.div>
  );

  const UserCard = ({ user }) => (
    <motion.div
      initial={{ opacity: 0, x: -20 }}
      animate={{ opacity: 1, x: 0 }}
      className="flex items-center justify-between p-4 bg-slate-50 rounded-lg hover:bg-slate-100 transition-colors duration-200"
    >
      <div className="flex items-center space-x-3">
        <div className="w-10 h-10 bg-gradient-primary rounded-full flex items-center justify-center">
          <span className="text-white text-sm font-medium">{user.avatar}</span>
        </div>
        <div>
          <h3 className="font-medium text-slate-900">{user.name}</h3>
          <p className="text-sm text-slate-600">{user.email}</p>
          <div className="flex items-center space-x-2 mt-1">
            <span className={`badge ${
              user.role === 'ADMIN' ? 'badge-danger' :
              user.role === 'INSTRUCTOR' ? 'badge-warning' :
              'badge-primary'
            }`}>
              {user.role}
            </span>
            <span className={`badge ${
              user.status === 'active' ? 'badge-success' : 'badge-gray'
            }`}>
              {user.status}
            </span>
          </div>
        </div>
      </div>
      <div className="flex items-center space-x-2">
        <div className="text-right">
          <p className="text-sm text-slate-600">Last active: {user.lastActive}</p>
          <p className="text-sm text-slate-600">Courses: {user.courses}</p>
        </div>
        <div className="flex space-x-1">
          <button className="p-2 text-slate-600 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors duration-200">
            <FiEye className="w-4 h-4" />
          </button>
          <button className="p-2 text-slate-600 hover:text-green-600 hover:bg-green-50 rounded-lg transition-colors duration-200">
            <FiEdit className="w-4 h-4" />
          </button>
          <button className="p-2 text-slate-600 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors duration-200">
            <FiTrash2 className="w-4 h-4" />
          </button>
        </div>
      </div>
    </motion.div>
  );

  const CourseApprovalCard = ({ course }) => (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="card"
    >
      <div className="card-body">
        <div className="flex items-start justify-between">
          <div className="flex-1">
            <h3 className="text-lg font-semibold text-slate-900 mb-2">{course.title}</h3>
            <div className="space-y-2 text-sm text-slate-600">
              <p><span className="font-medium">Instructor:</span> {course.instructor}</p>
              <p><span className="font-medium">Category:</span> {course.category}</p>
              <p><span className="font-medium">Duration:</span> {course.estimatedDuration}</p>
              <p><span className="font-medium">Submitted:</span> {course.submittedDate}</p>
            </div>
          </div>
          <div className="flex flex-col space-y-2 ml-4">
            <button className="btn btn-success btn-sm">
              <FiCheckCircle className="w-4 h-4 mr-1" />
              Approve
            </button>
            <button className="btn btn-outline btn-sm">
              <FiEye className="w-4 h-4 mr-1" />
              Review
            </button>
            <button className="btn btn-danger btn-sm">
              <FiTrash2 className="w-4 h-4 mr-1" />
              Reject
            </button>
          </div>
        </div>
      </div>
    </motion.div>
  );

  const AlertCard = ({ alert }) => (
    <motion.div
      initial={{ opacity: 0, x: -20 }}
      animate={{ opacity: 1, x: 0 }}
      className={`p-4 rounded-lg border-l-4 ${
        alert.type === 'error' ? 'border-red-500 bg-red-50' :
        alert.type === 'warning' ? 'border-yellow-500 bg-yellow-50' :
        'border-blue-500 bg-blue-50'
      }`}
    >
      <div className="flex items-start justify-between">
        <div className="flex items-start space-x-3">
          <div className={`w-8 h-8 rounded-full flex items-center justify-center ${
            alert.type === 'error' ? 'bg-red-100' :
            alert.type === 'warning' ? 'bg-yellow-100' :
            'bg-blue-100'
          }`}>
            {alert.type === 'error' ? (
              <FiAlertTriangle className="w-4 h-4 text-red-600" />
            ) : alert.type === 'warning' ? (
              <FiAlertTriangle className="w-4 h-4 text-yellow-600" />
            ) : (
              <FiBell className="w-4 h-4 text-blue-600" />
            )}
          </div>
          <div>
            <h4 className="font-medium text-slate-900">{alert.title}</h4>
            <p className="text-sm text-slate-600 mt-1">{alert.message}</p>
            <p className="text-xs text-slate-500 mt-1">{alert.timestamp}</p>
          </div>
        </div>
        <div className="flex items-center space-x-2">
          {alert.resolved ? (
            <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800">
              <FiCheckCircle className="w-3 h-3 mr-1" />
              Resolved
            </span>
          ) : (
            <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">
              <FiClock className="w-3 h-3 mr-1" />
              Pending
            </span>
          )}
        </div>
      </div>
    </motion.div>
  );

  return (
    <div className="p-6 space-y-8">
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex items-center justify-between"
      >
        <div>
          <h1 className="text-3xl font-bold text-slate-900 mb-2">
            Admin Dashboard
          </h1>
          <p className="text-slate-600">
            Manage your platform and monitor system health
          </p>
        </div>
        <div className="flex space-x-3">
          <button className="btn btn-outline">
            <FiDownload className="w-4 h-4 mr-2" />
            Export Data
          </button>
          <button className="btn btn-outline">
            <FiRefreshCw className="w-4 h-4 mr-2" />
            Refresh
          </button>
          <Link to="/admin/settings" className="btn btn-primary">
            <FiSettings className="w-4 h-4 mr-2" />
            Settings
          </Link>
        </div>
      </motion.div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-6">
        <StatCard
          title="Total Users"
          value={stats.totalUsers.toLocaleString()}
          icon={<FiUsers className="h-6 w-6 text-blue-600" />}
          color="blue"
          subtitle="Registered users"
          trend={12}
        />
        
        <StatCard
          title="Total Courses"
          value={stats.totalCourses}
          icon={<FiBookOpen className="h-6 w-6 text-green-600" />}
          color="green"
          subtitle="Published courses"
          trend={8}
        />
        
        <StatCard
          title="Total Revenue"
          value={`$${stats.totalRevenue.toLocaleString()}`}
          icon={<FiDollarSign className="h-6 w-6 text-yellow-600" />}
          color="yellow"
          subtitle="Platform earnings"
          trend={25}
        />
        
        <StatCard
          title="Active Users"
          value={stats.activeUsers.toLocaleString()}
          icon={<FiActivity className="h-6 w-6 text-purple-600" />}
          color="purple"
          subtitle="This month"
          trend={18}
        />
        
        <StatCard
          title="Pending Approvals"
          value={stats.pendingApprovals}
          icon={<FiClock className="h-6 w-6 text-orange-600" />}
          color="orange"
          subtitle="Awaiting review"
        />
        
        <StatCard
          title="System Health"
          value={`${stats.systemHealth}%`}
          icon={<FiShield className="h-6 w-6 text-red-600" />}
          color="red"
          subtitle="System status"
        />
      </div>

      {/* Quick Actions */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="card"
      >
        <div className="card-header">
          <h2 className="text-xl font-semibold text-slate-900">Quick Actions</h2>
        </div>
        <div className="card-body">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <Link to="/admin/users" className="flex flex-col items-center p-4 rounded-lg hover:bg-slate-50 transition-colors duration-200">
              <FiUsers className="w-8 h-8 text-blue-600 mb-2" />
              <span className="text-sm font-medium text-slate-700">Manage Users</span>
            </Link>
            <Link to="/admin/courses" className="flex flex-col items-center p-4 rounded-lg hover:bg-slate-50 transition-colors duration-200">
              <FiBookOpen className="w-8 h-8 text-green-600 mb-2" />
              <span className="text-sm font-medium text-slate-700">Course Approval</span>
            </Link>
            <Link to="/admin/analytics" className="flex flex-col items-center p-4 rounded-lg hover:bg-slate-50 transition-colors duration-200">
              <FiBarChart2 className="w-8 h-8 text-purple-600 mb-2" />
              <span className="text-sm font-medium text-slate-700">View Analytics</span>
            </Link>
            // <Link to="/admin/settings" className="flex flex-col items-center p-4 rounded-lg hover:bg-slate-50 transition-colors duration-200">
            //   <FiSettings className="w-8 h-8 text-orange-600 mb-2" />
            //   <span className="text-sm font-medium text-slate-700">System Settings</span>
            // </Link>
          </div>
        </div>
      </motion.div>

      {/* Pending Course Approvals */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-slate-900">Pending Course Approvals</h2>
          <Link to="/admin/courses" className="btn btn-outline">
            View All
          </Link>
        </div>
        
        <div className="space-y-4">
          {pendingCourses.map((course, index) => (
            <motion.div
              key={course.id}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.1 }}
            >
              <CourseApprovalCard course={course} />
            </motion.div>
          ))}
        </div>
      </motion.div>

      {/* Recent Users */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="card"
      >
        <div className="card-header">
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-semibold text-slate-900">Recent Users</h2>
            <Link to="/admin/users" className="text-sm text-blue-600 hover:text-blue-700 font-medium">
              View All
            </Link>
          </div>
        </div>
        <div className="card-body">
          <div className="space-y-4">
            {recentUsers.map((user, index) => (
              <motion.div
                key={user.id}
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ delay: index * 0.1 }}
              >
                <UserCard user={user} />
              </motion.div>
            ))}
          </div>
        </div>
      </motion.div>

      {/* System Alerts */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="card"
      >
        <div className="card-header">
          <h2 className="text-xl font-semibold text-slate-900">System Alerts</h2>
        </div>
        <div className="card-body">
          <div className="space-y-4">
            {systemAlerts.map((alert, index) => (
              <motion.div
                key={alert.id}
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ delay: index * 0.1 }}
              >
                <AlertCard alert={alert} />
              </motion.div>
            ))}
          </div>
        </div>
      </motion.div>

      {/* Analytics Overview */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="grid grid-cols-1 lg:grid-cols-2 gap-6"
      >
        <div className="card">
          <UserGrowthChart
            data={{
              labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
              values: [1200, 1350, 1420, 1580, 1650, 1720]
            }}
            title="User Growth"
            subtitle="New user registrations over time"
          />
        </div>

        <div className="card">
          <RevenueChart
            data={{
              labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
              values: [15000, 18000, 22000, 25000, 28000, 32000]
            }}
            title="Platform Revenue"
            subtitle="Monthly revenue from all courses"
          />
        </div>
      </motion.div>

      {/* Course Performance */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="card"
      >
        <CourseCompletionChart
          data={{
            labels: ['JavaScript', 'React', 'Node.js', 'Python', 'CSS', 'HTML'],
            values: [85, 78, 92, 88, 95, 90]
          }}
          title="Course Completion Rates"
          subtitle="Completion rates across all courses"
        />
      </motion.div>
    </div>
  );
};

export default AdminDashboard;
