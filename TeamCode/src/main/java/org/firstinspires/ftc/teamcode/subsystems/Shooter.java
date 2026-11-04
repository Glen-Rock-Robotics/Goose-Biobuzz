package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

/**
 * intake ig
 */
public class Shooter extends SubsystemBase {

    private final MotorEx shooter;

    private double targetVelocity = 62;
    private final double toleranceVelocity = 3;

    private boolean running = false;


    public Shooter(final HardwareMap hMap, final String name) {
        // initialize
        shooter = new MotorEx(hMap, name);
        // motor directions
        shooter.setInverted(false);
        // brake
        shooter.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        // runmode
        shooter.setRunMode(Motor.RunMode.VelocityControl);

        // reset stuff
        stop();
    }

    public void spin() {
        shooter.setVelocity(targetVelocity);
        running = true;
    }
    public void stop() {
        shooter.setVelocity(0);
        running = false;
    }
    public void setTargetVelocity(double vel) {
        targetVelocity = vel;
        if (running) {
            shooter.setVelocity(targetVelocity);
        }
    }
    public void changeTargetVelocity(double velChange) {
        setTargetVelocity(velChange + getTargetVelocity());
    }
    public double getTargetVelocity() {
        return targetVelocity;
    }
    public double getActualVelocity() {
        return shooter.getVelocity();
    }
    public boolean isRunning() {
        return running;
    }
    public boolean checkReady() {
        return Math.abs(this.getTargetVelocity() - this.getActualVelocity()) < toleranceVelocity;
    }
    public void setCoefficients(double[] pid, double[] ff) {
        double kP = pid[0];
        double kI = pid[1];
        double kD = pid[2];
        shooter.setVeloCoefficients(kP,kI,kD);

        double kS = ff[0];
        double kV = ff[1];
        shooter.setFeedforwardCoefficients(kS,kV);
    }
    public double[] getVelocityCoefficients() {
        return shooter.getVeloCoefficients();
    }
    public double[] getFeedforwardCoefficients() {
        return shooter.getFeedforwardCoefficients();
    }
}