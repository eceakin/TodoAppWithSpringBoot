import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { todoListService } from '../services/todoListService';
import { todoItemService } from '../services/todoItemService';
import CreateTodoItemModal from './CreateTodoItemModal';
import './TodoListDetail.css';

const TodoListDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [todoList, setTodoList] = useState(null);
  const [todoItems, setTodoItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    if (!id) return;
    fetchTodoListDetails();
    fetchTodoItems();
  }, [id]);

  const fetchTodoListDetails = async () => {
    try {
      const list = await todoListService.getTodoListById(id);
      setTodoList(list);
    } catch (error) {
      console.error('Error fetching todo list:', error);
      navigate('/dashboard');
    }
  };

  const fetchTodoItems = async () => {
    try {
      const items = await todoItemService.getTodoItemsByListId(id);
      setTodoItems(items);
    } catch (error) {
      console.error('Error fetching todo items:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTodoItem = async (todoItemData) => {
    try {
      await todoItemService.createTodoItem({ ...todoItemData, todoListId: id });
      setShowCreateModal(false);
      fetchTodoItems();
    } catch (error) {
      console.error('Error creating todo item:', error);
    }
  };

  const handleToggleComplete = async (itemId) => {
    try {
      await todoItemService.toggleTodoItemCompletion(itemId);
      fetchTodoItems();
    } catch (error) {
      console.error('Error toggling todo item:', error);
    }
  };

  const handleDeleteTodoItem = async (itemId) => {
    if (window.confirm('Are you sure you want to delete this item?')) {
      try {
        await todoItemService.deleteTodoItem(itemId);
        fetchTodoItems();
      } catch (error) {
        console.error('Error deleting todo item:', error);
      }
    }
  };

  const getFilteredItems = () => {
    switch (filter) {
      case 'completed':
        return todoItems.filter(item => item.completed);
      case 'pending':
        return todoItems.filter(item => !item.completed);
      default:
        return todoItems;
    }
  };

  const getPriorityColor = (priority) => {
    switch (priority) {
      case 'HIGH': return '#ef4444';
      case 'MEDIUM': return '#f59e0b';
      case 'LOW': return '#10b981';
      default: return '#6b7280';
    }
  };

  const isOverdue = (dueDate) => {
    if (!dueDate) return false;
    return new Date(dueDate) < new Date();
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
      </div>
    );
  }

  const filteredItems = getFilteredItems();
  const completedCount = todoItems.filter(item => item.completed).length;
  const progressPercentage = todoItems.length > 0
    ? (completedCount / todoItems.length) * 100
    : 0;

  return (
    <div className="todo-list-detail">
      <header className="header">
        <div className="header-left">
          <button
            onClick={() => navigate('/dashboard')}
            className="btn btn-secondary back-btn"
          >
            <i className="fas fa-arrow-left"></i> Back
          </button>
          <h1 className="header-title">{todoList?.title}</h1>
        </div>
        <div className="header-actions">
          <button
            onClick={() => setShowCreateModal(true)}
            className="btn btn-primary"
          >
            <i className="fas fa-plus"></i> Add Item
          </button>
        </div>
      </header>

      <div className="detail-container">
        <div className="detail-header">
          <div className="detail-info">
            <p className="detail-description">{todoList?.description}</p>
            <div className="detail-stats">
              <span className="stat-item">
                <i className="fas fa-check-circle"></i> {completedCount} completed
              </span>
              <span className="stat-item">
                <i className="fas fa-tasks"></i> {todoItems.length} total
              </span>
            </div>
          </div>
          <div className="progress-container">
            <div className="progress-bar">
              <div
                className="progress-fill"
                style={{ width: `${progressPercentage}%` }}
              ></div>
            </div>
          </div>
        </div>

        <div className="filter-tabs">
          <button
            className={`filter-tab ${filter === 'all' ? 'active' : ''}`}
            onClick={() => setFilter('all')}
          >
            All ({todoItems.length})
          </button>
          <button
            className={`filter-tab ${filter === 'pending' ? 'active' : ''}`}
            onClick={() => setFilter('pending')}
          >
            Pending ({todoItems.filter(item => !item.completed).length})
          </button>
          <button
            className={`filter-tab ${filter === 'completed' ? 'active' : ''}`}
            onClick={() => setFilter('completed')}
          >
            Completed ({completedCount})
          </button>
        </div>

        <div className="todo-items-container">
          {filteredItems.length === 0 ? (
            <div className="empty-state">
              <div className="empty-icon">📋</div>
              <h3>
                {filter === 'all'
                  ? 'No items yet'
                  : filter === 'completed'
                  ? 'No completed items'
                  : 'No pending items'}
              </h3>
              {filter === 'all' && (
                <>
                  <p>Add your first todo item to get started!</p>
                  <button
                    onClick={() => setShowCreateModal(true)}
                    className="btn btn-primary"
                  >
                    Add First Item
                  </button>
                </>
              )}
            </div>
          ) : (
            <div className="todo-items-list">
              {filteredItems.map((item) => (
                <div
                  key={item.id}
                  className={`todo-item ${item.completed ? 'completed' : ''} ${
                    isOverdue(item.dueDate) && !item.completed ? 'overdue' : ''
                  }`}
                >
                  <div className="todo-item-main">
                    <button
                      onClick={() => handleToggleComplete(item.id)}
                      className={`toggle-btn ${item.completed ? 'checked' : ''}`}
                      aria-label={item.completed ? 'Mark as pending' : 'Mark as completed'}
                    >
                      {item.completed && <i className="fas fa-check"></i>}
                    </button>

                    <div className="todo-item-content">
                      <div className="todo-item-header">
                        <h4 className="todo-item-title">{item.title}</h4>
                        <div className="todo-item-meta">
                          <span
                            className="priority-badge"
                            style={{ backgroundColor: getPriorityColor(item.priority) }}
                          >
                            {item.priority}
                          </span>
                          {item.dueDate && (
                            <span className={`due-date ${isOverdue(item.dueDate) && !item.completed ? 'overdue' : ''}`}>
                              Due: {new Date(item.dueDate).toLocaleDateString()}
                            </span>
                          )}
                        </div>
                      </div>
                      {item.description && (
                        <p className="todo-item-description">{item.description}</p>
                      )}
                      <div className="todo-item-timestamps">
                        <small>Created: {new Date(item.createdAt).toLocaleDateString()}</small>
                        {item.completedAt && (
                          <small>Completed: {new Date(item.completedAt).toLocaleDateString()}</small>
                        )}
                      </div>
                    </div>
                  </div>
                  <div className="todo-item-actions">
                    <button
                      onClick={() => handleDeleteTodoItem(item.id)}
                      className="btn btn-danger btn-sm"
                      aria-label="Delete item"
                    >
                      <i className="fas fa-trash"></i>
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {showCreateModal && (
        <CreateTodoItemModal
          onClose={() => setShowCreateModal(false)}
          onCreate={handleCreateTodoItem}
        />
      )}
    </div>
  );
};

export default TodoListDetail;
