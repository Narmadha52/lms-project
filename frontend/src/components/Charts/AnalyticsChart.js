
import React, { useRef } from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  Filler
} from 'chart.js';
import { Line, Bar, Doughnut, Pie, Radar } from 'react-chartjs-2';

// Register Chart.js components
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  Filler
);

const AnalyticsChart = ({ 
  type = 'line', 
  data, 
  options = {}, 
  height = 300,
  className = '',
  title,
  subtitle
}) => {
  const chartRef = useRef(null);

  // Default chart options
  const defaultOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top',
        labels: {
          usePointStyle: true,
          padding: 20,
          font: {
            family: 'Poppins, Inter, system-ui, sans-serif',
            size: 12
          }
        }
      },
      tooltip: {
        backgroundColor: 'rgba(0, 0, 0, 0.8)',
        titleColor: 'white',
        bodyColor: 'white',
        borderColor: 'rgba(255, 255, 255, 0.1)',
        borderWidth: 1,
        cornerRadius: 8,
        displayColors: true,
        padding: 12,
        titleFont: {
          family: 'Poppins, Inter, system-ui, sans-serif',
          size: 14,
          weight: '600'
        },
        bodyFont: {
          family: 'Poppins, Inter, system-ui, sans-serif',
          size: 12
        }
      }
    },
    scales: type !== 'doughnut' && type !== 'pie' ? {
      x: {
        grid: {
          display: false
        },
        ticks: {
          font: {
            family: 'Poppins, Inter, system-ui, sans-serif',
            size: 11
          },
          color: '#64748b'
        }
      },
      y: {
        grid: {
          color: 'rgba(148, 163, 184, 0.1)',
          drawBorder: false
        },
        ticks: {
          font: {
            family: 'Poppins, Inter, system-ui, sans-serif',
            size: 11
          },
          color: '#64748b'
        }
      }
    } : {},
    ...options
  };

  const renderChart = () => {
    const chartProps = {
      ref: chartRef,
      data,
      options: defaultOptions
    };

    switch (type) {
      case 'line':
        return <Line {...chartProps} />;
      case 'bar':
        return <Bar {...chartProps} />;
      case 'doughnut':
        return <Doughnut {...chartProps} />;
      case 'pie':
        return <Pie {...chartProps} />;
      case 'radar':
        return <Radar {...chartProps} />;
      default:
        return <Line {...chartProps} />;
    }
  };

  return (
    <div className={`chart-container ${className}`}>
      {(title || subtitle) && (
        <div className="mb-4">
          {title && (
            <h3 className="text-lg font-semibold text-slate-900 mb-1">{title}</h3>
          )}
          {subtitle && (
            <p className="text-sm text-slate-600">{subtitle}</p>
          )}
        </div>
      )}
      <div style={{ height: `${height}px` }}>
        {renderChart()}
      </div>
    </div>
  );
};

export default AnalyticsChart;
