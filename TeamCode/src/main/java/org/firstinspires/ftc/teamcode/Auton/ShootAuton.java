package org.firstinspires.ftc.teamcode.Auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.ivy.Scheduler;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous(name = "SHOOT -> move", group = "")
public class ShootAuton extends OpMode {

    private Follower follower;
    private Robot robot;
    private final Pose startPose = Robot.initialCellAimPose;
    private final Pose endPose = Robot.middleWallPose;

    // Path methods
    private Path moveToMiddleWall() {
        return line(startPose, endPose)
                .linear(startPose, endPose);
    }

    @Override
    public void init() {
        Scheduler.reset();
        CommandScheduler.getInstance().reset();

        robot = new Robot(hardwareMap);

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();

        telemetry.addLine("INITIALIZED!!!");
        telemetry.update();
    }

    @Override
    public void start() {
        //schedule(follow(follower, moveToMiddleWall()));
        CommandScheduler.getInstance().schedule(new SequentialCommandGroup(
                new InstantCommand( // spin up shooter
                        () -> robot.shooter.spin(),
                        robot.shooter
                ),
                new ParallelRaceGroup( // either shooter is ready or timeout
                        new WaitUntilCommand(
                                () -> robot.shooter.checkReady()
                        ),
                        new WaitCommand(Robot.maxSpinUpTime)
                ),
                new InstantCommand( // feed balls to shooter
                        () -> robot.intake.in(),
                        robot.intake
                ),
                new WaitCommand(Robot.shootTime), // wait until balls shot
                new InstantCommand( // stop everything
                        () -> {
                            robot.intake.stop();
                            robot.shooter.stop();
                        },
                        robot.intake,
                        robot.shooter
                ),
                new InstantCommand( // follow path
                        () -> schedule(follow(follower, moveToMiddleWall()))
                )
        ));
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();

        CommandScheduler.getInstance().run();
        telemetry.addData("Shooter Ready", robot.shooter.checkReady());
        telemetry.addData("Shooter Target", robot.shooter.getTargetVelocity());
        telemetry.addData("Shooter Actual", robot.shooter.getActualVelocity());

        Pose current = follower.pose();
        telemetry.addData("X", current.x());
        telemetry.addData("Y", current.y());
        telemetry.addData("Heading",
                Math.toDegrees(current.heading()));

        telemetry.addData("Follower Mode", follower.mode());
        telemetry.addData("Busy", follower.isBusy());
        telemetry.addData("Path completion", follower.completion());
        telemetry.addData("Distance remaining in path",
                follower.remainingDistance());


        /*
        FollowerLog debug = follower.debug();
        telemetry.addData("overall",debug.toString());
        telemetry.addData("drivetrain",debug.drivetrain().toString());
        telemetry.addData("algorithm",debug.algorithm().toString());
        telemetry.addData("localizer",debug.localizer().toString());
        // for more go to "logging" pedro page
        */

        telemetry.update();
    }
    @Override
    public void stop() {
        if (robot != null) {
            robot.intake.stop();
            robot.shooter.stop();
        }
        if (follower != null) {
            Robot.teleOpStartPose = follower.pose();
        }
    }
}