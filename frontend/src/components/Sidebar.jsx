import { useEffect, useState } from "react";
import logo from "../assets/logo.png";
import { createReservation } from "../api/reservationApi";
import "../index.css";

function Sidebar({
                     guests,
                     setGuests,
                     date,
                     setDate,
                     hour,
                     setHour,
                     duration,
                     setDuration,
                     selectedTableId,
                     onReservationSuccess
                 }) {
    const [name, setName] = useState("");
    const [message, setMessage] = useState(null);

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

    const increaseGuests = () => setGuests(g => Math.min(6, g + 1));
    const decreaseGuests = () => setGuests(g => Math.max(1, g - 1));

    const maxDuration = hour
        ? Math.min(MAX_DURATION, CLOSING_HOUR - hour)
        : MAX_DURATION;

    function handleReserve() {
        setMessage(null);

        if (!selectedTableId || !date || hour === "" || !name.trim()) {
            setMessage({ text: "Palun täida kõik väljad", type: "error" });
            return;
        }

        const start = new Date(date);
        start.setHours(hour, 0, 0, 0);

        const end = new Date(start);
        end.setHours(start.getHours() + duration);

        createReservation({
            tableId: selectedTableId,
            start: start.toISOString(),
            end: end.toISOString(),
            guests,
            customerName: name,
        })
            .then(res => {
                setMessage({ text: res.message, type: res.success ? "success" : "error" });

                if (res.success) {
                    onReservationSuccess();
                }
            })
            .catch(() => {
                setMessage({ text: "Midagi läks valesti", type: "error" });
            });
    }

    useEffect(() => {
        setMessage(null);
    }, [guests, date, hour, duration, name]);

    useEffect(() => {
        if (hour && duration > (CLOSING_HOUR - hour)) {
            setDuration(Math.min(MAX_DURATION, CLOSING_HOUR - hour));
        }
    }, [hour]);

    return (
        <aside className="sidebar">
            <div className="sidebar__logo">
                <img src={logo} alt="logo" />
            </div>

            <div className="sidebar__section">
                <label>Kuupäev ja kellaaeg</label>

                <div className="row">
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
                            return <option key={h} value={h}>{h}:00</option>;
                        })}
                    </select>
                </div>
            </div>

            <div className="sidebar__section">
                <label>Kestus</label>
                <div className="counter">
                    <button
                        className="mini-btn"
                        onClick={() => setDuration(d => Math.max(MIN_DURATION, d - 1))}
                        disabled={duration === MIN_DURATION}
                    >-</button>

                    <span>{duration}h</span>

                    <button
                        className="mini-btn"
                        onClick={() => setDuration(d => Math.min(maxDuration, d + 1))}
                        disabled={duration >= maxDuration}
                    >+</button>
                </div>
            </div>

            <div className="sidebar__section">
                <label>Külastajad</label>
                <div className="counter">
                    <button
                        className="mini-btn"
                        onClick={decreaseGuests}
                        disabled={guests === 1}
                    >-</button>

                    <span>{guests}</span>

                    <button
                        className="mini-btn"
                        onClick={increaseGuests}
                        disabled={guests === 6}
                    >+</button>
                </div>
            </div>

            <div className="sidebar__section">
                <label>Nimi</label>
                <input
                    type="text"
                    value={name}
                    placeholder="Sisesta nimi"
                    onChange={e => setName(e.target.value)}
                />
            </div>

            <button
                className="primary-btn"
                onClick={handleReserve}
                disabled={!selectedTableId}
            >
                Reserveeri
            </button>

            {message && (
                <div className={`message ${message.type}`}>
                    {message.text}
                </div>
            )}
        </aside>
    );
}

export default Sidebar;