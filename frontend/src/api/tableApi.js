const BASE_URL = "http://localhost:8080/api/tables";

/**
 * Fetch tables with optional filters
 */
export async function getTables({ start, end, guests } = {}) {
    const params = new URLSearchParams();

    if (start) params.append("start", start);
    if (end) params.append("end", end);
    if (guests) params.append("guests", guests);

    const url = params.toString()
        ? `${BASE_URL}?${params.toString()}`
        : BASE_URL;

    const response = await fetch(url);

    if (!response.ok) {
        throw new Error("Failed to fetch tables");
    }

    return response.json();
}