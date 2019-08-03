import React, {Component, Fragment} from "react";

class DataContainer extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    const that = this;
    return (
      <Fragment>
        {that.props.hasData ? that.props.todoData : that.props.noData}
      </Fragment>
    );
  }
}

export default DataContainer;