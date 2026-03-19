import {
    TABLE_WIDTH,
    TABLE_HEIGHT_SMALL,
    TABLE_HEIGHT_LARGE,
    CHAIR_SIZE
} from "./tableConstants";

export function drawTable(ctx, x, y, seats, status) {
    const tableColor =
        status === "OCCUPIED"
            ? "#ffffff"
            : status === "RECOMMENDED"
                ? "#d9edbb"
                : "#eae6e1";

    const strokeColor =
        status === "OCCUPIED"
            ? "#d5d5d5"
            : "#090909";

    ctx.fillStyle = tableColor;
    ctx.strokeStyle = strokeColor;

    const tableHeight =
        seats === 6 ? TABLE_HEIGHT_LARGE : TABLE_HEIGHT_SMALL;

    if (seats >= 4) {
        drawChair(
            ctx,
            x + TABLE_WIDTH / 2 - CHAIR_SIZE / 2,
            y - CHAIR_SIZE / 2
        );
    }

    if (seats >= 4) {
        drawChair(
            ctx,
            x + TABLE_WIDTH / 2 - CHAIR_SIZE / 2,
            y + tableHeight - CHAIR_SIZE / 2
        );
    }

    if (seats === 2 || seats === 4) {
        drawChair(
            ctx,
            x - CHAIR_SIZE / 2,
            y + tableHeight / 2 - CHAIR_SIZE / 2
        );
    }

    if (seats === 2 || seats === 4) {
        drawChair(
            ctx,
            x + TABLE_WIDTH - CHAIR_SIZE / 2,
            y + tableHeight / 2 - CHAIR_SIZE / 2
        );
    }

    if (seats === 6) {
        drawChair(
            ctx,
            x - CHAIR_SIZE / 2,
            y + tableHeight / 4 - CHAIR_SIZE / 2
        );

        drawChair(
            ctx,
            x - CHAIR_SIZE / 2,
            y + (3 * tableHeight) / 4 - CHAIR_SIZE / 2
        );

        drawChair(
            ctx,
            x + TABLE_WIDTH - CHAIR_SIZE / 2,
            y + tableHeight / 4 - CHAIR_SIZE / 2
        );

        drawChair(
            ctx,
            x + TABLE_WIDTH - CHAIR_SIZE / 2,
            y + (3 * tableHeight) / 4 - CHAIR_SIZE / 2
        );
    }

    ctx.beginPath();
    ctx.rect(x, y, TABLE_WIDTH, tableHeight);
    ctx.fill();
    ctx.stroke();
}

function drawChair(ctx, x, y) {
    ctx.beginPath();
    ctx.rect(x, y, CHAIR_SIZE, CHAIR_SIZE);
    ctx.fill();
    ctx.stroke();
}