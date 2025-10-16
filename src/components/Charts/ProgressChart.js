import React from 'react';
import AnalyticsChart from './AnalyticsChart';

const ProgressChart = ({ data, title = "Learning Progress", subtitle = "Your course completion progress" }) => {
  const chartData = {
    labels: data?.labels || ['Not Started', 'In Progress', 'Completed'],
    datasets: [
      {
        data: data?.values || [2, 5, 8],
        backgroundColor: [
          'rgba(148, 163, 184, 0.8)',
          'rgba(245, 158, 11, 0.8)',
          'rgba(34, 197, 94, 0.8)'
        ],
        borderColor: [
          'rgb(148, 163, 184)',
          'rgb(245, 158, 11)',
          'rgb(34, 197, 94)'
        ],
        borderWidth: 2,
        hoverOffset: 4
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
    }
  };

  return (
    <AnalyticsChart
      type="doughnut"
      data={chartData}
      options={options}
      title={title}
      subtitle={subtitle}
      height={300}
    />
  );
};

export default ProgressChart;
