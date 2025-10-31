import api from './auth';

export const coursesAPI = {
  // Public course endpoints
  getPublishedCourses: async () => {
    const response = await api.get('/courses/public');
    console.log("Courses API response:", response);
    return response.data.data;
  },

  searchCourses: async (query) => {
    const response = await api.get(`/courses/public/search?q=${encodeURIComponent(query)}`);
    return response.data.data;
  },

  getCoursesByCategory: async (category) => {
    const response = await api.get(`/courses/public/category/${encodeURIComponent(category)}`);
    return response.data.data;
  },

  getCoursesByDifficulty: async (difficulty) => {
    const response = await api.get(`/courses/public/difficulty/${difficulty}`);
    return response.data.data;
  },

  getFreeCourses: async () => {
    const response = await api.get('/courses/public/free');
    return response.data.data;
  },

  getLatestCourses: async (page = 0, size = 10) => {
    const response = await api.get(`/courses/public/latest?page=${page}&size=${size}`);
    return response.data.data;
  },

  getMostPopularCourses: async (page = 0, size = 10) => {
    const response = await api.get(`/courses/public/popular?page=${page}&size=${size}`);
    return response.data.data;
  },

  // Protected course endpoints
  getCourseById: async (id) => {
    const response = await api.get(`/courses/${id}`);
    return response.data.data;
  },

  createCourse: async (courseData) => {
    const response = await api.post('/courses', courseData);
    return response.data.data;
  },

  updateCourse: async (id, courseData) => {
    const response = await api.put(`/courses/${id}`, courseData);
    return response.data.data;
  },

  deleteCourse: async (id) => {
    const response = await api.delete(`/courses/${id}`);
    return response.data;
  },

  publishCourse: async (id) => {
    const response = await api.post(`/courses/${id}/publish`);
    return response.data.data;
  },

  unpublishCourse: async (id) => {
    const response = await api.post(`/courses/${id}/unpublish`);
    return response.data.data;
  },
};

