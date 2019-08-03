import React from "react";
import {TODO_STATUS} from "../models/constants";

const Todo = ({todo, deleteTodo}) => {
  const {id, text, status} = todo;

  return (
    <div>
      <span style={{textDecoration: status === TODO_STATUS.COMPLETED ? "line-through" : ""}}>{text}</span>
      <button className="delete-btn" onClick={() => deleteTodo(id)}>x</button>
    </div>
  );
};

export default Todo;