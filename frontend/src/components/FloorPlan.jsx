import { useEffect, useRef } from "react";
import {drawTable} from "../utils/tableDrawing";

function FloorPlan() {
    const canvasRef = useRef(null);

    useEffect(() => {
        const canvas = canvasRef.current;
        const ctx = canvas.getContext("2d");

        const tables = [
            { id: 1, x: 50, y: 450, seats: 2 },
            { id: 2, x: 300, y: 450, seats: 2 },
            { id: 3, x: 600, y: 450, seats: 2 },
            { id: 4, x: 50, y: 50, seats: 4 },
            { id: 5, x: 300, y: 50, seats: 4 },
            { id: 6, x: 600, y: 50, seats: 6 },
        ];

        ctx.clearRect(0, 0, canvas.width, canvas.height);
        tables.forEach(t => drawTable(ctx, t.x, t.y, t.seats));

    }, []);

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

export default FloorPlan;