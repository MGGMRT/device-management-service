import React from 'react';
import './NewDevice.css';
import DeviceForm from './DeviceForm.js';

const NewDevice = (props) => {
    const saveDeviceHandler = (enteredDeviceData) => {
        props.onAddDevice(enteredDeviceData);
    }
    return (
        <div className="new-device">
            <DeviceForm onSaveDeviceeData={saveDeviceHandler} />
        </div>
    )
}

export default NewDevice;