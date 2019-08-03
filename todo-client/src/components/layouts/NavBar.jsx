import React, {useContext} from "react";
import {ThemeContext} from "../../contexts/theme-context";

const NavBar = () => {

  const themeContext = useContext(ThemeContext);

  const {isLightTheme, light, dark} = themeContext;
  const theme = isLightTheme ? light : dark;

  const navStyle = {
    background: theme.ui,
    color: theme.syntax
  };

  return (
    <nav style={navStyle}>
      <h1>Todo App</h1>
    </nav>
  );
};
export default NavBar;
