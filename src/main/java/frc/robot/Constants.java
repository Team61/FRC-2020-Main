/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.ColorShim;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {

    /* SI Units are used */

    public static final class OIConstants {

        //Stick Ports
        public static final int jLeft = 0;
        public static final int jRight = 1;
        public static final int jLift = 2;
        public static final int jTurret = 3;
    }

    public static final class DriveConstants {

        public static final double kWheelDiameter = 0.2032; // Meters

        // Encoder Information
        public static final int kEncoderCPR = 360; // pulses per revolution
        public static final double gearRatio = 12.75; // This is 1 if the encoder is directly mounted to the wheel shaft which it should to account for slip
        public static final double kEncoderDistancePerPulse = (kWheelDiameter * Math.PI) / (gearRatio * kEncoderCPR);
        public static final boolean kLeftEncoderReversed = false;
        public static final boolean kRightEncoderReversed = true;



        // Motor Ports
        public static final int kFrontLeftPort = 1;
        public static final int kRearLeftPort = 2;
        public static final int kFrontRightPort = 3;
        public static final int kRearRightPort = 4;

        // Encoder Ports
        public static final int[] kLeftEncoderPorts = {4, 5};
        public static final int[] kRightEncoderPorts = {6, 7};

        // Operation Data
        public static final double kFudgeFactor = 1; // right side

    }

    public static final class AutoConstants {

        public static final String directory = "/U/";

        // Constraints
        public static final double kMaxVelocity = 0.3; // Meters per second
        public static final double kMaxAcceleration = 0.2; // Meters per second squared
        public static final double kMaxVoltage = 6;

        public static final double kTrackWidth = 0.69; // Meters
        public static final double kWheelBase = 0.94; // Meters
        public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackWidth);

        public static final TrapezoidProfile.Constraints kConstraints = new TrapezoidProfile.Constraints(kMaxVelocity, kMaxAcceleration);

        // Feedforward
        public static final double kS = 0.762; // Volts
        public static final double kV = 5.99; // Volts seconds per meters
        public static final double kA = 0.751; // Volts seconds per meters squared

        // Feedback
        public static final double kP = 0.987; // Volts seconds per meter
        public static final double kI = 0; // Volts seconds per meter
        public static final double kD = 0; // Volts per seconds per meter


        // Ramsete Controller
        public static final double kRamseteB = 2;
        public static final double kRamseteZeta = 0.7;

        // Path Planning
        public static final Pose2d kStartingPosition = new Pose2d(new Translation2d(3.323, -0.867), new Rotation2d(0));

        public static final String RightStartToTrench = "/home/lvuser/deploy/output/RightStartToTrench.wpilib.json";
        public static final String TrenchToRightStart = "/home/lvuser/deploy/output/TrenchToRightStart.wpilib.json";

        public static final String[] RightTrenchGroup = {RightStartToTrench, TrenchToRightStart};
    }

    public static final class TurretConstants {

        // Ports
        public static final int kMotorPort = 7;
        public static final int[] kEncoderPorts = {8, 9};
        public static final int kLimitSwitchPort = 3;
        public static final double kMaxSpeed = 20;

        // Encoder Information
        public static final double kWheelDiameter = 0.15; // Meters
        public static final int kEncoderCPR = 7; // cycles/pulses per revolution
        public static final double gearRatio = 3.1875; // This is 1 if the encoder is directly mounted to the wheel shaft which it should to account for slip
        public static final double kEncoderDistancePerPulse = (kWheelDiameter * Math.PI) / (gearRatio * kEncoderCPR);
        public static final boolean kEncoderReversed = false;
        public static final double kDistanceToDegrees = 180 / 45.0; // Move turret 180 degrees then measure encoder distance and divide

        /* Characterization */

        // Feedforward
        public static final double kS = 0.535; // Volts
        public static final double kV = 0.407; // Volts seconds per meters
        public static final double kA = 0.000143; // Volts seconds per meters squared

        // Feedback
        public static final double kP = 3.08; // Volts seconds per meter
        public static final double kI = 0; // Volts seconds per meter
        public static final double kD = 0; // Volts per seconds per meter

        // Constraints
        public static final double kMaxVoltage = 12;

        // Operation Data
        public static final double kDefaultState = 0; // Distance from set point
        public static final double kMaxDistance = 50;
        public static final double kTurretTolerance = 2;
        public static final double kVisionTolerance = 2; // Degrees
        public static final double kVisionVoltage = 7;
        public static final double kAdjustmentVoltage = 12;
        public static final double kAdjustmentDelay = 0.05;


        public static final Translation2d kGoalPosition = new Translation2d(3.358, -2.358);

    }

    public static final class FeederConstants {

        // Ports
        public static final int kMotorPort = 8;
        public static final int[] kSolenoidPorts = {0, 1, 2};
        public static final int[] kLimitSwitchPorts = {0, 1, 2};

        // Operation Data
        public static final double kMaxVoltage = 9;
        public static final double kFeederDelay = 0.4;
        public static final double[] kBallDelays = {0, 0.25};
        public static final double kAutoDelay = 1.1;

    }

    public static final class ShooterConstants {
        // Ports
        public static final int kMasterPort = 5;
        public static final int kSlavePort = 6;

        public static final int kEncoderCPR = 2048;
        // Operation Data
        public static final double kMaxVoltage = 11.5;
        public static final double kAutoVoltage = 11.3;
        public static final double[] kSpeeds = {10.5, 6.1, 7.7, 8};
        public static final double[] autoVoltages = {12, 9.28, 8.90, 9.22};
        public static final double kVoltagePerDistance = 1.2 / 0.8; // Volts per meter

        public static final double kS = 1.524; // Volts
        public static final double kV = 0.95; // Volts seconds per meters
        public static final double kA = 0.04; // Volts seconds per meters squared

        // Feedback
        public static final double kP = .164; // Volts seconds per meter
        public static final double kI = 0; // Volts seconds per meter
        public static final double kD = 0; // Volts per seconds per meter

    }

    public static final class LiftConstants {
        // Ports
        public static final int kSolenoidAPort = 3;
        public static final int kSolenoidBPort = 4;
    }

    public static final class VisionConstants {
        // Camera Information
        public static final double kCamera_width = 640; // in pixels
        public static final double kFieldOfView = 67; // in degrees
        public static final double kDegPerPixel = kFieldOfView / kCamera_width;

        // Camera Names
        public static final String TurretCamName = "TurretCam";
        public static final String DriveCamName = "DriveCam";

        // Object Data
        public static final double kTapeWidth = 0.2; // in meters

    }

    public static final class PhysicConstants {
        public static final double kGravity = 9.8;
    }
    public static final class WheelSpinnerConstants {
        public static final int kMotorPort = 9;

        public static final double kWheelVoltage = 12;

        public static final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
        public static final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
        public static final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
        public static final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

        public static final Color[] colors = {kBlueTarget, kGreenTarget, kRedTarget, kYellowTarget};
    }
    public static final class LEDContants {
        public static final int kLEDPort = 0;
        public static final int kLength = 68;

        public static final int kLiftSnakeSize = 7;

        public static final double kIncrementDelay = 0.02;
        public static final double kIntensityFactor = 0.02;
        public static final double kDimFactor = 10;

        public static final double kLiftFlashDelay = 0.5;
        public static final double kShootChargeDelay = 0.05;

    }

}
