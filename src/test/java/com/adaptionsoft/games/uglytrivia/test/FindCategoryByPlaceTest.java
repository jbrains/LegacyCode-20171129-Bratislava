package com.adaptionsoft.games.uglytrivia.test;

import com.adaptionsoft.games.uglytrivia.Game;
import io.vavr.*;
import io.vavr.collection.List;
import org.junit.Assert;
import org.junit.Test;

public class FindCategoryByPlaceTest {
    @Test
    public void happyPaths() throws Exception {
        checkCategoryByPlace("Pop", 0);
        checkCategoryByPlace("Science", 1);
        checkCategoryByPlace("Sports", 2);
        checkCategoryByPlace("Rock", 3);
        checkCategoryByPlace("Pop", 4);
        checkCategoryByPlace("Science", 5);
        checkCategoryByPlace("Sports", 6);
        checkCategoryByPlace("Rock", 7);
        checkCategoryByPlace("Pop", 8);
        checkCategoryByPlace("Science", 9);
        checkCategoryByPlace("Sports", 10);
        checkCategoryByPlace("Rock", 11);
    }

    @Test
    public void currentBehaviorOutsideTheRange() throws Exception {
        List.of(12, 13, 14, 15, 28, 30, 21376, 238476, -1, -2, -3, -4, -23746).map(
                expectPlaceToBeInCategory("Rock")
        );
    }

    private Function1<Integer, Boolean> expectPlaceToBeInCategory(final String categoryName) {
        return ((Function2<String, Integer, Boolean>) this::checkCategoryByPlace).curried()
                .apply(categoryName);
    }

    private boolean checkCategoryByPlace(final String expectedCategoryName, final int place) {
        Assert.assertEquals(expectedCategoryName, Game.findCategoryNameByPlace(place));
        return true;
    }
}
