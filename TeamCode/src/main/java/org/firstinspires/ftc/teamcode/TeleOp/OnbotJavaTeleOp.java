package org.firstinspires.ftc.teamcode.TeleOp;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp(name = "onbotjava code", group = "")
//@Disabled
public class OnbotJavaTeleOp extends OpMode {
    //
    double shooterVel = 62;
    final double intakeVel = 100;

    //
    boolean launchRun = false;
    boolean intakeRun = false;

    //
    private DcMotorEx frontLeft = null;
    private DcMotorEx backLeft = null;
    private DcMotorEx frontRight = null;
    private DcMotorEx backRight = null;

    //
    private DcMotorEx shooter;
    private DcMotorEx intake1;
    private DcMotorEx intake2;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        intake1 = hardwareMap.get(DcMotorEx.class, "intake1");
        intake2 = hardwareMap.get(DcMotorEx.class, "intake2");

        //

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        shooter.setDirection(DcMotor.Direction.FORWARD);

        intake1.setDirection(DcMotor.Direction.FORWARD);
        intake2.setDirection(DcMotor.Direction.REVERSE);

        //
        frontLeft.setZeroPowerBehavior(BRAKE);
        frontRight.setZeroPowerBehavior(BRAKE);
        backLeft.setZeroPowerBehavior(BRAKE);
        backRight.setZeroPowerBehavior(BRAKE);

        shooter.setZeroPowerBehavior(BRAKE);

        intake1.setZeroPowerBehavior(BRAKE);
        intake2.setZeroPowerBehavior(BRAKE);

        //
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(300, 0, 0, 10));

        //
        telemetry.addData("Status", "Initialized");
    }
    @Override
    public void init_loop() {
    }
    @Override
    public void start() {
        //shooter.setVelocity(-10);
    }
    @Override
    public void loop() {
        drive();

        intakeChange(gamepad1.leftBumperWasPressed());
        /*
        shooterChange(gamepad1.rightTriggerWasPressed());
        if (gamepad1.dpadUpWasPressed()) {
            shooterVel += 1;
            launchRun = !launchRun;
            shooterChange(true);
        } else if (gamepad1.dpadDownWasPressed()) {
            shooterVel -= 1;
            launchRun = !launchRun;
            shooterChange(true);
        }
*/
        telemetry.addData("shooter vel", shooterVel +", "+ shooter.getVelocity());
        telemetry.addData("intake vel", intakeVel +", "+ intake1.getVelocity()+", "+ intake2.getVelocity());
    }

    @Override
    public void stop() {
    }

    void intakeChange(boolean changeIntake) {
        if (changeIntake) {
            intake1.setVelocity( (intakeRun == true) ? 0 : intakeVel );
            intake2.setVelocity( (intakeRun == true) ? 0 : intakeVel );
            intakeRun = !intakeRun;
        }
    }
    /*
    void shooterChange(boolean changeLaunch) {
        if (changeLaunch) {
            launchRun = !launchRun;
            shooter.setVelocity( (launchRun==true) ? shooterVel : -5 );
        }
    }
    */
    void setIntake(double speed) {
        intake1.setVelocity( speed );
        intake2.setVelocity( speed );
        intakeRun = !intakeRun;
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
    }
}


