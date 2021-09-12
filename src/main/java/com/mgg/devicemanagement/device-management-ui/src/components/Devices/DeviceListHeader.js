import React from 'react';
import Card from '../UI/Card';
import './DeviceListHeader.css';

const DeviceListHeader = () => {
    return (
        <Card className='device-header'>
        <div className='device-header__description'>
          <h2>BRAND</h2>
          <h2>PRODUCT NAME</h2>
          <h2>ACTION</h2>
        </div>
        </Card>
    )
}

export default  DeviceListHeader;