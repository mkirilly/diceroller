package com.mkirilly.diceroller;

import android.widget.Button;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

public class OnClickRollTest {
    @Test
    // Test single d1-d20
    public void oneUnmodifiedDie() {
        Random r = new Random(123454321);   // use fixed seed to make this reproducible

        for (int dieSides = 1; dieSides<21; dieSides++) {
            String diceText = "d"+Integer.toString(dieSides);

            // test 20 rolls
            for (int tries = 0; tries <= 20; tries++) {
                Integer roll = MainActivity.getNextRoll(diceText, r);
                assertNotNull(roll);
                assertTrue("positive", roll > 0);
                assertTrue("roll <= " + Integer.toString(dieSides), roll <= dieSides);
            }
        }
    }

    @Test
    // Test multiple d6s (e.g. 3d6 or 4d6)
    public void multipleUnmodifiedD6() {
        Random r = new Random(123454321);   // use fixed seed to make this reproducible

        final int dieSides = 6;
        for (int numDice = 1; numDice<21; numDice++) {
            final String diceText = Integer.toString(numDice) + "d6";

            // test 20 rolls
            for (int rNum = 0; rNum <= 20; rNum++) {
                Integer roll = MainActivity.getNextRoll(diceText, r);
                assertNotNull(roll);
                assertTrue("roll >= " + numDice, roll >= numDice);
                assertTrue("roll <= " + numDice + "*" + dieSides, roll <= numDice*dieSides);
            }
        }
    }


    @Test
    // Test multiple modified d4s (e.g. 3d4-2 or 8d4+1)
    public void multipleModifiedD4() {
        Random r = new Random(123454321);   // use fixed seed to make this reproducible

        final int dieSides = 4;
        for (int numDice = 3; numDice<20; numDice += 10) {
            for (int modifier = -4; modifier < 5; modifier += 2) {
                final String modString = String.format("%+d", modifier);
                final String diceText = Integer.toString(numDice) + "d4" + modString;

                // test 20 rolls
                for (int rNum = 0; rNum <= 20; rNum++) {
                    Integer roll = MainActivity.getNextRoll(diceText, r);
                    assertNotNull(roll);
                    final Integer lLimit = numDice + modifier;
                    final Integer uLimit = numDice*dieSides +modifier;
                    assertTrue("roll >= " + numDice + modString,
                            roll >= lLimit);
                    assertTrue("roll <= " + numDice + "*" + dieSides + modString,
                            roll <= uLimit);
                }
            }
        }
    }
}
