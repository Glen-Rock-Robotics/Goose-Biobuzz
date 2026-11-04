package org.firstinspires.ftc.teamcode;

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
}
