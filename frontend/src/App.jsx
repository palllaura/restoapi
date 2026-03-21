import { useState } from "react";
import Sidebar from "./components/Sidebar";
import FloorPlan from "./components/FloorPlan";

function App() {

    const [guests, setGuests] = useState(2);

    return (
        <div style={{ display: "flex", height: "100vh" }}>
            <Sidebar guests={guests} setGuests={setGuests} />
            <FloorPlan guests={guests} />
        </div>
    );
}

export default App;