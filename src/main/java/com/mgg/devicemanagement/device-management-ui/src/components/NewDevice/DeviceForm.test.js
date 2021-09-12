import { render, screen, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import DeviceForm from './DeviceForm';

describe('DeviceForm component', () => {
    test('should capture the Name context when renders device form ', () => {  
        render(<DeviceForm />)
        const outputElement = screen.getByText('Device Name');
        expect(outputElement).toBeInTheDocument();
    })
    test('should capture the Brand context when renders device form ', () => {  
        render(<DeviceForm />)
        const outputElement = screen.getByText('Device Brand');
        expect(outputElement).toBeInTheDocument();
    })
    test('should capture the submit button title name when renders device form ', () => {  
        render(<DeviceForm />)
        const outputElement = screen.getByRole('button');
        expect(outputElement).toBeInTheDocument();
    })
    test('should call props func to create a new Device object on the resource when the submit button clicked ', () => {
        const onSaveDeviceeData = jest.fn();
        render(<DeviceForm onSaveDeviceeData={onSaveDeviceeData} />)
        
        const outputElement = screen.getByRole('button');
        userEvent.click(outputElement);

        const [inputsElement1, inputsElement2] = screen.getAllByRole('textbox');

        expect(onSaveDeviceeData).toHaveBeenCalledTimes(1);
        expect(inputsElement1.value).toBe('');
        expect(inputsElement2.value).toBe('');
        
    })

    test('should be filled inputs text before the submit button clicked ', () => {
        
        const onSaveDeviceeData = jest.fn();
        render(<DeviceForm onSaveDeviceeData={onSaveDeviceeData} />);

        const [inputsElement1, inputsElement2] = screen.getAllByRole('textbox');
        fireEvent.change(inputsElement1, {target: { value: 'mouse'}});
        fireEvent.change(inputsElement2, {target: { value: 'apple'}});

        expect(inputsElement1.value).toBe('mouse'); 
        expect(inputsElement2.value).toBe('apple');
        
    })
})
