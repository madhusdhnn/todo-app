import React, {Component} from "react";
import {ThemeContext} from "./theme-context";

class ThemeContextToggler extends Component {
  static contextType = ThemeContext;

  render() {
    const {toggleTheme, isLightTheme, light, dark} = this.context;
    const theme = isLightTheme ? light : dark;
    const toggleButtonStyle = {
      backgroundColor: theme.bg,
      color: theme.syntax
    };
    return (
      <div>
        <button className="toggle-theme-btn" onClick={toggleTheme} style={toggleButtonStyle}>toggle theme</button>
      </div>
    );
  }
}

export default ThemeContextToggler;