package com.adaptionsoft.games.uglytrivia.test;

import com.adaptionsoft.games.uglytrivia.Game;
import io.vavr.Function1;
import io.vavr.collection.List;
import org.junit.Assert;
import org.junit.Test;

public class FindCategoryByPlaceTest {
    @Test
    public void happyPaths() throws Exception {
        List.of(0, 4, 8).map(expectPlaceToBeInCategory("Pop"));
        List.of(1, 5, 9).map(expectPlaceToBeInCategory("Science"));
        List.of(2, 6, 10).map(expectPlaceToBeInCategory("Sports"));
        List.of(3, 7, 11).map(expectPlaceToBeInCategory("Rock"));
    }

    @Test
    public void currentBehaviorOutsideTheRange() throws Exception {
        List.of(12, 13, 14, 15, 28, 30, 21376, 238476, -1, -2, -3, -4, -23746).map(
                expectPlaceToBeInCategory("Rock")
        );
    }

    private Function1<Integer, Boolean> expectPlaceToBeInCategory(final String categoryName) {
        return (place) -> {
            Assert.assertEquals(categoryName, Game.findCategoryNameByPlace(place));
            return true;
        };
    }
}
