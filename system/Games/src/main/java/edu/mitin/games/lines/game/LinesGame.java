package edu.mitin.games.lines.game;

import edu.mitin.games.service.Game;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class LinesGame extends Game {

    private static final Integer NUM_POINTS = 16;
    private static final Integer FIELD_MAX_X = 11;
    private static final Integer FIELD_MAX_Y = 11;
    private List<Point> gamePoints = new LinkedList<>();
    private List<Line> gameLines = new LinkedList<>();

    public LinesGame(String leftPlayerName, String rightPlayerName) {
        super(leftPlayerName, rightPlayerName);
        initPoints();
        setPlayersGoals();
    }

    private void setPlayersGoals() {
        String goal = "";
        for (Point point : gamePoints) {
            goal += point.getX() + "-" + point.getY() + "; ";
        }
        gameResult.setLeftPlayerGoal(goal);
        gameResult.setRightPlayerGoal(goal);
    }

    /**
     * Ответы игроков принимаем по сценарию
     * x1-y1 x2-y2
     *
     * @param action строка в виде x1-y1 x2-y2
     * @return подлижит ли action шаблону, указанному выше, и существует ли данная точка в контексте игры и не занята ли она уже отрезком.
     */
    @Override
    public boolean isCorrectAction(String action) {
        String[] points = action.split(" ");
        if (points.length != 2) {
            return false;
        }
        String point1 = points[0];
        String point2 = points[1];
        String[] coord1 = point1.split("-");
        String[] coord2 = point2.split("-");
        if (coord1.length != 2 || coord2.length != 2) {
            return false;
        }
        Integer x1;
        Integer y1;
        Integer x2;
        Integer y2;
        try {
            x1 = Integer.parseInt(coord1[0]);
            y1 = Integer.parseInt(coord1[1]);
            x2 = Integer.parseInt(coord2[0]);
            y2 = Integer.parseInt(coord2[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        final Point p1 = new Point(x1, y1);
        final Point p2 = new Point(x2, y2);
        return gamePoints.contains(p1)
                && gamePoints.contains(p2)
                && !p1.equals(p2);
    }


    private Point getPoint(String point) {
        String[] coords = point.split("-");
        Integer x = Integer.parseInt(coords[0]);
        Integer y = Integer.parseInt(coords[1]);
        return new Point(x, y);
    }

    @Override
    public String executeLeftPlayerAction(String action) {
        gameResult.addLeftPlayerStep(action);
        String[] points = action.split(" ");
        final Point p1 = getPoint(points[0]);
        final Point p2 = getPoint(points[1]);
        final Line line = new Line(p1, p2);
        for (Line gameLine : gameLines) {
            if (isLinesIntersection(line, gameLine)) {
                return "-1 -1 -1 -1";
            }
        }
        gameLines.add(line);
        return createResponse(line);
    }

    @Override
    public String executeRightPlayerAction(String action) {
        gameResult.addRightPlayerStep(action);
        String[] points = action.split(" ");
        final Point p1 = getPoint(points[0]);
        final Point p2 = getPoint(points[1]);
        final Line line = new Line(p1, p2);
        for (Line gameLine : gameLines) {
            if (isLinesIntersection(line, gameLine)) {
                return "-1 -1 -1 -1";
            }
        }
        gameLines.add(line);
        return createResponse(line);
    }

    @Override
    public boolean isStandOff() {
        return gamePoints.stream().allMatch(this::isOccupiedPoint);
    }

    @Override
    public String getStart() {
        String response = "";
        for (Point point : gamePoints) {
            response += point.getX() + "-" + point.getY() + ";";
        }
        return response;
    }

    @Override
    public boolean isWinLeftPlayerAction(String action) {
        String[] points = action.split(" ");
        final Point p1 = getPoint(points[0]);
        final Point p2 = getPoint(points[1]);
        final Line line = new Line(p1, p2);
        for (Line gameLine : gameLines) {
            if (isLinesIntersection(line, gameLine)) {
                return false;
            }
        }
        List<Point> freePoints = new LinkedList<>();
        for (Point gamePoint: gamePoints) {
            if (!isOccupiedPoint(gamePoint)) {
                freePoints.add(gamePoint);
            }
        }
        freePoints.remove(p1);
        freePoints.remove(p2);
        List<Line> copy = new LinkedList<>(gameLines);
        copy.add(new Line(p1, p2));
        final boolean isWin = !hasChance(freePoints, copy);
        if (isWin) {
            gameResult.setWinner(leftPlayerName);
        }
        return isWin;
    }



    @Override
    public boolean isWinRightPlayerAction(String action) {
        String[] points = action.split(" ");
        final Point p1 = getPoint(points[0]);
        final Point p2 = getPoint(points[1]);
        final Line line = new Line(p1, p2);
        for (Line gameLine : gameLines) {
            if (isLinesIntersection(line, gameLine)) {
                return false;
            }
        }
        List<Point> freePoints = new LinkedList<>();
        for (Point gamePoint: gamePoints) {
            if (!isOccupiedPoint(gamePoint)) {
                freePoints.add(gamePoint);
            }
        }
        freePoints.remove(p1);
        freePoints.remove(p2);
        List<Line> copy = new LinkedList<>(gameLines);
        copy.add(new Line(p1, p2));
        final boolean isWin = !hasChance(freePoints, copy);
        if (isWin) {
            gameResult.setWinner(rightPlayerName);
        }
        return isWin;
    }

    private String createResponse(Line line) {
        return line.getStart().getX() + " " + line.getStart().getY() + " " + line.getEnd().getX() + " " + line.getEnd().getY();
    }

    private void initPoints() {
        for (int i = 0; i < NUM_POINTS; i++) {
            gamePoints.add(createPoint());
        }
    }

    private Point createPoint() {
        Integer x = new Random().nextInt(FIELD_MAX_X);
        Integer y = new Random().nextInt(FIELD_MAX_Y);
        Point point = new Point(x, y);
        if (gamePoints.contains(point)) {
            return createPoint();
        } else {
            return point;
        }
    }

    private boolean isOccupiedPoint(Point point) {
        return gameLines.stream().anyMatch(line -> line.getEnd().equals(point) || line.getStart().equals(point));
    }

    private boolean isLinesIntersection(Line l1, Line l2) {

        if (isPointIntersection(l1, l2.getStart())
                || isPointIntersection(l1, l2.getEnd())
                || isPointIntersection(l2, l1.getStart())
                || isPointIntersection(l2, l1.getEnd())) {
            return true;
        }

        Integer ax1 = l1.getStart().getX();
        Integer ay1 = l1.getStart().getY();
        Integer ax2 = l1.getEnd().getX();
        Integer ay2 = l1.getEnd().getY();

        Integer bx1 = l2.getStart().getX();
        Integer by1 = l2.getStart().getY();
        Integer bx2 = l2.getEnd().getX();
        Integer by2 = l2.getEnd().getY();

        Integer v1 = (bx2 - bx1) * (ay1 - by1) - (by2 - by1) * (ax1 - bx1);
        Integer v2 = (bx2 - bx1) * (ay2 - by1) - (by2 - by1) * (ax2 - bx1);
        Integer v3 = (ax2 - ax1) * (by1 - ay1) - (ay2 - ay1) * (bx1 - ax1);
        Integer v4 = (ax2 - ax1) * (by2 - ay1) - (ay2 - ay1) * (bx2 - ax1);
        return (v1*v2 < 0) && (v3*v4 < 0);
    }

    private boolean isPointIntersection(Line line, Point point) {
        Integer x = point.getX();
        Integer y = point.getY();
        Integer x1 = line.getStart().getX();
        Integer y1 = line.getStart().getY();
        Integer x2 = line.getEnd().getX();
        Integer y2 = line.getEnd().getY();
        Integer s = (x - x1) * (y2 - y1) - (x2 - x1) * (y - y1);
        if (s.equals(0)) {
            Integer xCheck = (x-x1)*(x2-x);
            Integer yCheck = (y-y1)*(y2-y);
            return xCheck >= 0 && xCheck <= (x1-x2)*(x1-x2) && yCheck >= 0 && yCheck <= (y1-y2)*(y1-y2);
        }
        return false;
    }

    private boolean hasChance(List<Point> freePoints, List<Line> gameLines) {
        for (int i = 0; i < freePoints.size(); i++) {
            for (int j = i + 1; j < freePoints.size(); j++) {
                final Point p1 = freePoints.get(i);
                final Point p2 = freePoints.get(j);
                final Line line = new Line(p1, p2);
                boolean isLineIntersection = false;
                for (Line gameLine : gameLines) {
                    if (isLinesIntersection(line, gameLine)) {
                        isLineIntersection = true;
                    }
                }
                if (!isLineIntersection) {
                    return true;
                }
            }
        }
        return false;
    }
}
