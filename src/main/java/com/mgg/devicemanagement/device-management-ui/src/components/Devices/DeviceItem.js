import React, { useState } from 'react';

import Card from "../UI/Card";
import './DeviceItem.css';
import Dialog from "../Dialog/Dialog";

const DeviceItem = (props) => {
  const [showTaskDialog, setShowTaskDialog] = useState(false);
  const [deviceKey, setDeviceKey] = useState('');

  const clickHandler = (event) => {
    const deviceKey = event.target.value;
    setDeviceKey((previous) => previous = deviceKey);
    setShowTaskDialog(true);
  };

  const confirm = () => {
    props.onDeleteDevice(deviceKey);
    setShowTaskDialog(false);
  };

  const cancel = () => {
    setShowTaskDialog(false);
  };

  return (<div>
    <Card className='device-item'>
      <div className='device-item__description'>
        <h2>{props.brand}</h2>
        <h2>{props.name}</h2>
        <button onClick={clickHandler} value={props.devicekey} >Delete</button>
      </div>
    </Card>

    <Dialog
      show={showTaskDialog}
      title="Delete a device?"
      description="Are you sure you want to delete this device?"
      confirm={confirm}
      cancel={cancel} />
  </div>)
}

export default DeviceItem;