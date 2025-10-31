import api from './auth';

export const enrollmentsAPI = {
  enrollInCourse: async (courseId) => {
    const response = await api.post(`/enrollments/${courseId}`);
    return response.data.data;
  },

  unenrollFromCourse: async (courseId) => {
    const response = await api.delete(`/enrollments/${courseId}`);
    return response.data;
  },

  getMyEnrollments: async () => {
    const response = await api.get('/enrollments/my-enrollments');
    return response.data.data;
  },

  getCourseEnrollments: async (courseId) => {
    const response = await api.get(`/enrollments/course/${courseId}`);
    return response.data.data;
  },

  isEnrolled: async (courseId) => {
    const response = await api.get(`/enrollments/${courseId}/status`);
    return response.data.data;
  },

  getEnrollment: async (courseId) => {
    const response = await api.get(`/enrollments/${courseId}`);
    return response.data.data;
  },
};

