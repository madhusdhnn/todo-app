import React, {useContext, useState} from "react";
import {ThemeContext} from "../contexts/theme-context";
import {TodoContext} from "../contexts/todo-context";

const TodoForm = () => {

  const [text, setText] = useState("");
  const themeContext = useContext(ThemeContext);
  const todoContext = useContext(TodoContext);

  const styleProvider = ({isLightTheme, light, dark}) => {
    const theme = isLightTheme ? light : dark;

    const formStyle = {
      background: theme.bg,
      color: theme.syntax
    };
    const buttonStyle = {
      background: theme.ui,
      color: theme.syntax,
      border: "none"
    };

    return {formStyle, buttonStyle};
  };

  const {formStyle, buttonStyle} = styleProvider(themeContext);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (text.trim().length === 0) {
      alert("Please add text");
    } else {
      const data = {
        todoText: text,
        todoGroupName: "Home"
      };
      todoContext.addTodo(data);
      setText("");
    }
  };

  return (
    <div style={formStyle}>
      <form className="todo-form" autoComplete="off" onSubmit={(e) => handleSubmit(e)}>
        <input
          type="text"
          name="text"
          value={text}
          onChange={(e) => setText(e.target.value)}
        />
        <button className="btn" type="submit" style={buttonStyle}>add todo</button>
      </form>
    </div>
  );
};

export default TodoForm;
