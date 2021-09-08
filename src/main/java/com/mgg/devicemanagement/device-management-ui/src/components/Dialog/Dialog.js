
import React from 'react';
import Modal from '../UI/Modal';
import './Dialog.css';

const Dialog = (props) => {

    if (!props.show) {
        return <></>;
    }

    return (
        <Modal onClose={props.cancel} >
            <div className="overlay">
                <div className="dialog">
                    <div className="dialog__content">
                        <h2 className="dialog__title">{props.title}</h2>
                        <p className="dialog__description">{props.description}</p>
                    </div>
                    <hr />
                    <div className="dialog__footer">
                        <button onClick={props.cancel} className="dialog__cancel">Cancel</button>
                        <button onClick={props.confirm} className="dialog__confirm">Yes, delete it</button>
                    </div>
                </div>
            </div>
        </Modal>
    )
}

export default Dialog;