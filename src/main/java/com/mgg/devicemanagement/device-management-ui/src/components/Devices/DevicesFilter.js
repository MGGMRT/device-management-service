import React from 'react';

import './DevicesFilter.css';

const DevicesFilter = (props) => {
  const onChangeSearchKey = (event) => {
    props.onChangeFilter(event.target.value);
  }

  return (
    <div className='devices-filter'>
      <div className='devices-filter__control'>
        <div className='device-filter__order' >
          <label>Search by brand</label>
          <input type="text" placeholder="Search brand name..." onChange={onChangeSearchKey} />
          <button onClick={props.onSearchDeviceBrand} > Search </button>
        </div>
      </div>
    </div>
  );
};

export default DevicesFilter;
