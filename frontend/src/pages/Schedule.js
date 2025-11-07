import React, { useState } from 'react';
import { Calendar, Clock, CheckCircle, XCircle, Award, Bell, User, Lock, Palette, Globe } from 'lucide-react';

export const Schedule = () => {
  const [view, setView] = useState('week');
  const [events] = useState([
    { id: 1, title: 'React Advanced Patterns', type: 'lesson', date: '2025-11-08', time: '10:00 AM', duration: '1h 30m', instructor: 'Sarah Johnson' },
    { id: 2, title: 'JavaScript Quiz', type: 'quiz', date: '2025-11-08', time: '2:00 PM', duration: '30m', questions: 15 },
    { id: 3, title: 'CSS Grid Workshop', type: 'lesson', date: '2025-11-09', time: '3:00 PM', duration: '2h', instructor: 'Mike Chen' },
    { id: 4, title: 'Node.js Fundamentals', type: 'lesson', date: '2025-11-10', time: '11:00 AM', duration: '1h 45m', instructor: 'Emma Davis' },
    { id: 5, title: 'React Hooks Quiz', type: 'quiz', date: '2025-11-11', time: '4:00 PM', duration: '45m', questions: 20 },
    { id: 6, title: 'Web Performance Seminar', type: 'event', date: '2025-11-12', time: '1:00 PM', duration: '3h', speaker: 'Alex Turner' }
  ]);

  const getEventColor = (type) => {
    switch(type) {
      case 'lesson': return 'bg-blue-100 border-blue-500 text-blue-800';
      case 'quiz': return 'bg-purple-100 border-purple-500 text-purple-800';
      case 'event': return 'bg-green-100 border-green-500 text-green-800';
      default: return 'bg-gray-100 border-gray-500 text-gray-800';
    }
  };

  const getEventIcon = (type) => {
    switch(type) {
      case 'lesson': return <Calendar size={18} />;
      case 'quiz': return <CheckCircle size={18} />;
      case 'event': return <Bell size={18} />;
      default: return <Clock size={18} />;
    }
  };

  return (
    <div className="p-6 max-w-6xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-800">Schedule</h1>
        <div className="flex gap-2">
          <button
            onClick={() => setView('week')}
            className={`px-4 py-2 rounded-lg ${view === 'week' ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'}`}
          >
            Week
          </button>
          <button
            onClick={() => setView('list')}
            className={`px-4 py-2 rounded-lg ${view === 'list' ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'}`}
          >
            List
          </button>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="space-y-4">
          {events.map((event) => (
            <div
              key={event.id}
              className={`border-l-4 rounded-lg p-4 ${getEventColor(event.type)}`}
            >
              <div className="flex items-start justify-between">
                <div className="flex items-start gap-3 flex-1">
                  <div className="mt-1">{getEventIcon(event.type)}</div>
                  <div className="flex-1">
                    <h3 className="font-semibold text-lg mb-1">{event.title}</h3>
                    <div className="flex flex-wrap gap-4 text-sm">
                      <span className="flex items-center gap-1">
                        <Calendar size={14} />
                        {new Date(event.date).toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric' })}
                      </span>
                      <span className="flex items-center gap-1">
                        <Clock size={14} />
                        {event.time}
                      </span>
                      <span>Duration: {event.duration}</span>
                    </div>
                    {event.instructor && (
                      <p className="text-sm mt-2">Instructor: {event.instructor}</p>
                    )}
                    {event.questions && (
                      <p className="text-sm mt-2">{event.questions} questions</p>
                    )}
                    {event.speaker && (
                      <p className="text-sm mt-2">Speaker: {event.speaker}</p>
                    )}
                  </div>
                </div>
                <button className="bg-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-opacity-90 transition-colors">
                  View Details
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Schedule;
