package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;


public class Robot {
    public Intake intake;
    public Shooter shooter;
    public Robot(HardwareMap hardwareMap) {
        intake = new Intake(hardwareMap, "intake1", "intake2","flowerServo");
        shooter = new Shooter(hardwareMap, "shooter");

        this.setState(State.IDLE);
    }
    // times in milliseconds
    public static final long maxSpinUpTime = 5000;
    public static final long shootTime = 2500;


    public enum State {
        IDLE, COLLECTING, PREPPING, READY
    }
    private State lastState = State.IDLE;
    private State currState = State.IDLE;
    public void setState(State newState) {
        if (newState != currState) {
            lastState = currState;
            currState = newState;
        }
    }
    public State getState() {
        return currState;
    }
    public State getLastState() {
        return lastState;
    }
    public boolean isState(State potentialState) { return currState==potentialState; }


    // dimensions of robot
    public static final double height = 3.0; // inches, doesnt matter lol
    public static final double width = 3.0; // inches
    public static final double length = 3.0; // inches

    // poses. reference: red (left side) of birds-eye view of field
    private static final PoseFactory p = PoseFactory.degrees();
    public static final Pose cornerPose =
            p.of(0, 141.5, 0);
    public static final Pose middleWallPose =
            p.of(width/2, 70.75, 0); // DONE!!!
    public static final Pose launchZoneFarPose =
            p.of(10, 141.5-24, 0); // tip of zone farther away from hives
    public static final Pose launchZoneClosePose =
            p.of(0, 0, 0); // tip of zone closer to hives
    public static final Pose initialCellAimPose =
            p.of(0, 0, 0);
    public static Pose teleOpStartPose = initialCellAimPose;
    // assume teleop starts in front of hive IF no auton
}
