
package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

public class SpitCommand extends CommandBase {

    private final Robot robot;

    private Robot.State previousState;

    public SpitCommand(Robot robot) {
        this.robot = robot;
        addRequirements(robot.intake);
    }

    @Override
    public void initialize() {
        previousState = robot.getState();
        robot.setState(Robot.State.COLLECTING);
        robot.intake.out();
    }

    @Override
    public void end(boolean interrupted) {
        robot.intake.in();
    }
}
