package frc.robot.commands.Feed;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants;
import frc.robot.subsystems.FeederSubsystem;

public class ResetBallCount extends InstantCommand {

    private FeederSubsystem m_feederSubsystem;

    public ResetBallCount(FeederSubsystem feederSubsystem) {
        m_feederSubsystem = feederSubsystem;
    }

    @Override
    public void execute() {
        for (int i = 0; i < Constants.FeederConstants.kSolenoidPorts.length; i++) {
            m_feederSubsystem.setSolenoidState(i, true);
        }

        m_feederSubsystem.setNumPowerCells(0);
    }
}
