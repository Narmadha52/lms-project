import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { enrollmentsAPI } from '../api/enrollments';
import LoadingSpinner from '../components/LoadingSpinner';

const MyCourses = () => {
  const [enrollments, setEnrollments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    fetchEnrollments();
  }, []);

  const fetchEnrollments = async () => {
    try {
      setLoading(true);
      const data = await enrollmentsAPI.getMyEnrollments();
      setEnrollments(data);
    } catch (error) {
      console.error('Failed to fetch enrollments:', error);
    } finally {
      setLoading(false);
    }
  };

  const filteredEnrollments = enrollments.filter(enrollment => {
    if (filter === 'completed') return enrollment.isCompleted;
    if (filter === 'in-progress') return !enrollment.isCompleted;
    return true;
  });

  const CourseCard = ({ enrollment }) => (
    <div className="card hover:shadow-lg transition-shadow duration-200">
      <div className="aspect-w-16 aspect-h-9">
        <img
          src="https://via.placeholder.com/300x200?text=Course+Image"
          alt={enrollment.courseTitle}
          className="w-full h-48 object-cover rounded-t-lg"
        />
      </div>
      <div className="card-body">
        <div className="flex items-center justify-between mb-2">
          <span className={`badge ${enrollment.isCompleted ? 'badge-success' : 'badge-warning'}`}>
            {enrollment.isCompleted ? 'Completed' : 'In Progress'}
          </span>
          <span className="text-sm text-gray-500">
            {enrollment.completedLessonsCount}/{enrollment.totalLessonsCount} lessons
          </span>
        </div>
        
        <h3 className="text-lg font-semibold text-gray-900 mb-2">
          {enrollment.courseTitle}
        </h3>
        
        <div className="mb-4">
          <div className="flex justify-between text-sm text-gray-600 mb-1">
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
          <span className="text-sm text-gray-500">
            Enrolled {new Date(enrollment.enrolledAt).toLocaleDateString()}
          </span>
          <Link
            to={`/courses/${enrollment.courseId}`}
            className="btn btn-primary"
          >
            {enrollment.isCompleted ? 'Review' : 'Continue'}
          </Link>
        </div>
      </div>
    </div>
  );

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  return (
    <div className="p-6">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">
          My Courses
        </h1>
        <p className="text-gray-600">
          Track your learning progress and continue your journey.
        </p>
      </div>

      {/* Filter Tabs */}
      <div className="mb-6">
        <div className="border-b border-gray-200">
          <nav className="-mb-px flex space-x-8">
            <button
              onClick={() => setFilter('all')}
              className={`py-2 px-1 border-b-2 font-medium text-sm ${
                filter === 'all'
                  ? 'border-primary-500 text-primary-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              All Courses ({enrollments.length})
            </button>
            <button
              onClick={() => setFilter('in-progress')}
              className={`py-2 px-1 border-b-2 font-medium text-sm ${
                filter === 'in-progress'
                  ? 'border-primary-500 text-primary-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              In Progress ({enrollments.filter(e => !e.isCompleted).length})
            </button>
            <button
              onClick={() => setFilter('completed')}
              className={`py-2 px-1 border-b-2 font-medium text-sm ${
                filter === 'completed'
                  ? 'border-primary-500 text-primary-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              Completed ({enrollments.filter(e => e.isCompleted).length})
            </button>
          </nav>
        </div>
      </div>

      {filteredEnrollments.length === 0 ? (
        <div className="text-center py-12">
          <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
          </svg>
          <h3 className="mt-2 text-sm font-medium text-gray-900">
            {filter === 'all' ? 'No courses yet' : `No ${filter.replace('-', ' ')} courses`}
          </h3>
          <p className="mt-1 text-sm text-gray-500">
            {filter === 'all' 
              ? 'Start by browsing and enrolling in courses.'
              : `You don't have any ${filter.replace('-', ' ')} courses yet.`
            }
          </p>
          <div className="mt-6">
            <Link to="/courses" className="btn btn-primary">
              Browse Courses
            </Link>
          </div>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredEnrollments.map((enrollment) => (
            <CourseCard key={enrollment.id} enrollment={enrollment} />
          ))}
        </div>
      )}
    </div>
  );
};

export default MyCourses;

