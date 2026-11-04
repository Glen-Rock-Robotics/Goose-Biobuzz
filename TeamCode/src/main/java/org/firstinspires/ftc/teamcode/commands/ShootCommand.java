
package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

public class ShootCommand extends CommandBase {

    private final Robot robot;
    public ShootCommand(Robot robot) {
        this.robot = robot;
        addRequirements(robot.intake, robot.shooter);
    }

    @Override
    public void initialize() {
        if (robot.isState(Robot.State.READY)) {
            robot.shooter.spin();
            robot.intake.in();
        }
    }

    @Override
    public void end(boolean interrupted) {
        robot.setState(Robot.State.PREPPING);
        robot.intake.stop();
        robot.shooter.spin();
    }
}
