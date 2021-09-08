

import React from 'react';

import DeviceItem from './DeviceItem';
import './DeviceList.css';

const DeviceList = (props) => {
  if (props.items.length === 0) {
    return <h2 className='device-list__fallback'>Found no device.</h2>;
  }

  return (
    <ul className='device-list'>
      {
        props.items.map((device, index) => (
          <DeviceItem
            key={index}
            brand={device.brand}
            name={device.name}
            devicekey={device.deviceKey}
            onDeleteDevice={props.onDeleteDevice}
          />))
      }
    </ul>
  );
};

export default DeviceList;