import React from "react";

import './ErrorHandling.css';

const ErrorHandling = (props) => {

    return <div className="container">
        <ul className='container_list'>
            {props.violations.map((item, index) => <li key={index}> {item.fieldName}  {item.message} </li>)}
        </ul>
    </div>
}

export default ErrorHandling;