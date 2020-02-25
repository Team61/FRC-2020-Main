/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OIConstants;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.FeederConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.Drive.FollowTrajectory;
import frc.robot.commands.Drive.TankDrive;
import frc.robot.commands.Feed.BeltDump;
import frc.robot.commands.Feed.Dump;
import frc.robot.commands.Feed.Intake;
import frc.robot.commands.Feed.ResetLimitSwitch;
import frc.robot.commands.Lift.Climb;
import frc.robot.commands.Shoot.Fire;
import frc.robot.commands.Turret.MoveTurretToPosition;
import frc.robot.commands.Turret.TurretAutoAimVision;
import frc.robot.commands.Turret.TurretWithJoysticks;
import frc.robot.subsystems.*;
import lib.components.LogitechJoystick;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...
    private final DriveSubsystem m_driveSubsystem = DriveSubsystem.getInstance();
    private final FeederSubsystem m_feederSubsystem = FeederSubsystem.getInstance();
    private final TurretSubsystem m_turretSubsystem = TurretSubsystem.getInstance();
    private final ShooterSubsystem m_shooterSubsystem = ShooterSubsystem.getInstance();
    private final LiftSubsystem m_liftSubsystem = LiftSubsystem.getInstance();
    private final VisionSubsystem m_visionSubsystem = VisionSubsystem.getInstance();

    private final LogitechJoystick jLeft = new LogitechJoystick(OIConstants.jLeft);
    private final LogitechJoystick jRight = new LogitechJoystick(OIConstants.jRight);
    private final LogitechJoystick jLift = new LogitechJoystick(OIConstants.jLift);
    private final LogitechJoystick jTurret = new LogitechJoystick(OIConstants.jTurret);

    private Trigger BeltDumpTrigger = new Trigger(()-> jTurret.btn_8.get() || jTurret.btn_10.get() || jTurret.btn_12.get());

    private final Command m_autoCommand = null;

    SendableChooser<Command> m_chooser = new SendableChooser<>();

    /**
     * The container for the robot.  Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        m_driveSubsystem.setDefaultCommand(new TankDrive(m_driveSubsystem, jLeft::getYAxis, jRight::getYAxis));
        m_turretSubsystem.setDefaultCommand(new TurretWithJoysticks(m_turretSubsystem, jTurret::getZAxis));

        // Configure the button bindings
        configureButtonBindings();
    }

    /**
     * Use this method to define your button->command mappings.  Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
     * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        jRight.btn_1.whileHeld(new Intake(m_feederSubsystem));

        jLift.btn_1.whenPressed(new Climb(m_liftSubsystem));

        jTurret.btn_1.whileHeld(new Fire(m_shooterSubsystem, m_feederSubsystem, ShooterConstants.kMaxVoltage));

        jTurret.btn_4.whileHeld(new MoveTurretToPosition(m_turretSubsystem, 0));
        jTurret.btn_5.whileHeld(new Dump(m_feederSubsystem));
        jTurret.btn_6.whileHeld(new MoveTurretToPosition(m_turretSubsystem, 180));

        jTurret.btn_7.whenPressed(new ResetLimitSwitch(m_feederSubsystem, 0));
        jTurret.btn_9.whenPressed(new ResetLimitSwitch(m_feederSubsystem, 1));
        jTurret.btn_11.whenPressed(new ResetLimitSwitch(m_feederSubsystem, 2));

        BeltDumpTrigger.whileActiveContinuous(new BeltDump(m_feederSubsystem, new BooleanSupplier[] {jTurret.btn_8::get, jTurret.btn_10::get, jTurret.btn_12::get}));

        jTurret.btn_2.whileHeld(new TurretAutoAimVision(m_turretSubsystem, m_visionSubsystem::getYaw));

    }


    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {

//        // Create a voltage constraint to ensure we don't accelerate too fast
//        DifferentialDriveVoltageConstraint autoVoltageConstraint =
//                new DifferentialDriveVoltageConstraint(
//                        new SimpleMotorFeedforward(AutoConstants.kS,
//                                AutoConstants.kV,
//                                AutoConstants.kA),
//                        AutoConstants.kDriveKinematics,
//                        AutoConstants.kMaxVoltage);
//
//
//        /* Characterization */
//
//        // Create config for trajectory
//        TrajectoryConfig config =
//                // Add constraints to trajectory
//                new TrajectoryConfig(AutoConstants.kMaxVelocity,
//                        AutoConstants.kMaxAcceleration)
//                        // Add kinematics to ensure max speed is actually obeyed
//                        .setKinematics(AutoConstants.kDriveKinematics)
//                        // Apply the voltage constraint
//                        .addConstraint(autoVoltageConstraint);
//
//         //An example trajectory to follow.  All units in meters.
//        Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
//                // Start at the origin facing the +X direction
//                new Pose2d(0, 0, new Rotation2d(0)),
//                // Pass through these two interior waypoints, making an 's' curve path
//                List.of(
//                        new Translation2d(5, 0)
//                ),
//                // End 3 meters straight ahead of where we started, facing forward
//                new Pose2d(5, 0, new Rotation2d(0)),
//                // Pass config
//                config
//        );

        String[] pathGroup = AutoConstants.RightTrenchGroup;

        Trajectory[] trajectories = new Trajectory[pathGroup.length];

        try {
            Path[] paths = new Path[pathGroup.length];
            for(int i = 0; i < paths.length; i++) {
                paths[i] = Filesystem.getDeployDirectory().toPath().resolve(pathGroup[i]);
                trajectories[i] = TrajectoryUtil.fromPathweaverJson(paths[i]);
            }
        } catch (IOException ex) {
            DriverStation.reportError("Unable to open trajectories", ex.getStackTrace());
        }

        // Run path following command, then stop at the end.
        try {
            ParallelDeadlineGroup m_fire = new ParallelDeadlineGroup(
                    new WaitCommand(FeederConstants.kAutoDelay),
                    new Fire(m_shooterSubsystem, m_feederSubsystem, ShooterConstants.kMaxVoltage));
           return m_fire.andThen(
                   new ParallelDeadlineGroup(
                       new FollowTrajectory(trajectories[0], m_driveSubsystem).andThen(new FollowTrajectory(trajectories[1], m_driveSubsystem)),
                       new Intake(m_feederSubsystem))
                   .andThen(m_fire));
        } catch (ArrayIndexOutOfBoundsException ex) {
            DriverStation.reportError("Trajectory array out of bounds", ex.getStackTrace());
            return null;
        }
    }
}
