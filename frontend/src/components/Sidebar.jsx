import logo from "../assets/logo.png";

function Sidebar({ guests, setGuests}) {

    const increaseGuests = () => {
        setGuests(prev => prev + 1);
    };

    const decreaseGuests = () => {
        setGuests(prev => Math.max(1, prev - 1));
    };

    return (
        <div
            style={{
                width: "250px",
                backgroundColor: "#eae6e1",
                padding: "20px",
                boxSizing: "border-box",
            }}
        >
            <div style={{ marginBottom: "40px" }}>
                <img src={logo} alt="logo" style={{ width: "100%" }} />
            </div>

            <div>
                <p>vali kuupäev</p>
                <input type="date" />

                <p style={{ marginTop: "20px" }}>külastajate arv</p>

                <div style={{ display: "flex", gap: "10px", alignItems: "center" }}>
                    <button onClick={decreaseGuests} disabled={guests === 1}>-</button>
                    <span>{guests}</span>
                    <button onClick={increaseGuests} disabled={guests === 6}>+</button>
                </div>

                <button style={{ marginTop: "30px", width: "100%" }}>
                    broneeri
                </button>
            </div>
        </div>
    );
}

export default Sidebar;