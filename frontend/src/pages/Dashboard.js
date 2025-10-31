import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { coursesAPI } from '../api/courses';
import { enrollmentsAPI } from '../api/enrollments';
import LoadingSpinner from '../components/LoadingSpinner';
import { motion, AnimatePresence } from 'framer-motion';
import ProgressChart from '../components/Charts/ProgressChart';
import StudentEngagementChart from '../components/Charts/StudentEngagementChart';
import {
  FiBookOpen,
  FiAward,
  FiTrendingUp,
  FiClock,
  FiTarget,
  FiStar,
  FiZap,
  FiUsers,
  FiFileText,
  FiPlay,
  FiCheckCircle,
  FiZap as FiFire,
  FiHeart
} from 'react-icons/fi';

const Dashboard = () => {
  const { user, isAdmin, isInstructor, isStudent } = useAuth();
  const [stats, setStats] = useState({
    totalCourses: 0,
    myCourses: 0,
    completedCourses: 0,
    inProgressCourses: 0,
    totalPoints: 0,
    currentStreak: 0,
    badges: 0,
    assignments: 0
  });
  const [recentCourses, setRecentCourses] = useState([]);
  const [myEnrollments, setMyEnrollments] = useState([]);
  const [assignments, setAssignments] = useState([]);
  const [achievements, setAchievements] = useState([]);
  const [leaderboard, setLeaderboard] = useState([]);
  const [loading, setLoading] = useState(true);

  // Mock data for gamification features
  const mockAchievements = [
    { id: 1, name: 'First Course', description: 'Completed your first course', icon: FiAward, earned: true, points: 100 },
    { id: 2, name: 'Quiz Master', description: 'Scored 90%+ on 5 quizzes', icon: FiTarget, earned: true, points: 150 },
    { id: 3, name: 'Streak Keeper', description: '7-day learning streak', icon: FiFire, earned: true, points: 200 },
    { id: 4, name: 'Course Creator', description: 'Created your first course', icon: FiBookOpen, earned: false, points: 300 },
    { id: 5, name: 'Social Learner', description: 'Helped 10 classmates', icon: FiUsers, earned: false, points: 250 }
  ];

  const mockLeaderboard = [
    { rank: 1, name: 'Alex Johnson', points: 2450, avatar: 'AJ', streak: 15 },
    { rank: 2, name: 'Sarah Wilson', points: 2380, avatar: 'SW', streak: 12 },
    { rank: 3, name: 'Mike Chen', points: 2200, avatar: 'MC', streak: 10 },
    { rank: 4, name: user?.firstName + ' ' + user?.lastName, points: 1950, avatar: user?.firstName?.charAt(0) + user?.lastName?.charAt(0), streak: 8 },
    { rank: 5, name: 'Emma Davis', points: 1800, avatar: 'ED', streak: 7 }
  ];

  const mockAssignments = [
    { id: 1, title: 'React Hooks Assignment', course: 'JavaScript Fundamentals', dueDate: '2024-01-15', status: 'pending', points: 100 },
    { id: 2, title: 'CSS Grid Layout', course: 'Advanced CSS', dueDate: '2024-01-18', status: 'submitted', points: 80 },
    { id: 3, title: 'API Integration Project', course: 'Full Stack Development', dueDate: '2024-01-20', status: 'pending', points: 150 }
  ];

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setLoading(true);
        
        // Fetch published courses
        const courses = await coursesAPI.getPublishedCourses();
        setRecentCourses(courses.slice(0, 6));
        
        // Fetch user's enrollments if student or instructor
        if (isStudent || isInstructor) {
          const enrollments = await enrollmentsAPI.getMyEnrollments();
          setMyEnrollments(enrollments);
          
          const completed = enrollments.filter(e => e.isCompleted).length;
          const inProgress = enrollments.filter(e => !e.isCompleted).length;
          
          setStats({
            totalCourses: courses.length,
            myCourses: enrollments.length,
            completedCourses: completed,
            inProgressCourses: inProgress,
            totalPoints: 1950,
            currentStreak: 8,
            badges: 3,
            assignments: mockAssignments.length
          });
        } else {
          setStats({
            totalCourses: courses.length,
            myCourses: 0,
            completedCourses: 0,
            inProgressCourses: 0,
            totalPoints: 0,
            currentStreak: 0,
            badges: 0,
            assignments: 0
          });
        }

        setAssignments(mockAssignments);
        setAchievements(mockAchievements);
        setLeaderboard(mockLeaderboard);
      } catch (error) {
        console.error('Failed to fetch dashboard data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, [isStudent, isInstructor]);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  const StatCard = ({ title, value, icon, color = 'primary', subtitle, trend }) => (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="card hover:shadow-lg transition-all duration-300"
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
      className="card hover:shadow-lg transition-all duration-300 group"
    >
      <div className="relative">
        <img
          src={course.thumbnailUrl || 'https://via.placeholder.com/300x200?text=Course+Image'}
          alt={course.title}
          className="w-full h-48 object-cover rounded-t-xl"
        />
        <div className="absolute top-4 right-4">
          <button className="p-2 bg-white/80 backdrop-blur-sm rounded-full hover:bg-white transition-colors duration-200">
            <FiHeart className="w-4 h-4 text-slate-600" />
          </button>
        </div>
        <div className="absolute bottom-4 left-4">
          <span className="bg-white/90 backdrop-blur-sm px-2 py-1 rounded-full text-xs font-medium">
            {course.difficultyLevel}
          </span>
        </div>
      </div>
      <div className="card-body">
        <h3 className="text-lg font-semibold text-slate-900 mb-2 line-clamp-2 group-hover:text-blue-600 transition-colors duration-200">
          {course.title}
        </h3>
        <p className="text-slate-600 text-sm mb-3 line-clamp-2">
          {course.description}
        </p>
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center space-x-2">
            <FiStar className="w-4 h-4 text-yellow-500" />
            <span className="text-sm text-slate-600">4.8 (124 reviews)</span>
          </div>
          <span className="text-lg font-bold text-blue-600">
            {course.isFree ? 'Free' : `$${course.price}`}
          </span>
        </div>
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <FiUsers className="w-4 h-4 text-slate-400" />
            <span className="text-sm text-slate-600">1.2k students</span>
          </div>
          <Link
            to={`/courses/${course.id}`}
            className="btn btn-primary btn-sm"
          >
            <FiPlay className="w-4 h-4 mr-1" />
            View Course
          </Link>
        </div>
      </div>
    </motion.div>
  );

  const AchievementCard = ({ achievement }) => (
    <motion.div
      initial={{ opacity: 0, scale: 0.9 }}
      animate={{ opacity: 1, scale: 1 }}
      className={`card ${achievement.earned ? 'bg-gradient-to-br from-yellow-50 to-orange-50 border-yellow-200' : 'bg-slate-50 border-slate-200'} transition-all duration-300`}
    >
      <div className="card-body text-center">
        <div className={`w-16 h-16 mx-auto mb-3 rounded-full flex items-center justify-center ${achievement.earned ? 'bg-yellow-100' : 'bg-slate-200'}`}>
          <achievement.icon className={`w-8 h-8 ${achievement.earned ? 'text-yellow-600' : 'text-slate-400'}`} />
        </div>
        <h3 className="font-semibold text-slate-900 mb-1">{achievement.name}</h3>
        <p className="text-sm text-slate-600 mb-2">{achievement.description}</p>
        <div className="flex items-center justify-center space-x-2">
          <FiZap className="w-4 h-4 text-yellow-500" />
          <span className="text-sm font-medium text-slate-700">{achievement.points} points</span>
        </div>
        {achievement.earned && (
          <div className="mt-2">
            <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800">
              <FiCheckCircle className="w-3 h-3 mr-1" />
              Earned
            </span>
          </div>
        )}
      </div>
    </motion.div>
  );

  return (
    <div className="p-6 space-y-8">
      {/* Welcome Section */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        className="mb-8"
      >
        <h1 className="text-4xl font-bold text-slate-900 mb-2">
          Welcome back, {user?.firstName}! 👋
        </h1>
        <p className="text-slate-600 text-lg">
          Ready to continue your learning journey? Here's what's happening today.
        </p>
      </motion.div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard
          title="My Courses"
          value={stats.myCourses}
          icon={<FiBookOpen className="h-6 w-6 text-blue-600" />}
          color="blue"
          subtitle={`${stats.completedCourses} completed`}
        />
        
        <StatCard
          title="Total Points"
          value={stats.totalPoints.toLocaleString()}
          icon={<FiZap className="h-6 w-6 text-yellow-600" />}
          color="yellow"
          subtitle="Keep learning to earn more!"
          trend={12}
        />
        
        <StatCard
          title="Current Streak"
          value={`${stats.currentStreak} days`}
          icon={<FiFire className="h-6 w-6 text-orange-600" />}
          color="orange"
          subtitle="Don't break the chain!"
        />
        
        <StatCard
          title="Badges Earned"
          value={stats.badges}
          icon={<FiAward className="h-6 w-6 text-purple-600" />}
          color="purple"
          subtitle="Out of 5 available"
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
            <Link to="/courses" className="flex flex-col items-center p-4 rounded-lg hover:bg-slate-50 transition-colors duration-200">
              <FiBookOpen className="w-8 h-8 text-blue-600 mb-2" />
              <span className="text-sm font-medium text-slate-700">Browse Courses</span>
            </Link>
            <Link to="/assignments" className="flex flex-col items-center p-4 rounded-lg hover:bg-slate-50 transition-colors duration-200">
              <FiFileText className="w-8 h-8 text-green-600 mb-2" />
              <span className="text-sm font-medium text-slate-700">Assignments</span>
            </Link>
            <Link to="/quizzes" className="flex flex-col items-center p-4 rounded-lg hover:bg-slate-50 transition-colors duration-200">
              <FiTarget className="w-8 h-8 text-purple-600 mb-2" />
              <span className="text-sm font-medium text-slate-700">Take Quiz</span>
            </Link>
            <Link to="/achievements" className="flex flex-col items-center p-4 rounded-lg hover:bg-slate-50 transition-colors duration-200">
              <FiAward className="w-8 h-8 text-yellow-600 mb-2" />
              <span className="text-sm font-medium text-slate-700">Achievements</span>
            </Link>
          </div>
        </div>
      </motion.div>

      {/* Recent Courses */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-slate-900">Featured Courses</h2>
          <Link to="/courses" className="btn btn-outline">
            View All
          </Link>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {recentCourses.map((course, index) => (
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

      {/* My Enrollments */}
      {(isStudent || isInstructor) && myEnrollments.length > 0 && (
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
        >
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-2xl font-bold text-slate-900">My Courses</h2>
            <Link to="/my-courses" className="btn btn-outline">
              View All
            </Link>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {myEnrollments.slice(0, 3).map((enrollment, index) => (
              <motion.div
                key={enrollment.id}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: index * 0.1 }}
                className="card"
              >
                <div className="card-body">
                  <h3 className="text-lg font-semibold text-slate-900 mb-2">
                    {enrollment.courseTitle}
                  </h3>
                  <div className="mb-4">
                    <div className="flex justify-between text-sm text-slate-600 mb-1">
                      <span>Progress</span>
                      <span>{enrollment.progressPercentage}%</span>
                    </div>
                    <div className="progress-bar">
                      <div 
                        className="progress-fill" 
                        style={{ width: `${enrollment.progressPercentage}%` }}
                      ></div>
                    </div>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className={`badge ${enrollment.isCompleted ? 'badge-success' : 'badge-warning'}`}>
                      {enrollment.isCompleted ? 'Completed' : 'In Progress'}
                    </span>
                    <Link
                      to={`/courses/${enrollment.courseId}`}
                      className="btn btn-primary btn-sm"
                    >
                      Continue
                    </Link>
                  </div>
                </div>
              </motion.div>
            ))}
          </div>
        </motion.div>
      )}

      {/* Assignments */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="card"
      >
        <div className="card-header">
          <h2 className="text-xl font-semibold text-slate-900">Recent Assignments</h2>
        </div>
        <div className="card-body">
          <div className="space-y-4">
            {assignments.map((assignment, index) => (
              <motion.div
                key={assignment.id}
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ delay: index * 0.1 }}
                className="flex items-center justify-between p-4 bg-slate-50 rounded-lg"
              >
                <div className="flex items-center space-x-3">
                  <div className={`w-10 h-10 rounded-full flex items-center justify-center ${
                    assignment.status === 'submitted' ? 'bg-green-100' : 'bg-yellow-100'
                  }`}>
                    {assignment.status === 'submitted' ? (
                      <FiCheckCircle className="w-5 h-5 text-green-600" />
                    ) : (
                      <FiClock className="w-5 h-5 text-yellow-600" />
                    )}
                  </div>
                  <div>
                    <h3 className="font-medium text-slate-900">{assignment.title}</h3>
                    <p className="text-sm text-slate-600">{assignment.course}</p>
                  </div>
                </div>
                <div className="text-right">
                  <p className="text-sm text-slate-600">Due: {assignment.dueDate}</p>
                  <p className="text-sm font-medium text-slate-900">{assignment.points} points</p>
                </div>
              </motion.div>
            ))}
          </div>
        </div>
      </motion.div>

      {/* Achievements */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-slate-900">Achievements</h2>
          <Link to="/achievements" className="btn btn-outline">
            View All
          </Link>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
          {achievements.map((achievement, index) => (
            <motion.div
              key={achievement.id}
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ delay: index * 0.1 }}
            >
              <AchievementCard achievement={achievement} />
            </motion.div>
          ))}
        </div>
      </motion.div>

      {/* Analytics Charts */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="grid grid-cols-1 lg:grid-cols-2 gap-6"
      >
        <div className="card">
          <ProgressChart
            data={{
              labels: ['Not Started', 'In Progress', 'Completed'],
              values: [2, 5, 8]
            }}
            title="Learning Progress"
            subtitle="Your course completion status"
          />
        </div>
        
        <div className="card">
          <StudentEngagementChart
            data={{
              labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4', 'Week 5', 'Week 6'],
              videoViews: [1200, 1350, 1420, 1580, 1650, 1720],
              quizAttempts: [800, 920, 1100, 1250, 1380, 1450],
              assignments: [600, 750, 850, 920, 980, 1050]
            }}
            title="Learning Activity"
            subtitle="Your weekly engagement metrics"
          />
        </div>
      </motion.div>

      {/* Leaderboard */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="card"
      >
        <div className="card-header">
          <h2 className="text-xl font-semibold text-slate-900">Leaderboard</h2>
        </div>
        <div className="card-body">
          <div className="space-y-3">
            {leaderboard.map((user, index) => (
              <motion.div
                key={user.rank}
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ delay: index * 0.1 }}
                className={`flex items-center justify-between p-3 rounded-lg ${
                  user.rank <= 3 ? 'bg-gradient-to-r from-yellow-50 to-orange-50' : 'bg-slate-50'
                }`}
              >
                <div className="flex items-center space-x-3">
                  <div className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-bold ${
                    user.rank === 1 ? 'bg-yellow-500 text-white' :
                    user.rank === 2 ? 'bg-gray-400 text-white' :
                    user.rank === 3 ? 'bg-orange-500 text-white' :
                    'bg-slate-300 text-slate-700'
                  }`}>
                    {user.rank}
                  </div>
                  <div className="w-10 h-10 bg-gradient-primary rounded-full flex items-center justify-center">
                    <span className="text-white text-sm font-medium">{user.avatar}</span>
                  </div>
                  <div>
                    <p className="font-medium text-slate-900">{user.name}</p>
                    <p className="text-sm text-slate-600">{user.streak} day streak</p>
                  </div>
                </div>
                <div className="text-right">
                  <p className="font-semibold text-slate-900">{user.points.toLocaleString()}</p>
                  <p className="text-sm text-slate-600">points</p>
                </div>
              </motion.div>
            ))}
          </div>
        </div>
      </motion.div>
    </div>
  );
};

export default Dashboard;

