import React from 'react';

import './DevicesFilter.css';

const DevicesFilter = (props) => {
  const onChangeSearchKey = (event) => {
    props.onChangeFilter(event.target.value);
  }

  return (
    <div className='devices-filter'>
      <div className='devices-filter__control'>
        <label>Search by brand</label>
        <input type="text" placeholder="Search brand name..." onChange={onChangeSearchKey} />
        <button onClick={props.onSearchDeviceBrand} > Search </button>
      </div>
    </div>
  );
};

export default DevicesFilter;
