import React, {useContext, useEffect} from "react";
import {ThemeContext} from "../contexts/theme-context";
import {TodoContext} from "../contexts/todo-context";
import Todo from "./Todo";
import DataContainer from "./DataContainer";

const TodoList = () => {

  const themeContext = useContext(ThemeContext);
  const todoContext = useContext(TodoContext);

  useEffect(() => todoContext.getTodos(), []);

  const styleProvider = (themeContext) => {
    const {isLightTheme, light, dark} = themeContext;
    const theme = isLightTheme ? light : dark;

    const todoListStyle = {
      color: theme.syntax,
      background: theme.bg
    };

    const todoListItemStyle = {
      background: theme.ui,
      color: theme.syntax
    };

    const textStyle = {
      color: "#d3d3d3",
      margin: "0",
      textTransform: "capitalize"
    };

    return {todoListStyle, todoListItemStyle, textStyle};
  };

  const dataProvider = (themeContext, todoContext) => {
    const {todoListStyle, todoListItemStyle, textStyle} = styleProvider(themeContext);
    const {todos, deleteTodo} = todoContext;
    const todoData = (
      <div className="todo-list" style={todoListStyle}>
        <ul>
          {todos.map((todo) => {
            const data = todo.todo;
            return (
              <Todo
                key={data.id}
                todo={data}
                deleteTodo={deleteTodo}
              />
            )
          }).map((todo, index) => (
            <li key={index} style={todoListItemStyle}>
              {todo}
            </li>)
          )}
        </ul>
      </div>
    );
    const noData = (
      <div style={{padding: "4px"}}>
        <img src="https://img.icons8.com/color/48/000000/nothing-found.png"
             alt={"Representation of no-todos-found"}/>
        <p style={textStyle}>no todos found. add one to get started</p>
      </div>
    );
    return {todoData, noData};
  };

  const {todoData, noData} = dataProvider(themeContext, todoContext);

  return (
    <DataContainer
      hasData={todoContext.todos.length > 0}
      todoData={todoData}
      noData={noData}
    />
  );
};

export default TodoList;
