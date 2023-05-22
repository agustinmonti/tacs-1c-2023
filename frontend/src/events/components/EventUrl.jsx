import { useState } from "react";

export const EventUrl = () => {

    const [copied, setCopied] = useState(false)

    const eventUrl = window.location.href;

    const handleCopyToClipboard = () => {
        setCopied(true);
        navigator.clipboard.writeText(eventUrl);
    }

    return (
        <div className="alert alert-primary shadow-sm animate__animated animate__fadeIn mb-3" role="alert">
            <p>Comparte este link con quien quieras que participe en esta votación</p>
            <div className="bg-white rounded d-flex justify-content-between align-items-center ps-3 fw-bold">
                { eventUrl }
                <button
                    type="button"
                    className="btn btn-primary"
                    style={{borderTopLeftRadius:0, borderBottomLeftRadius:0}}
                    onClick={handleCopyToClipboard}
                >
                    {
                        copied
                        ? <i className="fa-solid fa-clipboard-check"></i>
                        : <i className="fa-regular fa-clipboard"></i>
                    }
                </button>
            </div>
        </div>
    )
}
