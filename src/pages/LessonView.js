import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { coursesAPI } from '../api/courses';
import LoadingSpinner from '../components/LoadingSpinner';

const LessonView = () => {
  const { courseId, lessonId } = useParams();
  const [course, setCourse] = useState(null);
  const [currentLesson, setCurrentLesson] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchCourseAndLesson();
  }, [courseId, lessonId]);

  const fetchCourseAndLesson = async () => {
    try {
      setLoading(true);
      const courseData = await coursesAPI.getCourseById(courseId);
      setCourse(courseData);
      
      const lesson = courseData.lessons?.find(l => l.id === parseInt(lessonId));
      setCurrentLesson(lesson);
    } catch (error) {
      console.error('Failed to fetch course and lesson:', error);
    } finally {
      setLoading(false);
    }
  };

  const getNextLesson = () => {
    if (!course?.lessons) return null;
    const currentIndex = course.lessons.findIndex(l => l.id === parseInt(lessonId));
    return currentIndex < course.lessons.length - 1 ? course.lessons[currentIndex + 1] : null;
  };

  const getPreviousLesson = () => {
    if (!course?.lessons) return null;
    const currentIndex = course.lessons.findIndex(l => l.id === parseInt(lessonId));
    return currentIndex > 0 ? course.lessons[currentIndex - 1] : null;
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (!course || !currentLesson) {
    return (
      <div className="text-center py-12">
        <h1 className="text-2xl font-bold text-gray-900 mb-4">Lesson not found</h1>
        <Link to={`/courses/${courseId}`} className="btn btn-primary">
          Back to Course
        </Link>
      </div>
    );
  }

  const nextLesson = getNextLesson();
  const previousLesson = getPreviousLesson();

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-xl font-semibold text-gray-900">{course.title}</h1>
              <p className="text-sm text-gray-600">{currentLesson.title}</p>
            </div>
            <Link
              to={`/courses/${courseId}`}
              className="btn btn-outline"
            >
              Back to Course
            </Link>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
          {/* Main Content */}
          <div className="lg:col-span-3">
            <div className="card">
              <div className="card-body">
                <h2 className="text-2xl font-bold text-gray-900 mb-4">
                  {currentLesson.title}
                </h2>
                
                {currentLesson.description && (
                  <p className="text-gray-600 mb-6">{currentLesson.description}</p>
                )}

                {/* Lesson Content */}
                <div className="prose max-w-none">
                  {currentLesson.lessonType === 'TEXT' && (
                    <div className="whitespace-pre-wrap">
                      {currentLesson.content}
                    </div>
                  )}
                  
                  {currentLesson.lessonType === 'VIDEO' && (
                    <div className="aspect-w-16 aspect-h-9 mb-6">
                      <video
                        controls
                        className="w-full h-96 bg-black rounded-lg"
                        poster={currentLesson.thumbnailUrl}
                      >
                        <source src={currentLesson.fileUrl} type="video/mp4" />
                        Your browser does not support the video tag.
                      </video>
                    </div>
                  )}
                  
                  {currentLesson.lessonType === 'AUDIO' && (
                    <div className="mb-6">
                      <audio controls className="w-full">
                        <source src={currentLesson.fileUrl} type="audio/mpeg" />
                        Your browser does not support the audio element.
                      </audio>
                    </div>
                  )}
                  
                  {currentLesson.lessonType === 'PDF' && (
                    <div className="mb-6">
                      <iframe
                        src={currentLesson.fileUrl}
                        className="w-full h-96 border rounded-lg"
                        title={currentLesson.title}
                      />
                    </div>
                  )}
                </div>

                {/* Navigation */}
                <div className="flex justify-between items-center mt-8 pt-6 border-t border-gray-200">
                  <div>
                    {previousLesson ? (
                      <Link
                        to={`/courses/${courseId}/lessons/${previousLesson.id}`}
                        className="btn btn-outline"
                      >
                        ← Previous: {previousLesson.title}
                      </Link>
                    ) : (
                      <div></div>
                    )}
                  </div>
                  
                  <div>
                    {nextLesson ? (
                      <Link
                        to={`/courses/${courseId}/lessons/${nextLesson.id}`}
                        className="btn btn-primary"
                      >
                        Next: {nextLesson.title} →
                      </Link>
                    ) : (
                      <Link
                        to={`/courses/${courseId}`}
                        className="btn btn-success"
                      >
                        Complete Course
                      </Link>
                    )}
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Sidebar */}
          <div className="lg:col-span-1">
            <div className="card sticky top-8">
              <div className="card-header">
                <h3 className="text-lg font-semibold text-gray-900">Course Lessons</h3>
              </div>
              <div className="card-body p-0">
                <div className="space-y-1">
                  {course.lessons?.map((lesson, index) => (
                    <Link
                      key={lesson.id}
                      to={`/courses/${courseId}/lessons/${lesson.id}`}
                      className={`block px-4 py-3 text-sm hover:bg-gray-50 ${
                        lesson.id === parseInt(lessonId)
                          ? 'bg-primary-50 text-primary-700 border-r-2 border-primary-600'
                          : 'text-gray-700'
                      }`}
                    >
                      <div className="flex items-center justify-between">
                        <span className="font-medium">{lesson.title}</span>
                        <span className="text-xs text-gray-500">
                          {lesson.formattedDuration || 'N/A'}
                        </span>
                      </div>
                    </Link>
                  ))}
                </div>
              </div>
            </div>

            {/* Course Info */}
            <div className="card mt-6">
              <div className="card-header">
                <h3 className="text-lg font-semibold text-gray-900">Course Info</h3>
              </div>
              <div className="card-body">
                <div className="space-y-3">
                  <div className="flex justify-between">
                    <span className="text-gray-600">Total Lessons</span>
                    <span className="font-medium">{course.lessonCount}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Duration</span>
                    <span className="font-medium">
                      {course.lessons?.reduce((total, lesson) => total + (lesson.durationMinutes || 0), 0)} min
                    </span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Difficulty</span>
                    <span className="font-medium">{course.difficultyLevel}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LessonView;

