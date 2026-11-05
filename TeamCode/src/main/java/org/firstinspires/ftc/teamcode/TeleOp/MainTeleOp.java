package org.firstinspires.ftc.teamcode.TeleOp;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;

import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Robot.State;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp(name = "Main TeleOp", group = "")
public class MainTeleOp extends CommandOpMode {
    public Robot robot;
    public GamepadEx driver;
    private Follower follower;
    // gamepad
    private Trigger leftTrigger;
    private Trigger rightTrigger;
    private GamepadButton circle;
    private GamepadButton leftBumper;
    private GamepadButton rightBumper;
    private GamepadButton dpadUp;
    private GamepadButton dpadDown;
    // Commands
    private InstantCommand prep;
    private InstantCommand in;



    private DcMotorEx frontLeft = null;
    private DcMotorEx backLeft = null;
    private DcMotorEx frontRight = null;
    private DcMotorEx backRight = null;
    @Override
    public void initialize() {
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);




        // initialize
        follower = Constants.create(hardwareMap);
        robot = new Robot(hardwareMap);
        driver = new GamepadEx(gamepad1);

        dpadUp = driver.getGamepadButton(GamepadKeys.Button.DPAD_UP);
        dpadDown = driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN);
        leftTrigger = new Trigger(() -> gamepad1.left_trigger > 0.5);
        rightTrigger = new Trigger(() -> gamepad1.right_trigger > 0.5);
        circle = driver.getGamepadButton(GamepadKeys.Button.CIRCLE);
        leftBumper = driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER);
        rightBumper = driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER);

        prep = new InstantCommand(
                () -> {
                    robot.setState(State.PREPPING);
                    robot.intake.stop();
                    robot.shooter.spin();
                }, robot.intake, robot.shooter
        );
        in = new InstantCommand(
                () -> {
                    robot.setState(State.COLLECTING);
                    robot.intake.in();
                },
                robot.intake
        );

        // circle: start intake
        circle.whenPressed(in);
        // left trigger: spit when held, intakes when released
        leftTrigger.whenActive(new InstantCommand(
                () -> {
                    robot.setState(State.COLLECTING);
                    robot.intake.out();
                },
                robot.intake
        )).whenInactive(in);
        // left bumper: toggle flower
        leftBumper.whenPressed(new InstantCommand(
                () -> robot.intake.flowerToggle(),
                robot.intake
        ));

        // right bumper: prep shooter
        rightBumper.whenPressed(prep);
        // right trigger: shoot while held, prep when released
        rightTrigger.whileActiveContinuous(new RunCommand(
                () -> {
                    if (robot.isState(State.READY)) {
                        robot.intake.in();
                    } else {
                        robot.intake.stop();
                    }
                },
                robot.intake
        )).whenInactive(prep);

        // change shooter velocity with dpad
        dpadUp.whenPressed(new InstantCommand(
            () -> robot.shooter.changeTargetVelocity(1), robot.shooter
        ));
        dpadDown.whenPressed(new InstantCommand(
            () -> robot.shooter.changeTargetVelocity(-1), robot.shooter
        ));

        // telemetry
        telemetry.addLine("INITIALIZED!!!");
        telemetry.update();
    }
    @Override
    public void run() {
        super.run();
        drive();
        if (robot.shooter.isRunning()) {
            robot.setState( (robot.shooter.checkReady()) ? State.READY : State.PREPPING );
        }

        telemetry.addData("state:", robot.getState().toString());
        telemetry.addData("", "");
        telemetry.addData("", "");
        telemetry.addData("kP, kI, kD:", robot.shooter.getVelocityCoefficients());
        telemetry.addData("kS, kV:", robot.shooter.getFeedforwardCoefficients());
        telemetry.addData("Shooter target", robot.shooter.getTargetVelocity());
        telemetry.addData("Shooter actual", robot.shooter.getActualVelocity());
        telemetry.addData("", "");

        if (robot.isState(State.READY)) {
            for (int i=0; i<8; i+=1) {
                telemetry.addData("","READY TO SHOOT!!!!");
            }
        }

        telemetry.update();
    }
    void drive() {
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);

        /*
        follower.manual(
                y,
                x,
                rx
        );
        follower.update();
        */
        /*
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x,
                follower.pose().heading()
        );
        follower.manual(powers);
        follower.update();
        */

        Pose robotPose = follower.pose(); // returns a Pose object
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
    }
}
