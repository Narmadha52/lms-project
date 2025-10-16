import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { coursesAPI } from '../api/courses';
import { enrollmentsAPI } from '../api/enrollments';
import { useAuth } from '../context/AuthContext';
import LoadingSpinner from '../components/LoadingSpinner';

const CourseDetail = () => {
  const { id } = useParams();
  const { user, isStudent } = useAuth();
  const [course, setCourse] = useState(null);
  const [isEnrolled, setIsEnrolled] = useState(false);
  const [loading, setLoading] = useState(true);
  const [enrolling, setEnrolling] = useState(false);

  useEffect(() => {
    fetchCourseDetails();
  }, [id]);

  const fetchCourseDetails = async () => {
    try {
      setLoading(true);
      const courseData = await coursesAPI.getCourseById(id);
      setCourse(courseData);
      
      if (isStudent) {
        const enrollmentStatus = await enrollmentsAPI.isEnrolled(id);
        setIsEnrolled(enrollmentStatus);
      }
    } catch (error) {
      console.error('Failed to fetch course details:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleEnroll = async () => {
    try {
      setEnrolling(true);
      await enrollmentsAPI.enrollInCourse(id);
      setIsEnrolled(true);
    } catch (error) {
      console.error('Failed to enroll in course:', error);
      alert('Failed to enroll in course. Please try again.');
    } finally {
      setEnrolling(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (!course) {
    return (
      <div className="text-center py-12">
        <h1 className="text-2xl font-bold text-gray-900 mb-4">Course not found</h1>
        <Link to="/courses" className="btn btn-primary">
          Browse Courses
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Main Content */}
        <div className="lg:col-span-2">
          <div className="card">
            <div className="aspect-w-16 aspect-h-9">
              <img
                src={course.thumbnailUrl || 'https://via.placeholder.com/800x450?text=Course+Image'}
                alt={course.title}
                className="w-full h-64 lg:h-80 object-cover rounded-t-lg"
              />
            </div>
            
            <div className="card-body">
              <div className="flex items-center justify-between mb-4">
                <span className="badge badge-primary">
                  {course.difficultyLevel}
                </span>
                <span className="text-2xl font-bold text-primary-600">
                  {course.isFree ? 'Free' : `$${course.price}`}
                </span>
              </div>
              
              <h1 className="text-3xl font-bold text-gray-900 mb-4">
                {course.title}
              </h1>
              
              <p className="text-gray-600 text-lg mb-6">
                {course.description}
              </p>
              
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
                <div className="text-center">
                  <div className="text-2xl font-bold text-gray-900">{course.lessonCount}</div>
                  <div className="text-sm text-gray-600">Lessons</div>
                </div>
                <div className="text-center">
                  <div className="text-2xl font-bold text-gray-900">{course.enrollmentCount}</div>
                  <div className="text-sm text-gray-600">Students</div>
                </div>
                <div className="text-center">
                  <div className="text-2xl font-bold text-gray-900">{course.difficultyLevel}</div>
                  <div className="text-sm text-gray-600">Level</div>
                </div>
                <div className="text-center">
                  <div className="text-2xl font-bold text-gray-900">{course.category}</div>
                  <div className="text-sm text-gray-600">Category</div>
                </div>
              </div>
              
              <div className="border-t pt-6">
                <h3 className="text-lg font-semibold text-gray-900 mb-4">About the Instructor</h3>
                <div className="flex items-center">
                  <div className="w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center">
                    <span className="text-primary-600 font-semibold">
                      {course.instructorName?.charAt(0)}
                    </span>
                  </div>
                  <div className="ml-4">
                    <p className="font-semibold text-gray-900">{course.instructorName}</p>
                    <p className="text-gray-600">Course Instructor</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Sidebar */}
        <div className="lg:col-span-1">
          <div className="card sticky top-8">
            <div className="card-body">
              {isStudent ? (
                isEnrolled ? (
                  <div>
                    <div className="text-center mb-4">
                      <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-2">
                        <svg className="w-8 h-8 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                        </svg>
                      </div>
                      <h3 className="text-lg font-semibold text-gray-900 mb-2">You're enrolled!</h3>
                      <p className="text-gray-600">Start learning now</p>
                    </div>
                    <Link
                      to={`/courses/${id}/lessons/${course.lessons?.[0]?.id || 1}`}
                      className="btn btn-primary w-full mb-2"
                    >
                      Start Course
                    </Link>
                    <Link
                      to="/my-courses"
                      className="btn btn-outline w-full"
                    >
                      My Courses
                    </Link>
                  </div>
                ) : (
                  <div>
                    <div className="text-center mb-4">
                      <h3 className="text-lg font-semibold text-gray-900 mb-2">Ready to start?</h3>
                      <p className="text-gray-600">Join {course.enrollmentCount} other students</p>
                    </div>
                    <button
                      onClick={handleEnroll}
                      disabled={enrolling}
                      className="btn btn-primary w-full mb-2"
                    >
                      {enrolling ? (
                        <LoadingSpinner size="sm" />
                      ) : (
                        'Enroll Now'
                      )}
                    </button>
                    <p className="text-xs text-gray-500 text-center">
                      {course.isFree ? 'Free forever' : 'One-time payment'}
                    </p>
                  </div>
                )
              ) : (
                <div className="text-center">
                  <p className="text-gray-600 mb-4">
                    Sign in as a student to enroll in this course.
                  </p>
                  <Link to="/login" className="btn btn-primary w-full">
                    Sign In
                  </Link>
                </div>
              )}
            </div>
          </div>

          {/* Course Info */}
          <div className="card mt-6">
            <div className="card-header">
              <h3 className="text-lg font-semibold text-gray-900">Course Details</h3>
            </div>
            <div className="card-body">
              <div className="space-y-3">
                <div className="flex justify-between">
                  <span className="text-gray-600">Category</span>
                  <span className="font-medium">{course.category}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Difficulty</span>
                  <span className="font-medium">{course.difficultyLevel}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Lessons</span>
                  <span className="font-medium">{course.lessonCount}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Students</span>
                  <span className="font-medium">{course.enrollmentCount}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Price</span>
                  <span className="font-medium">{course.isFree ? 'Free' : `$${course.price}`}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CourseDetail;

