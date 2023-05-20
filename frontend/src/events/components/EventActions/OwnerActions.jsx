import { CloseEventAction } from "./CloseEventAction"

export const OwnerActions = () => {
    return (
        <div className="btn-group">
            <CloseEventAction />
            <button className="btn btn-danger">
                Eliminar
            </button>
        </div>
    )
}
