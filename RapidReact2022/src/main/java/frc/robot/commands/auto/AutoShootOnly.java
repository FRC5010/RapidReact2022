// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.CalibrateHood;
import frc.robot.commands.FenderShot;
import frc.robot.commands.SpinIntake;
import frc.robot.commands.SpinTurret;
import frc.robot.mechanisms.Transport;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.UpperIndexerSubsystem;
import frc.robot.subsystems.vision.VisionSystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoShootOnly extends SequentialCommandGroup {
  /** Creates a new AutoShootOnly. */
  IntakeSubsystem intakeSubsystem; 
  IndexerSubsystem indexerSubsystem; 
  TurretSubsystem turretSubsystem;   
  VisionSystem shooterVision; 
  ShooterSubsystem shooterSubsystem; 
  UpperIndexerSubsystem upperIndexerSubsystem;
  public AutoShootOnly(Transport transport, VisionSystem shooterVision, SequentialCommandGroup drivingGroup) {
    intakeSubsystem = transport.getIntakeSubsystem();
    indexerSubsystem = transport.getIndexerSubsystem();
    turretSubsystem = transport.getTurretSubsystem();
    this.shooterVision = shooterVision;
    shooterSubsystem = transport.getShooterSubsystem();
    upperIndexerSubsystem = transport.getUpperIndexerSubsystem(); 
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ParallelCommandGroup(
        new CalibrateHood(shooterSubsystem),
        new InstantCommand(() -> intakeSubsystem.deployIntake())
      ),
      new ParallelCommandGroup(
        new SpinIntake(intakeSubsystem, indexerSubsystem, 1.0),
        new SpinTurret(turretSubsystem, shooterVision),
        new FenderShot(shooterSubsystem, upperIndexerSubsystem, true)//,
        //drivingGroup
    ));

  }
}
