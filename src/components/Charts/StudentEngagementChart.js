import React from 'react';
import AnalyticsChart from './AnalyticsChart';

const StudentEngagementChart = ({ data, title = "Student Engagement", subtitle = "Weekly engagement metrics" }) => {
  const chartData = {
    labels: data?.labels || ['Week 1', 'Week 2', 'Week 3', 'Week 4', 'Week 5', 'Week 6'],
    datasets: [
      {
        label: 'Video Views',
        data: data?.videoViews || [1200, 1350, 1420, 1580, 1650, 1720],
        borderColor: 'rgb(59, 130, 246)',
        backgroundColor: 'rgba(59, 130, 246, 0.1)',
        borderWidth: 3,
        fill: true,
        tension: 0.4,
        yAxisID: 'y'
      },
      {
        label: 'Quiz Attempts',
        data: data?.quizAttempts || [800, 920, 1100, 1250, 1380, 1450],
        borderColor: 'rgb(34, 197, 94)',
        backgroundColor: 'rgba(34, 197, 94, 0.1)',
        borderWidth: 3,
        fill: true,
        tension: 0.4,
        yAxisID: 'y'
      },
      {
        label: 'Assignment Submissions',
        data: data?.assignments || [600, 750, 850, 920, 980, 1050],
        borderColor: 'rgb(245, 158, 11)',
        backgroundColor: 'rgba(245, 158, 11, 0.1)',
        borderWidth: 3,
        fill: true,
        tension: 0.4,
        yAxisID: 'y'
      }
    ]
  };

  const options = {
    plugins: {
      legend: {
        position: 'top',
        labels: {
          usePointStyle: true,
          padding: 20
        }
      }
    },
    scales: {
      y: {
        type: 'linear',
        display: true,
        position: 'left',
        beginAtZero: true,
        ticks: {
          callback: function(value) {
            return value.toLocaleString();
          }
        }
      }
    }
  };

  return (
    <AnalyticsChart
      type="line"
      data={chartData}
      options={options}
      title={title}
      subtitle={subtitle}
      height={300}
    />
  );
};

export default StudentEngagementChart;
