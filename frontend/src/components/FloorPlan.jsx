import {useEffect, useRef, useState} from "react";
import {drawTable} from "../utils/tableDrawing";

import {
    TABLE_WIDTH,
    TABLE_HEIGHT_SMALL,
    TABLE_HEIGHT_LARGE
} from "../utils/tableConstants";

function FloorPlan() {
    const canvasRef = useRef(null);

    const [tables, setTables] = useState([]);
    const [selectedTableId, setSelectedTableId] = useState(null);

    const tableLayout = {
        1: {x: 50, y: 450},
        2: {x: 300, y: 450},
        3: {x: 600, y: 450},
        4: {x: 50, y: 50},
        5: {x: 300, y: 50},
        6: {x: 600, y: 50},
    };

    useEffect(() => {
        fetch("http://localhost:8080/tables")
            .then(res => res.json())
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
                } else if (tablesWithLayout.length > 0) {
                    setSelectedTableId(tablesWithLayout[0].id);
                }
            })
            .catch(err => console.error("Failed to fetch tables:", err));
    }, []);

    useEffect(() => {
        const canvas = canvasRef.current;
        const ctx = canvas.getContext("2d");

        ctx.clearRect(0, 0, canvas.width, canvas.height);

        tables.forEach(t => {
            const resolvedStatus = resolveTableStatus(t, selectedTableId);

            drawTable(ctx, t.x, t.y, t.seats, resolvedStatus);
        });
    }, [tables, selectedTableId]);

    useEffect(() => {
        const canvas = canvasRef.current;

        const handleClick = (event) => {
            const rect = canvas.getBoundingClientRect();

            const clickX = event.clientX - rect.left;
            const clickY = event.clientY - rect.top;

            const clickedTable = tables.find(t =>
                isPointInsideTable(clickX, clickY, t)
            );

            if (clickedTable && clickedTable.status !== "OCCUPIED") {
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