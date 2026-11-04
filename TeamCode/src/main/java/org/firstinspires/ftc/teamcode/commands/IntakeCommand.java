
package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

public class IntakeCommand extends CommandBase {
    private final Robot robot;

    public IntakeCommand(Robot robot, boolean reverse) {
        this.robot = robot;
        addRequirements(robot.intake,robot.shooter);
    }

    @Override
    public void initialize() {
        robot.setState(Robot.State.COLLECTING);
        //robot.intake.setDirection( (reverse) ? Intake.Direction.SPITTING : Intake.Direction.INTAKING );

        robot.intake.in();
        robot.shooter.stop();
    }
    @Override
    public void end(boolean interrupted) {
        robot.intake.stop();

        // Preserve the state if another command interrupted us.
        if (!interrupted) {
            robot.setState(Robot.State.IDLE);
        }
    }
}
