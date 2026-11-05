package org.firstinspires.ftc.teamcode.Auton;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

import static com.pedropathing.api.Paths.*;

import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.pedro.Constants;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.ivy.Scheduler;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous(name = "AdjacentAuton", group = "")
public class AdjacentAuton extends OpMode {

    private Follower follower;
    private final PoseFactory p = PoseFactory.degrees();

    // TODO: Replace with measured field coordinates. ya ik its not the center of the robot but the corner..
    private final Pose startPose = p.of(0, 141.5, 0);
    private final Pose endPose = p.of(10, 141.5-24, 0);
    private Path path;


    // Path methods
    private Path park() {
        return line(startPose, endPose).linear(startPose, endPose);
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
        schedule(follow(follower, park()));
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

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading",
                Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.addData("Busy", follower.isBusy());

        telemetry.update();
    }
}