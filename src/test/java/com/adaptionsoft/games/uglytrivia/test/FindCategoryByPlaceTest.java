package com.adaptionsoft.games.uglytrivia.test;

import com.adaptionsoft.games.uglytrivia.Game;
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

    private void checkCategoryByPlace(final int place, final String expectedCategoryName) {
        Assert.assertEquals(expectedCategoryName, Game.findCategoryNameByPlace(place));
    }
}
