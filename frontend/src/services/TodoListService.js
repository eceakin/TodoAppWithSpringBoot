import { authService } from './authService';

const API_BASE_URL = 'http://localhost:8080/api';

class TodoListService {
  getAuthHeaders() {
    const token = authService.getToken();
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    };
  }

  async createTodoList(todoListData) {
    try {
      const response = await fetch(`${API_BASE_URL}/todolists`, {
        method: 'POST',
        headers: this.getAuthHeaders(),
        body: JSON.stringify(todoListData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to create todo list');
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  }

  async getMyTodoLists() {
    try {
      const response = await fetch(`${API_BASE_URL}/todolists`, {
        method: 'GET',
        headers: this.getAuthHeaders(),
      });

      if (!response.ok) {
        throw new Error('Failed to fetch todo lists');
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  }

  async getTodoListById(id) {
    try {
      const response = await fetch(`${API_BASE_URL}/todolists/${id}`, {
        method: 'GET',
        headers: this.getAuthHeaders(),
      });

      if (!response.ok) {
        throw new Error('Failed to fetch todo list');
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  }

  async updateTodoList(id, todoListData) {
    try {
      const response = await fetch(`${API_BASE_URL}/todolists/${id}`, {
        method: 'PUT',
        headers: this.getAuthHeaders(),
        body: JSON.stringify(todoListData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to update todo list');
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  }

  async deleteTodoList(id) {
    try {
      const response = await fetch(`${API_BASE_URL}/todolists/${id}`, {
        method: 'DELETE',
        headers: this.getAuthHeaders(),
      });

      if (!response.ok) {
        throw new Error('Failed to delete todo list');
      }

      return true;
    } catch (error) {
      throw error;
    }
  }

  async searchTodoListsByTitle(title) {
    try {
      const response = await fetch(`${API_BASE_URL}/todolists/search?title=${encodeURIComponent(title)}`, {
        method: 'GET',
        headers: this.getAuthHeaders(),
      });

      if (!response.ok) {
        throw new Error('Failed to search todo lists');
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  }
}

export const todoListService = new TodoListService();