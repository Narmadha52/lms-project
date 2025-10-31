import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { coursesAPI } from '../api/courses';
import LoadingSpinner from '../components/LoadingSpinner';

const CourseList = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [selectedDifficulty, setSelectedDifficulty] = useState('');
  const [showFreeOnly, setShowFreeOnly] = useState(false);

  const categories = [
    'Programming',
    'Web Development',
    'Data Science',
    'Design',
    'Business',
    'Language'
  ];

  const difficulties = ['BEGINNER', 'INTERMEDIATE', 'ADVANCED'];

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = async () => {
    try {
      setLoading(true);
      const data = await coursesAPI.getPublishedCourses();
      setCourses(data);
    } catch (error) {
      console.error('Failed to fetch courses:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    try {
      setLoading(true);
      let data;
      
      if (searchTerm.trim()) {
        data = await coursesAPI.searchCourses(searchTerm);
      } else {
        data = await coursesAPI.getPublishedCourses();
      }
      
      // Apply filters
      let filteredData = data;
      
      if (selectedCategory) {
        filteredData = filteredData.filter(course => course.category === selectedCategory);
      }
      
      if (selectedDifficulty) {
        filteredData = filteredData.filter(course => course.difficultyLevel === selectedDifficulty);
      }
      
      if (showFreeOnly) {
        filteredData = filteredData.filter(course => course.isFree);
      }
      
      setCourses(filteredData);
    } catch (error) {
      console.error('Failed to search courses:', error);
    } finally {
      setLoading(false);
    }
  };

  const clearFilters = () => {
    setSearchTerm('');
    setSelectedCategory('');
    setSelectedDifficulty('');
    setShowFreeOnly(false);
    fetchCourses();
  };

  const CourseCard = ({ course }) => (
    <div className="card hover:shadow-lg transition-shadow duration-200">
      <div className="aspect-w-16 aspect-h-9">
        <img
          src={course.thumbnailUrl || 'https://via.placeholder.com/300x200?text=Course+Image'}
          alt={course.title}
          className="w-full h-48 object-cover rounded-t-lg"
        />
      </div>
      <div className="card-body">
        <div className="flex items-center justify-between mb-2">
          <span className="badge badge-primary">
            {course.difficultyLevel}
          </span>
          <span className="text-sm text-gray-500">
            {course.enrollmentCount} students
          </span>
        </div>
        
        <h3 className="text-lg font-semibold text-gray-900 mb-2 line-clamp-2">
          {course.title}
        </h3>
        
        <p className="text-gray-600 text-sm mb-3 line-clamp-2">
          {course.description}
        </p>
        
        <div className="flex items-center justify-between mb-4">
          <span className="text-sm text-gray-500">
            by {course.instructorName}
          </span>
          <span className="text-lg font-bold text-primary-600">
            {course.isFree ? 'Free' : `$${course.price}`}
          </span>
        </div>
        
        <div className="flex items-center justify-between">
          <span className="text-sm text-gray-500">
            {course.lessonCount} lessons
          </span>
          <Link
            to={`/courses/${course.id}`}
            className="btn btn-primary"
          >
            View Course
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
          Browse Courses
        </h1>
        <p className="text-gray-600">
          Discover amazing courses to enhance your skills and knowledge.
        </p>
      </div>

      {/* Search and Filters */}
      <div className="card mb-8">
        <div className="card-body">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-4">
            <div>
              <label className="form-label">Search</label>
              <input
                type="text"
                placeholder="Search courses..."
                className="form-input"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
            
            <div>
              <label className="form-label">Category</label>
              <select
                className="form-input"
                value={selectedCategory}
                onChange={(e) => setSelectedCategory(e.target.value)}
              >
                <option value="">All Categories</option>
                {categories.map(category => (
                  <option key={category} value={category}>{category}</option>
                ))}
              </select>
            </div>
            
            <div>
              <label className="form-label">Difficulty</label>
              <select
                className="form-input"
                value={selectedDifficulty}
                onChange={(e) => setSelectedDifficulty(e.target.value)}
              >
                <option value="">All Levels</option>
                {difficulties.map(difficulty => (
                  <option key={difficulty} value={difficulty}>{difficulty}</option>
                ))}
              </select>
            </div>
            
            <div className="flex items-end">
              <label className="flex items-center">
                <input
                  type="checkbox"
                  className="rounded border-gray-300 text-primary-600 focus:ring-primary-500"
                  checked={showFreeOnly}
                  onChange={(e) => setShowFreeOnly(e.target.checked)}
                />
                <span className="ml-2 text-sm text-gray-700">Free only</span>
              </label>
            </div>
          </div>
          
          <div className="flex space-x-4">
            <button
              onClick={handleSearch}
              className="btn btn-primary"
            >
              Search
            </button>
            <button
              onClick={clearFilters}
              className="btn btn-secondary"
            >
              Clear Filters
            </button>
          </div>
        </div>
      </div>

      {/* Results */}
      <div className="mb-4">
        <p className="text-gray-600">
          {courses.length} course{courses.length !== 1 ? 's' : ''} found
        </p>
      </div>

      {courses.length === 0 ? (
        <div className="text-center py-12">
          <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
          </svg>
          <h3 className="mt-2 text-sm font-medium text-gray-900">No courses found</h3>
          <p className="mt-1 text-sm text-gray-500">
            Try adjusting your search criteria or browse all courses.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {courses.map((course) => (
            <CourseCard key={course.id} course={course} />
          ))}
        </div>
      )}
    </div>
  );
};

export default CourseList;

