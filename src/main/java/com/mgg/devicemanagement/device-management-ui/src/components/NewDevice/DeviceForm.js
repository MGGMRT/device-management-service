import React, { useState } from 'react';
import './DeviceForm.css'

const DeviceForm = (props) => {
    const[enteredName,setEnteredName] = useState('');
    const[enteredBrand,setEnteredBrand] = useState('');

    const nameChangeHandler = event => {
        setEnteredName(event.target.value);
    }

    const brandChangeHandler = event => {
        setEnteredBrand(event.target.value);
    }

    const submitHandler = (event) => {
        event.preventDefault();

        const newDevice = {
          brandName : enteredBrand,
          deviceName :enteredName
        }
       
        props.onSaveDeviceeData(newDevice);
        setEnteredName('');
        setEnteredBrand('')
    }

    return (
        <form onSubmit={submitHandler}>
        <div className='new-device__controls'>
        <div className='new-device__control'>
          <label>Device Name</label>
          <input
            type='text'
            value={enteredName}
            onChange={nameChangeHandler}
          />
        </div>
        <div className='new-device__control'>
          <label>Device Brand</label>
          <input
            type='text'
            value={enteredBrand}
            onChange={brandChangeHandler}
          />
        </div>
      </div>
      <div className='new-device__actions'>
        <button type='submit'>Add Device</button>
      </div>
    </form>
    )
}

export default DeviceForm;