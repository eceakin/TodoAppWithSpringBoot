import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { todoListService } from '../services/todoListService';
import CreateTodoListModal from './CreateTodoListModal';
import './Dashboard.css';

const Dashboard = ({ onLogout }) => {
  const [todoLists, setTodoLists] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [currentUser, setCurrentUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetchTodoLists();
    const userStr = localStorage.getItem('user');
    if (userStr) setCurrentUser(JSON.parse(userStr));
  }, []);

  const fetchTodoLists = async () => {
    try {
      const lists = await todoListService.getMyTodoLists();
      setTodoLists(lists);
    } catch (error) {
      console.error('Error fetching todo lists:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTodoList = async (todoListData) => {
    try {
      await todoListService.createTodoList(todoListData);
      setShowCreateModal(false);
      fetchTodoLists();
    } catch (error) {
      console.error('Error creating todo list:', error);
    }
  };

  const handleDeleteTodoList = async (id) => {
    if (window.confirm('Are you sure you want to delete this todo list?')) {
      try {
        await todoListService.deleteTodoList(id);
        fetchTodoLists();
      } catch (error) {
        console.error('Error deleting todo list:', error);
      }
    }
  };

  const handleEditTodoList = (id) => {
    navigate(`/todolist/${id}`);
  };

  const calculateProgress = (completed, total) => {
    if (total === 0) return 0;
    return Math.round((completed / total) * 100);
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
      </div>
    );
  }

  return (
    <div className="dashboard">
      <header className="header">
        <h1 className="header-title">TodoApp</h1>
        <div className="header-actions">
          {currentUser && (
            <span className="user-info">
              👋 Welcome, {currentUser.firstName || currentUser.username}!
            </span>
          )}
          <button
            onClick={onLogout}
            className="btn btn-outline"
          >
            <i className="fas fa-sign-out-alt"></i> Logout
          </button>
        </div>
      </header>

      <main className="dashboard-main">
        <div className="dashboard-header">
          <h2>My Todo Lists</h2>
          <p>Manage your tasks and stay organized</p>
        </div>

        {todoLists.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">📝</div>
            <h2>No Todo Lists Yet</h2>
            <p>Create your first todo list to get started with task management</p>
            <button
              onClick={() => setShowCreateModal(true)}
              className="btn btn-primary btn-lg"
            >
              <i className="fas fa-plus"></i> Create Your First List
            </button>
          </div>
        ) : (
          <>
            <div className="create-list-btn-container">
              <button
                onClick={() => setShowCreateModal(true)}
                className="btn btn-primary btn-lg"
              >
                <i className="fas fa-plus"></i> Create New List
              </button>
            </div>
            
            <div className="todo-lists-grid">
              {todoLists.map((list, index) => {
                const completed = list.completedItems || 0;
                const total = list.totalItems || 0;
                const progress = calculateProgress(completed, total);
                
                return (
                  <div
                    key={list.id}
                    className="todo-list-card"
                  >
                    <div className="card-header">
                      <h3 className="card-title">{list.title}</h3>
                      <div className="card-stats">
                        <span className="stats-badge">
                          {completed}/{total}
                        </span>
                      </div>
                    </div>

                    <div className="card-description">
                      <p>{list.description || 'No description available'}</p>
                    </div>

                    {total > 0 && (
                      <div className="progress-container">
                        <div 
                          className="progress-fill" 
                          style={{ width: `${progress}%` }}
                        ></div>
                        <div className="progress-text">{progress}% completed</div>
                      </div>
                    )}

                    <div className="card-footer">
                      <div className="card-date">
                        <i className="fas fa-calendar-alt"></i> Created: {new Date(list.createdAt).toLocaleDateString()}
                      </div>
                      
                      <div className="card-actions">
                        <button
                          onClick={() => handleEditTodoList(list.id)}
                          className="btn btn-primary-dark"
                        >
                          <i className="fas fa-edit"></i> Edit
                        </button>
                        <button
                          onClick={() => handleDeleteTodoList(list.id)}
                          className="btn btn-light"
                        >
                          <i className="fas fa-trash"></i> Delete
                        </button>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </>
        )}
      </main>

      {showCreateModal && (
        <CreateTodoListModal
          onClose={() => setShowCreateModal(false)}
          onCreate={handleCreateTodoList}
        />
      )}
    </div>
  );
};

export default Dashboard;