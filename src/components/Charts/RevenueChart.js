import React from 'react';
import AnalyticsChart from './AnalyticsChart';

const RevenueChart = ({ data, title = "Revenue Analytics", subtitle = "Monthly revenue trends" }) => {
  const chartData = {
    labels: data?.labels || ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
    datasets: [
      {
        label: 'Revenue',
        data: data?.values || [12000, 15000, 18000, 22000, 25000, 28000],
        borderColor: 'rgb(59, 130, 246)',
        backgroundColor: 'rgba(59, 130, 246, 0.1)',
        borderWidth: 3,
        fill: true,
        tension: 0.4,
        pointBackgroundColor: 'rgb(59, 130, 246)',
        pointBorderColor: 'white',
        pointBorderWidth: 2,
        pointRadius: 6,
        pointHoverRadius: 8
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
            return '$' + value.toLocaleString();
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

export default RevenueChart;
