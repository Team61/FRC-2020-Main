package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.controller.PIDController;

public class DriveTrain extends SubsystemBase {

    private static final double WHEEL_DIAMETER = 8;
    private static final double PULSE_PER_REVOLUTION = 1440;
    private static final double ENCODER_GEAR_RATIO = 1;
    private static final double GEAR_RATIO = 12.75;

    private static final double distancePerPulse = Math.PI * WHEEL_DIAMETER / PULSE_PER_REVOLUTION / ENCODER_GEAR_RATIO / GEAR_RATIO;

    protected SpeedController m_frontLeft = new PWMTalonSRX(DriveConstants.mFrontLeft);
    protected SpeedController m_rearLeft = new PWMTalonSRX(DriveConstants.mRearLeft);
    protected SpeedControllerGroup m_leftGroup = new SpeedControllerGroup(m_frontLeft, m_rearLeft);

    protected SpeedController m_frontRight = new PWMTalonSRX(DriveConstants.mFrontRight);
    protected SpeedController m_rearRight = new PWMTalonSRX(DriveConstants.mRearRight);
    protected SpeedControllerGroup m_rightGroup = new SpeedControllerGroup(m_frontRight, m_rearRight);

    protected Encoder m_leftEncoder = new Encoder(DriveConstants.eLeftA, DriveConstants.eLeftB, false);
    protected Encoder m_rightEncoder  = new Encoder(DriveConstants.eRightA, DriveConstants.eRightB, true);

    private final AnalogGyro m_gyro = new AnalogGyro(DriveConstants.gDrive);

    private final DifferentialDrive m_differentialDrive = new DifferentialDrive(m_leftGroup, m_rightGroup);

    private final DifferentialDriveKinematics m_kinematics = new DifferentialDriveKinematics(
            DriveConstants.kTrackWidth);

    private final DifferentialDriveOdometry m_odometry;

    private final PIDController m_leftPIDController = new PIDController(1, 0, 0);
    private final PIDController m_rightPIDController = new PIDController(1, 0, 0);

    public DriveTrain() {

        m_leftEncoder
                .setDistancePerPulse(2 * Math.PI * DriveConstants.kWheelRadius / DriveConstants.kEncoderResolution);
        m_rightEncoder
                .setDistancePerPulse(2 * Math.PI * DriveConstants.kWheelRadius / DriveConstants.kEncoderResolution);

        m_leftEncoder.reset();
        m_rightEncoder.reset();

        m_odometry = new DifferentialDriveOdometry(getHeading());
    }

    /**
     * Movement commands
     */

    public void tankDrive(final double leftSpeed, final double rightSpeed, final boolean squaredInputs) {
        m_differentialDrive.tankDrive(leftSpeed, rightSpeed, squaredInputs);
    }

    public void tankDrive(final double leftSpeed, final double rightSpeed) {
        tankDrive(leftSpeed, rightSpeed, true);
    }

    public void tankDrive(final double speed) {
        tankDrive(speed, speed);
    }

    public void stopTankDrive() {
        tankDrive(0);
    }

    public void setMaxOutput(final double maxOutput) {
        m_differentialDrive.setMaxOutput(maxOutput);
    }

    /**
     * Methods for Encoder Data
     **/

    public void setDistancePerPulse() {
        m_leftEncoder.setDistancePerPulse(distancePerPulse);
        m_rightEncoder.setDistancePerPulse(distancePerPulse);
    }

    public double getLeftEncoder() {
        return m_leftEncoder.getDistance();
    }

    public double getRightEncoder() {
        return m_rightEncoder.getDistance();
    }

    public void resetLeftEncoder() {
        m_leftEncoder.reset();
    }

    public void resetRightEncoder() {
        m_rightEncoder.reset();
    }

    public void resetEncoders() {
        resetRightEncoder();
        resetLeftEncoder();
    }


    public double getDistanceTraveled() {
        return (getLeftEncoder() + getRightEncoder()) / 2;
    }

    /**
     * Methods for Gyro Data
     * 
     * @return The displacement in degrees from -180 to 180
     */


    public Rotation2d getHeading() {
        return Rotation2d.fromDegrees(m_gyro.getAngle());
    }

    public void resetGryo() {
        m_gyro.reset();
    }

    /**
     * Sets the desired wheel speeds.
     *
     * @param speeds The desired wheel speeds.
     */
    public void setSpeeds(final DifferentialDriveWheelSpeeds speeds) {
        double leftOutput = m_leftPIDController.calculate(m_leftEncoder.getRate(),
        speeds.leftMetersPerSecond);
    double rightOutput = m_rightPIDController.calculate(m_rightEncoder.getRate(),
        speeds.rightMetersPerSecond);
        m_leftGroup.set(leftOutput);
        m_rightGroup.set(rightOutput);
    }

    /**
     * Drives the robot with the given linear velocity and angular velocity.
     *
     * @param xSpeed Linear velocity in m/s.
     * @param rot    Angular velocity in rad/s.
     */
    @SuppressWarnings("ParameterName")
    public void drive(final double xSpeed, final double rot) {
        final DifferentialDriveWheelSpeeds wheelSpeeds = m_kinematics.toWheelSpeeds(new ChassisSpeeds(xSpeed, 0.0, rot));
        setSpeeds(wheelSpeeds);
  }

  /**
   * Updates the field-relative position.
   */

   public Pose2d getPose2d() {
       return m_odometry.getPoseMeters();
   }
  public void updateOdometry() {
    m_odometry.update(getHeading(), m_leftEncoder.getDistance(), m_rightEncoder.getDistance());
  }
}
