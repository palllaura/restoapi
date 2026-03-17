import Sidebar from "./components/Sidebar";
import FloorPlan from "./components/FloorPlan";

function App() {
  return (
      <div style={{ display: "flex", height: "100vh" }}>
        <Sidebar />
        <FloorPlan />
      </div>
  );
}

export default App;