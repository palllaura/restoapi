import { useState } from "react";
import Sidebar from "./components/Sidebar";
import FloorPlan from "./components/FloorPlan";

function App() {

    const [selectedTableId, setSelectedTableId] = useState(null);
    const [refreshKey, setRefreshKey] = useState(0);

    const [guests, setGuests] = useState(2);
    const [date, setDate] = useState("");
    const [hour, setHour] = useState("");
    const [duration, setDuration] = useState(2);

    return (
        <div style={{ display: "flex", height: "100vh" }}>
            <Sidebar
                guests={guests}
                setGuests={setGuests}
                date={date}
                setDate={setDate}
                hour={hour}
                setHour={setHour}
                duration={duration}
                setDuration={setDuration}
                selectedTableId={selectedTableId}
                onReservationSuccess={() => setRefreshKey(k => k + 1)}
            />
            <FloorPlan
                guests={guests}
                date={date}
                hour={hour}
                duration={duration}
                selectedTableId={selectedTableId}
                setSelectedTableId={setSelectedTableId}
                refreshKey={refreshKey}
            />
        </div>
    );
}

export default App;