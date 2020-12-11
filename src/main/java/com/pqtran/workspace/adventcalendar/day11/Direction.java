package com.pqtran.workspace.adventcalendar.day11;

import java.util.*;

enum Direction {
    TopLeft,
    Top,
    TopRight,
    Left,
    Right,
    BottomLeft,
    Bottom,
    BottomRight;

    static List<Direction> valuesAsList() {
        return Arrays.asList(Direction.values());
    }
}
