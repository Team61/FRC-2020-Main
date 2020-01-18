package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LauncherConstants;
import lib.components.LimitSwitch;

public class Launcher extends SubsystemBase {

    private static Launcher m_instance;

    private WPI_TalonSRX m_flywheelA = new WPI_TalonSRX(LauncherConstants.kFlywheelMotorAPort);
    private WPI_TalonSRX m_flywheelB = new WPI_TalonSRX(LauncherConstants.kFlywheelMotorBPort);
    private SpeedControllerGroup m_flywheel = new SpeedControllerGroup(m_flywheelA, m_flywheelB);

    private Encoder m_encoder = new Encoder(LauncherConstants.kEncoderPorts[0], LauncherConstants.kEncoderPorts[1], LauncherConstants.kEncoderReversed);

    private LimitSwitch m_limitSwitch = new LimitSwitch(LauncherConstants.kLimitSwitchPort);

    private double targetSpeedPer;
    private double targetSpeedRPM;

    public Launcher() {
        m_flywheelB.follow(m_flywheelA);
    }

    public static Launcher getInstance() {
        if (m_instance == null) {
            m_instance = new Launcher();
        }

        return m_instance;
    }

    public void set(double speed) {
        m_flywheel.set(speed);
    }

    public void setVoltage(double voltage) {
        m_flywheel.setVoltage(voltage);
    }

    public void stop() {
        set(0);
    }

    public void setTargetSpeedPer(double targetSpeedPer) {
        this.targetSpeedPer = targetSpeedPer;
    }

    public double getTargetSpeedPer() {
        return targetSpeedPer;
    }

    public void setTargetSpeedRPM(double targetSpeedPer) {
        this.targetSpeedPer = targetSpeedPer;
    }

    public double getTargetSpeedRPM() {
        return targetSpeedPer;
    }

    public boolean isSwitchSet() {
        return m_limitSwitch.isSwitchSet();
    }

    public int getEncoder() {
        return m_encoder.get();
    }

    public double getEncoderRate() {
        return m_encoder.getRate();
    }
}
