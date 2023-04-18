import Modal from "react-modal";
import { CreateEventForm } from "./CreateEventForm";
import { useUiStore } from "../../hooks/useUiStore";

const customStyles = {
	content: {
		top: "50%",
		left: "50%",
		right: "auto",
		bottom: "auto",
		marginRight: "-50%",
		transform: "translate(-50%, -50%)",
	},
};
Modal.setAppElement("#root");

export const CreateEventModal = () => {

    const { isCreateEventModalOpen, closeCreateEventModal } = useUiStore();

    return (
            <Modal
                isOpen={isCreateEventModalOpen}
                onRequestClose={closeCreateEventModal}
                style={customStyles}
                className="modal"
                overlayClassName="modal-fondo"
                closeTimeoutMS={200}
            >
                <h1>Nuevo evento</h1>
                <hr />
                <CreateEventForm />
            </Modal>
    );
}
