const BASE_URL = "http://localhost:8080/api/reservations";

export async function createReservation(payload) {
    const response = await fetch(BASE_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    });

    if (!response.ok) {
        throw new Error("Failed to create reservation");
    }
}