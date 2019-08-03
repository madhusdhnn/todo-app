import React from "react";
import NavBar from "./components/layouts/NavBar";
import ThemeContextProvider from "./contexts/theme-context";
import TodoList from "./components/TodoList";
import TodoForm from "./components/TodoForm";
import ThemeContextToggler from "./contexts/theme-context-toggler";
import TodoContextProvider from "./contexts/todo-context";

const App = () => {
  return (
    <div className="app">
      <ThemeContextProvider>
        <TodoContextProvider>
          <NavBar/>
          <TodoList/>
          <TodoForm/>
        </TodoContextProvider>
        <ThemeContextToggler/>
      </ThemeContextProvider>
    </div>
  );
};

export default App;
