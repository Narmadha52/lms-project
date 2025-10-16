import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { motion, AnimatePresence } from 'framer-motion';
import RevenueChart from '../components/Charts/RevenueChart';
import UserGrowthChart from '../components/Charts/UserGrowthChart';
import CourseCompletionChart from '../components/Charts/CourseCompletionChart';
import {
  FiBookOpen,
  FiUsers,
  FiTrendingUp,
  FiDollarSign,
  FiStar,
  FiClock,
  FiTarget,
  FiBarChart2,
  FiPlus,
  FiEdit,
  FiEye,
  FiDownload,
  FiMessageCircle,
  FiCalendar,
  FiAward,
  FiZap,
  FiPlay,
  FiPause,
  FiSettings,
  FiMoreHorizontal,
  FiChevronRight,
  FiFilter,
  FiSearch
} from 'react-icons/fi';

const InstructorDashboard = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    totalCourses: 0,
    totalStudents: 0,
    totalRevenue: 0,
    averageRating: 0,
    completionRate: 0,
    activeStudents: 0
  });
  const [courses, setCourses] = useState([]);
  const [recentStudents, setRecentStudents] = useState([]);
  const [analytics, setAnalytics] = useState({});

  // Mock data for instructor dashboard
  const mockCourses = [
    {
      id: 1,
      title: 'Complete React Development Course',
      students: 1240,
      revenue: 12400,
      rating: 4.8,
      status: 'published',
      thumbnail: 'https://via.placeholder.com/300x200?text=React+Course',
      lastUpdated: '2024-01-10',
      completionRate: 78
    },
    {
      id: 2,
      title: 'JavaScript Fundamentals',
      students: 890,
      revenue: 8900,
      rating: 4.6,
      status: 'published',
      thumbnail: 'https://via.placeholder.com/300x200?text=JS+Course',
      lastUpdated: '2024-01-08',
      completionRate: 82
    },
    {
      id: 3,
      title: 'Advanced CSS Techniques',
      students: 650,
      revenue: 6500,
      rating: 4.7,
      status: 'draft',
      thumbnail: 'https://via.placeholder.com/300x200?text=CSS+Course',
      lastUpdated: '2024-01-05',
      completionRate: 0
    }
  ];

  const mockStudents = [
    {
      id: 1,
      name: 'Alex Johnson',
      email: 'alex@example.com',
      course: 'Complete React Development Course',
      progress: 85,
      lastActive: '2 hours ago',
      avatar: 'AJ'
    },
    {
      id: 2,
      name: 'Sarah Wilson',
      email: 'sarah@example.com',
      course: 'JavaScript Fundamentals',
      progress: 92,
      lastActive: '1 day ago',
      avatar: 'SW'
    },
    {
      id: 3,
      name: 'Mike Chen',
      email: 'mike@example.com',
      course: 'Complete React Development Course',
      progress: 67,
      lastActive: '3 days ago',
      avatar: 'MC'
    }
  ];

  useEffect(() => {
    const fetchInstructorData = async () => {
      try {
        setLoading(true);
        
        // Simulate API calls
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        setStats({
          totalCourses: 3,
          totalStudents: 2780,
          totalRevenue: 27800,
          averageRating: 4.7,
          completionRate: 80,
          activeStudents: 1240
        });
        
        setCourses(mockCourses);
        setRecentStudents(mockStudents);
        setAnalytics({
          monthlyRevenue: [1200, 1500, 1800, 2200, 1900, 2400],
          studentEngagement: [85, 78, 92, 88, 95, 90],
          courseCompletion: [78, 82, 0, 0, 0, 0]
        });
      } catch (error) {
        console.error('Failed to fetch instructor data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchInstructorData();
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

  const CourseCard = ({ course }) => (
    <motion.div
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      whileHover={{ scale: 1.02 }}
      className="card hover:shadow-lg transition-all duration-300"
    >
      <div className="relative">
        <img
          src={course.thumbnail}
          alt={course.title}
          className="w-full h-48 object-cover rounded-t-xl"
        />
        <div className="absolute top-4 right-4">
          <span className={`px-2 py-1 rounded-full text-xs font-medium ${
            course.status === 'published' 
              ? 'bg-green-100 text-green-800' 
              : 'bg-yellow-100 text-yellow-800'
          }`}>
            {course.status}
          </span>
        </div>
      </div>
      <div className="card-body">
        <h3 className="text-lg font-semibold text-slate-900 mb-2 line-clamp-2">
          {course.title}
        </h3>
        
        <div className="space-y-3 mb-4">
          <div className="flex items-center justify-between text-sm">
            <span className="text-slate-600">Students</span>
            <span className="font-medium">{course.students.toLocaleString()}</span>
          </div>
          <div className="flex items-center justify-between text-sm">
            <span className="text-slate-600">Revenue</span>
            <span className="font-medium">${course.revenue.toLocaleString()}</span>
          </div>
          <div className="flex items-center justify-between text-sm">
            <span className="text-slate-600">Rating</span>
            <div className="flex items-center">
              <FiStar className="w-4 h-4 text-yellow-500 mr-1" />
              <span className="font-medium">{course.rating}</span>
            </div>
          </div>
          <div className="flex items-center justify-between text-sm">
            <span className="text-slate-600">Completion</span>
            <span className="font-medium">{course.completionRate}%</span>
          </div>
        </div>

        <div className="flex items-center justify-between">
          <div className="flex space-x-2">
            <button className="p-2 text-slate-600 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors duration-200">
              <FiEye className="w-4 h-4" />
            </button>
            <button className="p-2 text-slate-600 hover:text-green-600 hover:bg-green-50 rounded-lg transition-colors duration-200">
              <FiEdit className="w-4 h-4" />
            </button>
            <button className="p-2 text-slate-600 hover:text-purple-600 hover:bg-purple-50 rounded-lg transition-colors duration-200">
              <FiBarChart2 className="w-4 h-4" />
            </button>
          </div>
          <button className="p-2 text-slate-600 hover:text-slate-800 hover:bg-slate-100 rounded-lg transition-colors duration-200">
            <FiMoreHorizontal className="w-4 h-4" />
          </button>
        </div>
      </div>
    </motion.div>
  );

  const StudentCard = ({ student }) => (
    <motion.div
      initial={{ opacity: 0, x: -20 }}
      animate={{ opacity: 1, x: 0 }}
      className="flex items-center justify-between p-4 bg-slate-50 rounded-lg hover:bg-slate-100 transition-colors duration-200"
    >
      <div className="flex items-center space-x-3">
        <div className="w-10 h-10 bg-gradient-primary rounded-full flex items-center justify-center">
          <span className="text-white text-sm font-medium">{student.avatar}</span>
        </div>
        <div>
          <h3 className="font-medium text-slate-900">{student.name}</h3>
          <p className="text-sm text-slate-600">{student.course}</p>
        </div>
      </div>
      <div className="text-right">
        <div className="flex items-center space-x-2 mb-1">
          <span className="text-sm font-medium text-slate-900">{student.progress}%</span>
          <div className="w-16 bg-slate-200 rounded-full h-2">
            <div 
              className="bg-blue-600 h-2 rounded-full transition-all duration-300"
              style={{ width: `${student.progress}%` }}
            ></div>
          </div>
        </div>
        <p className="text-xs text-slate-500">Last active: {student.lastActive}</p>
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
            Instructor Dashboard
          </h1>
          <p className="text-slate-600">
            Manage your courses and track student progress
          </p>
        </div>
        <div className="flex space-x-3">
          <button className="btn btn-outline">
            <FiDownload className="w-4 h-4 mr-2" />
            Export Data
          </button>
          <Link to="/instructor/create-course" className="btn btn-primary">
            <FiPlus className="w-4 h-4 mr-2" />
            Create Course
          </Link>
        </div>
      </motion.div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-6">
        <StatCard
          title="Total Courses"
          value={stats.totalCourses}
          icon={<FiBookOpen className="h-6 w-6 text-blue-600" />}
          color="blue"
          subtitle="Published courses"
        />
        
        <StatCard
          title="Total Students"
          value={stats.totalStudents.toLocaleString()}
          icon={<FiUsers className="h-6 w-6 text-green-600" />}
          color="green"
          subtitle="Across all courses"
          trend={15}
        />
        
        <StatCard
          title="Total Revenue"
          value={`$${stats.totalRevenue.toLocaleString()}`}
          icon={<FiDollarSign className="h-6 w-6 text-yellow-600" />}
          color="yellow"
          subtitle="Lifetime earnings"
          trend={23}
        />
        
        <StatCard
          title="Average Rating"
          value={stats.averageRating}
          icon={<FiStar className="h-6 w-6 text-purple-600" />}
          color="purple"
          subtitle="Course ratings"
        />
        
        <StatCard
          title="Completion Rate"
          value={`${stats.completionRate}%`}
          icon={<FiTarget className="h-6 w-6 text-orange-600" />}
          color="orange"
          subtitle="Student success"
        />
        
        <StatCard
          title="Active Students"
          value={stats.activeStudents.toLocaleString()}
          icon={<FiZap className="h-6 w-6 text-red-600" />}
          color="red"
          subtitle="This month"
          trend={8}
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
            <Link to="/instructor/create-course" className="flex flex-col items-center p-4 rounded-lg hover:bg-slate-50 transition-colors duration-200">
              <FiPlus className="w-8 h-8 text-blue-600 mb-2" />
              <span className="text-sm font-medium text-slate-700">Create Course</span>
            </Link>
            <Link to="/instructor/analytics" className="flex flex-col items-center p-4 rounded-lg hover:bg-slate-50 transition-colors duration-200">
              <FiBarChart2 className="w-8 h-8 text-green-600 mb-2" />
              <span className="text-sm font-medium text-slate-700">View Analytics</span>
            </Link>
            <Link to="/instructor/live-classes" className="flex flex-col items-center p-4 rounded-lg hover:bg-slate-50 transition-colors duration-200">
              <FiPlay className="w-8 h-8 text-purple-600 mb-2" />
              <span className="text-sm font-medium text-slate-700">Live Classes</span>
            </Link>
            <Link to="/instructor/students" className="flex flex-col items-center p-4 rounded-lg hover:bg-slate-50 transition-colors duration-200">
              <FiUsers className="w-8 h-8 text-orange-600 mb-2" />
              <span className="text-sm font-medium text-slate-700">Manage Students</span>
            </Link>
          </div>
        </div>
      </motion.div>

      {/* My Courses */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-slate-900">My Courses</h2>
          <div className="flex items-center space-x-3">
            <div className="relative">
              <FiSearch className="absolute left-3 top-1/2 transform -translate-y-1/2 text-slate-400 w-4 h-4" />
              <input
                type="text"
                placeholder="Search courses..."
                className="search-input pl-10 pr-4 py-2 w-64"
              />
            </div>
            <button className="btn btn-outline">
              <FiFilter className="w-4 h-4 mr-2" />
              Filter
            </button>
            <Link to="/instructor/courses" className="btn btn-outline">
              View All
            </Link>
          </div>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {courses.map((course, index) => (
            <motion.div
              key={course.id}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.1 }}
            >
              <CourseCard course={course} />
            </motion.div>
          ))}
        </div>
      </motion.div>

      {/* Recent Students */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="card"
      >
        <div className="card-header">
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-semibold text-slate-900">Recent Students</h2>
            <Link to="/instructor/students" className="text-sm text-blue-600 hover:text-blue-700 font-medium">
              View All
            </Link>
          </div>
        </div>
        <div className="card-body">
          <div className="space-y-4">
            {recentStudents.map((student, index) => (
              <motion.div
                key={student.id}
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ delay: index * 0.1 }}
              >
                <StudentCard student={student} />
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
          <RevenueChart
            data={{
              labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
              values: [12000, 15000, 18000, 22000, 25000, 28000]
            }}
            title="Revenue Trend"
            subtitle="Monthly revenue from your courses"
          />
        </div>

        <div className="card">
          <UserGrowthChart
            data={{
              labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
              values: [1200, 1350, 1420, 1580, 1650, 1720]
            }}
            title="Student Growth"
            subtitle="New student enrollments over time"
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
            labels: ['Complete React Development', 'JavaScript Fundamentals', 'Advanced CSS', 'Node.js Backend', 'Python Basics', 'HTML & CSS'],
            values: [85, 78, 92, 88, 95, 90]
          }}
          title="Course Completion Rates"
          subtitle="Student completion rates for your courses"
        />
      </motion.div>
    </div>
  );
};

export default InstructorDashboard;
