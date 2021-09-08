import React from 'react';

import './DevicesFilter.css';

const DevicesFilter = (props) => {
  const dropdownChangeHandler = (event) => {
    props.onChangeFilter(event.target.value);
  };

  return (
    <div className='devices-filter'>
      <div className='devices-filter__control'>
        <label>Filter by brand</label>
        <select value={props.selected} onChange={dropdownChangeHandler}>
          {
            [...new Set(props.filterData.map(item => item.brand))].map((item, index) => <option key={index} value={item}>{item.toUpperCase()}</option>)
          }
        </select>
        <button onClick={props.refresh} >Refresh</button>
      </div>
    </div>
  );
};

export default DevicesFilter;
