import React from 'react';
import AnalyticsChart from './AnalyticsChart';

const UserGrowthChart = ({ data, title = "User Growth", subtitle = "New user registrations over time" }) => {
  const chartData = {
    labels: data?.labels || ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
    datasets: [
      {
        label: 'New Users',
        data: data?.values || [1200, 1350, 1420, 1580, 1650, 1720],
        backgroundColor: [
          'rgba(34, 197, 94, 0.8)',
          'rgba(34, 197, 94, 0.6)',
          'rgba(34, 197, 94, 0.4)',
          'rgba(34, 197, 94, 0.2)',
          'rgba(34, 197, 94, 0.1)',
          'rgba(34, 197, 94, 0.05)'
        ],
        borderColor: 'rgb(34, 197, 94)',
        borderWidth: 2,
        borderRadius: 4,
        borderSkipped: false
      }
    ]
  };

  const options = {
    plugins: {
      legend: {
        display: false
      }
    },
    scales: {
      y: {
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
      type="bar"
      data={chartData}
      options={options}
      title={title}
      subtitle={subtitle}
      height={300}
    />
  );
};

export default UserGrowthChart;
