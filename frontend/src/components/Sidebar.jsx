import { useEffect } from "react";
import logo from "../assets/logo.png";

function Sidebar({
                     guests,
                     setGuests,
                     date,
                     setDate,
                     hour,
                     setHour,
                     duration,
                     setDuration
                 }) {

    const OPENING_HOUR = 10;
    const CLOSING_HOUR = 22;

    const MIN_DURATION = 1;
    const MAX_DURATION = 3;

    const MAX_DAYS_IN_FUTURE = 3;

    const today = new Date().toISOString().split("T")[0];

    function getMaxDate() {
        const d = new Date();
        d.setDate(d.getDate() + MAX_DAYS_IN_FUTURE);
        return d.toISOString().split("T")[0];
    }

    const handleHourChange = (e) => {
        const value = e.target.value;
        setHour(value === "" ? "" : Number(value));
    };

    const increaseGuests = () => {
        setGuests(prev => prev + 1);
    };

    const decreaseGuests = () => {
        setGuests(prev => Math.max(1, prev - 1));
    };

    const maxDuration = hour
        ? Math.min(MAX_DURATION, CLOSING_HOUR - hour)
        : MAX_DURATION;

    useEffect(() => {
        if (hour && duration > (CLOSING_HOUR - hour)) {
            setDuration(Math.min(MAX_DURATION, CLOSING_HOUR - hour));
        }
    }, [hour]);

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
                <p>Kuupäev ja kellaaeg</p>

                <div style={{ display: "flex", gap: "10px" }}>
                    <input
                        type="date"
                        value={date}
                        min={today}
                        max={getMaxDate()}
                        onChange={e => setDate(e.target.value)}
                    />

                    <select value={hour} onChange={handleHourChange}>
                        <option value="">vali aeg</option>
                        {[...Array(CLOSING_HOUR - OPENING_HOUR)].map((_, i) => {
                            const h = OPENING_HOUR + i;
                            return (
                                <option key={h} value={h}>
                                    {h}:00
                                </option>
                            );
                        })}
                    </select>
                </div>

                <p style={{ marginTop: "20px" }}>Külastuse kestus</p>

                <div style={{ display: "flex", gap: "10px", alignItems: "center" }}>
                    <button
                        onClick={() => setDuration(d => Math.max(MIN_DURATION, d - 1))}
                        disabled={duration === MIN_DURATION}
                    >
                        -
                    </button>

                    <span>{duration}h</span>

                    <button
                        onClick={() => setDuration(d => Math.min(maxDuration, d + 1))}
                        disabled={duration >= maxDuration}
                    >
                        +
                    </button>
                </div>

                <p style={{ marginTop: "20px" }}>Külastajate arv</p>

                <div style={{ display: "flex", gap: "10px", alignItems: "center" }}>
                    <button onClick={decreaseGuests} disabled={guests === 1}>
                        -
                    </button>

                    <span>{guests}</span>

                    <button onClick={increaseGuests} disabled={guests === 6}>
                        +
                    </button>
                </div>

                <button style={{ marginTop: "30px", width: "100%" }}>
                    Reserveeri
                </button>
            </div>
        </div>
    );
}

export default Sidebar;