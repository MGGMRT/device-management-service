import React, { useState } from 'react';

import DeviceList from './DeviceList';
import DeviceFilter from './DevicesFilter';
import Card from '../UI/Card';

const Devices = (props) => {

  const [filteredBrand, setFilteredBrand] = useState('apple');

  const filterChangeHandler = selectedBrand => {
    setFilteredBrand(selectedBrand);
  };

  const filteredDevices = props.items.filter((device) => {
    return device.brand.toUpperCase() === filteredBrand.toUpperCase();
  });

  return (
    <div>
      <Card className="devices">
        <DeviceFilter selected={filteredBrand} filterData={props.items} onChangeFilter={filterChangeHandler} refresh={props.refresh} />
        <DeviceList items={filteredDevices} onDeleteDevice={props.onDeleteDevice} />
      </Card>
    </div>
  )
}

export default Devices;