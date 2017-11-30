package com.adaptionsoft.games.uglytrivia.test;

import com.adaptionsoft.games.uglytrivia.Game;
import io.vavr.collection.List;
import org.junit.Assert;
import org.junit.Test;

public class FindCategoryByPlaceTest {
    @Test
    public void happyPaths() throws Exception {
        checkCategoryByPlace(0, "Pop");
        checkCategoryByPlace(1, "Science");
        checkCategoryByPlace(2, "Sports");
        checkCategoryByPlace(3, "Rock");
        checkCategoryByPlace(4, "Pop");
        checkCategoryByPlace(5, "Science");
        checkCategoryByPlace(6, "Sports");
        checkCategoryByPlace(7, "Rock");
        checkCategoryByPlace(8, "Pop");
        checkCategoryByPlace(9, "Science");
        checkCategoryByPlace(10, "Sports");
        checkCategoryByPlace(11, "Rock");
    }

    @Test
    public void currentBehaviorOutsideTheRange() throws Exception {
        List.of(12, 13, 14, 15, 28, 30, 21376, 238476, -1, -2, -3, -4, -23746).forEach(
                n -> checkCategoryByPlace(n, "Rock")
        );
    }

    private void checkCategoryByPlace(final int place, final String expectedCategoryName) {
        Assert.assertEquals(expectedCategoryName, Game.findCategoryNameByPlace(place));
    }
}
