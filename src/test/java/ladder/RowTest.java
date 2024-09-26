package ladder;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RowTest {

    @Test
    void 한_칸_사다리_이동() {
        //when
        GreaterThanOne numberOfPerson = GreaterThanOne.from(2);
        Row row = new Row(numberOfPerson);

        //given
        int position = 0;

        //then
        assertThat(row.nextPosition(position)).isEqualTo(0);
    }

    @Test
    void 두_칸_사다리_선_이동() {
        //when
        GreaterThanOne numberOfPerson = GreaterThanOne.from(2);
        Row row = new Row(numberOfPerson);
        row.drawLine(0);

        //given
        int position = 0;

        //then
        assertThat(row.nextPosition(position)).isEqualTo(1);

        //given
        position = 1;

        //then
        assertThat(row.nextPosition(position)).isEqualTo(0);
    }

    @Test
    void 세_칸_사다리_선_이동() {
        //when
        GreaterThanOne numberOfPerson = GreaterThanOne.from(3);
        Row row = new Row(numberOfPerson);
        row.drawLine(0);

        //given
        int position = 0;

        //then
        assertThat(row.nextPosition(position)).isEqualTo(1);

        //given
        position = 1;

        //then
        assertThat(row.nextPosition(position)).isEqualTo(0);

        //given
        position = 2;

        //then
        assertThat(row.nextPosition(position)).isEqualTo(2);
    }

//    @Test
//    void 사다리_사람수_예외_처리() {
//        assertThatThrownBy(() -> new Row(GreaterThanOne.from(0))
//                .isInstanceOf(IllegalArgumentException.class)
//        );
//    }

    @Test
    void 사다리_최대_사람수_초과_예외() {
        //when
        GreaterThanOne numberOfPerson = GreaterThanOne.from(3);
        Row row = new Row(numberOfPerson);

        //given
        int position = 3;

        //then
        assertThatThrownBy(() -> row.nextPosition(position))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 사다리_최소_사람수_미만_예외() {
        //when
        GreaterThanOne numberOfPerson = GreaterThanOne.from(3);
        Row row = new Row(numberOfPerson);

        //given
        int position = -1;

        //then
        assertThatThrownBy(() -> row.nextPosition(position))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 사다리_그리기_위치_초과_예외() {
        //when
        GreaterThanOne numberOfPerson = GreaterThanOne.from(3);
        Row row = new Row(numberOfPerson);

        //given
        int position = 3;

        //then
        assertThatThrownBy(() -> row.drawLine(position))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 사다리_그리기_위치_미만_예외() {
        //when
        GreaterThanOne numberOfPerson = GreaterThanOne.from(3);
        Row row = new Row(numberOfPerson);

        //given
        int position = -1;

        //then
        assertThatThrownBy(() -> row.drawLine(position))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 사다리_그리기_좌측_선_중복_예외() {
        //when
        GreaterThanOne numberOfPerson = GreaterThanOne.from(3);
        Row row = new Row(numberOfPerson);
        row.drawLine(0);

        //then
        assertThatThrownBy(() -> row.drawLine(1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 사다리_그리기_우측_선_중복_예외() {
        //when
        GreaterThanOne numberOfPerson = GreaterThanOne.from(3);
        Row row = new Row(numberOfPerson);
        row.drawLine(1);

        //then
        assertThatThrownBy(() -> row.drawLine(0))
                .isInstanceOf(IllegalArgumentException.class);

    }
}