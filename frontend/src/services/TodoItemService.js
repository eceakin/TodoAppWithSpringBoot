import { authService } from './authService';

const API_BASE_URL = 'http://localhost:8080/api';

class TodoItemService {
  getAuthHeaders() {
    const token = authService.getToken();
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    };
  }

  async createTodoItem(todoItemData) {
    try {
      const response = await fetch(`${API_BASE_URL}/todoitems`, {
        method: 'POST',
        headers: this.getAuthHeaders(),
        body: JSON.stringify(todoItemData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to create todo item');
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  }

  async getTodoItemById(id) {
    try {
      const response = await fetch(`${API_BASE_URL}/todoitems/${id}`, {
        method: 'GET',
        headers: this.getAuthHeaders(),
      });

      if (!response.ok) {
        throw new Error('Failed to fetch todo item');
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  }

  async getMyTodoItems() {
    try {
      const response = await fetch(`${API_BASE_URL}/todoitems`, {
        method: 'GET',
        headers: this.getAuthHeaders(),
      });

      if (!response.ok) {
        throw new Error('Failed to fetch todo items');
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  }

  async getTodoItemsByListId(todoListId) {
    try {
      const response = await fetch(`${API_BASE_URL}/todoitems/todolist/${todoListId}`, {
        method: 'GET',
        headers: this.getAuthHeaders(),
      });

      if (!response.ok) {
        throw new Error('Failed to fetch todo items');
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  }

  async getTodoItemsByPriority(todoListId, priority) {
    try {
      const response = await fetch(`${API_BASE_URL}/todoitems/todolist/${todoListId}/priority/${priority}`, {
        method: 'GET',
        headers: this.getAuthHeaders(),
      });

      if (!response.ok) {
        throw new Error('Failed to fetch todo items by priority');
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  }

  async getMyOverdueTodoItems() {
    try {
      const response = await fetch(`${API_BASE_URL}/todoitems/my/overdue`, {
        method: 'GET',
        headers: this.getAuthHeaders(),
      });

      if (!response.ok) {
        throw new Error('Failed to fetch overdue todo items');
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  }

  async updateTodoItem(id, todoItemData) {
    try {
      const response = await fetch(`${API_BASE_URL}/todoitems/${id}`, {
        method: 'PUT',
        headers: this.getAuthHeaders(),
        body: JSON.stringify(todoItemData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to update todo item');
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  }

  async toggleTodoItemCompletion(id) {
    try {
      const response = await fetch(`${API_BASE_URL}/todoitems/${id}/toggle`, {
        method: 'PATCH',
        headers: this.getAuthHeaders(),
      });

      if (!response.ok) {
        throw new Error('Failed to toggle todo item completion');
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  }

  async deleteTodoItem(id) {
    try {
      const response = await fetch(`${API_BASE_URL}/todoitems/${id}`, {
        method: 'DELETE',
        headers: this.getAuthHeaders(),
      });

      if (!response.ok) {
        throw new Error('Failed to delete todo item');
      }

      return true;
    } catch (error) {
      throw error;
    }
  }
}

export const todoItemService = new TodoItemService();