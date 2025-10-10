import React from 'react';
import AnalyticsChart from './AnalyticsChart';

const CourseCompletionChart = ({ data, title = "Course Completion", subtitle = "Completion rates by course" }) => {
  const chartData = {
    labels: data?.labels || ['JavaScript', 'React', 'Node.js', 'Python', 'CSS', 'HTML'],
    datasets: [
      {
        label: 'Completion Rate (%)',
        data: data?.values || [85, 78, 92, 88, 95, 90],
        backgroundColor: [
          'rgba(59, 130, 246, 0.8)',
          'rgba(34, 197, 94, 0.8)',
          'rgba(245, 158, 11, 0.8)',
          'rgba(239, 68, 68, 0.8)',
          'rgba(139, 92, 246, 0.8)',
          'rgba(236, 72, 153, 0.8)'
        ],
        borderColor: [
          'rgb(59, 130, 246)',
          'rgb(34, 197, 94)',
          'rgb(245, 158, 11)',
          'rgb(239, 68, 68)',
          'rgb(139, 92, 246)',
          'rgb(236, 72, 153)'
        ],
        borderWidth: 2
      }
    ]
  };

  const options = {
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          padding: 20,
          usePointStyle: true
        }
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        max: 100,
        ticks: {
          callback: function(value) {
            return value + '%';
          }
        }
      }
    }
  };

  return (
    <AnalyticsChart
      type="bar"
      data={chartData}
      options={options}
      title={title}
      subtitle={subtitle}
      height={300}
    />
  );
};

export default CourseCompletionChart;
