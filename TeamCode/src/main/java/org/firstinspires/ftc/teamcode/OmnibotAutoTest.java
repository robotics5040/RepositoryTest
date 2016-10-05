package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;


/**
 * Created by bense on 9/19/2016.
 */

@Autonomous(name = "Omnibot Autonomous Testing", group = "Testing")
public class OmnibotAutoTest extends OpMode {
    int control = 1, degrees = 0, previousHeading = 0, heading, trueHeading, target, start;
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    GyroSensor gyro;
    Long time, startTime, startTime2;


    public void init()
    {
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        startTime = System.currentTimeMillis();
        time = startTime;
        startTime2 = startTime;
    }

    public void loop()
    {
        time = System.currentTimeMillis();

        heading = gyro.getHeading();
        trueHeading = degrees + heading;
        checkHeading();

        switch (control)
        {
            case 0:
            {
                if (rotate('l', -90)) {
                    control = 1;
                    startTime2 = System.currentTimeMillis();
                }
                break;
            }
            case 1:
            {
                if (navigate(60, .5, 2000))
                    control = 3;
                break;
            }
            case 2:
            {
                if (rotate('r', 90))
                    control = 3;
                break;
            }
            default:
            {
                allStop();
            }
        }

        telemetry.addData("Timer", time - startTime);
        telemetry.addData("Function Timer", time - startTime2);
        telemetry.addData("Control", control);
        telemetry.addData("Gyro", heading);
        telemetry.addData("Actual Rotation", trueHeading);
        telemetry.addData("Target", target);
        telemetry.addData("Distance from target", trueHeading - target);

        previousHeading = gyro.getHeading();
    }

    public boolean navigate(int deg, double power, long millis) //like unit circle, 90 forwards, 270 backwards
    {
        double currentPosition =
        if (time - startTime2 < millis)
        {
            double x = Math.cos(deg + 0.0), y = Math.sin(deg + 0.0);

            frontLeft.setPower((-(-y - x)/2) * power);
            backLeft.setPower(((-y + x)/2) * power);
            frontRight.setPower(((y - x)/2) * power);
            backRight.setPower((-(y + x)/2) * power);

            return false;
        }
        return true;
    }

    public boolean rotate(char direction, int deg)
    {
        target = deg;
        if (direction == 'r' && trueHeading - target < 0)
        {
            frontRight.setPower(-.25);
            frontLeft.setPower(-.25);
            backRight.setPower(-.25);
            backLeft.setPower(-.25);
            return false;
        }
        else if (direction == 'l' && trueHeading - target > 0)
        {
            frontRight.setPower(.25);
            frontLeft.setPower(.25);
            backRight.setPower(.25);
            backLeft.setPower(.25);
            return false;
        }
        return true;
    }

    public void checkHeading()
    {
        if (previousHeading - heading > 300)
            degrees += 360;
        else if (heading - previousHeading > 300)
            degrees -= 360;
    }

    public void allStop()
    {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
}
