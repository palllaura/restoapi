import { useEffect, useRef, useState } from "react";
import { drawTable } from "../utils/tableDrawing";
import { getTables } from "../api/tableApi";

import {
    TABLE_WIDTH,
    TABLE_HEIGHT_SMALL,
    TABLE_HEIGHT_LARGE
} from "../utils/tableConstants";

function FloorPlan({ guests, date, hour, duration, selectedTableId, setSelectedTableId, refreshKey }) {
    const canvasRef = useRef(null);

    const [tables, setTables] = useState([]);

    const tableLayout = {
        1: { x: 50, y: 450 },
        2: { x: 300, y: 450 },
        3: { x: 600, y: 450 },
        4: { x: 50, y: 50 },
        5: { x: 300, y: 50 },
        6: { x: 600, y: 50 },
    };

    function buildDateTimes() {
        if (!date || hour === "") return {};

        const start = new Date(date);
        start.setHours(hour, 0, 0, 0);

        const end = new Date(start);
        end.setHours(start.getHours() + duration);

        return {
            start: start.toISOString(),
            end: end.toISOString(),
        };
    }

    useEffect(() => {
        const { start, end } = buildDateTimes();

        getTables({ start, end, guests })
            .then(data => {
                const tablesWithLayout = data
                    .map(t => ({
                        ...t,
                        ...tableLayout[t.id],
                    }))
                    .filter(t => t.x !== undefined && t.y !== undefined);

                setTables(tablesWithLayout);

                const recommended = tablesWithLayout.find(
                    t => t.status === "RECOMMENDED"
                );

                if (recommended) {
                    setSelectedTableId(recommended.id);
                } else {
                    const firstSelectable = tablesWithLayout.find(t => t.selectable);

                    setSelectedTableId(firstSelectable ? firstSelectable.id : null);
                }
            })
            .catch(err => console.error("Failed to fetch tables:", err));
    }, [guests, date, hour, duration, refreshKey]);

    useEffect(() => {
        const canvas = canvasRef.current;
        if (!canvas) return;

        const ctx = canvas.getContext("2d");

        ctx.clearRect(0, 0, canvas.width, canvas.height);

        tables.forEach(t => {
            const resolvedStatus = resolveTableStatus(t, selectedTableId);

            drawTable(ctx, t.x, t.y, t.seats, resolvedStatus, t.selectable);
        });
    }, [tables, selectedTableId]);

    useEffect(() => {
        const canvas = canvasRef.current;
        if (!canvas) return;

        const handleClick = (event) => {
            const rect = canvas.getBoundingClientRect();

            const clickX = event.clientX - rect.left;
            const clickY = event.clientY - rect.top;

            const clickedTable = tables.find(t =>
                isPointInsideTable(clickX, clickY, t)
            );

            if (clickedTable && clickedTable.selectable) {
                setSelectedTableId(clickedTable.id);
            }
        };

        canvas.addEventListener("click", handleClick);

        return () => {
            canvas.removeEventListener("click", handleClick);
        };
    }, [tables]);

    return (
        <div
            style={{
                flex: 1,
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                backgroundColor: "#f5f5f5",
            }}
        >
            <canvas
                ref={canvasRef}
                width={800}
                height={600}
                style={{
                    border: "2px solid black",
                    backgroundColor: "white",
                }}
            />
        </div>
    );
}

function resolveTableStatus(table, selectedTableId) {
    if (table.id === selectedTableId) return "RECOMMENDED";
    if (table.status === "RECOMMENDED") return "FREE";
    return table.status;
}

function isPointInsideTable(px, py, table) {
    const tableHeight =
        table.seats === 6
            ? TABLE_HEIGHT_LARGE
            : TABLE_HEIGHT_SMALL;

    return (
        px >= table.x &&
        px <= table.x + TABLE_WIDTH &&
        py >= table.y &&
        py <= table.y + tableHeight
    );
}

export default FloorPlan;