import React from 'react';

import DeviceList from './DeviceList';
import DeviceFilter from './DevicesFilter';
import DeviceListHeader from './DeviceListHeader';
import Card from '../UI/Card';

const Devices = (props) => {
  return (
    <div>
      <Card className="devices">
        <DeviceFilter onChangeFilter={props.searchKeyChangedHandler} onSearchDeviceBrand={props.onSearchDeviceByBrand} />
        <DeviceListHeader />
        <DeviceList items={props.searchedDeviceItem} onDeleteDevice={props.onDeleteDevice} />
      </Card>
    </div>
  )
}

export default Devices;