package ladder;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class LadderTest {

    @Test
    void 사다리_생성_확인() {
        //given
        GreaterThanOne numberOfRow = GreaterThanOne.from(3);
        GreaterThanOne numberOfPerson = GreaterThanOne.from(5);

        //when
        Ladder ladder = new Ladder(numberOfRow, numberOfPerson);

        //then
        assertThat(ladder).isNotNull();
    }

    @Test
    void 사다리_사람_예외_처리_확인() {
        //when
        GreaterThanOne row = GreaterThanOne.from(2);
        GreaterThanOne numberOfPerson = GreaterThanOne.from(3);
        Ladder ladder = new Ladder(row, numberOfPerson);

        //given
        Position nthOfPerson = Position.from(4);

        //then
        assertThatThrownBy(() -> ladder.run(nthOfPerson))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 사다리_결과_확인() {
        //when
        GreaterThanOne numberOfPerson = GreaterThanOne.from(4);
        GreaterThanOne row = GreaterThanOne.from(3);
        Ladder ladder = new Ladder(row, numberOfPerson);

        ladder.drawLine(Position.from(0),Position.from(0));
        ladder.drawLine(Position.from(1),Position.from(1));
        ladder.drawLine(Position.from(2),Position.from(0));

        //given
        Position nthOfPerson = Position.from(0);

        //then
        assertThat(ladder.run(nthOfPerson)).isEqualTo(2);

        //given
        nthOfPerson = Position.from(1);

        //then
        assertThat(ladder.run(nthOfPerson)).isEqualTo(1);

        //given
        nthOfPerson = Position.from(2);

        //then
        assertThat(ladder.run(nthOfPerson)).isEqualTo(0);
    }
}