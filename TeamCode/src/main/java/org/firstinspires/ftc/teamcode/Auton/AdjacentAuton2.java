package org.firstinspires.ftc.teamcode.Auton;

import com.pedropathing.follower.FollowerLog;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.ivy.Scheduler;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous(name = "middle -> MOVE LEFT -> zone", group = "")
public class AdjacentAuton2 extends OpMode {

    private Follower follower;
    private final Pose startPose = Robot.middleWallPose;
    private final Pose endPose = Robot.launchZoneClosePose;

    // Path methods
    private Path moveToLaunchZone() {
        return line(startPose, endPose)
                .linear(startPose, endPose);
    }

    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();

        telemetry.addLine("INITIALIZED!!!");
        telemetry.update();
    }

    @Override
    public void start() {
        schedule(follow(follower, moveToLaunchZone()));
        /*
        sequential(
                follow(follower, startToScore()),
                // Add mechanism commands here.
                follow(follower, park())
        );
        */
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();

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
        if (follower != null) {
            Robot.teleOpStartPose = follower.pose();
        }
    }
}