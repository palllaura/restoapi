export async function createReservation(data) {
    const response = await fetch("http://localhost:8080/api/reservations", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        throw new Error("Failed to create reservation");
    }

    return response.json();
}