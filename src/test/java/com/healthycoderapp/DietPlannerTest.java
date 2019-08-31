package com.healthycoderapp;

import com.sun.tools.javac.jvm.Code;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DietPlannerTest {

    private DietPlanner dietPlanner;

    @BeforeEach
    void setUp() {
        this.dietPlanner = new DietPlanner(
                20,
                30,
                50);
    }

    @AfterEach
    void tearDown() {
        System.out.println("one unit test completed");
    }


    @RepeatedTest(10)
    void should_ReturnCorrectDietPlan_when_CorrectCoder() {
        //given
        Coder coder = new Coder(1.82,75.0,26,Gender.MALE);
        DietPlan expected = new DietPlan(2202, 110,73,275);

        //when
        DietPlan actual = this.dietPlanner.calculateDiet(coder);

        //then
        assertAll(
                () -> assertEquals(actual.getCalories(), expected.getCalories()),
                () -> assertEquals(actual.getCarbohydrate(), expected.getCarbohydrate()),
                () -> assertEquals(actual.getFat(), expected.getFat()),
                () -> assertEquals(actual.getProtein(), expected.getProtein())
        );
    }
}