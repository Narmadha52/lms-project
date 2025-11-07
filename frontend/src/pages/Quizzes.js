import React, { useState } from 'react';
import { Calendar, Clock, CheckCircle, XCircle, Award, Bell, User, Lock, Palette, Globe } from 'lucide-react';

export const Quizzes = () => {
  const [activeQuiz, setActiveQuiz] = useState(null);
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [score, setScore] = useState(0);
  const [showResults, setShowResults] = useState(false);
  const [selectedAnswer, setSelectedAnswer] = useState(null);

  const quizzes = [
    {
      id: 1,
      title: 'JavaScript Fundamentals',
      questionCount: 10,
      duration: '15 min',
      difficulty: 'Beginner',
      completed: true,
      score: 85,
      questions: [
        {
          question: 'What does "const" keyword do in JavaScript?',
          options: ['Creates a mutable variable', 'Creates a constant variable', 'Creates a function', 'Deletes a variable'],
          correct: 1
        },
        {
          question: 'Which method adds an element to the end of an array?',
          options: ['pop()', 'shift()', 'push()', 'unshift()'],
          correct: 2
        }
      ]
    },
    {
      id: 2,
      title: 'React Hooks',
      questionCount: 8,
      duration: '12 min',
      difficulty: 'Intermediate',
      completed: false,
      questions: [
        {
          question: 'Which hook is used for side effects in React?',
          options: ['useState', 'useEffect', 'useContext', 'useMemo'],
          correct: 1
        },
        {
          question: 'What does useState return?',
          options: ['An object', 'A string', 'An array with two elements', 'A function'],
          correct: 2
        }
      ]
    },
    {
      id: 3,
      title: 'CSS Grid & Flexbox',
      questionCount: 12,
      duration: '18 min',
      difficulty: 'Intermediate',
      completed: false,
      questions: [
        {
          question: 'How do you center items with Flexbox?',
          options: ['text-align: center', 'justify-content: center', 'margin: auto', 'position: center'],
          correct: 1
        },
        {
          question: 'What is the default flex-direction value?',
          options: ['column', 'row', 'wrap', 'block'],
          correct: 1
        }
      ]
    }
  ];

  const startQuiz = (quiz) => {
    setActiveQuiz(quiz);
    setCurrentQuestion(0);
    setScore(0);
    setShowResults(false);
    setSelectedAnswer(null);
  };

  const handleAnswer = (answerIndex) => {
    setSelectedAnswer(answerIndex);
    if (answerIndex === activeQuiz.questions[currentQuestion].correct) {
      setScore(score + 1);
    }

    setTimeout(() => {
      if (currentQuestion < activeQuiz.questions.length - 1) {
        setCurrentQuestion(currentQuestion + 1);
        setSelectedAnswer(null);
      } else {
        setShowResults(true);
      }
    }, 1000);
  };

  const resetQuiz = () => {
    setActiveQuiz(null);
    setCurrentQuestion(0);
    setScore(0);
    setShowResults(false);
    setSelectedAnswer(null);
  };

  if (activeQuiz && !showResults) {
    const question = activeQuiz.questions[currentQuestion];
    return (
      <div className="p-6 max-w-3xl mx-auto">
        <div className="mb-6">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-2xl font-bold text-gray-800">{activeQuiz.title}</h2>
            <button onClick={resetQuiz} className="text-gray-600 hover:text-gray-800">Exit Quiz</button>
          </div>
          <div className="w-full bg-gray-200 rounded-full h-2">
            <div 
              className="bg-blue-600 h-2 rounded-full transition-all duration-300"
              style={{ width: `${((currentQuestion + 1) / activeQuiz.questions.length) * 100}%` }}
            />
          </div>
          <p className="text-sm text-gray-600 mt-2">Question {currentQuestion + 1} of {activeQuiz.questions.length}</p>
        </div>

        <div className="bg-white rounded-lg shadow-md p-8">
          <h3 className="text-xl font-semibold mb-6 text-gray-800">{question.question}</h3>
          <div className="space-y-3">
            {question.options.map((option, idx) => (
              <button
                key={idx}
                onClick={() => handleAnswer(idx)}
                disabled={selectedAnswer !== null}
                className={`w-full p-4 text-left rounded-lg border-2 transition-all ${
                  selectedAnswer === null
                    ? 'border-gray-300 hover:border-blue-500 hover:bg-blue-50'
                    : selectedAnswer === idx
                    ? idx === question.correct
                      ? 'border-green-500 bg-green-50'
                      : 'border-red-500 bg-red-50'
                    : idx === question.correct
                    ? 'border-green-500 bg-green-50'
                    : 'border-gray-300 bg-gray-50'
                }`}
              >
                {option}
                {selectedAnswer !== null && idx === question.correct && (
                  <CheckCircle className="inline ml-2 text-green-600" size={20} />
                )}
                {selectedAnswer === idx && idx !== question.correct && (
                  <XCircle className="inline ml-2 text-red-600" size={20} />
                )}
              </button>
            ))}
          </div>
        </div>
      </div>
    );
  }

  if (showResults) {
    const percentage = Math.round((score / activeQuiz.questions.length) * 100);
    return (
      <div className="p-6 max-w-2xl mx-auto">
        <div className="bg-white rounded-lg shadow-lg p-8 text-center">
          <Award className="mx-auto text-yellow-500 mb-4" size={64} />
          <h2 className="text-3xl font-bold mb-4 text-gray-800">Quiz Complete!</h2>
          <div className="text-6xl font-bold text-blue-600 mb-2">{percentage}%</div>
          <p className="text-xl text-gray-600 mb-6">
            You scored {score} out of {activeQuiz.questions.length}
          </p>
          <div className="flex gap-4 justify-center">
            <button
              onClick={() => startQuiz(activeQuiz)}
              className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700"
            >
              Retake Quiz
            </button>
            <button
              onClick={resetQuiz}
              className="bg-gray-200 text-gray-800 px-6 py-3 rounded-lg hover:bg-gray-300"
            >
              Back to Quizzes
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="p-6 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-6 text-gray-800">Quizzes</h1>
      <div className="grid gap-6 md:grid-cols-2">
        {quizzes.map((quiz) => (
          <div key={quiz.id} className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
            <div className="flex justify-between items-start mb-4">
              <h3 className="text-xl font-semibold text-gray-800">{quiz.title}</h3>
              <span className={`px-3 py-1 rounded-full text-sm ${
                quiz.difficulty === 'Beginner' ? 'bg-green-100 text-green-800' :
                quiz.difficulty === 'Intermediate' ? 'bg-yellow-100 text-yellow-800' :
                'bg-red-100 text-red-800'
              }`}>
                {quiz.difficulty}
              </span>
            </div>
            <div className="flex gap-4 text-sm text-gray-600 mb-4">
              <span className="flex items-center gap-1">
                <CheckCircle size={16} />
                {quiz.questionCount} questions
              </span>
              <span className="flex items-center gap-1">
                <Clock size={16} />
                {quiz.duration}
              </span>
            </div>
            {quiz.completed ? (
              <div className="mb-4">
                <div className="flex justify-between text-sm mb-2">
                  <span className="text-gray-600">Previous Score:</span>
                  <span className="font-semibold text-green-600">{quiz.score}%</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div 
                    className="bg-green-500 h-2 rounded-full"
                    style={{ width: `${quiz.score}%` }}
                  />
                </div>
              </div>
            ) : null}
            <button
              onClick={() => startQuiz(quiz)}
              className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition-colors"
            >
              {quiz.completed ? 'Retake Quiz' : 'Start Quiz'}
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Quizzes;
