import React, { useState } from 'react';
import { Calendar, Clock, CheckCircle, XCircle, Award, Bell, User, Lock, Palette, Globe } from 'lucide-react';


const Settings = () => {
  const [settings, setSettings] = useState({
    name: 'John Doe',
    email: 'john.doe@example.com',
    notifications: true,
    emailDigest: 'daily',
    theme: 'light',
    language: 'English',
    autoPlay: false,
    subtitles: true
  });

  const handleChange = (key, value) => {
    setSettings({ ...settings, [key]: value });
  };

  return (
    <div className="p-6 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-6 text-gray-800">Settings</h1>
      
      <div className="space-y-6">
        {/* Account Settings */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex items-center gap-2 mb-4">
            <User className="text-blue-600" size={24} />
            <h2 className="text-xl font-semibold text-gray-800">Account Settings</h2>
          </div>
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Full Name</label>
              <input
                type="text"
                value={settings.name}
                onChange={(e) => handleChange('name', e.target.value)}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Email Address</label>
              <input
                type="email"
                value={settings.email}
                onChange={(e) => handleChange('email', e.target.value)}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
            <button className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700">
              Update Account
            </button>
          </div>
        </div>

        {/* Notification Settings */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex items-center gap-2 mb-4">
            <Bell className="text-blue-600" size={24} />
            <h2 className="text-xl font-semibold text-gray-800">Notifications</h2>
          </div>
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="font-medium text-gray-800">Push Notifications</p>
                <p className="text-sm text-gray-600">Receive notifications about lessons and quizzes</p>
              </div>
              <label className="relative inline-block w-12 h-6">
                <input
                  type="checkbox"
                  checked={settings.notifications}
                  onChange={(e) => handleChange('notifications', e.target.checked)}
                  className="sr-only peer"
                />
                <div className="w-12 h-6 bg-gray-300 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-6 after:content-[''] after:absolute after:top-0.5 after:left-0.5 after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
              </label>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Email Digest</label>
              <select
                value={settings.emailDigest}
                onChange={(e) => handleChange('emailDigest', e.target.value)}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                <option value="daily">Daily</option>
                <option value="weekly">Weekly</option>
                <option value="never">Never</option>
              </select>
            </div>
          </div>
        </div>

        {/* Appearance Settings */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex items-center gap-2 mb-4">
            <Palette className="text-blue-600" size={24} />
            <h2 className="text-xl font-semibold text-gray-800">Appearance</h2>
          </div>
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Theme</label>
              <div className="grid grid-cols-2 gap-4">
                <button
                  onClick={() => handleChange('theme', 'light')}
                  className={`p-4 border-2 rounded-lg ${settings.theme === 'light' ? 'border-blue-600 bg-blue-50' : 'border-gray-300'}`}
                >
                  <div className="text-center">☀️ Light</div>
                </button>
                <button
                  onClick={() => handleChange('theme', 'dark')}
                  className={`p-4 border-2 rounded-lg ${settings.theme === 'dark' ? 'border-blue-600 bg-blue-50' : 'border-gray-300'}`}
                >
                  <div className="text-center">🌙 Dark</div>
                </button>
              </div>
            </div>
          </div>
        </div>

        {/* Language & Region */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex items-center gap-2 mb-4">
            <Globe className="text-blue-600" size={24} />
            <h2 className="text-xl font-semibold text-gray-800">Language & Region</h2>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Language</label>
            <select
              value={settings.language}
              onChange={(e) => handleChange('language', e.target.value)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option>English</option>
              <option>Spanish</option>
              <option>French</option>
              <option>German</option>
              <option>Chinese</option>
            </select>
          </div>
        </div>

        {/* Privacy & Security */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex items-center gap-2 mb-4">
            <Lock className="text-blue-600" size={24} />
            <h2 className="text-xl font-semibold text-gray-800">Privacy & Security</h2>
          </div>
          <div className="space-y-3">
            <button className="w-full text-left px-4 py-3 bg-gray-50 hover:bg-gray-100 rounded-lg transition-colors">
              Change Password
            </button>
            <button className="w-full text-left px-4 py-3 bg-gray-50 hover:bg-gray-100 rounded-lg transition-colors">
              Two-Factor Authentication
            </button>
            <button className="w-full text-left px-4 py-3 bg-gray-50 hover:bg-gray-100 rounded-lg transition-colors">
              Privacy Settings
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Settings;
