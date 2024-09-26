package ladder;

import static ladder.Direction.*;

public class Row {
    Node[] nodes;

    public Row(GreaterThanOne numberOfPerson) {
        nodes = new Node[numberOfPerson.getNumber()];
        for (int i = 0; i < numberOfPerson.getNumber(); i++) {
            nodes[i] = Node.from(NONE);
        }
    }

    public void drawLine(Position startPosition) {
        validateDrawLinePosition(startPosition);
        setDirectionNextPosition(startPosition);
    }

    private void setDirectionNextPosition(Position position) {
        nodes[position.getPosition()].setRightNode(position);
        position.next();
        nodes[position.getPosition()].setLeftNode(position);
    }

    public void nextPosition(Position position) {
        validatePosition(position);

        nodes[position.getPosition()].move(position);
    }


    private void validatePosition(Position position) {
        if (position.isBiggerThan(nodes.length -1)) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_DRAW_POSITION.getMessage());
        }
    }

    private void validateDrawLinePosition(Position startPosition) {
        if (isInvalidPosition(startPosition) || isLineAtPosition(startPosition) || isLineAtNextPosition(startPosition)) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_DRAW_POSITION.getMessage());
        }
    }

    private boolean isInvalidPosition(Position startPosition) {
        return startPosition.isBiggerThan(nodes.length -1);
    }

    private boolean isLineAtPosition(Position startPosition) {
        return !nodes[startPosition.getPosition()].isAlreadysetDirection();
        // 라인을 그릴 때 라인(L), 라인(R)이 다 존재하는지 확인
    }

    private boolean isLineAtNextPosition(Position startPosition) {
        startPosition.next();
        return !nodes[startPosition.getPosition()].isAlreadysetDirection();
    }
}