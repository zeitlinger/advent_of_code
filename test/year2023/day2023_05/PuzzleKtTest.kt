package year2023.day2023_05

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PuzzleKtTest {

    @Test
    fun cut() {
        // outside
        assertThat(cut(1L..10, 20L..30)).isEqualTo(
            SubRanges(
                emptyList(),
                listOf(1L..10)
            )
        )
        // inter
        assertThat(cut(1L..10, 5L..7)).isEqualTo(
            SubRanges(
                listOf(5L..7),
                listOf(1L..4, 8L..10)
            )
        )
        // included
        assertThat(cut(1L..10, 0L..20)).isEqualTo(
            SubRanges(
                listOf(1L..10),
                emptyList()
            )
        )
        // beginning
        assertThat(cut(1L..10, 0L..5)).isEqualTo(
            SubRanges(
                listOf(1L..5),
                listOf(6L..10)
            )
        )
        // end
        assertThat(cut(1L..10, 5L..20)).isEqualTo(
            SubRanges(
                listOf(5L..10),
                listOf(1L..4)
            )
        )

    }
}
