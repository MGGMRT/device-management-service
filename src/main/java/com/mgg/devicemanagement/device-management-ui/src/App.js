import React, { useState } from 'react';

import './App.css';
import NewDevice from './components/NewDevice/NewDevice';
import Devices from './components/Devices/Devices';
import ErrorHandling from './components/UI/ErrorHandling';

function App() {
  const [devices, setDevices] = useState([]);
  const [error, setError] = useState([]);

  const getDevicesHandler = () => {
    fetch('http://localhost:8080/api/devices', {
      method: "GET",
      headers: {
        "Accept": "application/json"
      }
    })
      .then(response => response.json())
      .then(data => {
        setDevices(data);
        setError([]);
      })
      .catch(err => console.log(err));
  }

  const addDeviceHandler = device => {
    fetch('http://localhost:8080/api/devices', {
      method: "POST",
      body: JSON.stringify(device),
      headers: {
        "Accept": "application/json",
        "Content-Type": "application/json; charset=UTF-8"
      }
    })
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          return Promise.reject(response);
        }
      })
      .then(data => getDevicesHandler())
      .catch((response) => {
        response.json().then((json) => {
          setError(json.violations);
        });
      });
  };

  const deleteDeviceHandler = deviceKey => {
    fetch('http://localhost:8080/api/devices/' + deviceKey, {
      method: "DELETE",
      headers: {
        "Accept": "application/json",
        "Content-Type": "application/json; charset=UTF-8"
      }
    })
      .then(response => {
        if (response.ok) {
          return response;
        } else {
          return Promise.reject(response);
        }
      })
      .then(() => getDevicesHandler())
      .catch(err => console.log(err.message));
  }

  return (
    <div>
      {error.length > 0 && <ErrorHandling violations={error} />}
      <NewDevice onAddDevice={addDeviceHandler} />
      <Devices items={devices} refresh={getDevicesHandler} onDeleteDevice={deleteDeviceHandler} />
    </div>
  );
}

export default App;
