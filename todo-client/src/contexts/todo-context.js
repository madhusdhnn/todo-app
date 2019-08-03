import React, {Component, createContext} from "react";
import Todo from "../models/Todo";
import axios from "axios";
import {axiosConfig} from "../configs/axios-configs";

export const TodoContext = createContext();

class TodoContextProvider extends Component {
  constructor(props) {
    super(props);
    this.state = {
      userId: "100",
      todos: [],
      todo: {}
    };
  }

  withCompleteUrl = (relativeUrl = "") => {
    return `/todoapp/api/todo/${relativeUrl}`;
  };

  getTodoByType = ({type, payload}) => {
    switch (type) {
      case "ADD_TODO":
        return Object.assign({}, {todo: new Todo(payload)});
      case "GET_TODOS":
        const {todos} = payload;
        return todos.map(todo => Object.assign({}, {todo: new Todo(todo)}));
    }
  };

  getTodos() {
    axios.get(this.withCompleteUrl(`todos?userId=${this.state.userId}&groupName=Home`), axiosConfig)
      .then(response => this.setTodos(response.data))
      .catch(error => console.error(error));
  }

  addTodo(data) {
    axios.post(this.withCompleteUrl(`create?userId=${this.state.userId}`), data, axiosConfig)
      .then(response => this.setTodo(response.data))
      .catch(error => console.error(error));
  }

  deleteTodo(id) {
    axios.delete(this.withCompleteUrl(`delete/${id}?userId=${this.state.userId}&groupName=Home`), axiosConfig)
      .then(response => {
        const {id} = response.data;
        this.setState({
          ...this.state,
          todos: [...this.state.todos.filter(todo => todo.todo.id !== id)]
        });
      })
      .catch(error => console.error(error));
  }

  setTodos(data) {
    const action = {
      type: "GET_TODOS",
      payload: data
    };
    const todos = this.getTodoByType(action);
    this.setState({
      ...this.state,
      todos: [...todos]
    })
  }

  setTodo(data) {
    const action = {
      type: "ADD_TODO",
      payload: data
    };
    const todo = this.getTodoByType(action);
    this.state.todos.push(todo);
    this.setState({
      ...this.state,
      todo: todo,
      todos: [...this.state.todos]
    })
  }

  render() {
    const state = {
      ...this.state,
      getTodos: this.getTodos.bind(this),
      addTodo: this.addTodo.bind(this),
      deleteTodo: this.deleteTodo.bind(this)
    };
    const that = this;
    return (
      <TodoContext.Provider value={state}>
        {that.props.children}
      </TodoContext.Provider>
    );
  }

}

export default TodoContextProvider;