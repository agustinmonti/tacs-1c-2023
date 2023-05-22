import { useEventsStore } from "../../../hooks"
import { CloseEventAction } from "./CloseEventAction"
import { DeleteEventAction } from "./DeleteEventAction";

export const OwnerActions = () => {

    const { isActive } = useEventsStore();

    return (
        <div className="btn-group">
            {
                isActive
                && <CloseEventAction />
            }
            <DeleteEventAction />
        </div>
    )
}
