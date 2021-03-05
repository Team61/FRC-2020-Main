package frc.robot.commands.Shoot;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.ShooterSubsystem;

public class Shoot extends CommandBase {

    private ShooterSubsystem m_shooterSubsystem;
    private double m_velocity;

    public Shoot(ShooterSubsystem shooterSubsystem, double velocity) {
        m_shooterSubsystem = shooterSubsystem;
        m_velocity = velocity;

        addRequirements(shooterSubsystem);
    }

    @Override
    public void execute() {
        m_shooterSubsystem.setVoltage(m_shooterSubsystem.getOutput(m_velocity));
    }

    @Override
    public void end(boolean interrupted) {
        m_shooterSubsystem.stop();
    }
}
