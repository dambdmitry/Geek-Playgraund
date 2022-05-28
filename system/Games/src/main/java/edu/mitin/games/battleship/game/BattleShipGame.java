package edu.mitin.games.battleship.game;

import edu.mitin.games.service.Game;

import java.util.Random;

/**
 * Поле сражения
 * 0 - пустая клетка
 * 1 - стоит корабль
 * 2 - раненная клетка
 * 3 - убитая клетка
 * Ответы игроку
 * 0 - мимо
 * 1 - ранил
 * 2 - убил
 */
public class BattleShipGame extends Game {

    private int[][] leftPlayerField = new int[10][10];
    private int[][] rightPlayerField = new int[10][10];

    public BattleShipGame(String leftPlayerName, String rightPlayerName) {
        super(leftPlayerName, rightPlayerName);
        initField(leftPlayerField);
        initField(rightPlayerField);
        setPlayersFields();
    }

    private void setPlayersFields() {
        StringBuilder leftField = new StringBuilder();
        StringBuilder rightField = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                leftField.append(leftPlayerField[i][j]).append(" ");
                rightField.append(rightPlayerField[i][j]).append(" ");
            }
            leftField.append("\n");
            rightField.append("\n");
        }
        super.gameResult.setLeftPlayerGoal(leftField.toString());
        super.gameResult.setRightPlayerGoal(rightField.toString());
    }


    /**
     * принимаем ходы в таком виде 1-2, 4-2, 10-10 и тд
     * где левое число номер строки, правое - номер столбца
     * от 1 до 10
     * @param action строка в виде строка-столбец
     * @return корректен ли ввод
     */
    @Override
    public boolean isCorrectAction(String action) {
        String[] split = action.split("-");
        if (split.length != 2) return false;
        try {
            int row = Integer.parseInt(split[0]);
            int column = Integer.parseInt(split[1]);
            return row >= 1 && row <= 10 && column >= 1 && column <= 10;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    @Override
    public String executeLeftPlayerAction(String action) {
        String[] split = action.split("-");
        int row = Integer.parseInt(split[0]) - 1;
        int column = Integer.parseInt(split[1]) - 1;
        int cell = rightPlayerField[row][column];
        if (cell == 1) {
            if (isKillShot(row, column, rightPlayerField)) {
                return "2";
            }
            rightPlayerField[row][column] = 2;
            return "1";
        } else {
            return "0";
        }
    }

    @Override
    public String executeRightPlayerAction(String action) {
        String[] split = action.split("-");
        int row = Integer.parseInt(split[0]) - 1;
        int column = Integer.parseInt(split[1]) - 1;
        int cell = leftPlayerField[row][column];
        if (cell == 1) {
            if (isKillShot(row, column, leftPlayerField)) {
                return "2";
            }
            leftPlayerField[row][column] = 2;
            return "1";
        } else {
            return "0";
        }
    }

    @Override
    public boolean isWinLeftPlayerAction(String action) {
        gameResult.addLeftPlayerStep(action);
        String[] split = action.split("-");
        int row = Integer.parseInt(split[0]) - 1;
        int column = Integer.parseInt(split[1]) - 1;
        int cell = rightPlayerField[row][column];
        if (cell == 1) {
            int countAliveDecks = 0;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (rightPlayerField[i][j] == 1) {
                        countAliveDecks++;
                        if (countAliveDecks > 1){
                            return false;
                        }
                    }
                }
            }
            gameResult.setWinner(leftPlayerName);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isWinRightPlayerAction(String action) {
        gameResult.addRightPlayerStep(action);
        String[] split = action.split("-");
        int row = Integer.parseInt(split[0]) - 1;
        int column = Integer.parseInt(split[1]) - 1;
        int cell = leftPlayerField[row][column];
        if (cell == 1) {
            int countAliveDecks = 0;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (leftPlayerField[i][j] == 1) {
                        countAliveDecks++;
                        if (countAliveDecks > 1){
                            return false;
                        }
                    }
                }
            }
            gameResult.setWinner(rightPlayerName);
            return true;
        } else {
            return false;
        }
    }

    private boolean isKillShot(int row, int column, int[][] field) {
        if (row + 1 <= 9 && field[row+1][column] != 0 || row - 1 >= 0 && field[row-1][column] != 0) {
            int rightRunner = row + 1;
            while (rightRunner <= 9 && field[rightRunner][column] != 0) {
                if (field[rightRunner][column] == 1) {
                    return false;
                }
                rightRunner++;
            }
            int leftRunner = row - 1;
            while (leftRunner >= 0 && field[leftRunner][column] != 0) {
                if (field[leftRunner][column] == 1) {
                    return false;
                }
                leftRunner--;
            }
            for (int k = leftRunner+1; k < rightRunner; k++) {
                field[k][column] = 3;
            }
            return true;
        } else if (column + 1 <= 9 && field[row][column+1] != 0 || column - 1 >= 0 && field[row][column-1] != 0) {
            int downRunner = column + 1;
            while (downRunner <= 9 && field[row][downRunner] != 0) {
                if (field[row][downRunner] == 1) {
                    return false;
                }
                downRunner++;
            }
            int upRunner = column - 1;
            while (upRunner >= 0 && field[row][upRunner] != 0) {
                if (field[row][upRunner] == 1) {
                    return false;
                }
                upRunner--;
            }
            for (int k = upRunner+1; k < downRunner; k++) {
                field[row][k] = 3;
            }
            return true;
        } else {
            field[row][column] = 3;
            return true;
        }
    }

    private void initField(int[][] field) {
        fillShip(field, 4);

        fillShip(field, 3);
        fillShip(field, 3);

        fillShip(field, 2);
        fillShip(field, 2);
        fillShip(field, 2);

        fillShip(field, 1);
        fillShip(field, 1);
        fillShip(field, 1);
        fillShip(field, 1);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (field[i][j] == 2) {
                    field[i][j] = 0;
                }
            }
        }
    }

    private void fillShip(int[][] field, int numDecks) {
        int i = new Random().nextInt(10);
        int j = new Random().nextInt(10);
        boolean isRowShip = new Random().nextBoolean();
        if (isRowShip) {
            if (isAvailableLeftDirection(i, j, field, numDecks)) {
                fillLeftDirection(i, j, numDecks, field);
            } else if (isAvailableRightDirection(i, j, field, numDecks)) {
                fillRightDirection(i, j, numDecks, field);
            } else {
                fillShip(field, numDecks);
            }
        } else {
            if (isAvailableUpDirection(i, j, field, numDecks)) {
                fillUpDirection(i, j, numDecks, field);
            } else if (isAvailableDownDirection(i, j, field, numDecks)) {
                fillDownDirection(i, j, numDecks, field);
            } else {
                fillShip(field, numDecks);
            }
        }
    }

    private void fillLeftDirection(int row, int column, int numDecks, int[][] field) {
        boolean isExistsUpRow = row - 1 >= 0;
        boolean isExistsDownRow = row + 1 <= 9;
        boolean isExistsLeftColumn = column - numDecks >= 0;
        boolean isExistsRightColumn = column + 1 <= 9;
        if (isExistsLeftColumn) {
            fillOccupiedInRow(isExistsUpRow, isExistsDownRow, row, column - numDecks, field);
        }
        if (isExistsRightColumn) {
            fillOccupiedInRow(isExistsUpRow, isExistsDownRow, row, column + 1, field);
        }
        for (int k = column - numDecks + 1; k <= column; k++) {
            field[row][k] = 1;
            fillOccupiedInRow(isExistsUpRow, isExistsDownRow, row, k, field);
        }
    }

    private void fillRightDirection(int row, int column, int numDecks, int[][] field) {
        boolean isExistsUpRow = row - 1 >= 0;
        boolean isExistsDownRow = row + 1 <= 9;
        boolean isExistsLeftColumn = column - 1 >= 0;
        boolean isExistsRightColumn = column + numDecks <= 9;
        if (isExistsLeftColumn) {
            fillOccupiedInRow(isExistsUpRow, isExistsDownRow, row, column - 1, field);
        }
        if (isExistsRightColumn) {
            fillOccupiedInRow(isExistsUpRow, isExistsDownRow, row, column + numDecks, field);
        }
        for (int k = column; k < column + numDecks; k++) {
            field[row][k] = 1;
            fillOccupiedInRow(isExistsUpRow, isExistsDownRow, row, k, field);
        }
    }

    private void fillUpDirection(int row, int column, int numDecks, int[][] field) {
        boolean isExistsLeftColumn = column - 1 >= 0;
        boolean isExistsRightColumn = column + 1 <= 9;
        boolean isExistsUpRow = row - numDecks >= 0;
        boolean isExistsDownRow = row + 1 <= 9;
        if (isExistsUpRow) {
            fillOccupiedInColumn(isExistsLeftColumn, isExistsRightColumn, row - numDecks, column, field);
        }
        if (isExistsDownRow) {
            fillOccupiedInColumn(isExistsLeftColumn, isExistsRightColumn, row + 1, column, field);
        }
        for (int k = row - numDecks + 1; k <= row; k++) {
            field[k][column] = 1;
            fillOccupiedInColumn(isExistsLeftColumn, isExistsRightColumn, k, column, field);
        }
    }

    private void fillDownDirection(int row, int column, int numDecks, int[][] field) {
        boolean isExistsLeftColumn = column - 1 >= 0;
        boolean isExistsRightColumn = column + 1 <= 9;
        boolean isExistsUpRow = row - 1 >= 0;
        boolean isExistsDownRow = row + numDecks <= 9;
        if (isExistsUpRow) {
            fillOccupiedInColumn(isExistsLeftColumn, isExistsRightColumn, row - 1, column, field);
        }
        if (isExistsDownRow) {
            fillOccupiedInColumn(isExistsLeftColumn, isExistsRightColumn, row + numDecks, column, field);
        }
        for (int k = row; k < row + numDecks; k++) {
            field[k][column] = 1;
            fillOccupiedInColumn(isExistsLeftColumn, isExistsRightColumn, k, column, field);
        }
    }

    private boolean isAvailableUpDirection(int row, int column, int[][] field, int numDecks) {
        for (int k = row - numDecks + 1; k <= row; k++) {
            if (k < 0 || field[k][column] != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isAvailableDownDirection(int row, int column, int[][] field, int numDecks) {
        for (int k = row; k < row + numDecks; k++) {
            if (k > 9 || field[k][column] != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isAvailableRightDirection(int row, int column, int[][] field, int numDecks) {
        for (int k = column; k < column + numDecks; k++) {
            if (k > 9 || field[row][k] != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isAvailableLeftDirection(int row, int column, int[][] field, int numDecks) {
        for (int k = column - numDecks + 1; k <= column; k++) {
            if (k < 0 || field[row][k] != 0) {
                return false;
            }
        }
        return true;
    }

    private static void fillOccupiedInRow(boolean isExistsUpRow, boolean isExistsDownRow, int row, int column, int[][] field) {
        if (field[row][column] != 1) {
            field[row][column] = 2;
        }
        if (isExistsUpRow) {
            field[row - 1][column] = 2;
        }
        if (isExistsDownRow) {
            field[row + 1][column] = 2;
        }
    }

    private static void fillOccupiedInColumn(boolean isExistsLeftColumn, boolean isExistsRightColumn, int row, int column, int[][] field) {
        if (field[row][column] != 1) {
            field[row][column] = 2;
        }
        if (isExistsLeftColumn) {
            field[row][column - 1] = 2;
        }
        if (isExistsRightColumn) {
            field[row][column + 1] = 2;
        }
    }
}
