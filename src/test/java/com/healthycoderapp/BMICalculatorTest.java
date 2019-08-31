package com.healthycoderapp;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BMICalculatorTest {

    private String environment = "prod";

    //to set up expensive tasks once for all, such as db connection
    @BeforeAll
    public static void beforeAll(){
        System.out.println("before all unit tests");
    }

    //to close expensive operations, such as shutdown server
    @AfterAll
    public static void afterAll(){
        System.out.println("after all unit tests");
    }

    @Nested
    class isDietRecommendedTests {
        @ParameterizedTest(name = "weight={0},height={1}") // name is optional
        @CsvFileSource(resources = "/diet_input_data.csv", numLinesToSkip = 1)
        void should_ReturnTrue_when_DietRecommended(double coderWeight, double coderHeight) {

            // given
            double weight = coderWeight;
            double height = coderHeight;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);

        }

        @org.junit.jupiter.api.Test
        void should_ReturnFalse_when_DietNotRecommended() {

            // given
            double weight = 50.0;
            double height = 1.92;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertFalse(recommended);

        }

        @org.junit.jupiter.api.Test
        void should_ThrowArithmeticException_when_HeightIsZero() {

            // given
            double weight = 50.0;
            double height = 0.0;

            //when
            Executable executable = () -> BMICalculator.isDietRecommended(weight, height);

            //then
            assertThrows(ArithmeticException.class, executable);

        }
    }

    @Nested
    class findCoderWithWorstBMITests {
        @org.junit.jupiter.api.Test
        void should_ReturnWorstBMICoder_when_CodeListIsNotEmpty() {

            // given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.82, 98.0));
            coders.add(new Coder(1.82, 64.7));

            //when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //use below syntax to prevent execute only part of the assertion
            assertAll(
                    () -> assertEquals(1.82, coderWorstBMI.getHeight()),
                    () -> assertEquals(98.0, coderWorstBMI.getWeight())
            );
        }

        @org.junit.jupiter.api.Test
        void should_ReturnNull_when_CodeListIsEmpty() {

            // given
            List<Coder> coders = new ArrayList<>();


            //when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertNull(coderWorstBMI);
        }
    }

    @Nested
    class getBMIScoresTests {
        @org.junit.jupiter.api.Test
        void should_ReturnBMIArray_when_CodeListIsNotEmpty() {

            // given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.82, 98.0));
            coders.add(new Coder(1.82, 64.7));

            double[] expected = {18.52, 29.59, 19.53};

            //when
            double[] bmiScores = BMICalculator.getBMIScores(coders);

            //then
            assertArrayEquals(bmiScores, expected);

        }

        @org.junit.jupiter.api.Test
        void should_ReturnWorstBMICoderWithin1Ms_when_CodeListIsNotEmpty() {

            // given
            Assumptions.assumeTrue(BMICalculatorTest.this.environment.equals("prod"));

            List<Coder> coders = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                coders.add(new Coder(1.0 + i, 10.0 + i));
            }


            //when
            Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertTimeout(Duration.ofMillis(500), executable);
        }
    }






}