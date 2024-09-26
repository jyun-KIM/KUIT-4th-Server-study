package ladder;

import static ladder.Direction.*;

public class Node {
    private Direction direction;

    private Node(Direction direction) {
        this.direction = direction;
    }

    public static Node from(Direction direction) {
        return new Node(direction);
    }

    public void setRightNode(Position position) {
        direction = RIGHT;
    }

    public void setLeftNode(Position position) {
        direction = LEFT;
    }

    public void move(Position position){
        if (isRight()) {
            position.next();
            return;
        }
        if (isLeft()) {
            position.prev(); // 그라디오 터미널 어디간거야 씨 9그게뭔데!!!
        }
    }

    public boolean isAlreadysetDirection(){
        return isNone();
    }

    private boolean isRight(){
        return direction == RIGHT;
    }

    private boolean isLeft(){
        return direction == LEFT;
    }

    private boolean isNone() {
        return direction == NONE;
    }
}
