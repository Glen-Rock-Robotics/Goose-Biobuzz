package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

public class Intake extends SubsystemBase {

    private final MotorEx intakeL;
    private final MotorEx intakeR;
    private final ServoEx flower;

    private static final double velocity = 1;

    private boolean flowerDown = false;

    private double flowerUpAngle = 0.0;
    private double flowerDownAngle = 90.0; // FIX THIS

    public Intake(final HardwareMap hMap, final String leftName, final String rightName, final String flowerName) {
        // initialize
        intakeL = new MotorEx(hMap, leftName);
        intakeR = new MotorEx(hMap, rightName);

        flower = new ServoEx(hMap, flowerName, flowerUpAngle, flowerDownAngle);
        // motor directions
        flower.setInverted(false);

        intakeL.setInverted(false);
        intakeR.setInverted(true);

        // brake
        intakeL.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        intakeR.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        // runmode
        intakeL.setRunMode(Motor.RunMode.VelocityControl);
        intakeR.setRunMode(Motor.RunMode.VelocityControl);

        // reset stuff
        stop();
        flowerUp();
    }

    public void in() {
        intakeL.set(velocity);
        intakeR.set(velocity);
    }
    public void out() {
        intakeL.set(-velocity);
        intakeR.set(-velocity);
    }
    public void flowerUp() {
        flower.set(flowerUpAngle);
        flowerDown = false;
    }
    public void flowerDown() {
        flower.set(flowerDownAngle);
        flowerDown = true;
    }
    public void flowerToggle() {
        if (flowerDown) {
            flowerUp();
        } else { flowerDown(); }
    }
    public void stop() {
        intakeL.setVelocity(0);
        intakeR.setVelocity(0);
    }
}